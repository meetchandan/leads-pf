package kafka

import java.util.concurrent.TimeUnit
import java.util.{Properties, Random}

import com.google.gson.Gson
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.joda.time.DateTime

import scala.concurrent.TimeoutException

case class Lead(name: String, email: String, location: String, datetime: String = DateTime.now().toString)


object Producer extends App {
  private def getProducer = {
    val props = new Properties()
    props.put("bootstrap.servers", util.Config.kafkaHostPort)
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    new KafkaProducer[String, String](props)
  }

  val topic = "leads"
  val producer = getProducer
  val rnd = new Random
  val names = (0 to 70).map(a => "user " + a)
  val location = (0 to 1000).map(a => "location " + a)
  val gson = new Gson()
  while (true) {
    try {
      val nameIndex = rnd.nextInt(70)
      val locIndex = rnd.nextInt(1000)
      val leadObj = Lead(names(nameIndex), names(nameIndex) + "@pf.com", location(locIndex))
      producer.send(new ProducerRecord[String, String](topic, gson.toJson(leadObj))).get(1, TimeUnit.SECONDS)
      Thread.sleep(10)
    } catch {
      case ex: TimeoutException =>
        println("Exception")
        println(ex.printStackTrace())
    }
  }
}
