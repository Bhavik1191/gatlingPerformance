import io.gatling.core.Predef._
import io.gatling.http.Predef._



class MyFirstClass extends Simulation{


  //1. Http conf

  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept","application/json")


  //2 Scenario Conf

  val scenrioConf = scenario("MY first class")
    .exec(http("get video games")
          .get("videogames"))



  //3 Load scenario

  setUp(
    scenrioConf.inject(atOnceUsers(1))
  ).protocols(httpConf)


}
