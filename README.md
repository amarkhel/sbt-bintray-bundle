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
lazy val bundle1 = (project in file(".")).
  enablePlugins(ConductRPlugin, JavaAppPackaging).
  settings(
    // commonSettings,
    // bundleSettings,
    name := "bundle1",
    // Naming convention for a bundle is owner/name, like a Github repo.
    bintrayBundleOwner := "orgname",
    // Optionally, if you want to publish to an org repo
    bintrayOrganization := Some("orgname"),
    // Optionally, if you want to change the name of the repo
    bintrayRepository := "bundle"
  )
```

Usage
-----

Go to Bintray.com, and create the package with your bundle name, such as `bundle1`.

To stage your Conductr bundle to Bintray,

```scala
> bundle:publish
```

Go back to Bintray.com, and hit the publish button to release it.
