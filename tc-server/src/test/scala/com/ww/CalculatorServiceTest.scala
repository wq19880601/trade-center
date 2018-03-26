package com.ww

import com.google.inject.Stage
import com.twitter.finatra.thrift.EmbeddedThriftServer
import com.twitter.inject.server.FeatureTest
import com.twitter.util.Future
import com.ww.calculcator.Application
import com.ww.tradecenter.thriftscala.Calculator

class CalculatorServiceTest extends FeatureTest {
  val server = new EmbeddedThriftServer(twitterServer = new Application, stage = Stage.PRODUCTION)


  val client = server.thriftClient[Calculator[Future]](clientId = "haha")

  test("addnumber test") {
    val value = client.addNumbers(1, 2).value
    assert(value > 0)
  }

}
