package com.deliveryhero

import com.deliveryhero.domain.Restaurant

/**
  * @author Andrei Tupitcyn
  */
object algebra {
  trait RestaurantRepositoryAlg[F[_]] {
    def get(id: Long): F[Option[Restaurant]]
    def getAll(): F[List[Restaurant]]
    def create(
        name: String,
        phone: String,
        cuisines: Seq[String],
        address: String,
        description: String
    ): F[Restaurant]

    def update(restaurant: Restaurant): F[Unit]
    def delete(id: Long): F[Unit]
  }
}
