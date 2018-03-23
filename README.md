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
* java-json.jar