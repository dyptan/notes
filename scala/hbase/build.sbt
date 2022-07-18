scalaVersion := "2.12.1"

name := "HBase TEst"

resolvers ++= Seq(
    "maven-repo" at "https://mvnrepository.com/artifact/",
  "MapR Repository" at "https://repository.mapr.com/maven/",
  //  "jboss" at "https://repository.jboss.org/"
)

// https://mvnrepository.com/artifact/org.apache.hbase/hbase-client
libraryDependencies += "org.apache.hbase" % "hbase-client" % "2.2.2"

// libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.4"
