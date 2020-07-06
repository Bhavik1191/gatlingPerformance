package simulations

import io.gatling.http.Predef._
import io.gatling.core.Predef._
import scala.concurrent.duration.DurationInt


class ResponseWithJsonPath extends Simulation{


  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept","application/json")


  val scenarioConf = scenario("multiple request with ass pause time")

    .exec(http("get specific video game")
      .get("videogames/1")
      .check(status.is(200))
      .check(jsonPath("$.name").is("Resident Evil 4")))
    .pause(5)


    .exec(http("get video game")
      .get("videogames")
      .check(status.is(200))
      .check(jsonPath("$[1].id").saveAs("gameId"))
      .check(bodyString.saveAs("BODY")))


    .exec(session => {
      val response = session("BODY").as[String]
      println(s"Response body: \n$response")
      session
    })

      //.exec { session => println(session): session }



    .exec(http("get only one video game")
      .get("videogames/${gameId}")
      .check(status.is(200))
      .check(bodyString.saveAs("Response_Body")))
    .pause(5)



  setUp(
    scenarioConf.inject(atOnceUsers(1))
  ).protocols(httpConf)




}
