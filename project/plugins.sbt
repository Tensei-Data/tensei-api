addSbtPlugin("org.foundweekends"  % "sbt-bintray"     % "0.5.1")
addSbtPlugin("com.typesafe.sbt"   % "sbt-git"         % "0.9.3")
addSbtPlugin("de.heikoseeberger"  % "sbt-header"      % "2.0.0")
addSbtPlugin("pl.project13.scala" % "sbt-jmh"         % "0.2.27")
addSbtPlugin("com.jsuereth"       % "sbt-pgp"         % "1.0.1")
addSbtPlugin("com.thesamet"       % "sbt-protoc"      % "0.99.6")
addSbtPlugin("com.lucidchart"     % "sbt-scalafmt"    % "1.10")
addSbtPlugin("org.wartremover"    % "sbt-wartremover" % "2.1.1")

// The library dependencies are needed for the Scala Protobuf plugin:
libraryDependencies += "com.trueaccord.scalapb" %% "compilerplugin" % "0.6.2"

