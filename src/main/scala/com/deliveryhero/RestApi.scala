package com.deliveryhero

import akka.actor.ActorSystem
import akka.http.scaladsl.marshalling.{Marshaller, ToResponseMarshaller}
import akka.http.scaladsl.server.{HttpApp, Route}
import cats.Id
import com.deliveryhero.interepter.InMemoryRestaurantRepository
import com.deliveryhero.service.RestaurantService
import com.typesafe.config.ConfigFactory
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport

/**
  * @author Andrei Tupitcyn
  */
object RestApi extends HttpApp {
  implicit val system = ActorSystem("restaurantApi")

  val restaurantRepo = new InMemoryRestaurantRepository
  val restaurantService = new RestaurantService(restaurantRepo)

  implicit def idMarshaller[A](implicit aMarshaller: ToResponseMarshaller[A])
    : ToResponseMarshaller[Id[A]] =
    Marshaller.oneOf(aMarshaller)

  case class RestaurantEntity(
      name: String,
      phone: String,
      cuisines: Seq[String],
      address: String,
      description: String
  )

  case class RestaurantOptionalEntity(
      name: Option[String] = None,
      phone: Option[String] = None,
      cuisines: Option[Seq[String]] = None,
      address: Option[String] = None,
      description: Option[String] = None
  )

  import FailFastCirceSupport._
  import io.circe.generic.auto._

  override def routes: Route =
    pathPrefix("v1") {
      pathPrefix("restaurants") {
        rejectEmptyResponse {
          pathEndOrSingleSlash {
            get {
              complete {
                restaurantService.getAll()
              }
            } ~
              post {
                entity(as[RestaurantEntity]) { r =>
                  complete {
                    restaurantService.newRestaurant(
                      name = r.name,
                      phone = r.phone,
                      cuisines = r.cuisines,
                      address = r.address,
                      description = r.description
                    )
                  }
                }
              }
          } ~
            path(LongNumber) { id =>
              {
                get {
                  complete {
                    restaurantService.getRestaurant(id)
                  }
                } ~
                  put {
                    entity(as[RestaurantOptionalEntity]) { r =>
                      complete {
                        restaurantService.updateRestaurant(
                          id = id,
                          name = r.name,
                          phone = r.phone,
                          cuisines = r.cuisines,
                          address = r.address,
                          description = r.description
                        )
                      }
                    }
                  } ~
                  delete {
                    complete {
                      restaurantService.deleteRestaurant(id)
                    }
                  }
              }
            }
        }
      } ~
      path("healthcheck") {
        get {
          complete(Unit)
        }
      }
    }

  def run(host: String, port: Int): Unit = {
    startServer(host, port, system)
    system.terminate()
  }

  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load().getConfig("http")
    run(config.getString("host"), config.getInt("port"))
  }
}
