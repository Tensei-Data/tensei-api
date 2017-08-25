// *****************************************************************************
// Projects
// *****************************************************************************

// Calculate the current year for usage in copyright notices and license headers.
lazy val currentYear: Int = java.time.OffsetDateTime.now().getYear

lazy val tenseiApi =
  project
    .in(file("."))
    .enablePlugins(AutomateHeaderPlugin, GitBranchPrompt, GitVersioning)
    .settings(settings ++ publishSettings)
    .settings(
      name := "tensei-api",
      libraryDependencies ++= Seq(
        library.akkaActor   % Provided,
        library.argonaut,
        library.akkaTestkit % Test,
        library.scalaCheck  % Test,
        library.scalaTest   % Test
      ),
      wartremoverWarnings in (Compile, compile) ++= Warts.unsafe
    )
    .aggregate(tenseiApiMessages)

lazy val tenseiApiMessages =
  project
    .in(file("messages"))
    .enablePlugins(AutomateHeaderPlugin, GitBranchPrompt, GitVersioning)
    .settings(settings ++ protoBufSettings)
    .settings(
      name := "tensei-api-messages",
      libraryDependencies ++= Seq(
        library.scalaCheck      % Test,
        library.scalaTest       % Test,
        // The following dependencies are just needed because ScalaPB bails out with an error otherwise. :-(
        library.easyMock        % Test,
        library.easyMockClassic % Test,
        library.jUnit           % Test
      )
    )

lazy val benchmarks =
  project
    .in(file("benchmarks"))
    .enablePlugins(AutomateHeaderPlugin, GitBranchPrompt, GitVersioning, JmhPlugin)
    .settings(settings)
    .settings(
      name := "tensei-api-benchmarks",
      javaOptions ++= (dependencyClasspath in Test).map(makeAgentOptions).value,
      javaOptions in run ++= List("-Xmx2g", "-XX:MaxMetaspaceSize=2g"),
      fork := true,
      libraryDependencies ++= Seq(
        library.jaInstrumenter,
        library.jaMemoryMeter
      )
    )
    .dependsOn(tenseiApi, tenseiApiMessages)

/**
  * Helper function to generate options for instrumenting memory analysis.
  *
  * @param cp The current classpath.
  * @return A list of options (strings).
  */
def makeAgentOptions(cp: Classpath): Seq[String] = {
  val jammJar = cp.map(_.data).filter(_.toString.contains("jamm")).head
  val jaiJar = cp.map(_.data).filter(_.toString.contains("instrumenter")).head
  Seq(s"-javaagent:$jammJar", s"-javaagent:$jaiJar")
}

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {
    object Version {
      val akka            = "2.4.17"
      val argonaut        = "6.0.4"
      val cats            = "0.9.0"
      val circe           = "0.7.0"
      val easyMock        = "3.4"
      val easyMockClassic = "3.2"
      val jaMemoryMeter   = "0.3.1"
      val jaInstrumenter  = "3.0.1"
      val jUnit           = "4.12"
      val scalaCheck      = "1.13.5"
      val scalaTest       = "3.0.4"
    }
    val akkaActor: ModuleID       = "com.typesafe.akka"      %% "akka-actor"             % Version.akka
    val akkaCluster: ModuleID     = "com.typesafe.akka"      %% "akka-cluster"           % Version.akka
    val akkaTestkit: ModuleID     = "com.typesafe.akka"      %% "akka-testkit"           % Version.akka
    val argonaut: ModuleID        = "io.argonaut"            %% "argonaut"               % Version.argonaut
    val cats: ModuleID            = "org.typelevel"          %% "cats"                   % Version.cats
    val circeCore: ModuleID       = "io.circe"               %% "circe-core"             % Version.circe
    val circeGeneric: ModuleID    = "io.circe"               %% "circe-generic"          % Version.circe
    val circeParser: ModuleID     = "io.circe"               %% "circe-parser"           % Version.circe
    val easyMock: ModuleID        = "org.easymock"           %  "easymock"               % Version.easyMock
    val easyMockClassic: ModuleID = "org.easymock"           %  "easymockclassextension" % Version.easyMockClassic
    val jUnit: ModuleID           = "junit"                  %  "junit"                  % Version.jUnit
    val jaMemoryMeter: ModuleID   = "com.github.jbellis"     %  "jamm"                   % Version.jaMemoryMeter
    val jaInstrumenter: ModuleID  = "com.google.code.java-allocation-instrumenter" %  "java-allocation-instrumenter" % Version.jaInstrumenter
    val scalaCheck: ModuleID      = "org.scalacheck"         %% "scalacheck"             % Version.scalaCheck
    val scalaTest: ModuleID       = "org.scalatest"          %% "scalatest"              % Version.scalaTest
  }

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val settings =
  commonSettings ++
  gitSettings ++
  resolverSettings ++
  scalafmtSettings

lazy val commonSettings =
  Seq(
    headerLicense := Some(HeaderLicense.AGPLv3(s"2014 - $currentYear", "Contributors as noted in the AUTHORS.md file")),
    scalaVersion in ThisBuild := "2.11.11",
    //crossScalaVersions := Seq("2.11.11", "2.12.3"),
    organization := "com.wegtam.tensei",
    organizationName := "Wegtam GmbH",
    startYear := Option(2014),
    licenses += ("AGPL-V3", url("https://www.gnu.org/licenses/agpl.html")),
    mappings.in(Compile, packageBin) += baseDirectory.in(ThisBuild).value / "LICENSE" -> "LICENSE",
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8",
      "-feature",
      "-target:jvm-1.8",
      "-unchecked",
      //"-Xfatal-warnings",
      "-Xfuture",
      "-Xlint",
      "-Ybackend:GenBCode",
      "-Ydelambdafy:method",
      "-Yno-adapted-args",
      "-Ywarn-numeric-widen",
      "-Ywarn-unused-import",
      "-Ywarn-value-discard"
    ),
    javacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-source", "1.8",
      "-target", "1.8"
    ),
    unmanagedSourceDirectories.in(Compile) := Seq(scalaSource.in(Compile).value),
    unmanagedSourceDirectories.in(Test) := Seq(scalaSource.in(Test).value),
    incOptions := incOptions.value.withNameHashing(nameHashing = true),
    autoAPIMappings := true
  )

lazy val gitSettings =
  Seq(
    git.useGitDescribe := true
  )

lazy val protoBufSettings =
  Seq(
    PB.runProtoc          := (args => Process("protoc", args).!),
    PB.targets in Compile := Seq(
      scalapb.gen(
        flatPackage = true
      ) -> (sourceManaged in Compile).value
    ),
    libraryDependencies += "com.trueaccord.scalapb" %% "scalapb-runtime" % com.trueaccord.scalapb.compiler.Version.scalapbVersion % "protobuf"
  )

lazy val publishSettings =
  Seq(
    bintrayOrganization := Option("wegtam"),
    bintrayPackage := "tensei-api",
    bintrayReleaseOnPublish in ThisBuild := false,
    bintrayRepository := "tensei-data",
    developers += Developer(
      "wegtam",
      "Wegtam GmbH",
      "tech@wegtam.com",
      url("https://www.wegtam.com")
    ),
    homepage := Option(url("https://github.com/Tensei-Data/tensei-api")),
    pomIncludeRepository := (_ => false),
    publishArtifact in Test := false,
    publish := (publish dependsOn (test in Test)).value,
    scmInfo := Option(
      ScmInfo(
        url("https://github.com/Tensei-Data/tensei-api"),
        "git@github.com:Tensei-Data/tensei-api.git"
      )
    )
  )

lazy val resolverSettings =
  Seq(
    resolvers += "Tensei-Data" at "https://dl.bintray.com/wegtam/tensei-data"
  )

lazy val scalafmtSettings =
  Seq(
    scalafmtOnCompile := true,
    scalafmtOnCompile.in(Sbt) := false,
    scalafmtVersion := "1.2.0"
  )

