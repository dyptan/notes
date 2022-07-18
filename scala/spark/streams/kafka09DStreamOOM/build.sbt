name := "mapr-streams"

version := "0.1"

scalaVersion := "2.11.12"

resolvers ++= Seq(
//  "maven-repo" at "https://mvnrepository.com/artifact/",
  "MapR Repository" at "http://repository.mapr.com/maven/",
//  "apache repo" at "https://repo1.maven.org/maven2"
  // Fixes Xerses deps error:
  "jboss" at "https://repository.jboss.org/"
)

//val sparkVersion = "2.3.1-mapr-1808-r9"
val sparkVersion = "2.4.4.0-mapr-630"

libraryDependencies += "org.apache.kafka" % "kafka-clients" % "1.1.1-mapr-1808"
libraryDependencies += "org.apache.hadoop" % "hadoop-common"% "2.7.0-mapr-1803"
libraryDependencies += "com.mapr.streams" % "mapr-streams"% "6.0.0-mapr"

libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % sparkVersion
libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % sparkVersion
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-9_2.11" % sparkVersion

dependencyOverrides ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.7.2"
)

