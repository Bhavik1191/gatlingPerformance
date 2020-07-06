package simulations

//Add gatling's basic requirements
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import io.gatling.http.Predef._
import io.gatling.core.Predef._
import io.gatling.core.scenario.Scenario
import scala.concurrent.duration._


import scala.util.Random

/**
  * Always extends Simulation class
  */
class CompleteFunctionalities extends Simulation{


  // 1. Http config
  /**
    * Add base URL & ad header here in one val
    */


  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept","application/json")


  // Define all the parameters required for the scripts
  val ids = (15 to 20).iterator
  val gameId = ids.next()
  val id = gameId
  val rnd = new Random()
  val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  def getRandomString(length: Int)={
    rnd.alphanumeric.filter(_.isLetter).take(length).mkString
  }

  def getRandomDate(startDate:LocalDate,random:Random)={
    startDate.minusDays(random.nextInt(30)).format(pattern)
  }
  // If there are any data feed required then create custom feeder over here

  val customFeeder = Iterator.continually(Map(
    "gameId" -> id,
    "name" -> ("Game-"+getRandomString(5)),
    "releaseDate" -> getRandomDate(LocalDate.now(),rnd),
    "reviewScore" -> rnd.nextInt(10),
    "category" -> ("category-"+getRandomString(5)),
    "rating"->("rating-"+getRandomString(7))
  ))

  // Write Before section here, It can be wrote anywhere. but this is good to read the script
  // Things like system variable or other informative things will be print or assigned here
  before
  {
    println("Game id for new creation : "+ gameId)


  }




  // 2. Scenario Definition

  /**
    * Create all defs and scenario here
    * For multiple requests first define method of that request and then use them in scenario definition
    */


  // method for get all games

  def getAllGames()={
    exec(
      http("Get All Video Games")
        .get("videogames")
        .check(status.is(200))
    )
  }


  // method for create new game

  def createNewGame()={
    feed(customFeeder)
      .exec(
        http("Create New Game")
          .post("videogames/")
          .body(ElFileBody("/Users/bhavikshah/Documents/Workspace/gatling3-fundamentals/src/test/scala/bodies/VideoGamePostRequest.json")).asJson
//          .body(StringBody(
//          "{\n  \"id\": 16,\n  \"name\": \"string\",\n  \"releaseDate\": \"2020-07-05T10:51:22.360Z\",\n  \"reviewScore\": 0,\n  \"category\": \"string\",\n  \"rating\": \"string\"\n}"
//        )).asJson
          .check(status.is(200))
          .check(bodyString.saveAs("BODY"))
      )
      .exec(session => {
        val response = session("BODY").as[String]
        println(s"Response body: \n$response")
        session
      })
  }


  // method for get single game defined above

  def getSingleVideoGame()={
    exec(
      http("Get a single video game")
        .get("videogames/"+id)
        .check(status.is(200))
        .check(bodyString.saveAs("BODY"))
    )
      .exec(session => {
        val response = session("BODY").as[String]
        println(s"Response body: \n$response")
        session
      })
  }



  // delete game which is defined above

  def deleteNewGame()={
    exec(
      http("Delete newly created game")
        .delete("videogames/"+id)
        .check(status.is(200))
        .check(bodyString.saveAs("BODY"))
    )
      .exec(session => {
        val response = session("BODY").as[String]
        println(s"Response body: \n$response")
        session
      })
  }


  // scenario for including all above four methods
  val scn = scenario("Final test with all the APIs")
    .forever{
        exec(getAllGames())
        .pause(1)
        .exec(createNewGame())
        .pause(1)
        .exec(getSingleVideoGame())
        .pause(1)
        .exec(deleteNewGame())
        .pause(1)
    }




  // 3. Load scenario
  /**
    * Add user and time here for which the above scenario should run
    * Ramup and maxduration like all things will go under this section
    */


  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)
    .maxDuration(10 seconds)


}
