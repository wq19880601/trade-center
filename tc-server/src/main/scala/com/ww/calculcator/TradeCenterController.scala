package com.ww.calculcator

import javax.inject.Inject

import com.twitter.finagle.Service
import com.twitter.finatra.thrift.Controller
import com.twitter.util.Future
import com.ww.calculcator.service.DeliveryAddressService
import com.ww.tradecenter.thriftscala.Calculator
import com.ww.tradecenter.thriftscala.Calculator.{AddNumbers, AddStrings, Increment}
import com.ww.user.thriftscala.UserService

class TradeCenterController @Inject()(val deliveryAddressService: DeliveryAddressService, userService: UserService.MethodPerEndpoint)
  extends Controller with Calculator.ServicePerEndpoint {

  override def increment: Service[Increment.Args, Int] = handle(Increment) { args: Increment.Args =>
    val user = userService.getById("7193788d-b6c1-42ba-bd52-a611a2201977")
    user.map(_.id)
  }

  override def addNumbers: Service[AddNumbers.Args, Int] = handle(AddNumbers) { args: AddNumbers.Args =>
    val value = deliveryAddressService.getDeliverInfo()
    val result: Future[Int] = value.map {
      case (Some(a), Some(b)) => a.id + b.id
      case _ => 0
    }
    result
  }

  override def addStrings: Service[AddStrings.Args, String] = handle(AddStrings) { args: AddStrings.Args =>
    val value = deliveryAddressService.getDeliverInfo()
    val result: Future[String] = value.map {
      case (Some(a), Some(b)) => (a.id + b.id).toString
      case _ => "0"
    }
    result
  }
}
