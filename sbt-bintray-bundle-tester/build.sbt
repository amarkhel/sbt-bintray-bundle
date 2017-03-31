import ByteConversions._

name := "simple-test"
organization in ThisBuild := "com.typesafe.sbt.bintraybundle"
licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

version := "0.1.0-SNAPSHOT"

normalizedName in Bundle := "simple-test-frontend"

BundleKeys.nrOfCpus := 0.1
BundleKeys.memory := 384.MiB
BundleKeys.diskSpace := 10.MB
BundleKeys.roles := Set("web-server")

BundleKeys.configurationName := "frontend"

lazy val BackendRegion = config("backend-region").extend(Bundle)
BundlePlugin.bundleSettings(BackendRegion)
inConfig(BackendRegion)(Seq(
  normalizedName := "simple-test-backend"
))

inConfig(Bundle)(Seq(
  bintrayVcsUrl := Some("https://github.com/sbt/sbt-bintray-bundle"),
  bintrayRepository := "test-bundle-repo",
  bintrayOrganization := Some("typesafe")
))
BintrayBundle.settings(BackendRegion)

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .configs(BackendRegion)
