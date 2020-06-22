// Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0

package com.aws.amazonmq.blog.util;

import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.util.List;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

public class CSVUtil {

	public static void writePutMetricsToFile(Path path, List<PutMsgMetric> putMsgMetricList) throws Exception {
		Writer writer = new FileWriter(path.toString());
		StatefulBeanToCsv<PutMsgMetric> sbc = new StatefulBeanToCsvBuilder<PutMsgMetric>(writer)
				.withSeparator(CSVWriter.DEFAULT_SEPARATOR).build();
		sbc.write(putMsgMetricList);
		writer.close();
		writer.close();
	}

	public static void writeGetMetricsToFile(Path path, List<GetMsgMetric> getMsgMetricList) throws Exception {
		Writer writer = new FileWriter(path.toString());
		StatefulBeanToCsv<GetMsgMetric> sbc = new StatefulBeanToCsvBuilder<GetMsgMetric>(writer)
				.withSeparator(CSVWriter.DEFAULT_SEPARATOR).build();
		sbc.write(getMsgMetricList);
		writer.close();
		writer.close();
	}
}
