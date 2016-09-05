import scalariform.formatter.preferences.{PreserveDanglingCloseParenthesis, DoubleIndentClassDeclaration, AlignSingleLineCaseStatements}

sbtPlugin := true

organization := "com.typesafe.sbt"
name := "sbt-bintray-bundle"
description := "sbt plugin to publish a ConductR bundle to Bintray."

scalaVersion := "2.10.4"
scalacOptions ++= List(
  "-unchecked",
  "-deprecation",
  "-language:_",
  "-target:jvm-1.7",
  "-encoding", "UTF-8"
)

scalariformSettings
ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(PreserveDanglingCloseParenthesis, true)

addSbtPlugin("me.lessis"              % "bintray-sbt"  % "0.3.0")
addSbtPlugin("com.lightbend.conductr" % "sbt-conductr" % "2.1.9")

releaseSettings
ReleaseKeys.versionBump := sbtrelease.Version.Bump.Minor

licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))
publishMavenStyle := false
bintrayRepository := "sbt-plugins"
bintrayOrganization := Some("sbt")
bintrayRepository := "sbt-plugin-releases"
bintrayReleaseOnPublish := false

scriptedSettings
scriptedLaunchOpts <+= version apply { v => s"-Dproject.version=$v" }
