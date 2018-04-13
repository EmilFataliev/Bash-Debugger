# Bash Debugger [![Build Status](https://travis-ci.org/emfataliev/Bash-Debugger.svg?branch=master)](https://travis-ci.org/emfataliev/Bash-Debugger) [![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0) 

The Bash Debugger Project is a source-code debugger for bash that allows:
* start your script, specifying something that might affect its behavior
* step-by-step execution
* examine what has happened, when your script has stopped

### In progress
* make your script stop on specified conditions (breakpoints)
* managing the status of the script (variables)

# Developer information

### Building

Execute the script to build the .jar file

 ```./gradlew clean build```
 
### Examples

#### Command Line
Example of simple executing: 

```java -jar build/libs/bashdb-1.0-SNAPSHOT-all.jar -d [script path]```

## License

The contents of this repository are covered under the [GNU General Public License v3.0](LICENSE)
