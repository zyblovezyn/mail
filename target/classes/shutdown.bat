@echo off
set port=8080
set /P port="??���v??�I�[���C��?443�F"
rem if "%port%" == "" (set port=443)
echo ?�n�r��'netstat -ano^|findstr ":%port%" '

for /f "delims=" %%i in ( 'netstat -ano^|findstr :%port%' ) do set pid=%%i
set id=%pid:~-5%
echo �g�p�[��%port%�I?��pid�F%id%�G?�n?����
TASKKILL /PID %id% /F
pause
exit;
