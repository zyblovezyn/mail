@echo off
set port=8080
set /P port="??入要??的端口，默?443："
rem if "%port%" == "" (set port=443)
echo ?始搜索'netstat -ano^|findstr ":%port%" '

for /f "delims=" %%i in ( 'netstat -ano^|findstr :%port%' ) do set pid=%%i
set id=%pid:~-5%
echo 使用端口%port%的?程pid：%id%；?始?束它
TASKKILL /PID %id% /F
pause
exit;
