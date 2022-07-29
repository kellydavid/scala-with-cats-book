// scalac options come from the sbt-tpolecat plugin so need to set any here
name := "scala-with-cats-book"
inThisBuild(
  List(
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.8",
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % "2.8.0"
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.13.2" cross CrossVersion.full)
  )
)
