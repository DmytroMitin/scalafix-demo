lazy val auxifyMeta = "com.github.dmytromitin" %% "auxify-meta" % "0.4"
import com.geirsson.coursiersmall.{Repository => R}
scalafixResolvers.in(ThisBuild) += new R.Maven("https://oss.sonatype.org/content/groups/public/")
scalafixDependencies in ThisBuild += auxifyMeta
libraryDependencies in ThisBuild += auxifyMeta

lazy val V = _root_.scalafix.sbt.BuildInfo
inThisBuild(
  List(
    organization := "com.github.dmytromitin",
    homepage := Some(url("https://github.com/DmytroMitin/AUXify")),
    licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
      Developer(
        "DmytroMitin",
        "Dmytro Mitin",
        "dmitin3@gmail.com",
        url("https://github.com/DmytroMitin")
      )
    ),
    scalaVersion := V.scala212,
    addCompilerPlugin(scalafixSemanticdb),
    scalacOptions ++= List(
      "-Yrangepos",
//      "-P:semanticdb:synthetics:on"
    ),
  )
)

skip in publish := true

lazy val rules = project.settings(
  moduleName := "scalafix",
  libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion
)

lazy val input = project.settings(
  skip in publish := true
)

lazy val output = project.settings(
  skip in publish := true
)

lazy val tests = project
  .settings(
    skip in publish := true,
    libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test cross CrossVersion.full,
    compile.in(Compile) := 
      compile.in(Compile).dependsOn(compile.in(input, Compile)).value,
    scalafixTestkitOutputSourceDirectories :=
      sourceDirectories.in(output, Compile).value,
    scalafixTestkitInputSourceDirectories :=
      sourceDirectories.in(input, Compile).value,
    scalafixTestkitInputClasspath :=
      fullClasspath.in(input, Compile).value,
  )
  .dependsOn(rules)
  .enablePlugins(ScalafixTestkitPlugin)
