package com.ww.calculcator.db

import com.ww.calculcator.domain.DeliveryAddress
import com.ww.calculcator.finatra.QuillDatabaseModule.QuillDatabaseSource

trait TableSchema {

  val ctx: QuillDatabaseSource

  import ctx._

  val deliveryAddressTable: ctx.Quoted[ctx.EntityQuery[DeliveryAddress]] = quote {
    querySchema[DeliveryAddress]("delivery_address")
  }
}
