name := """life-server"""

version := "1.0-SNAPSHOT"

lazy val root = project.in(file(".")).enablePlugins(PlayScala)

resolvers += "Local Maven Repository" at "http://eif-repository.moex.com/nexus/content/repositories/releases"

libraryDependencies ++= {
  val akkaV = "2.3.6"
  Seq(
    "org.specs2" %% "specs2-core" % "3.6.5" % "test",
    "org.specs2" %% "specs2-junit" % "3.6.5" % "test",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
    "org.webjars" %% "webjars-play" % "2.4.0-1",
    "org.webjars" % "bootstrap" % "3.3.5",
    "com.typesafe.slick" %% "slick" % "3.0.1",
    "com.h2database" % "h2" % "1.3.176"
  )
}


fork in run := true
