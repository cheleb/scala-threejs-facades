import java.nio.charset.StandardCharsets
//ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
import org.scalajs.linker.interface.ModuleSplitStyle

inThisBuild(
  Seq(
    scalaVersion := "3.6.3"
  )
)

lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    // Version
    organization := "io.github.dcascaval",
    name := "scala-threejs-facades",
    version := "0.172.0",

    // Publishing SCM information
    crossPaths := false,
    // publishTo := sonatypePublishToBundle.value,

    // Dependencies
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "2.8.0"
  )

lazy val demo = Project("demo", file("examples/demo"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= { config =>
      config
        .withModuleKind(ModuleKind.ESModule)
        .withSourceMap(false)
        .withModuleSplitStyle(ModuleSplitStyle.SmallestModules)
    }
  )
  .dependsOn(root)
  .settings(
    libraryDependencies += "com.raquo" %%% "laminar" % "17.2.0" // Requires Scala.js 1.13.2+
  )

// credentials += Credentials(
//   "GnuPG Key ID",
//   "gpg",
//   "AAE611EE1965DFC7C54E07DFA48354A0A0A8C6A8", // key identifier
//   "ignored" // this field is ignored; passwords are supplied by pinentry
// )

Global / onLoad := {
  val scalaVersionValue = (demo / scalaVersion).value
  val outputFile =
    baseDirectory.value / "scripts" / "target" / "build-env.sh"
  IO.writeLines(
    outputFile,
    s"""  
  |# Generated file see build.sbt
  |SCALA_VERSION="$scalaVersionValue"
  |""".stripMargin.split("\n").toList,
    StandardCharsets.UTF_8
  )

  (Global / onLoad).value
}
