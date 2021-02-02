
// The simplest possible sbt build file is just one line:

scalaVersion := "2.11.12"
// That is, to create a valid sbt build, all you've got to do is define the
// version of Scala you'd like your project to use.

// ============================================================================

// Lines like the above defining `scalaVersion` are called "settings". Settings
// are key/value pairs. In the case of `scalaVersion`, the key is "scalaVersion"
// and the value is "2.13.3"

// It's possible to define many kinds of settings, such as:

name := "stream-mapr-spark"
organization := "com.dyptan"
version := "1.0"

// Note, it's not required for you to define these three settings. These are
// mostly only necessary if you intend to publish your library's binaries on a
// place like Sonatype or Bintray.


// Want to use a published library in your project?
// You can define other libraries as dependencies in your build like this:

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"

resolvers ++= Seq(
//  "maven-repo" at "https://mvnrepository.com/artifact/",
  "MapR Repository" at "https://repository.mapr.com/maven/",
//  "apache repo" at "https://repo1.maven.org/maven2"
  "jboss" at "https://repository.jboss.org/"
)

// Spark libs are unmanaged (linked lib -> /opt/mapr/spark/spark-2.2.1/jars)
val sparkVersion = "2.2.1-mapr-2009"
// libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % sparkVersion
// libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion
libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % sparkVersion
// libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-producer_2.11" % sparkVersion
// libraryDependencies += "org.apache.kafka" % "kafka-clients" % "1.1.1-mapr-1808"

// libraryDependencies += "org.apache.hadoop" % "hadoop-common" % "2.7.0-mapr-1803"
// libraryDependencies += "org.apache.hadoop" % "hadoop-hdfs" % "2.7.0-mapr-1803"
// libraryDependencies += "org.apache.hadoop" % "hadoop-mapreduce-client-core" % "2.7.0-mapr-1803"
// libraryDependencies += "com.mapr.streams" % "mapr-streams" % "6.0.1-mapr"
// libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-10_2.11" % "2.2.1"

unmanagedJars in Compile ++= 
  (file("/opt/mapr/lib") * "*.jar").classpath