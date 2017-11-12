#!/bin/bash

javac *.java

echo "Manifest-Version: 1.0
Created-By: 1.8.0_162 (Oracle Corporation)
Main-Class: task21" > MANIFEST.MF

jar cfm task21.jar MANIFEST.MF *.class *.java

echo "Manifest-Version: 1.0
Created-By: 1.8.0_162 (Oracle Corporation)
Main-Class: task22" > MANIFEST.MF

jar cfm task22.jar MANIFEST.MF *.class *.java
