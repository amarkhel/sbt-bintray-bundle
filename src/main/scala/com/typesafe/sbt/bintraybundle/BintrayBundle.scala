package com.typesafe.sbt.bintraybundle

import bintry.Licenses
import sbt._
import com.lightbend.conductr.sbt.BundlePlugin
import com.typesafe.sbt.packager.universal.UniversalPlugin
import bintray._

/**
 * Provides the machinery to publish bundles to bintray adhering to the urn:x-bundle scheme
 * described here: https://github.com/typesafehub/conductr-cli/issues/72
 */
object BintrayBundle extends sbt.AutoPlugin {

  import UniversalPlugin.autoImport._
  import BundlePlugin.autoImport._

  override def `requires` = BundlePlugin && BintrayPlugin
  override def trigger = AllRequirements

  override def projectSettings: Seq[Setting[_]] =
    settings(Bundle) ++ settings(BundleConfiguration, isBundleConfiguration = true)

  /**
   * Declare bintray bundle related settings for a given bundle configuration.
   */
  def settings(config: Configuration, isBundleConfiguration: Boolean = false): Seq[Setting[_]] =
    inConfig(config)(Seq(
      BintrayKeys.bintrayReleaseOnPublish in config := false,
      BintrayKeys.bintrayRepository := "bundle",
      Keys.publishMavenStyle := false,
      BintrayKeys.bintrayPackage := {
        if (isBundleConfiguration)
          (BundleKeys.configurationName in config).value
        else
          (Keys.normalizedName in config).value
      },
      Keys.publish := {
        val log = Keys.streams.value.log

        val packageName = (BintrayKeys.bintrayPackage in config).value

        val licenses = (Keys.licenses in config).value

        ensureLicenses(licenses, (BintrayKeys.bintrayOmitLicense in config).value)

        val repo = Bintray.cachedRepo(
          ensuredCredentials((BintrayKeys.bintrayCredentialsFile in config).value).get,
          (BintrayKeys.bintrayOrganization in config).value,
          (BintrayKeys.bintrayRepository in config).value
        )

        val vcs = (BintrayKeys.bintrayVcsUrl in config).value.getOrElse {
          sys.error("""bintrayVcsUrl not defined. assign this with bintrayVcsUrl := Some("git@github.com:you/your-repo.git")""")
        }

        val bundleDist = (dist in config).value
        val compatVersion = (BundleKeys.compatibilityVersion in config).value.dropWhile(_ == 'v')
        val digest = bundleDist.getName.split("-").toList.takeRight(1).mkString("-").replaceAll("""\.zip$""", "")
        val digestVersionStr = s"v$compatVersion-$digest"
        val attributes = (BintrayKeys.bintrayPackageAttributes in config).value ++
          Map(s"latest-v" + compatVersion -> Seq(bintry.Attr.Version(digestVersionStr)))

        val path = s"/${repo.owner}/$packageName/$digestVersionStr/$packageName-$digestVersionStr.zip"
        val desc = s"${repo.owner}/$packageName@$digestVersionStr"

        log.info(s"Checking package for $desc")
        repo.ensurePackage(
          packageName,
          attributes,
          (Keys.description in config).value,
          vcs,
          licenses,
          (BintrayKeys.bintrayPackageLabels in config).value)

        log.info(s"Staging $desc")
        repo.upload(packageName, digestVersionStr, path, bundleDist, log)

        if ((BintrayKeys.bintrayReleaseOnPublish in config).value) {
          log.info(s"Publishing $desc")
          repo.release(packageName, digestVersionStr, log)
        }
      }
    ))

  private def ensureLicenses(licenses: Seq[(String, URL)], omit: Boolean): Unit =
    if (!omit && licenses.isEmpty)
      sys.error(s"you must define at least one license for this project.")

  // FIXME: Imported from the bintray plugin due to it being private
  private def ensuredCredentials(credsFile: File, prompt: Boolean = true): Option[BintrayCredentials] =
    BintrayCredentials.read(credsFile).fold(sys.error, {
      case None =>
        if (prompt) {
          println("bintray-sbt requires your bintray credentials.")
          saveBintrayCredentials(credsFile)(requestCredentials())
          ensuredCredentials(credsFile, prompt)
        } else {
          println(s"Missing bintray credentials $credsFile. Some bintray features depend on this.")
          None
        }
      case creds => creds
    })

  // FIXME: Imported from the bintray plugin due to it being private
  private def saveBintrayCredentials(to: File)(creds: (String, String)) = {
    println(s"saving credentials to $to")
    val (name, pass) = creds
    BintrayCredentials.writeBintray(name, pass, to)
    println("reload project for sbt setting `publishTo` to take effect")
  }

  // FIXME: Imported from the bintray plugin due to it being private
  private def requestCredentials(defaultName: Option[String] = None, defaultKey: Option[String] = None): (String, String) = {
    val name =
      Prompt("Enter bintray username%s".format(defaultName.map(" (%s)".format(_)).getOrElse("")))
        .orElse(defaultName)
        .getOrElse(sys.error("bintray username required"))
    val pass =
      Prompt
        .descretely("Enter bintray API key %s".format(defaultKey.map(_ => "(use current)").getOrElse("(under https://bintray.com/profile/edit)")))
        .orElse(defaultKey)
        .getOrElse(sys.error("bintray API key required"))
    (name, pass)
  }

}
