sbt-bintray-bundle
==================

sbt-bintray-bundle = sbt-bundle + bintray-sbt

This is an sbt plugin to publish your Conductr bundle to Bintray.

Setup
-----

In `project/bintraybundle.sbt`:

```scala
addSbtPlugin("com.typesafe.sbt" % "sbt-bintray-bundle" % "0.1.0")
```

Declaring the native packager or any of its other plugins should be sufficient. For example, in your build.sbt file:

```scala
lazy val root = (project in file(".")).enablePlugins(JavaAppPackaging)
```
