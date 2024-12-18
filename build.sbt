ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.5.0"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.10"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % Test

libraryDependencies += "org.scalafx" %% "scalafx" % "22.0.0-R33"

libraryDependencies ++= {
  lazy val osName = System.getProperty("os.name") match {
    case n if n.startsWith("Linux") => "linux"
    case n if n.startsWith("Mac") => "mac"
    case n if n.startsWith("Windows") => "win"
    case _ => throw new Exception("Unknown platform!")
  }
  Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
    .map(m => "org.openjfx" % s"javafx-$m" % "16" classifier osName)
}

lazy val root = (project in file("."))
  .settings(name := "Schwimmen")