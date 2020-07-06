package simulations

import io.gatling.http.Predef._
import io.gatling.core.Predef._
import scala.concurrent.duration.DurationInt


class AddPauseTimer extends Simulation{


  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept","application/json")


  val scenarioConf = scenario("multiple request with ass pause time")

    .exec(http("get video game")
      .get("videogames"))
    .pause(5)

    .exec(http("get first video game")
      .get("videogames/1"))
    .pause(1,10)


    .exec(http("get video game")
      .get("videogames"))
    .pause(2000.milliseconds)


  setUp(
    scenarioConf.inject(atOnceUsers(1))
  ).protocols(httpConf)




}
