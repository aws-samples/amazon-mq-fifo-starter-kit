// Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package com.aws.amazonmq.blog.util;

public class PutMsgMetric {

	String msg_id;
	String usecase_id;
	String msg_body;
	String msg_group;
	String producer_id;
	String time_to_put_in_millis;
	String time_of_put_currenttime_millis;

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

	public String getProducer_id() {
		return producer_id;
	}

	public void setProducer_id(String producer_id) {
		this.producer_id = producer_id;
	}

	public String getTime_to_put_in_millis() {
		return time_to_put_in_millis;
	}

	public void setTime_to_put_in_millis(String time_to_put_in_millis) {
		this.time_to_put_in_millis = time_to_put_in_millis;
	}

	public String getTime_of_put_currenttime_millis() {
		return time_of_put_currenttime_millis;
	}

	public void setTime_of_put_currenttime_millis(String time_of_put_currenttime_millis) {
		this.time_of_put_currenttime_millis = time_of_put_currenttime_millis;
	}

	public String getUsecase_id() {
		return usecase_id;
	}

	public void setUsecase_id(String usecase_id) {
		this.usecase_id = usecase_id;
	}

}
