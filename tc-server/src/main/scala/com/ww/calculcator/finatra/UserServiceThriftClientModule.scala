package com.ww.calculcator.finatra

import javax.inject.Singleton

import com.google.inject.Provides
import com.twitter.finagle.ThriftMux
import com.twitter.finagle.service.Retries.Budget
import com.twitter.finagle.service.{Backoff, Retries, RetryBudget}
import com.twitter.finagle.thrift.ClientId
import com.twitter.finagle.thriftmux.MethodBuilder
import com.twitter.finagle.thriftmux.service.ThriftMuxResponseClassifier
import com.twitter.inject.thrift.ThriftMethodBuilderFactory
import com.twitter.inject.thrift.modules.ThriftMethodBuilderClientModule
import com.twitter.util.Duration
import com.twitter.util.TimeConversions._
import com.ww.user.thriftscala.UserService

object UserServiceThriftClientModule extends ThriftMethodBuilderClientModule[UserService.ServicePerEndpoint, UserService.MethodPerEndpoint] {

  override def label: String = "user-center"

  override def dest: String = "localhost:9998"

  override protected def sessionAcquisitionTimeout: Duration = 10.minute

  override protected def requestTimeout: Duration = 10.seconds

  override protected def retryBudget: Retries.Budget = {
    val budget = RetryBudget()
    Budget(budget, Backoff.exponentialJittered(1.seconds,  50.seconds))
  }


  override protected def configureThriftMuxClient(client: ThriftMux.Client): ThriftMux.Client = {
    client.withRequestTimeout(10.seconds).withAdmissionControl.maxPendingRequests(1000).withSession.acquisitionTimeout(3.seconds)
  }

  override protected def configureMethodBuilder(methodBuilder: MethodBuilder): MethodBuilder = super.configureMethodBuilder(methodBuilder)

  override protected def configureServicePerEndpoint(builder: ThriftMethodBuilderFactory[UserService.ServicePerEndpoint],
                                                     servicePerEndpoint: UserService.ServicePerEndpoint): UserService.ServicePerEndpoint = {
    // detail config,  see https://twitter.github.io/finagle/guide/MethodBuilder.html
    servicePerEndpoint
      .withAddNumbers(builder.method(UserService.AddNumbers).withTimeoutPerRequest(10.seconds)
        .withRetryForClassifier(ThriftMuxResponseClassifier.ThriftExceptionsAsFailures)
        .withTimeoutTotal(50.seconds)
        .idempotent(0.01).service)
      .withAddStrings(builder.method(UserService.AddStrings).withTimeoutPerRequest(10.seconds).nonIdempotent.service)
      .withGetById(builder.method(UserService.GetById).withTimeoutPerRequest(10.seconds).service)
      .withQueryList(builder.method(UserService.QueryList).withTimeoutPerRequest(10.seconds).withRetryDisabled.service)
  }


  @Provides
  @Singleton
  def getClientId(): ClientId = {
    ClientId("tc-client")
  }

}
