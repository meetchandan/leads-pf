package util

import com.typesafe.config.ConfigFactory
import scala.collection.JavaConversions._

object Config {

  val config = ConfigFactory.load()
  val port = config.getInt("cassandra.port")
  val hosts = config.getStringList("cassandra.hosts").toList
  val replicationFactor = config.getString("cassandra.replication_factor").toInt
  val readConsistency = config.getString("cassandra.read_consistency")
  val writeConsistency = config.getString("cassandra.write_consistency")

  val kafkaHostPort = config.getString("kafka_host")


}
