package cassandra

import com.datastax.driver.core.{Cluster, ConsistencyLevel, QueryOptions, Session}
import org.joda.time.DateTime
import util.Config.{hosts, port, replicationFactor, writeConsistency}

class CassandraRepo {

  val defaultConsistencyLevel = ConsistencyLevel.valueOf(writeConsistency)
  val cluster = new Cluster.Builder()
    .withClusterName("Cassandra Cluster")
    .addContactPoints(hosts.toArray: _*)
    .withPort(port)
    .withQueryOptions(new QueryOptions().setConsistencyLevel(defaultConsistencyLevel)).build

  val session = cluster.connect

  def getLastHourCount() = {
    val maxStart = session.execute("select max(window.start) from leads.lead_count").one().getTimestamp(0)
    val hourStart = new DateTime(maxStart).withMinuteOfHour(0)
    val hourEnd = new DateTime(maxStart).plusHours(1).withMinuteOfHour(0)
    val query = s"select window.start,window.end,count from leads.lead_count where window = {start: '$hourStart', end: '$hourEnd'}"
    val result = session.execute(query).one()
    s"From: $hourStart to $hourEnd = ${
      result.getLong(2)
    }"
  }

  def getCurrentDayCount() = {
    val maxStart = session.execute("select max(window.start) from leads.lead_count").one().getTimestamp(0)
    val dayStart = new DateTime(maxStart).withHourOfDay(4).withMinuteOfHour(0)
    val dayEnd = new DateTime(maxStart).plusDays(1).withHourOfDay(4).withMinuteOfHour(0)
    val query = s"select window.start,window.end,count from leads.lead_count where window = {start: '$dayStart', end: '$dayEnd'}"
    val result = session.execute(query).one()
    s"From: $dayStart to $dayEnd = ${
      result.getLong(2)
    }"
  }

  def getTop10LocationsHour() = {
    val maxStart = session.execute("select max(window.start) from leads.lead_loc_count").one().getTimestamp(0)
    val hourStart = new DateTime(maxStart).withMinuteOfHour(0)
    val hourEnd = new DateTime(maxStart).plusHours(1).withMinuteOfHour(0)
    /* ToDo: Order by is supported only on partition keys. Hence cannot sort by count desc and limit to 10. Find a better approach */
    val query = s"select location,count from leads.lead_loc_count where window = {start: '$hourStart', end: '$hourEnd'}"
    var tuples: List[(String, Long)] = List()
    val result = session.execute(query).all()
    (0 until result.size()).foreach(index => {
      tuples = tuples :+ ((result.get(index).getString("location"), result.get(index).getLong("count")))
    })
    var res = s"Top 10 locations for Hour $hourStart  to $hourEnd" + "\n"
    val top10 = tuples.sortWith(_._2 > _._2).take(10)
    top10.indices.foreach(index => {
      res += top10(index)._1 + "-> " + top10(index)._2 + "\n"
    })
    res
  }


  def getTop10LocationsDay() = {
    val maxStart = session.execute("select max(window.start) from leads.lead_count").one().getTimestamp(0)
    val dayStart = new DateTime(maxStart).withHourOfDay(4).withMinuteOfHour(0)
    val dayEnd = new DateTime(maxStart).plusDays(1).withHourOfDay(4).withMinuteOfHour(0)
    /* ToDo: Order by is supported only on partition keys. Hence cannot sort by count desc and limit to 10. Find a better approach */
    val query = s"select location,count from leads.lead_loc_count where window = {start: '$dayStart', end: '$dayEnd'}"
    var tuples: List[(String, Long)] = List()
    val result = session.execute(query).all()
    (0 until result.size()).foreach(index => {
      tuples = tuples :+ ((result.get(index).getString("location"), result.get(index).getLong("count")))
    })
    var res = s"Top 10 locations for Day: $dayStart  to $dayEnd" + "\n"
    val top10 = tuples.sortWith(_._2 > _._2).take(10)
    top10.indices.foreach(index => {
      res += top10(index)._1 + "-> " + top10(index)._2 + "\n"
    })
    res
  }
}
