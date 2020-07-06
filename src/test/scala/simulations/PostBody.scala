package simulations


import java.time.LocalDate
import java.time.format.DateTimeFormatter

import io.gatling.http.Predef._
import io.gatling.core.Predef._

import scala.util.Random


class PostBody extends Simulation{

  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept","application/json")


  val rnd = new Random()

  val ids = (11 to 20).iterator

  def randomString(length :Int ) = {
    rnd.alphanumeric.filter(_.isLetter).take(length).mkString
  }

  val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  def getRandomDate(startDate:LocalDate,random:Random)={
    startDate.minusDays(random.nextInt(30)).format(pattern)
  }

  val customFeeder = Iterator.continually(Map(

    "gameId" -> ids.next(),
    "name" -> ("Game-" + randomString(5)),
    "releaseDate" -> getRandomDate(LocalDate.now(),rnd),
    "reviewScore" -> rnd.nextInt(100),
    "category" -> randomString(10),
    "rating" -> randomString(8)
  ))



  def postGame()= {
    repeat(5) {
      feed(customFeeder)
        .exec(http("Post Game")
          .post("videogames/")
            .body(ElFileBody("/Users/bhavikshah/Documents/Workspace/gatling3-fundamentals/src/test/scala/bodies/VideoGamePostRequest.json")).asJson
          .check(status.is(200))
        ).pause(1)
    }
  }



//
//  {
//    "id": 0,
//    "name": "string",
//    "releaseDate": "2020-07-04T06:13:15.340Z",
//    "reviewScore": 0,
//    "category": "string",
//    "rating": "string"
//  }



  val scenarioConf = scenario("Post call with body in Json file")
      .exec(postGame())


  setUp(
    scenarioConf.inject(atOnceUsers(1))
  ).protocols(httpConf)


}
