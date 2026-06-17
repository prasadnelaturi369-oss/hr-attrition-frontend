@echo off
echo ========================================
echo Starting Customer Service
echo ========================================

cd /d C:\Users\LENOVO\OneDrive\Desktop\stackly-tasks\banking-platform\customer-service

echo Setting temp directory...
set TEMP=C:\temp
set TMP=C:\temp
mkdir C:\temp 2>nul

echo Cleaning...
mvn clean -DskipTests

echo Starting Customer Service on port 8081...
echo.
mvn spring-boot:run -DskipTests

pause