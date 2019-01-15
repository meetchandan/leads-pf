import Dependencies._
import sbt._

name := "leads-pf"

scalaVersion in ThisBuild := scalaV

organization in ThisBuild := "com.alefeducation"

fullResolvers ++= Seq("redshift" at "http://redshift-maven-repository.s3-website-us-east-1.amazonaws.com/release")

dependencyOverrides ++= jacksonDependencies ++ nettyDependencies
libraryDependencies ++= sparkDependencies ++ kafka ++
  configDepenedencies ++
  cassandraConnector

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.13",
  "com.typesafe.akka" %% "akka-stream" % "2.5.13",
  "com.typesafe.akka" %% "akka-http" % "10.1.3"
)

libraryDependencies += "com.codahale.metrics" % "metrics-json" % "3.0.1"
libraryDependencies += "com.google.code.gson" % "gson" % "1.7.1"

