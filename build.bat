@echo off
mkdir bin\images
javac -d ./bin/ ./src/*.java
copy images bin\images
pause