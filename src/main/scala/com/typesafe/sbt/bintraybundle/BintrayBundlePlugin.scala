package com.typesafe.sbt.bintraybundle

import sbt._
import Keys._
import com.typesafe.sbt.bundle.SbtBundle, SbtBundle.autoImport._
import com.typesafe.sbt.packager.universal.UniversalPlugin, UniversalPlugin.autoImport._
import bintray.BintrayPlugin, BintrayPlugin.autoImport._
import _root_.bintray.InternalBintrayKeys._

object BintrayBundle extends sbt.AutoPlugin {
  override def `requires` = SbtBundle && BintrayPlugin
  override def trigger = AllRequirements

  override def buildSettings: Seq[Setting[_]] = Seq(
    bintrayReleaseOnPublish := false
  )

  override def projectSettings: Seq[Setting[_]] = Seq(
    // bintrayOrganization := Some("typesafe"),
    bintrayRepository := "bundle",
    publishMavenStyle := false,
    publishArtifact in Compile := false,
    publish in Bundle := {
      val bundleOwner = organization.value
      val bundleName = normalizedName.value
      val repo = bintrayRepo.value
      val b = (dist in Bundle).value
      val ver = b.getName.split("-").toList.takeRight(2).mkString("-").replaceAll("""\.zip$""", "")
      val bundleExt = ".zip"
      val path = s"/$bundleOwner/$bundleName/$ver/$bundleName-$ver$bundleExt"
      repo.upload(bintrayPackage.value, ver, path, b, sLog.value)
    },
    publish := ()
  )
}
