// Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package com.aws.amazonmq.blog.util;

public class GetMsgMetric {

	String msg_id;
	String usecase_id;
	String msg_body;
	String msg_group;
	String consumer_id;
	String time_to_get_in_millis;
	String time_of_get_currenttime_millis;

	public String getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}

	public String getMsg_body() {
		return msg_body;
	}

	public void setMsg_body(String msg_body) {
		this.msg_body = msg_body;
	}

	public String getMsg_group() {
		return msg_group;
	}

	public void setMsg_group(String msg_group) {
		this.msg_group = msg_group;
	}

	public String getConsumer_id() {
		return consumer_id;
	}

	public void setConsumer_id(String consumer_id) {
		this.consumer_id = consumer_id;
	}

	public String getTime_to_get_in_millis() {
		return time_to_get_in_millis;
	}

	public void setTime_to_get_in_millis(String time_to_get_in_millis) {
		this.time_to_get_in_millis = time_to_get_in_millis;
	}

	public String getTime_of_get_currenttime_millis() {
		return time_of_get_currenttime_millis;
	}

	public void setTime_of_get_currenttime_millis(String time_of_get_currenttime_millis) {
		this.time_of_get_currenttime_millis = time_of_get_currenttime_millis;
	}

	public String getUsecase_id() {
		return usecase_id;
	}

	public void setUsecase_id(String usecase_id) {
		this.usecase_id = usecase_id;
	}
}