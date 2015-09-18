name := "Codeego"

organization  := "ues.li"

version       := "0.1"

scalaVersion  := "2.11.7"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature")

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

fork := true

fork in Test := true

libraryDependencies ++= Seq(
  "org.postgresql"         %   "postgresql"         % "9.4-1202-jdbc42",
  "com.typesafe.slick"    %%   "slick"              % "3.0.3",
  "org.slf4j"              %   "slf4j-nop"          % "1.6.4",
  "com.mchange"            %   "c3p0"               % "0.9.5",
  "joda-time"              %   "joda-time"          % "2.7",
  "org.joda"               %   "joda-convert"       % "1.7",
  "com.github.tototoshi"  %%   "slick-joda-mapper"  % "2.0.0",
  "net.debasishg"       %%  "redisclient"       % "3.0",
  "io.spray"            %%  "spray-can"         % "1.3.3",
  "io.spray"            %%  "spray-routing"     % "1.3.3",
  "io.spray"            %%  "spray-json"        % "1.3.2",
  "io.spray"            %%  "spray-testkit"     % "1.3.3"  % "test",
  "com.typesafe.akka"   %%  "akka-actor"        % "2.3.14",
  "com.typesafe.akka"   %%  "akka-testkit"      % "2.3.14" % "test"
)

Revolver.settings

Revolver.reColors := Seq("green")

logBuffered in Test := false

parallelExecution in Test := true