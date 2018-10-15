package com.deliveryhero

import cats.Monad
import cats.data.OptionT
import com.deliveryhero.algebra.RestaurantRepositoryAlg
import com.deliveryhero.domain.Restaurant

/**
  * @author Andrei Tupitcyn
  */
object service {
  class RestaurantService[F[_]](restaurantRepo: RestaurantRepositoryAlg[F])(
      implicit M: Monad[F]) {
    def newRestaurant(
        name: String,
        phone: String,
        cuisines: Seq[String],
        address: String,
        description: String
    ): F[Restaurant] =
      restaurantRepo.create(name, phone, cuisines, address, description)

    def updateRestaurant(
        id: Long,
        name: Option[String] = None,
        phone: Option[String] = None,
        cuisines: Option[Seq[String]] = None,
        address: Option[String] = None,
        description: Option[String] = None
    ): F[Option[Restaurant]] = {
      (for {
        r <- OptionT(restaurantRepo.get(id))
        u <- OptionT.pure {
          var res = r
          name.foreach(v => res = res.copy(name = v))
          phone.foreach(v => res = res.copy(phone = v))
          cuisines.foreach(v => res = res.copy(cuisines = v))
          address.foreach(v => res = res.copy(address = v))
          description.foreach(v => res = res.copy(description = v))
          res
        }
        _ <- OptionT.liftF(restaurantRepo.update(u))
      } yield u).value
    }

    def getRestaurant(id: Long): F[Option[Restaurant]] = restaurantRepo.get(id)

    def getAll(): F[List[Restaurant]] = restaurantRepo.getAll()

    def deleteRestaurant(id: Long): F[Option[Restaurant]] = {
      (for {
        r <- OptionT(restaurantRepo.get(id))
        _ <- OptionT.liftF(restaurantRepo.delete(r.id))
      } yield r).value
    }
  }
}
