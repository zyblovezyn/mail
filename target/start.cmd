@echo off
set port=8080
for /f "delims=" %%i in ( 'netstat -ano^|findstr :%port%' ) do set pid=%%i
set id=%pid:~-5%
TASKKILL /PID %id% /F
java -jar mail-1.0.jar