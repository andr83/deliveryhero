package com.deliveryhero

/**
  * @author Andrei Tupitcyn
  */
object domain {
  case class Restaurant(
    id: Long,
    name: String,
    phone: String,
    cuisines: Seq[String],
    address: String,
    description: String
  )
}
