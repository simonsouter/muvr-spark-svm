//
// http://spark.apache.org/docs/latest/quick-start.html#a-standalone-app-in-scala
//
name := """muvr-spark-svm"""

scalaVersion := "2.10.4"

libraryDependencies ++= Dependencies.sparkHadoop

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.10" % "1.4.0",
  "org.apache.spark" % "spark-mllib_2.10" % "1.4.0"
//  "org.apache.hadoop" % "hadoop-client" % "2.7.0"
)

releaseSettings

scalariformSettings

fork in run := true

resolvers += "Akka Repository" at "http://repo.akka.io/release/"