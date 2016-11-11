sbt-bintray-bundle
==================

[![Build Status](https://api.travis-ci.org/sbt/sbt-bintray-bundle.png?branch=master)](https://travis-ci.org/sbt/sbt-bintray-bundle)

sbt-bintray-bundle = sbt-bundle + bintray-sbt

This is an sbt plugin to publish your [ConductR](http://conductr.typesafe.com/) bundle and its configuration to Bintray.

Setup
-----

In `project/bintraybundle.sbt`:

```scala
addSbtPlugin("com.typesafe.sbt" % "sbt-bintray-bundle" % "1.1.0")
```

Declaring the native packager or any of its other plugins should be sufficient. For example, in your build.sbt file:

```scala
// bundleSettings,
name := "bundle1"

// A license is required for bintray packages
licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

inConfig(Bundle)(Seq(
  // A version control system url is required for bintray packages
  bintrayVcsUrl := Some("https://github.com/sbt/sbt-bintray-bundle"),
  // Optionally, if you want to publish to an org repo other than your own
  bintrayOrganization := Some("orgname")
  // Optionally, if you want to change the name of the repo ("bundle" is the default)
  bintrayRepository := "test-bundle-repo",
))

lazy val root = (project in file(".")).enablePlugins(JavaAppPackaging)
```

Usage
-----

To stage your Conductr bundle to Bintray,

```scala
> bundle:publish
```

Go back to Bintray.com, and hit the publish button to release it (set the `bintrayReleaseOnPublish` to true
if you'd prefer to release immediately).

Most [Bintray plugin](https://github.com/softprops/bintray-sbt#bintray-sbt) keys are honored and can be scoped specifically for bundles e.g.: `bintrayReleaseOnPublish in Bundle`
means that the setting will be applied for the specific bundle.

In addition, where a project has multiple bundle types and/or bundle configuration then bintray settings can be applied
distinctly. Check out the sbt-bintray-bundle-tester sub project for an example.

> Note that bundle configurations can also be published: `configuration:publish`. *Take care when publishing configurations* that contain sensitive data e.g. passwords and secrets. Ensure that the target repository on Bintray is protected by credentials. Note also that the configuration repo/package on Bintray will not default to the same repo/package as the bundle. Configuration is regarded as sensitive and so you must consider carefully where it lies.

&copy; Lightbend Inc., 2015
