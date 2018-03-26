package com.ww.calculcator.service

import javax.inject.Inject

import com.twitter.util.Future
import com.ww.calculcator.dao.DeliveryAddressRepository
import com.ww.calculcator.domain.DeliveryAddress

class DeliveryAddressService @Inject()(val deliveryAddressRepository: DeliveryAddressRepository) {


  def getDeliverInfo(): Future[(Option[DeliveryAddress], Option[DeliveryAddress])] = {
    val value = deliveryAddressRepository.ascFirst()

    val result: Future[(Option[DeliveryAddress], Option[DeliveryAddress])] = for {
      first <- deliveryAddressRepository.descFirst()
      last <- deliveryAddressRepository.ascFirst()
    } yield (first, last)
    result
  }

  def getFirst(): Future[Option[DeliveryAddress]] = {
    deliveryAddressRepository.ascFirst()
  }

}
