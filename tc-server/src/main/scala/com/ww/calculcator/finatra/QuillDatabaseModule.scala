package com.ww.calculcator.finatra

import javax.inject.Singleton

import com.google.inject.Provides
import com.twitter.inject.TwitterModule
import io.getquill.{FinagleMysqlContext, SnakeCase}

object QuillDatabaseModule extends TwitterModule {

  type QuillDatabaseSource = FinagleMysqlContext[SnakeCase]

  @Provides
  @Singleton
  def provideDataBaseSource(): QuillDatabaseSource =
    new FinagleMysqlContext(SnakeCase, "quill.db")

}
