# ChangeLog for the Tensei-Data API

All notable changes to this project will be documented in this file.
This project adheres to [Semantic Versioning](http://semver.org/).

## Conventions when editing this file.

Please follow the listed conventions when editing this file:

* one subsection per version
* reverse chronological order (latest entry on top)
* write all dates in iso notation (`YYYY-MM-DD`)
* each version should group changes according to their impact:
    * `Added` for new features.
    * `Changed` for changes in existing functionality.
    * `Deprecated` for once-stable features removed in upcoming releases.
    * `Removed` for deprecated features removed in this release.
    * `Fixed` for any bug fixes.
    * `Security` to invite users to upgrade in case of vulnerabilities.

## Unreleased

## 1.92.0 (2017-08-25)

### Changed

- update sbt to 0.13.16 and serveral plugins
- switch akka dependency to `provided`

### Removed

- `BaseApplication` and `Settings` because they are no longer used.
- Akka is no longer bundled in the publised artefact

## 1.91.0 (2017-05-18)

### Added

- protobuf messages

### Canged

- use default secure random number generator from JVM

### Fixed

- strong secure random number generator blocks endless on systems with low entropy

## 1.90.0 (2017-03-09)

### Added

- add field `languageTag` to `ConnectionInformation`

## 1.89.0 (2017-03-06)

### Added

- ParserState extended

## 1.88.0 (2017-03-03)

### Removed

- unused/obsolete classes

## 1.87.0 (2017-03-03)

### Added

- custom class for schema extractor options

### Changed

- update Akka to 2.4.17
- global message `ExtractSchema` now uses `ExtractSchemaOptions` instead of strings
- switch to scalafmt for code formatting
- more strict compiler flags

## 1.86.0 (2016-06-23)

### Added

- files for contribution guide
    - [AUTHORS.md](AUTHORS.md)
    - this CHANGELOG file
    - [CONTRIBUTING.md](CONTRIBUTING.md)
    - [LICENSE](LICENSE)

### Changed

- update Akka to 2.4.7

### Fixed

- minor code style issues

## 1.85.0 (2016-05-10)

### Added

- activate `sbt-wartremover`

### Changed

- code cleanup
- update Akka to 2.3.15

### Removed

- `-Xfatal-warnings` flag
- unused dependencies

## 1.84.0 (2016-03-18)

### Changed

- add agent id to log message classes

## 1.83.0 (2016-03-16)

### Added

- classes for log message handling
- `sbt-wartremover` for clean code warnings

### Changed

- major code cleanup
- sbt plugin configuration

## 1.82.1 (2016-03-14)

### Added

- compiler flags for better Java 8 support
    - `-Ybackend:GenBCode`
    - `-Ydelambdafy:method`

### Changed

- update Scala to 2.11.8
- update sbt to 0.13.11

## 1.82.0 (2016-02-22)

### Removed

- `TenseiDataType` because it is only used in the agent

## 1.81.0 (2016-02-19)

### Added

- data type for `Boolean`

## 1.80.0 (2016-02-18)

### Added

- helper function to convert data types into `TenseiDataType` (boxing)

## 1.79.0 (2016-02-16)

### Added

- container for binary data

## 1.78.0 (2016-02-16)

### Changed

- renamed trait `TenseiDataTypes` to `TenseiDataType`

## 1.77.0 (2016-02-16)

### Added

- data type for `ElementReference`
- data type for auto increment values
- `sbt-scalariform` for code style enforcement

### Changed

- restructure sbt plugins
- update scalatest and switch test matchers from `should` to `must`
- clean up sbt resolver settings

### Removed

- `sbt-scoverage` because of hard linking of profiling dependencies

## 1.73.0 (2015-10-13)

### Changed

- update Akka to 2.3.14
- update sbt-scoverage
- update `.gitignore`
- update sbt to 0.13.9
- code cleanup for scalaz disjunction

## 1.72.0 (2015-07-17)

### Added

- messages for statistics

## 1.71.0 (2015-07-15)

### Changed

- update Akka to 2.3.12
- prefer scala version defined in `build.sbt` upon conflicts

## 1.70.1 (2015-06-30)

### Changed

- add field for group id to `User`
- adjust permissions checks to group names which have to be unique
- maximum value in `License` is now `Int.MaxValue` instead of `-1`
- publish to our own repository (Apache Archiva)
- update Akka to 2.3.11
- update Scala to 2.11.7
- extend messages for schema extraction

## 1.64.0 (2015-05-04)

### Added

- message `NoLicenseInstalled`

## 1.63.1 (2015-04-27)

### Added

- move some messages from agent into the api
- move some messages from server into the api
- helper method on `Cookbook` that returns mapped source ids
- special messages for license handling
- helpers for cryptographic functions
- helper method to create licenses
- code coverage via `sbt-scoverage`

### Changed

- update Scala to 2.11.6
- renames and refactoring
- `Recipe` can include an empty mapping list
- replace transformation id with unique identifier (UUID)

### Fixed

- some typos

## 1.36.0 (2014-12-30)

### Changed

- new agent state `CleaningUp`

## 1.35.0 (2014-12-30)

### Changed

- remove `Bootable` from base cluster application

### Removed

- remove Akka microkernel

## 1.34.0 (2014-12-29)

### Added

- integrated akka-camel
- messages for push notification support from server to frontend

### Changed

- update Akka to 2.3.8
- more fields for runtime statistics

## 1.31.0 (2014-12-15)

### Added

- included Akka microkernel and `Bootable`
- global messages to create a `DFASDL` from a `ConnectionInformation`
- global messages for errors

### Changed

- base application extends `Bootable`
- new agent state `Aborting`

## 1.28.0 (2014-12-10)

### Added

- sealed trait and tests for states of agent, processor and parser
- implement `equals` and `hashCode` on `DFASDL`

### Changed

- update sbt to 0.13.7
- require test run before `sbt publish`

### Fixed

- error in tests

## 1.21.1 (2014-11-13)

### Added

- version field for `DFASDL`
- role name for cluster nodes
- base class for cluster applications
- `DFASDL` function for generating missing element ids
- global messages
- license data type
- agent working states
- runtime statistics

### Changed

- update Akka to 2.3.7
- migration to Java 8 and Scala 2.11
- `target` field in `Cookbook` is now optional
- `DFASDL` checks for empty id
- `ConnectionInformation` fields use `Option` now instead of empty strings

## 1.2.1 (2014-07-11)

### Changed

- adjust json codec for key field definition

## 1.2.0 (2014-07-11)

### Changed

- adjust mapping key field definition

## 1.1.0 (2014-07-11)

### Added

- key field definitions

## 1.0.1 (2014-07-10)

### Added

- Tests
- helper functions

## 1.0.0 (2014-07-10)

- initial release
