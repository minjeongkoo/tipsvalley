/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.co.tipsvalley.tipsSaprk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import scala.Tuple2;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.apache.spark.streaming.Durations;

/**
 * Consumes messages from one or more topics in Kafka and does wordcount.
 * Usage: JavaDirectKafkaWordCount <brokers> <groupId> <topics>
 *   <brokers> is a list of one or more Kafka brokers
 *   <groupId> is a consumer group name to consume from topics
 *   <topics> is a list of one or more kafka topics to consume from
 *
 * Example:
 *    $ bin/run-example streaming.JavaDirectKafkaWordCount broker1-host:port,broker2-host:port \
 *      consumer-group topic1,topic2
 */

public final class JavaDirectKafkaWordCount {
  private static final Pattern SPACE = Pattern.compile(" ");

  public static void main(String[] args) throws Exception {
//    if (args.length < 3) {
//      System.err.println("Usage: JavaDirectKafkaWordCount <brokers> <groupId> <topics>\n" +
//                         "  <brokers> is a list of one or more Kafka brokers\n" +
//                         "  <groupId> is a consumer group name to consume from topics\n" +
//                         "  <topics> is a list of one or more kafka topics to consume from\n\n");
//      System.exit(1);
//    }
//    StreamingExamples.setStreamingLogLevels();
    
    System.setProperty("hadoop.home.dir", "C:\\spark");

    String brokers = "kafka1:9092,kafka2:9092,kafka3:9092";
    String groupId = "perter-consumer4";
    String topics = "tips_demo_sensor1";
    
    String appName = "JavaDirectKafkaWordCount5";
	String master = "spark://sparksa1:7077";
//	String master = "local[*]";

    // Create context with a 2 seconds batch interval
    SparkConf sparkConf = new SparkConf().setMaster(master).setAppName(appName);
//    sparkConf.set("spark.rpc.netty.dispatcher.numThreads","2");
    JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(10));

    Set<String> topicsSet = new HashSet<>(Arrays.asList(topics.split(",")));
    Map<String, Object> kafkaParams = new HashMap<>();
    kafkaParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
    kafkaParams.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    kafkaParams.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    kafkaParams.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    // Create direct kafka stream with brokers and topics
    JavaInputDStream<ConsumerRecord<String, String>> messages = KafkaUtils.createDirectStream(
        jssc,
        LocationStrategies.PreferConsistent(),
        ConsumerStrategies.Subscribe(topicsSet, kafkaParams));

    // Get the lines, split them into words, count the words and print
    System.out.println("lines start");
    JavaDStream<String> lines = messages.map(ConsumerRecord::value);
    System.out.println("lines end");
    
    System.out.println("words start");
    JavaDStream<String> words = lines.flatMap(x -> Arrays.asList(SPACE.split(x)).iterator());
    System.out.println("words end");
    
    JavaPairDStream<String, Integer> wordCounts = words.mapToPair(s -> new Tuple2<>(s, 1))
        .reduceByKey((i1, i2) -> i1 + i2);
    
    System.out.println("wordCounts start");
    wordCounts.print();
    System.out.println("wordCounts end");

    // Start the computation
    jssc.start();
    jssc.awaitTermination();
    
    System.out.println("Reached the end.");
  }
}