package com.typesafe.sbt.bintraybundle

import sbt._
import Keys._

trait BintrayBundleKeys {
  val bintrayBundleOwner = settingKey[String]("owner of the bundle")
  val bintrayBundleName  = settingKey[String]("name of the bundle")
}

object BintrayBundleKeys extends BintrayBundleKeys
