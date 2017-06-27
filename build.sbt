name := "armadillo"

scalaVersion := "2.11.8"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-encoding", "UTF-8",
  "-unchecked",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-Xexperimental"
)

libraryDependencies ++= Seq(
  // You don’t *have to* use Spark, but in case you want to, we have added the dependency
  "org.apache.spark" %% "spark-sql" % "2.1.0",
  // You don’t *have to* use akka-stream, but in case you want to, we have added the dependency
  "org.scalacheck" %% "scalacheck" % "1.12.1" % Test,
  "junit" % "junit" % "4.10" % Test
)


parallelExecution in Test := false // So that tests are executed for each milestone, one after the other
