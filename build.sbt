name := """life-server"""

version := "1.0-SNAPSHOT"

lazy val root = project.in(file(".")).enablePlugins(PlayScala)

resolvers += "Local Maven Repository" at "http://eif-repository.moex.com/nexus/content/repositories/releases"

libraryDependencies ++= {
  val akkaV = "2.3.6"
  Seq(
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
    "org.webjars" %% "webjars-play" % "2.3.0-2",
    "org.webjars" % "bootstrap" % "2.3.2",
    "com.typesafe.slick" %% "slick" % "3.0.1",
    //"org.slf4j" % "slf4j-nop" % "1.6.4",
    "com.h2database" % "h2" % "1.3.176"
  )
}


fork in run := true
