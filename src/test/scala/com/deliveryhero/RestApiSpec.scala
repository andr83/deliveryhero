package com.deliveryhero

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import com.deliveryhero.domain.Restaurant
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.syntax._
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._

/**
  * @author Andrei Tupitcyn
  */
class RestApiSpec extends FlatSpec with Matchers with ScalatestRouteTest {
  implicit val timeout: RouteTestTimeout = RouteTestTimeout(10.seconds)

  import FailFastCirceSupport._
  import io.circe.generic.auto._

  val testRestaraunt = Restaurant(
    id = 1,
    name = "Kyubey",
    phone = "911",
    cuisines = Seq("Japan", "Seafood"),
    address = "Tokio",
    description = "The best!"
  )

  "A rest api" should "create restaurant via POST request" in {
    Post(
      "/v1/restaurants",
      HttpEntity(ContentTypes.`application/json`,
                 testRestaraunt.asJson.noSpaces)
    ) ~> RestApi.routes ~> check {
      status shouldEqual StatusCodes.OK
      responseAs[Restaurant] shouldBe testRestaraunt
    }

    Post(
      "/v1/restaurants",
      HttpEntity(ContentTypes.`application/json`,
                 testRestaraunt.asJson.noSpaces)
    ) ~> RestApi.routes ~> check {
      status shouldEqual StatusCodes.OK
      responseAs[Restaurant] shouldBe testRestaraunt.copy(id = 2)
    }
  }

  it should "update entity via PUT request" in {
    Put(
      "/v1/restaurants/2",
      HttpEntity(ContentTypes.`application/json`,
                 testRestaraunt.copy(address = "Kyoto").asJson.noSpaces)
    ) ~> RestApi.routes ~> check {
      status shouldEqual StatusCodes.OK
      responseAs[Restaurant] shouldBe testRestaraunt.copy(id = 2,
                                                          address = "Kyoto")
    }
  }

  it should "return entity by id via GET request" in {
    Get("/v1/restaurants/1") ~> RestApi.routes ~> check {
      status shouldEqual StatusCodes.OK
      responseAs[Restaurant] shouldBe testRestaraunt
    }

    Get("/v1/restaurants/3") ~> Route.seal(RestApi.routes) ~> check {
      status shouldEqual StatusCodes.NotFound
    }
  }

  it should "return all entities via GET request" in {
    Get("/v1/restaurants") ~> RestApi.routes ~> check {
      status shouldEqual StatusCodes.OK
      responseAs[List[Restaurant]] shouldBe
        testRestaraunt :: testRestaraunt.copy(id = 2, address = "Kyoto") :: Nil
    }
  }
}
