package com.ww

import com.twitter.inject.app.TestInjector
import com.twitter.inject.modules.StatsReceiverModule
import com.twitter.inject.{InjectorModule, IntegrationTest, TypeUtils}
import com.twitter.util.Await
import com.ww.calculcator.finatra.UserServiceThriftClientModule
import com.ww.user.thriftscala.UserService

class UserServiceClientTest extends IntegrationTest {

  override val injector = TestInjector(
    modules = Seq(
      UserServiceThriftClientModule,
      StatsReceiverModule,
      InjectorModule
    )
  ).create

  test("client") {
    val userServiceClient = injector.instance[UserService.MethodPerEndpoint](TypeUtils.asManifest[UserService.MethodPerEndpoint])
    val value = userServiceClient.getById("7193788d-b6c1-42ba-bd52-a611a2201977")
    val user = Await.result(value)
    assert(user != null)
  }
}
