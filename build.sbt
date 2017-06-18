name := "ScalajsOcello"

version := "1.0"

enablePlugins(ScalaJSPlugin)

scalaVersion := "2.12.2"
scalaJSStage in Global := FastOptStage
skip in packageJSDependencies := false
libraryDependencies += "be.doeraene" %%% "scalajs-jquery" % "0.9.1"
libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.1"
libraryDependencies += "com.lihaoyi" %%% "scalatags" % "0.6.5"
libraryDependencies += "com.lihaoyi" %%% "scalarx" % "0.3.2"

jsDependencies += "org.webjars" % "jquery" % "2.1.4" / "2.1.4/jquery.js"
jsDependencies += RuntimeDOM
scalaJSUseMainModuleInitializer := true