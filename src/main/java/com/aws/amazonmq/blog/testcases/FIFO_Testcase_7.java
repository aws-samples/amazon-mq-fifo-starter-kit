// Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package com.aws.amazonmq.blog.testcases;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aws.amazonmq.blog.util.MsgConsumer_CustomPrefetch;
import com.aws.amazonmq.blog.util.MsgProducer_FIFO_Close_Message_Group;

/**
 * This class demonstrates FIFO Test case # 6 where:
 *  - a queue has three producers 
 *  - a queue has three consumers
 *  - a queue has three message groups
 *  - each consumer uses a pre-fetch size of 1
 *  
 *  - first consumer started earlier than second and third consumers
 *  - second consumer started 30 seconds after first consumer
 *  - third consumer started 60 seconds after first consumer
 *  
 *  - first producer closes message group A after sending 2500 messages and sends 2500 more messages
 *  - second producer closes message group B after sending 2500 messages and sends 2500 more messages
 *  - third producer closes message group C after sending 2500 messages and sends 2500 more messages
 * 
 * @author Ravi Itha
 *
 */
public class FIFO_Testcase_7 {

	public static void main(String[] args) throws InterruptedException {

		Logger logger = LoggerFactory.getLogger(FIFO_Testcase_7.class);
		String amazonMQSSLEndPoint = args[0]; // OpenWire Endpoint for AmazonMQ Broker
		String username = args[1]; // Username
		String password = args[2]; // Password
		String queueName = args[3]; // name of the AmazonMQ Queue Name
		int numMsgs = Integer.valueOf(args[4]); // number of messages to be inserted by each producer
		String useCaseId = args[5];
		String prefetchSize = args[6];
		try {
			// Grab the Scheduler instance from the Factory and start it off
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
			// define producer jobs and tie them to MsgProducer which does the actual work
			JobDetail job1 = newJob(MsgProducer_FIFO_Close_Message_Group.class).withIdentity(useCaseId.concat("-").concat("producer-job1"), "group1")
					.usingJobData("producerName", "producer_1")
					.usingJobData("queueName", queueName)
					.usingJobData("amazonMQSSLEndPoint", amazonMQSSLEndPoint)
					.usingJobData("username", username)
					.usingJobData("password", password)
					.usingJobData("useCaseId", useCaseId)
					.usingJobData("msgGroup", "Group-A")
					.usingJobData("numMsgs", numMsgs)
					.usingJobData("msgIdSequence", 250000)
					.usingJobData("msgPrefix", "A-").build();
			JobDetail job2 = newJob(MsgProducer_FIFO_Close_Message_Group.class).withIdentity(useCaseId.concat("-").concat("producer-job2"), "group1")
					.usingJobData("producerName", "producer_2")
					.usingJobData("queueName", queueName)
					.usingJobData("amazonMQSSLEndPoint", amazonMQSSLEndPoint)
					.usingJobData("username", username)
					.usingJobData("password", password)
					.usingJobData("useCaseId", useCaseId)
					.usingJobData("msgGroup", "Group-B")
					.usingJobData("numMsgs", numMsgs)
					.usingJobData("msgIdSequence", 300000)
					.usingJobData("msgPrefix", "B-").build();
			JobDetail job3 = newJob(MsgProducer_FIFO_Close_Message_Group.class).withIdentity(useCaseId.concat("-").concat("producer-job3"), "group1")
					.usingJobData("producerName", "producer_3")
					.usingJobData("queueName", queueName)
					.usingJobData("amazonMQSSLEndPoint", amazonMQSSLEndPoint)
					.usingJobData("username", username)
					.usingJobData("password", password)
					.usingJobData("useCaseId", useCaseId)
					.usingJobData("msgGroup", "Group-C")
					.usingJobData("numMsgs", numMsgs)
					.usingJobData("msgIdSequence", 350000)
					.usingJobData("msgPrefix", "C-").build();
			// define consumer jobs and tie them to MsgConsumer which does the actual work
			JobDetail job4 = newJob(MsgConsumer_CustomPrefetch.class).withIdentity(useCaseId.concat("-").concat("consumer-job1"), "group1")
					.usingJobData("consumerName", "consumer_1")
					.usingJobData("queueName", queueName)
					.usingJobData("amazonMQSSLEndPoint", amazonMQSSLEndPoint)
					.usingJobData("username", username)
					.usingJobData("password", password)
					.usingJobData("useCaseId", useCaseId)
					.usingJobData("numMsgs", numMsgs * 4) // this consumer job makes consumer.receive(50); calls 20000 times
					.usingJobData("prefetchSize", prefetchSize).build();
			JobDetail job5 = newJob(MsgConsumer_CustomPrefetch.class).withIdentity(useCaseId.concat("-").concat("consumer-job2"), "group1")
					.usingJobData("consumerName", "consumer_2")
					.usingJobData("queueName", queueName)
					.usingJobData("amazonMQSSLEndPoint", amazonMQSSLEndPoint)
					.usingJobData("username", username)
					.usingJobData("password", password)
					.usingJobData("useCaseId", useCaseId)
					.usingJobData("numMsgs", numMsgs * 4) // this consumer job makes consumer.receive(50); calls 20000 times
					.usingJobData("prefetchSize", prefetchSize).build();
			JobDetail job6 = newJob(MsgConsumer_CustomPrefetch.class).withIdentity(useCaseId.concat("-").concat("consumer-job3"), "group1")
					.usingJobData("consumerName", "consumer_3")
					.usingJobData("queueName", queueName)
					.usingJobData("amazonMQSSLEndPoint", amazonMQSSLEndPoint)
					.usingJobData("username", username)
					.usingJobData("password", password)
					.usingJobData("useCaseId", useCaseId)
					.usingJobData("numMsgs", numMsgs * 4) // this consumer job makes consumer.receive(50); calls 20000 times
					.usingJobData("prefetchSize", prefetchSize).build();
			// Trigger the jobs
			Trigger trigger1 = newTrigger().withIdentity("trigger1", "group1").startNow()
					.withSchedule(simpleSchedule()
					.withRepeatCount(0)).build();
			Trigger trigger2 = newTrigger().withIdentity("trigger2", "group1").startNow()
					.withSchedule(simpleSchedule()
					.withRepeatCount(0))
					.build();
			Trigger trigger3 = newTrigger().withIdentity("trigger3", "group1").startNow()
					.withSchedule(simpleSchedule()
					.withRepeatCount(0)).build();
			Trigger trigger4 = newTrigger().withIdentity("trigger4", "group1").startNow()
					.withSchedule(simpleSchedule()
					.withRepeatCount(0))
					.build();
			Trigger trigger5 = newTrigger().withIdentity("trigger5", "group1").startNow()
					.withSchedule(simpleSchedule()
					.withRepeatCount(0)).build();
			Trigger trigger6 = newTrigger().withIdentity("trigger6", "group1").startNow()
					.withSchedule(simpleSchedule()
					.withRepeatCount(0))
					.build();
			logger.info("scheduling producer jobs");
			scheduler.scheduleJob(job1, trigger1);
			scheduler.scheduleJob(job2, trigger2);
			scheduler.scheduleJob(job3, trigger3);
			
			logger.info("scheduling consumer jobs");
			logger.info("scheduling the first consumer job");
			scheduler.scheduleJob(job4, trigger4);
			
			int sleepInterval = 30000;
			System.out.printf("Main thread sleeping for %d seonds before launching the second consumer. \n", sleepInterval/1000);
			Thread.sleep(sleepInterval);
			logger.info("scheduling the second consumer job");
			scheduler.scheduleJob(job5, trigger5);
			
			System.out.printf("Main thread sleeping for %d seonds before launching the third consumer. \n", sleepInterval/1000);
			Thread.sleep(sleepInterval);
			logger.info("scheduling the third consumer job");
			scheduler.scheduleJob(job6, trigger6);
			
			Thread.sleep(180000 * 2); // Sleep for sometime before shutting down scheduled tasks. 180000 = 3 minutes
			
			logger.info("unscheduling jobs and stopping the scheduler. Use case id: " + useCaseId);
			scheduler.unscheduleJob(trigger1.getKey());
			scheduler.unscheduleJob(trigger2.getKey());
			scheduler.unscheduleJob(trigger3.getKey());
			scheduler.unscheduleJob(trigger4.getKey());
			scheduler.unscheduleJob(trigger5.getKey());
			scheduler.unscheduleJob(trigger6.getKey());
			scheduler.shutdown();
		} catch (SchedulerException se) {
			se.printStackTrace();
		}
	}
}
