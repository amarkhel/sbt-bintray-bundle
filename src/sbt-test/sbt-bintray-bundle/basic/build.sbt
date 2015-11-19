import ByteConversions._
import org.scalatest.Matchers._

name := "simple-test"
organization := "com.typesafe.sbt.bintraybundle"
licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

version := "0.1.0-SNAPSHOT"

normalizedName in Bundle := "simple-test-frontend"

BundleKeys.nrOfCpus := 1.0
BundleKeys.memory := 64.MiB
BundleKeys.diskSpace := 10.MB
BundleKeys.roles := Set("web-server")

BundleKeys.configurationName := "frontend"

inConfig(Bundle)(Seq(
  bintrayVcsUrl := Some("https://github.com/sbt/sbt-bintray-bundle"),
  bintrayRepository := "test-bundle-repo",
  bintrayOrganization := Some("typesafe")
))

val checkSettings = taskKey[Unit]("")

checkSettings := {
  (bintrayPackage in Bundle).value shouldBe "simple-test-frontend"
}

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
