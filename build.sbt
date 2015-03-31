name := """life-server"""

version := "1.0-SNAPSHOT"

lazy val root = project.in(file(".")).enablePlugins(PlayScala)

libraryDependencies ++= {
  val akkaV = "2.3.6"
  Seq(
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test"
  )
}

fork in run := true
