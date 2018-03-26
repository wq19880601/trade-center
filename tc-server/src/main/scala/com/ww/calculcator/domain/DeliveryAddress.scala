package com.ww.calculcator.domain

import java.util.Date

case class DeliveryAddress(id: Int, uid: Int, province: Int,
                           city: Int, receiverUser: Option[String] = None,
                           receiverMobile: Int, addrDistinct: String, addrDetail: String,
                           isDefault: Byte = 0, areaCode: Option[Int] = None, isDeleted: Byte,
                           createTime: Date, modifyTime: Date)
