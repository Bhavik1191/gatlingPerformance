package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._



class MethodsCall extends Simulation{

  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept","application/json")


  val scenarioConf = scenario("multiple request with ass pause time")
    .exec(getAllVideoGame())
    .pause(10)
    .exec(getSpecificVideoGame())
    .pause(10)
    .exec(getAllVideoGame())

//    .exec(http("get video game")
//      .get("videogames"))
//
//
//    .exec(http("get first video game")
//      .get("videogames/1"))
//    .pause(1,10)
//
//
//    .exec(http("get video game")
//      .get("videogames"))


  def getAllVideoGame()={
    repeat(3){
      exec(http("get video game")
        .get("videogames")
        .check(status.is(200)))
    }
  }


  def getSpecificVideoGame()={
    repeat(5){
      exec(http("get first video game")
        .get("videogames/1")
        .check(status.in(200 to 210)))
    }
  }




  setUp(
    scenarioConf.inject(atOnceUsers(1))
  ).protocols(httpConf)


}
