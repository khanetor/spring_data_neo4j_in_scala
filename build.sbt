name := "sdn_demo"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "org.springframework.data" % "spring-data-neo4j-rest" % "3.3.0.RELEASE",
  "com.sun.jersey" % "jersey-bundle" % "1.19",
  "org.slf4j" % "slf4j-nop" % "1.7.12",
  "com.jsuereth" %% "scala-arm" % "1.4"
)