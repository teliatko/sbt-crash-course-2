// Default keys

name := "Hello"

// libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"

// Custom keys

val gitHeadCommitSha = taskKey[String]("Determines the current git commit SHA")

val makeVersionProperties = taskKey[Seq[File]]("Creates a version.properties file we can find at runtime.")

// Custom task declarations

gitHeadCommitSha in ThisBuild := Process("git rev-parse HEAD").lines.head

// Common settings and definitions

def SuperDuperProject(name: String): Project = {
  Project(name, file(name))
  .settings(
    version := "1.0",
    scalaVersion := "2.10.0",
	libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"
  )
}

// Subprojects

lazy val common = {
  SuperDuperProject("common")
  .settings(
    version in Compile := version.value + "-PROD",
    version in Test := version.value + "-JUST-TEST",
    makeVersionProperties := {
      val propFile = (resourceManaged in Compile).value / "version.properties"
      val content = "version=%s" format (gitHeadCommitSha.value)
      IO.write(propFile, content)
      Seq(propFile)
    },
    resourceGenerators in Compile <+= makeVersionProperties
  )
}

val web = {
  SuperDuperProject("web")
  .dependsOn(common)
  .settings()
}