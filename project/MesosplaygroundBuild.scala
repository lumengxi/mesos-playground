import sbt._
import Keys._

import com.typesafe.sbt.SbtScalariform._
import scalariform.formatter.preferences._
import sbtassembly.AssemblyPlugin.autoImport._

object MesosplaygroundBuild extends Build {

  val ORGANIZATION    = "com.lumengxi"
  val PROJECT_NAME    = "mesos-playground"
  val PROJECT_VERSION = "0.1.0"
  val SCALA_VERSION   = "2.11.4"

  val MESOS_VERSION           = "0.22.1"
  val TYPESAFE_CONFIG_VERSION = "1.2.1"
  val SCALATEST_VERSION       = "2.2.2"
  val SLF4J_VERSION           = "1.7.9"
  val LOGBACK_VERSION         = "1.1.2"

  val pathToMesosLibs = "/usr/local/lib"

  lazy val root = Project(
    id = PROJECT_NAME,
    base = file("."),
    settings = commonSettings
  )

  lazy val commonSettings = Project.defaultSettings ++
                            basicSettings ++
                            formatSettings ++
                            net.virtualvoid.sbt.graph.Plugin.graphSettings

  lazy val basicSettings = Seq(
    version := PROJECT_VERSION,
    organization := ORGANIZATION,
    scalaVersion := SCALA_VERSION,

    libraryDependencies ++= Seq(
      "org.apache.mesos" % "mesos"           % MESOS_VERSION,
      "com.typesafe"     % "config"          % TYPESAFE_CONFIG_VERSION,
      "org.slf4j"        % "slf4j-api"       % SLF4J_VERSION,
      "ch.qos.logback"   % "logback-classic" % LOGBACK_VERSION % "runtime",
      "org.scalatest"   %% "scalatest"       % SCALATEST_VERSION % "test"
    ),

    mergeStrategy in assembly <<= (mergeStrategy in assembly) {
      (old) => {
        case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
        case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
        case s if s.endsWith("reference.conf") => MergeStrategy.concat
        case s if s.endsWith("application.conf") => MergeStrategy.concat
        case s if s.endsWith(".class") => MergeStrategy.last
        case s if s.endsWith(".properties") => MergeStrategy.last
        case x => old(x)
      }
    },

    scalacOptions in Compile ++= Seq(
      "-unchecked",
      "-deprecation",
      "-feature"
    ),

    javaOptions += "-Djava.library.path=%s:%s".format(
      sys.props("java.library.path"),
      pathToMesosLibs
    ),

    fork in run := true,

    fork in Test := true,

    parallelExecution in Test := false
  )

  lazy val formatSettings = scalariformSettings ++ Seq(
    ScalariformKeys.preferences := FormattingPreferences()
      .setPreference(IndentWithTabs, false)
      .setPreference(IndentSpaces, 2)
      .setPreference(AlignParameters, false)
      .setPreference(DoubleIndentClassDeclaration, true)
      .setPreference(MultilineScaladocCommentsStartOnFirstLine, false)
      .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true)
      .setPreference(PreserveDanglingCloseParenthesis, true)
      .setPreference(CompactControlReadability, true)
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(PreserveSpaceBeforeArguments, true)
      .setPreference(SpaceBeforeColon, false)
      .setPreference(SpaceInsideBrackets, false)
      .setPreference(SpaceInsideParentheses, false)
      .setPreference(SpacesWithinPatternBinders, true)
      .setPreference(FormatXml, true)
  )

}
