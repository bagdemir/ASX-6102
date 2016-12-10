name := "ai-project"
version := "1.0"
scalaVersion := "2.11.8"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.0"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"