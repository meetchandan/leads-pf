package api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import cassandra.CassandraRepo


object c extends CassandraRepo

object getlastHourCount {
  val route =
    path("lastHourCount") {
      get {
        complete {
          "Last Hour Count" + c.getLastHourCount()
        }
      }
    }
}

object getCurrentDayCount {
  val route =
    path("dayCount") {
      get {
        complete {
          "Last Day Count" + c.getCurrentDayCount()
        }
      }
    }
}

object getTopLocationsHour {
  val route =
    path("topLocationsHour") {
      get {
        complete {
          c.getTop10LocationsHour()
        }
      }
    }
}


object getTopLocationsDay {
  val route =
    path("topLocationsDay") {
      get {
        complete {
          c.getTop10LocationsDay()
        }
      }
    }
}


object getAllReports {
  val route =
    path("getAll") {
      get {
        complete {
          c.getLastHourCount() + "\n\n" +
          c.getCurrentDayCount() + "\n\n" +
          c.getTop10LocationsHour() + "\n\n" +
          c.getTop10LocationsDay()
        }
      }
    }
}

object AkkaHttpHelloWorld {

  def main(args: Array[String]) {

    implicit val actorSystem = ActorSystem("system")
    implicit val actorMaterializer = ActorMaterializer()

    val route =
      getlastHourCount.route ~
      getCurrentDayCount.route ~
      getTopLocationsDay.route ~
      getTopLocationsHour.route ~
      getAllReports.route
    val port = 9001
    Http().bindAndHandle(route, "localhost", port)

    println(s"server started at $port with the below API's")
    println(s"http://localhost:$port/lastHourCount")
    println(s"http://localhost:$port/dayCount")
    println(s"http://localhost:$port/topLocationsHour")
    println(s"http://localhost:$port/topLocationsDay")
    println(s"http://localhost:$port/getAll")
  }
}