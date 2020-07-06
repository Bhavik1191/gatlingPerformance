package simulations

import io.gatling.http.Predef._
import io.gatling.core.Predef._

class CustomFeeders extends Simulation {

  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept","application/json")



  var idNumbers = (1 to 10).iterator

  val customFeeder = Iterator.continually(Map("gameId" -> idNumbers.next()))

  def getVideoGameWithCustomFeeders()={
    repeat(10){
      feed(customFeeder)
      .exec(http("Get video game")
      .get("videogames/${gameId}")  //
      .check(status.is(200)))
        .pause(1)
    }
  }


  val scenarioConf = scenario("multiple request with ass pause time")
      .exec(getVideoGameWithCustomFeeders())

  setUp(
    scenarioConf.inject(atOnceUsers(1))
  ).protocols(httpConf)






}
