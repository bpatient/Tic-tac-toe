import com.github.play2war.plugin._

name := "TicTacToe"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaCore,
  javaJdbc,
  cache
)     

Play2WarPlugin.play2WarSettings

Play2WarKeys.servletVersion := "3.0"

play.Project.playJavaSettings
