@REM SETTING VARIABLES
set "src-dir=src"
set "lib-dir=."

set "target-name=%1"
if "%1" == "" (
    set "target-name=toolkit-firebase"
)

set "target-dir=./"

@REM REMOVE EXISTING PROJECT FILES
rmdir /q/s "temp"
echo "%target-dir%"
del "%target-dir%/%target-name%.jar"

@REM CREATE A BASE PROJECT STRUCTURE
mkdir "temp/bin"
mkdir "temp/src"

@REM COPY UTILITY FILES INTO THE PROJECT
echo D | xcopy /q/s/y "%src-dir%\toolkit\firebase\auth" "temp\src"
echo D | xcopy /q/s/y "%src-dir%\toolkit\firebase\exception" "temp\src"
echo D | xcopy /q/s/y "%src-dir%\toolkit\firebase\firestore" "temp\src"
echo D | xcopy /q/s/y "%src-dir%\toolkit\http" "temp\src"
echo D | xcopy /q/s/y "%src-dir%\toolkit\mail" "temp\src"

@REM  COMPITLE JAVA CODE
javac -parameters --release 17 -g -cp "lib\*" -d "temp/bin" temp/src/*.java
@REM javadoc -d "temp/doc" "temp/src/Arrays.java" "temp/src/Maths.java"

@REM ZIP PROJECT INTO .jar FILE
jar cf "temp/%target-name%.jar" -C "temp/bin" .
@REM jar cf "temp/%target-name%.jar" -C "temp/bin" . -C "temp/doc" .
rmdir /q/s "temp/bin"
rmdir /q/s "temp/src"
rmdir /q/s "temp/doc"

@REM COPY JAR FILE INTO test
echo D | xcopy /q/s/y "temp" "%target-dir%"
@REM DELETE TEMPORARY FILES
@REM rmdir /q/s "temp"

pause
cls