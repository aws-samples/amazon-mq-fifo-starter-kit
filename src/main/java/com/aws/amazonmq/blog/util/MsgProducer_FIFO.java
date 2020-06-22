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
import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgProducer_FIFO implements Job {

	Logger logger = LoggerFactory.getLogger(MsgProducer_FIFO.class);

	public MsgProducer_FIFO() {
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		// Retrieve job specific settings
		JobKey key = context.getJobDetail().getKey();
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String amazonMQSSLEndpoint = dataMap.getString("amazonMQSSLEndPoint");
		String username = dataMap.getString("username");
		String password = dataMap.getString("password");
		String queueName = dataMap.getString("queueName");
		String producerName = dataMap.getString("producerName");
		String useCaseId = dataMap.getString("useCaseId");
		int numMsgs = dataMap.getInt("numMsgs");
		String msgGroup = dataMap.getString("msgGroup");
		String msgPrefix = dataMap.getString("msgPrefix");
		int msgIdSequence = dataMap.getInt("msgIdSequence");
		System.out.println("Running Job: " + key.getName());
		// Create AmazonMQ connection factory
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(amazonMQSSLEndpoint);
		connectionFactory.setUserName(username);
		connectionFactory.setPassword(password);
		try {
			// Create a connection
			long t1 = System.currentTimeMillis();
			Connection connection = connectionFactory.createConnection();
			connection.start();
			long t2 = System.currentTimeMillis();
			System.out.printf("Connection started for %s. It took %d milliseconds. \n", producerName, (t2 - t1));
			t1 = System.currentTimeMillis();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination producerDestination = session.createQueue(queueName);
			MessageProducer producer = session.createProducer(producerDestination);
			t2 = System.currentTimeMillis();
			System.out.printf("Producer Session for Thread %s Started. It took %d milliseconds \n", producerName,
					(t2 - t1));
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			System.out.printf("Writing messages into Amazon MQ. Queue Name: %s, Producer Id: %s, Message Group: %s \n",
					queueName, producerName, msgGroup);
			AtomicInteger counter = new AtomicInteger(msgIdSequence);
			int temp = counter.get();
			List<PutMsgMetric> putMsgMetricList = new ArrayList<PutMsgMetric>();
			for (int i = temp; i < temp + numMsgs; i++) {
				int msgId = counter.incrementAndGet();
				String msgBody = msgPrefix + msgId;
				TextMessage tMsg = session.createTextMessage(msgBody);
				tMsg.setStringProperty("JMSXGroupID", msgGroup);
				long t3 = System.currentTimeMillis();
				producer.send(tMsg);
				long t4 = System.currentTimeMillis();
				PutMsgMetric putMsgMetric = new PutMsgMetric();
				putMsgMetric.setMsg_id(Integer.valueOf(msgId).toString());
				putMsgMetric.setMsg_body(msgBody);
				putMsgMetric.setMsg_group(msgGroup);
				putMsgMetric.setProducer_id(producerName);
				putMsgMetric.setUsecase_id(useCaseId);
				putMsgMetric.setTime_to_put_in_millis(Long.valueOf(t4 - t3).toString());
				putMsgMetric.setTime_of_put_currenttime_millis(Long.valueOf(System.currentTimeMillis()).toString());
				putMsgMetricList.add(putMsgMetric);

			}
			if (putMsgMetricList.size() != 0) {
				try {
					Path pathToPutMetricsFile = Paths
							.get(new File("").getAbsolutePath().concat("/amazon-mq-poc-put-metrics").concat("-")
									.concat(useCaseId).concat("-").concat(producerName).concat("-").concat(Long.toString(System.currentTimeMillis())).concat(".csv"));
					Files.createDirectories(pathToPutMetricsFile.getParent());
					Files.createFile(pathToPutMetricsFile);
					CSVUtil.writePutMetricsToFile(pathToPutMetricsFile, putMsgMetricList);
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
			producer.close();
			session.close();
			connection.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}