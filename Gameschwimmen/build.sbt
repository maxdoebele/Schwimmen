ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.5.0"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.10"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % Test

lazy val root = (project in file("."))
  .settings(
    name := "Gameschwimmen"
  )

