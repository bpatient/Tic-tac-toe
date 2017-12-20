// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.2.6")

// generate war plugin
// addSbtPlugin("com.github.play2war" % "play2-war-plugin" % "1.2-beta1")
addSbtPlugin("com.github.play2war" % "play2-war-plugin" % "1.2")
addSbtPlugin("com.heroku" % "sbt-heroku" % "2.0.0")