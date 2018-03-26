# CS5530 UUber
### Authors: Jaden Holladay, Juno Kim

### Running:
1. If the build directory doesn't exist: `mkdir build`
2. Compile: `javac -d "./build/" -cp "./libs/*" "./classes/cs5530/*.java" "./classes/cs5530/models/*.java"`
2. * In windows system: `java -cp "./libs/*;./build/" cs5530.testdriver2`
   * In Linux/Unix system: `java -cp ./libs/*:./build/: cs5530.testdriver2`

### Requires:
* mysql.jar
* jsch-0.1.54.jar
* json_simple-1.1.jar

### Formatting
* Use YYYY-MM-DD HH:MM:SS for date/times.
  * ie. 2018-03-24 12:00:00