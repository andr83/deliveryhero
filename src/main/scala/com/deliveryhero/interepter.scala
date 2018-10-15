package com.deliveryhero

import cats.Id
import com.deliveryhero.algebra.RestaurantRepositoryAlg

import scala.collection.immutable.LongMap

/**
  * @author Andrei Tupitcyn
  */
object interepter {
  class InMemoryRestaurantRepository extends RestaurantRepositoryAlg[Id] {
    private var restaurants = LongMap.empty[domain.Restaurant]
    private var currentId = 1l

    override def get(id: Long): Id[Option[domain.Restaurant]] =
      restaurants.get(id)

    override def getAll(): Id[List[domain.Restaurant]] =
      restaurants.values.toList

    override def create(name: String,
                        phone: String,
                        cuisines: Seq[String],
                        address: String,
                        description: String): Id[domain.Restaurant] = {
      val r = domain.Restaurant(
        id = currentId,
        name = name,
        phone = phone,
        cuisines = cuisines,
        address = address,
        description = description
      )
      restaurants += r.id -> r
      currentId += 1
      r
    }

    override def update(restaurant: domain.Restaurant): Id[Unit] = {
      restaurants += (restaurant.id -> restaurant)
    }

    override def delete(id: Long): Id[Unit] = {
      restaurants = restaurants - id
    }
  }
}
