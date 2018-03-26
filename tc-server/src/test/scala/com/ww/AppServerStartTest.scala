package com.ww

import com.google.inject.Stage
import com.twitter.finatra.thrift.EmbeddedThriftServer
import com.twitter.inject.server.FeatureTest
import com.ww.calculcator.Application

class AppServerStartTest extends FeatureTest {
  val server = new EmbeddedThriftServer(twitterServer = new Application, stage = Stage.PRODUCTION)

  test("server") {
    server.assertHealthy()
  }


}
