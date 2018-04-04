# Bash debugger [![Build Status](https://travis-ci.org/emfataliev/Bash-Debugger.svg?branch=master)](https://travis-ci.org/emfataliev/Bash-Debugger)

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

 ``` mvn clean install```
 
### Examples

#### Command Line
```java -jar target/bashdb-v1.0-SNAPSHOT -d /Users/exampl/my_script.sh```

## License

The contents of this repository are covered under the [GNU General Public License v3.0](LICENSE)
