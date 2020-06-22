# Building efficient and scalable distributed applications using Amazon MQ

This is a companion source code for AWS Blog <Link to the blog>


## CloudFormation Templates & Supporting Resources
| Template                         | Purpose 	   |
|----------------------------------| -------------- |
| [CF_Template_AmazonMQBroker.yaml](./src/main/resources/cloudformation-scripts/CF_Template_AmazonMQBroker.yaml) |	Sample template to provision Amazon MQ broker |
| [broker-config-sample.xml](./src/main/resources/broker-config-sample.xml) |	Sample Broker Configuration File |


## Setup Instructions
1. Clone or download this source code
2. Import the source code to Eclipse or IntelliJ IDEA as a Maven project
3. Build the project using Maven commands
4. This will generate a jar file with name **amazonmqpoc-1.0.jar** 
5. Provision Amazon MQ Broker using [CF_Template_AmazonMQBroker.yaml](./src/main/resources/cloudformation-scripts/CF_Template_AmazonMQBroker.yaml)

 **Note:** To make the set up easy for PoC, you can provision the broker in a public subnet with a security group that has inbound access from your Laptop / EC2 instance. 
 For production deployments, check the following resources:
     
	1. [Using Amazon MQ Securely](https://docs.aws.amazon.com/amazon-mq/latest/developer-guide/using-amazon-mq-securely.html).
	2. [Accessing the ActiveMQ Web Console of a Broker without Public Accessibility](https://docs.aws.amazon.com/amazon-mq/latest/developer-guide/accessing-web-console-of-broker-without-private-accessibility.html).
	
7. Optionally, you can use AWS Management Console to provision broker by following this [AWS Blog](https://aws.amazon.com/blogs/aws/amazon-mq-managed-message-broker-service-for-activemq/)
	
8. From AWS Management Console, go to Amazon MQ, select the broker, and make a note of the below values. You will need them to access Broker console and run the PoC. 
	1. ActiveMQ Web Console URL
	2. OpenWire Endpoint URL
9.  Configure Security Group from Broker details section on AWS Console as shown in the below screenshot.
	![Alt](./src/main/resources/AmazonMQ_InboundConnection_DetailedInstructions.png)
	
## Running the PoC from IDE on your Laptop
1. Find out the Public IP address of your system and follow **step # 8** of [Setup Instructions](#setup-instructions) 
2. Run the program **AmazonMQPoC_TestRunner** with the following program arguments separated by a space
	1. Amazon MQ OpenWire Endpoint URL
	2. Amazon MQ username
	3. Amazon MQ password
	4. Number of messages to be inserted per producer

## Running the PoC from Command-line on your Laptop
1. Go to the folder **/Amazonmqpoc/target** and run the jar file. 

	```
	java -jar amazonmq-poc-0.1.jar \
	Replace_this_with_OpenWire_Endpoint_URL \
	Replace_this_with_AmazonMQ_UserName \
	Replace_this_with_AmazonMQ_Password \
	5000
	```
	
## Running the PoC from an EC2 Instance
1. Launch an EC2 instance with an IAM role that has read and write permission on DynamoDB.
2. Take the Private IP address of your EC2 instance and follow **step # 8** of [Setup Instructions](#setup-instructions) 
3. Log on to EC2 instance and do the following:

	```
	sudo yum -y install java-1.8.0-openjdk.x86_64
	mkdir amazonmq-poc
	cd amazonmq-poc/
	```
4. Copy the Jar file **amazonmqpoc-1.0.jar** from your Laptop to EC2 instance using secure copy (SCP) or PuTTY:
	
	1. An example command for SCP is below:
	```
	scp -i my_ec2_keypair.pem amazonmqpoc-1.0.jar ec2-user@IP_Address_of_EC2:/home/ec2-user/amazonmq-poc
	```
	2. To use PuTTY, refer [Connecting to Your Linux Instance from Windows Using PuTTY](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/putty.html).
5. Run the Java Application
	
	```
	java -jar amazonmqpoc-1.0.jar \
	Replace_this_with_OpenWire_Endpoint_URL \
	Replace_this_with_AmazonMQ_UserName \
	Replace_this_with_AmazonMQ_Password \
	5000
	```
6. The above step generates message metrics in CSV files. Use the following command to import them back to your Laptop for analysis.

	```
	scp -i /The_Path_To/my_ec2_keypair.pem ec2-user@IP_Address_of_EC2:/home/ec2-user/amazonmq-poc/*.csv .
	```

## ActiveMQ Web Console

You can access ActiveMQ Web Console to analyze queues and message statistics. The credentials for the Console are same as what you provided during broker setup.

## Source code Summary

### FIFO Test Case 1 to 5. Producers and Consumers operated at the same time. All consumers started at the same time.
| Test Case Details                                             | Java Class 	 |
|-------------------------------------------------------------- | -------------- |
| The driver program to execute all test cases| [AmazonMQPoCRunner](./src/main/java/com/aws/amazonmq/blog/runner/AmazonMQPoCRunner.java) |
| Queue with 3 producers, 3 message groups, 1 consumer  | [FIFO_Testcase_1](./src/main/java/com/aws/amazonmq/blog/testcases/FIFO_Testcase_1.java) |
| Queue with 3 producers, 3 message groups, 2 consumers | [FIFO_Testcase_2 ](./src/main/java/com/aws/amazonmq/blog/testcases/FIFO_Testcase_2.java) |
| Queue with 3 producers, 3 message groups, 3 consumers | [FIFO_Testcase_3](./src/main/java/com/aws/amazonmq/blog/testcases/FIFO_Testcase_3.java) |
| Queue with 3 producers, 3 message groups, 4 consumers | [FIFO_Testcase_4](./src/main/java/com/aws/amazonmq/blog/testcases/FIFO_Testcase_4.java) |
| Queue with 4 producers, 4 message groups, 3 consumers | [FIFO_Testcase_5](./src/main/java/com/aws/amazonmq/blog/testcases/FIFO_Testcase_5.java) |

### FIFO Test Case 6 - Producers and Consumers operated at the same time. One of the three consumers started first.
| Test Case Details                                             | Java Class 	   |
|-------------------------------------------------------------- | --------------   |
| Queue with 3 producers, 3 message groups, 3 consumers  | [FIFO_Testcase_6 ](./src/main/java/com/aws/amazonmq/blog/testcases/FIFO_Testcase_6.java) |

### FIFO Test Case 7 - Demonstrated message distribution improvement by closing message groups explicitly.
| Test Case Details                                             | Java Class 	   |
|-------------------------------------------------------------- | --------------   |
| Queue with 3 producers, 3 message groups, 3 consumers  | [FIFO_Testcase_7 ](./src/main/java/com/aws/amazonmq/blog/testcases/FIFO_Testcase_7.java) |

### FIFO Test Case 8 - Demonstrated message distribution improvement by closing consumer session and restarting.
| Test Case Details                                             | Java Class 	 |
|-------------------------------------------------------------- | -------------- |
| Queue with 3 producers, 3 message groups, 3 consumers | [FIFO_Testcase_8](./src/main/java/com/aws/amazonmq/blog/testcases/FIFO_Testcase_8.java) |

## Tags
Amazon MQ, Enterprise Messaging, Queues, FIFO, Message Ordering

## License Summary
This sample code is made available under the MIT license. See the LICENSE file.