lazy val buildSettings: Seq[Setting[_]] = inThisBuild(Seq(
  git.baseVersion := "0.1.0",
  organization := "com.typesafe.sbt"
))

val sbtbundle = "com.typesafe.sbt" % "sbt-bundle" % "0.23.0"
val bintraysbt = "me.lessis" % "bintray-sbt" % "0.3.0"

lazy val root = (project in file(".")).
  enablePlugins(GitVersioning).
  settings(
    buildSettings,
    sbtPlugin := true,
    name := "sbt-bintray-bundle",
    description := "sbt plugin to publish a ConductR bundle to Bintray.",
    licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    scalacOptions := Seq("-deprecation", "-unchecked"),
    addSbtPlugin(sbtbundle),
    addSbtPlugin(bintraysbt)
  )
