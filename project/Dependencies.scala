import sbt.{ExclusionRule, _}

object Dependencies {
  val scalaV = "2.11.8"
  val sparkV = "2.4.0"
  val hadoopAwsV = "2.8.3"
  val scalaTestV = "3.0.5"
  val kafkaConnectV = "1.0.1"
  val redshiftV = "1.2.12.1017"
  val configV = "1.3.2"
  val mysqlConnectorV = "6.0.6"
  val jacksonV = "2.9.5"

  val jacksonDependencies = Seq(
    "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonV,
    "com.fasterxml.jackson.core" % "jackson-databind" % jacksonV,
    "com.fasterxml.jackson.core" % "jackson-core" % jacksonV,
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonV
  )

  val nettyDependencies = Seq("io.netty" % "netty-all" % "4.1.17.Final")

//  val kafkaClients = Seq("org.apache.kafka" % "kafka-clients" % "1.0.1" excludeAll (ExclusionRule(organization = "net.jpountz.lz4", name = "lz4")))
  val kafka = Seq("org.apache.kafka" %% "kafka" % "0.8.2.1", "org.apache.spark" %% "spark-streaming-kafka-0-8" % "2.2.2" excludeAll ExclusionRule(organization = "net.jpountz.lz4", name = "lz4"))

  val sparkDependencies = Seq(
    "org.apache.spark" %% "spark-core" % sparkV % "provided",
    "org.apache.spark" %% "spark-streaming" % sparkV % "provided",
    "org.apache.spark" %% "spark-sql" % sparkV % "provided",
    "org.apache.spark" %% "spark-hive" % sparkV % "provided"
  )


  val cassandraConnector = Seq(
    "com.datastax.spark" %% "spark-cassandra-connector" % "2.3.0"
  )

  val configDepenedencies = Seq(
    "com.typesafe" % "config" % configV,
    "com.github.pureconfig" %% "pureconfig" % "0.9.1"
  )

  val scalaTestDependencies = Seq(
    "org.scalactic" %% "scalactic" % scalaTestV % "test",
    "org.scalatest" %% "scalatest" % scalaTestV % "test"
  )

}
