package spark

import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions.{col, window}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Streaming extends App {

  val conf = new SparkConf().setMaster("local[2]").setAppName("Kafka to Cassandra Real Time")
    .set("spark.cassandra.connection.host", "0.0.0.0")
    .set("spark.cassandra.auth.username", "cassandra")

  val ssc = new StreamingContext(conf, Seconds(10))
  val topicsSet = Set("leads")
  ssc.checkpoint("checkpoint")

  val kafkaParams = Map[String, String]("bootstrap.servers" -> "0.0.0.0:9092",
    "key.deserializer" -> "org.apache.kafka.common.serialization.StringSerializer",
    "value.deserializer" -> "org.apache.kafka.common.serialization.StringSerializer",
    "group.id" -> "speed_layer",
    "auto.offset.reset" -> "smallest"
  )

  val leads: DStream[String] = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet).map { case (_, message) => message }

  leads.foreachRDD { rdd: RDD[String] =>
    val spark = SparkSession.builder.config(rdd.sparkContext.getConf).getOrCreate()
    rdd.cache()
    val l: DataFrame = spark.read.json(rdd)
    if(!l.isEmpty) {
      l.cache()
      val lastHourCount = l.groupBy(window(col("datetime"), "1 hour")).count()
      lastHourCount.write.mode(SaveMode.Append)
        .format("org.apache.spark.sql.cassandra")
        .options(Map("table" -> "lead_count", "keyspace" -> "leads"))
        .save()

      val lastDayCount = l.groupBy(window(col("datetime"), "1 day")).count()
      lastDayCount.write.mode(SaveMode.Append)
        .format("org.apache.spark.sql.cassandra")
        .options(Map("table" -> "lead_count", "keyspace" -> "leads"))
        .save()

      val locHourCount = l.groupBy(col("location"), window(col("datetime"), "1 hour")).count()
      locHourCount.write.mode(SaveMode.Append)
        .format("org.apache.spark.sql.cassandra")
        .options(Map("table" -> "lead_loc_count", "keyspace" -> "leads"))
        .save()

      val locDayCount = l.groupBy(col("location"), window(col("datetime"), "1 day")).count()
      locDayCount.write.mode(SaveMode.Append)
        .format("org.apache.spark.sql.cassandra")
        .options(Map("table" -> "lead_loc_count", "keyspace" -> "leads"))
        .save()

      l.write.mode(SaveMode.Append)
        .format("org.apache.spark.sql.cassandra")
        .options(Map("table" -> "master", "keyspace" -> "leads"))
        .save()

      l.unpersist()
    }
  }

  ssc.start()
  ssc.awaitTermination()
}
