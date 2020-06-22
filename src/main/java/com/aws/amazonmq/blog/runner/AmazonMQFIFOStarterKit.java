// Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package com.aws.amazonmq.blog.runner;

import com.aws.amazonmq.blog.testcases.FIFO_Testcase_1;
import com.aws.amazonmq.blog.testcases.FIFO_Testcase_2;
import com.aws.amazonmq.blog.testcases.FIFO_Testcase_3;
import com.aws.amazonmq.blog.testcases.FIFO_Testcase_4;
import com.aws.amazonmq.blog.testcases.FIFO_Testcase_5;
import com.aws.amazonmq.blog.testcases.FIFO_Testcase_6;
import com.aws.amazonmq.blog.testcases.FIFO_Testcase_8;
import com.aws.amazonmq.blog.testcases.FIFO_Testcase_7;

/**
 * This is a driver program to run test cases This program needs Amazon MQ
 * broker URL as an argument It is usually in the form of
 * ssl://b-Xxxxxxxxxxxxxxxxxxxxxxxx-550217887828-1.mq.us-east-1.amazonaws.com:61617
 * 
 * The other arguments needed to run the program are hard coded in this file.
 * Modify them per your requirement.
 * 
 * @author itharav
 *
 */
public class AmazonMQFIFOStarterKit {

	public static void main(String[] args) {
		// program arguments
		String amazonMQSSLEndPoint = args[0];// OpenWire Endpoint for AmazonMQ Broker
		String amazonMQUsername = args[1]; // Username
		String amazonMQPassword = args[2]; // Password
		String numMsgs = args[3]; // number of messages to be inserted by each producer
		String customPrefetch = "1";
		String queue_1 = "queue_1";
		String queue_2 = "queue_2";
		String queue_3 = "queue_3";
		String queue_4 = "queue_4";
		String queue_5 = "queue_5";
		String queue_6 = "queue_6";
		String queue_7 = "queue_7";
		String queue_8 = "queue_8";

		String[] useCaseIds = new String[] { "FIFO_Testcase_1", "FIFO_Testcase_2", "FIFO_Testcase_3", "FIFO_Testcase_4",
				"FIFO_Testcase_5", "FIFO_Testcase_6", "FIFO_Testcase_7", "FIFO_Testcase_8"};
		String[] t1_args = new String[] { amazonMQSSLEndPoint, amazonMQUsername, amazonMQPassword, queue_1, numMsgs,
				useCaseIds[0], customPrefetch };
		String[] t2_args = new String[] { amazonMQSSLEndPoint, amazonMQUsername, amazonMQPassword, queue_2, numMsgs,
				useCaseIds[1], customPrefetch };
		String[] t3_args = new String[] { amazonMQSSLEndPoint, amazonMQUsername, amazonMQPassword, queue_3, numMsgs,
				useCaseIds[2], customPrefetch };
		String[] t4_args = new String[] { amazonMQSSLEndPoint, amazonMQUsername, amazonMQPassword, queue_4, numMsgs,
				useCaseIds[3], customPrefetch };
		String[] t5_args = new String[] { amazonMQSSLEndPoint, amazonMQUsername, amazonMQPassword, queue_5, numMsgs,
				useCaseIds[4], customPrefetch };
		String[] t6_args = new String[] { amazonMQSSLEndPoint, amazonMQUsername, amazonMQPassword, queue_6, numMsgs,
				useCaseIds[5], customPrefetch };
		String[] t7_args = new String[] { amazonMQSSLEndPoint, amazonMQUsername, amazonMQPassword, queue_7, numMsgs,
				useCaseIds[6], customPrefetch };
		String[] t8_args = new String[] { amazonMQSSLEndPoint, amazonMQUsername, amazonMQPassword, queue_8, numMsgs,
				useCaseIds[7], customPrefetch };
		try {
			/**
			 * FIFO Test Case 1,
			 * Number of queue(s) = 1,
			 * Number of producers = 3,
			 * Number of message groups = 3,
			 * Number of consumers = 1,
			 * Custom Pre-fetch size = 1,
			 * Producers start time = T seconds,
			 * Consumer start time = T seconds
			 * Consumer end time   => C1 = T+A+B seconds
			 */
			System.out.println("Start: Test case - " + useCaseIds[0]);
			FIFO_Testcase_1.main(t1_args);
			System.out.println("End: Test case - " + useCaseIds[0]);

			/**
			 * FIFO Test Case 2,
			 * Number of queue(s) = 1,
			 * Number of producers = 3,
			 * Number of message groups = 3,
			 * Number of consumers = 2,
			 * Custom Pre-fetch size = 1, 
			 * Producers start time = T seconds,
			 * Consumers start time = T seconds
			 * Consumers end time   => C1 = T+A+B seconds, C2 = T+A+B seconds
			 */
			System.out.println("Start: Test case - " + useCaseIds[1]);
			FIFO_Testcase_2.main(t2_args);
			System.out.println("End: Test case - " + useCaseIds[1]);

			/**
			 * FIFO Test Case 3,
			 * Number of queue(s) = 1,
			 * Number of producers = 3,
			 * Number of message groups = 3,
			 * Number of consumers = 3,
			 * Custom Pre-fetch size = 1,
			 * Producers start time = T seconds,
			 * Consumers start time = T seconds,
			 * Consumers end time   => C1 = T+A+B seconds, C2 = T+A+B seconds, C3 = T+A+B seconds
			 */
			System.out.println("Start: Test case - " + useCaseIds[2]);
			FIFO_Testcase_3.main(t3_args);
			System.out.println("End: Test case - " + useCaseIds[2]);

			/**
			 * FIFO Test Case 4,
			 * Number of queue(s) = 1,
			 * Number of producers = 3,
			 * Number of message groups = 3,
			 * Number of consumers = 4,
			 * Custom Pre-fetch size = 1, 
			 * Producers start time = T seconds,
			 * Consumers start time = T seconds,
			 */
			System.out.println("Start: Test case - " + useCaseIds[3]);
			FIFO_Testcase_4.main(t4_args);
			System.out.println("End: Test case - " + useCaseIds[3]);

			/**
			 * FIFO Test Case 5,
			 * Number of queue(s) = 1,
			 * Number of producers = 4,
			 * Number of message groups = 3,
			 * Number of consumers = 3,
			 * Custom Pre-fetch size = 1,
			 * Producers start time = T seconds,
			 * Consumers start time = T seconds,
			 */
			System.out.println("Start: Test case - " + useCaseIds[4]);
			FIFO_Testcase_5.main(t5_args);
			System.out.println("End: Test case - " + useCaseIds[4]);

			/**
			 * FIFO Test Case 6,
			 * Number of queue(s) = 1,
			 * Number of producers = 3,
			 * Number of message groups = 3,
			 * Number of consumers = 3,
			 * Custom Pre-fetch size = 1, 
			 * Producers start time = T seconds,
			 * Consumers start time => C1 = T seconds, C2 = T+30 seconds, C3 = T+60 seconds,
			 */
			System.out.println("Start: Test case - " + useCaseIds[5]);
			FIFO_Testcase_6.main(t6_args);
			System.out.println("End: Test case - " + useCaseIds[5]);
			
			/**
			 * FIFO Test Case 7,
			 * Number of queue(s) = 1,
			 * Number of producers = 3,
			 * Number of message groups = 3,
			 * Number of consumers = 3,
			 * Custom Pre-fetch size = 1, 
			 * Producers start time = T seconds,
			 * Consumers start time => C1 = T seconds, C2 = T+30 seconds, C3 = T+60 seconds
			 * Note: C1 is terminated before C2 and C3
			 */
			System.out.println("Start: Test case - " + useCaseIds[6]);
			FIFO_Testcase_7.main(t7_args);
			System.out.println("End: Test case - " + useCaseIds[6]);
			
			System.out.println("Start: Test case - " + useCaseIds[7]);
			FIFO_Testcase_8.main(t8_args);
			System.out.println("End: Test case - " + useCaseIds[7]);

			Thread.sleep(180000 * 2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}