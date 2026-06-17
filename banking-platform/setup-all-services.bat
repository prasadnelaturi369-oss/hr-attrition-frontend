@echo off
echo Creating all service directories...

cd C:\Users\LENOVO\OneDrive\Desktop\stackly-tasks\banking-platform

echo.
echo Starting Customer Service...
start "Customer Service" cmd /k "cd customer-service && mvn spring-boot:run"

echo Starting Account Service...
start "Account Service" cmd /k "cd account-service && mvn spring-boot:run"

echo Starting Transaction Service...
start "Transaction Service" cmd /k "cd transaction-service && mvn spring-boot:run"

echo Starting Notification Service...
start "Notification Service" cmd /k "cd notification-service && mvn spring-boot:run"

echo.
echo All services starting!
pause