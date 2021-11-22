
// The simplest possible sbt build file is just one line:

scalaVersion := "2.11.12"

name := "stream-mapr-spark"
organization := "com.dyptan"
version := "1.0"


resolvers ++= Seq(
//  "maven-repo" at "https://mvnrepository.com/artifact/",
  "MapR Repository" at "https://repository.mapr.com/maven/",
  //http://maven.corp.maprtech.com/nexus/content/groups/public
//  "Mapr dev repo" at "https://10.10.50.104/nexus/content/groups/public",
  "jboss" at "https://repository.jboss.org/"
)

// if Spark libs are unmanaged link as symlink: lib -> /opt/mapr/spark/spark-2.2.1/jars

val sparkVersion = "2.4.4.3-mapr-630"
 libraryDependencies += "org.apache.spark" %% "spark-streaming" % sparkVersion
 libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion
// libraryDependencies += "org.apache.kafka" % "kafka-clients" % "0.9.0.0"

val hadoopVersion="2.7.0-mapr-1803"
libraryDependencies += "org.apache.hadoop" % "hadoop-common" % hadoopVersion
libraryDependencies += "org.apache.hadoop" % "hadoop-hdfs" % hadoopVersion
libraryDependencies += "org.apache.hadoop" % "hadoop-mapreduce-client-core" % hadoopVersion
// libraryDependencies += "com.mapr.streams" % "mapr-streams" % "6.0.1-mapr"

// for linking mapr libs on local fs

//unmanagedJars in Compile ++=
//  (file("/opt/mapr/lib") * "*.jar").classpath