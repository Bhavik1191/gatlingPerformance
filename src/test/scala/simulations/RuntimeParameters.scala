package simulations

import io.gatling.http.Predef._
import io.gatling.core.Predef._
import scala.concurrent.duration._

class RuntimeParameters extends Simulation{

  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept","application/json")



  private def getProperty(propertyName: String, defaultValue: String)={

    Option(System.getenv(propertyName))
      .orElse(Option(System.getProperty(propertyName)))
      .getOrElse(defaultValue)
  }


  def users: Int = getProperty("users","10").toInt
  def ramupPeriod: Int = getProperty("rampupPeriod","10").toInt
  def duration: Int = getProperty("duration","20").toInt



  before {
    println("Users :" + users)
    println("ramupPeriod :" + ramupPeriod)
    println("duration :" + duration)
  }

  def getAllVideoGames()={
    exec(
      http("Get all video games")
        .get("videogames")
        .check(status.is(200))
    )
  }

  val scenarioConf = scenario("Get parameters from Command line")
      .forever(
        exec(getAllVideoGames())
          .pause(1)
      )

  setUp(
    scenarioConf.inject(
      atOnceUsers(1),
      rampUsers(users) during(ramupPeriod seconds)
    )
  ).protocols(httpConf)
    .maxDuration(duration seconds)

}
