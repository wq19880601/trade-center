package com.ww.calculcator.dao

import javax.inject.Inject

import com.twitter.util.Future
import com.ww.calculcator.db.TableSchema
import com.ww.calculcator.domain.DeliveryAddress
import com.ww.calculcator.finatra.QuillDatabaseModule.QuillDatabaseSource

class DeliveryAddressRepository @Inject()(val ctx: QuillDatabaseSource) extends TableSchema {

  import ctx._

  def descFirst(): Future[Option[DeliveryAddress]] = {

    val value: Future[Option[DeliveryAddress]] = ctx.run {
      quote {
        query[DeliveryAddress].sortBy(p => p.createTime)(Ord.desc)
      }.take(1)
    }.map(_.headOption)
    value
  }

  def ascFirst(): Future[Option[DeliveryAddress]] = {
    ctx.run {
      quote {
        query[DeliveryAddress].sortBy(p => p.createTime)(Ord.asc)
      }
    }.map(_.headOption)
  }

}
