// Default keys

name := "Hello"

version := "1.0"

scalaVersion in ThisBuild := "2.10.0"

// libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"

// Custom keys

val gitHeadCommitSha = taskKey[String]("Determines the current git commit SHA")

val makeVersionProperties = taskKey[Seq[File]]("Creates a version.properties file we can find at runtime.")

// Custom task declarations

gitHeadCommitSha in ThisBuild := Process("git rev-parse HEAD").lines.head

makeVersionProperties := {
      val propFile = (resourceManaged in Compile).value / "version.properties"
      val content = "version=%s" format (gitHeadCommitSha.value)
      IO.write(propFile, content)
      Seq(propFile)
    }

resourceGenerators in Compile <+= makeVersionProperties

// Subprojects

lazy val common = {
  Project("common", file("common"))
  .settings(
    version := "1.0",
	libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"
  )
}

val web = {
  Project("web", file("web"))
  .dependsOn(common)
  .settings(
  	version := "1.0",
    libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"
  )
}