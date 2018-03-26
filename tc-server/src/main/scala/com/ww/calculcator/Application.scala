package com.ww.calculcator

import com.twitter.finagle.ThriftMux
import com.twitter.finatra.thrift.ThriftServer
import com.twitter.finatra.thrift.exceptions.FinatraThriftExceptionMapper
import com.twitter.finatra.thrift.filters._
import com.twitter.finatra.thrift.routing.ThriftRouter
import com.twitter.util.TimeConversions._
import com.ww.calculcator.finatra.{FinatraTypeSafeConfigModule, QuillDatabaseModule, UserServiceThriftClientModule}


object ApplicationMain extends Application

class Application extends ThriftServer {

  override def modules = Seq(
    FinatraTypeSafeConfigModule, QuillDatabaseModule, UserServiceThriftClientModule)


  private[this] val serviceLabel = "trade-center"

  override val name: String = serviceLabel


  override protected def defaultFinatraThriftPort: String = ":9998"


  override def defaultAdminPort: Int = 9091


  override protected def configureThrift(router: ThriftRouter): Unit = {
    router
      .filter[LoggingMDCFilter]
      .filter[TraceIdMDCFilter]
      .filter[ThriftMDCFilter]
      .filter[AccessLoggingFilter]
      .filter[StatsFilter]
      .filter[ExceptionMappingFilter]
      .exceptionMapper[FinatraThriftExceptionMapper]
      .add[TradeCenterController]
  }

  override protected def configureThriftServer(server: ThriftMux.Server): ThriftMux.Server = {
    server.withLabel(serviceLabel).withRequestTimeout(10.seconds).withAdmissionControl.concurrencyLimit(10, 1000)
  }
}
