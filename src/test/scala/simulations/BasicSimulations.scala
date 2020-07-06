package simulations

import io.gatling.http.Predef._
import io.gatling.core.Predef._
import scala.concurrent.duration.DurationInt

class BasicSimulations extends Simulation {

  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept","application/json")


  def getAllVideoGames()={
    exec(
      http("Get all video Games")
        .get("videogames")
        .check(status.is(200))
    )
  }


  def getSpecificGame()  =
  {
    exec(
      http("Get specific video game")
        .get("videogames/2")
        .check(status.is(200))
    )
  }


  val scenarioConf = scenario("Post call with body in Json file")
    .exec(getAllVideoGames())
    .pause(1)
    .exec(getSpecificGame())
    .pause(1)
    .exec(getAllVideoGames())


  setUp(
    scenarioConf.inject(
      nothingFor(5 seconds),
//      atOnceUsers(5),
//      rampUsers(20) during (10 seconds)
//      constantUsersPerSec(10) during(10 seconds)
      rampUsersPerSec(1)to(5) during (120 seconds)
    ).protocols(httpConf.inferHtmlResources())
  )
}
