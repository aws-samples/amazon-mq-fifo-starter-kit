// Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package com.aws.amazonmq.blog.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

public class MsgConsumer_CustomPrefetch implements Job {

	Logger logger = LoggerFactory.getLogger(MsgConsumer_CustomPrefetch.class);

	public MsgConsumer_CustomPrefetch() {
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		AmazonDynamoDB dynamoDBClient = AmazonDynamoDBClientBuilder.standard().build();
		// Retrieve job specific settings
		JobKey key = context.getJobDetail().getKey();
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String amazonMQSSLEndpoint = dataMap.getString("amazonMQSSLEndPoint");
		String amazonMQLoginUsername = dataMap.getString("username");
		String amazonMQLoginPWD = dataMap.getString("password");
		String queueName = dataMap.getString("queueName");
		String consumerName = dataMap.getString("consumerName");
		String useCaseId = dataMap.getString("useCaseId");
		int numMsgs = dataMap.getInt("numMsgs");
		String prefetchSize = dataMap.getString("prefetchSize");

		System.out.println("Running Job: " + key.getName());
		// Create AmazonMQ connection factory
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(amazonMQSSLEndpoint);
		connectionFactory.setUserName(amazonMQLoginUsername);
		connectionFactory.setPassword(amazonMQLoginPWD);
		try {
			// Create a connection
			long t1 = System.currentTimeMillis();
			Connection connection = connectionFactory.createConnection();
			connection.start();
			long t2 = System.currentTimeMillis();
			System.out.printf("Connection started for %s. It took %d milliseconds. \n", consumerName, (t2 - t1));
			// Create a session.
			t1 = System.currentTimeMillis();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			ActiveMQQueue queue = new ActiveMQQueue(
					queueName + "?consumer.dispatchAsync=false&consumer.prefetchSize=" + prefetchSize);
			MessageConsumer consumer = session.createConsumer(queue);
			t2 = System.currentTimeMillis();
			System.out.printf("Consumer Session for Thread %s started. It took %d milliseconds \n", consumerName,
					(t2 - t1));
			System.out.printf("Getting messages from Amazon MQ. Queue Name: %s, Consumer Id: %s \n", queueName,
					consumerName);
			List<GetMsgMetric> getMsgMetricList = new ArrayList<GetMsgMetric>();
			for (int i = 1; i < numMsgs + 1; i++) {
				long t3 = System.currentTimeMillis();
				Message consumerMessage = consumer.receive(50); // milliseconds is the timeout
				long t4 = System.currentTimeMillis();
				Optional<Message> message = Optional.ofNullable(consumerMessage);
				if (message.isPresent()) {
					TextMessage txtMessage = (TextMessage) message.get();
					String msgBody = txtMessage.getText();
					GetMsgMetric getMsgMetric = new GetMsgMetric();
					getMsgMetric.setMsg_id(Integer.valueOf(msgBody.substring(msgBody.lastIndexOf("-") + 1)).toString());
					getMsgMetric.setMsg_body(msgBody);
					getMsgMetric.setMsg_group(txtMessage.getStringProperty("JMSXGroupID"));
					getMsgMetric.setUsecase_id(useCaseId);
					getMsgMetric.setConsumer_id(consumerName);
					getMsgMetric.setTime_to_get_in_millis(Long.valueOf(t4 - t3).toString());
					getMsgMetric.setTime_of_get_currenttime_millis(Long.valueOf(System.currentTimeMillis()).toString());
					getMsgMetricList.add(getMsgMetric);
					// System.out.println("message returned at call #: " + i + ", consumer id: " +
					// consumerName);
				}
				// else {
				// System.out.println("no message returned at call #: " + i + ", consumer id: "
				// + consumerName);
				// }
			}
			if (getMsgMetricList.size() != 0) {
				try {
					Path pathToGetMetricsFile = Paths
							.get(new File("").getAbsolutePath().concat("/amazon-mq-poc-get-metrics").concat("-")
									.concat(useCaseId).concat("-").concat(consumerName).concat("-").concat(Long.toString(System.currentTimeMillis())).concat(".csv"));
					Files.createDirectories(pathToGetMetricsFile.getParent());
					Files.createFile(pathToGetMetricsFile);
					CSVUtil.writeGetMetricsToFile(pathToGetMetricsFile, getMsgMetricList);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			consumer.close();
			session.close();
			connection.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dynamoDBClient.shutdown();
	}
}
