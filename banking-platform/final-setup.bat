@echo off
echo ========================================
echo Banking Platform - Complete Setup
echo ========================================

cd C:\Users\LENOVO\OneDrive\Desktop\stackly-tasks\banking-platform

echo.
echo Step 1: Deleting test folders...
rmdir /s /q service-registry\src\test 2>nul
rmdir /s /q api-gateway\src\test 2>nul
rmdir /s /q customer-service\src\test 2>nul
rmdir /s /q account-service\src\test 2>nul
rmdir /s /q transaction-service\src\test 2>nul
rmdir /s /q notification-service\src\test 2>nul
echo Tests deleted!

echo.
echo Step 2: Building all services...
mvn clean install -DskipTests

if %errorlevel% neq 0 (
    echo Build failed! Check errors above.
    pause
    exit /b %errorlevel%
)

echo Build successful!

echo.
echo Step 3: Starting Service Registry...
start "Service Registry" cmd /k "cd service-registry && mvn spring-boot:run -DskipTests"

echo Waiting 15 seconds for Service Registry...
timeout /t 15 /nobreak > nul

echo.
echo Step 4: Starting API Gateway...
start "API Gateway" cmd /k "cd api-gateway && mvn spring-boot:run -DskipTests"

echo Waiting 10 seconds...
timeout /t 10 /nobreak > nul

echo.
echo Step 5: Starting Customer Service...
start "Customer Service" cmd /k "cd customer-service && mvn spring-boot:run -DskipTests"

echo Waiting 5 seconds...
timeout /t 5 /nobreak > nul

echo.
echo Step 6: Starting Account Service...
start "Account Service" cmd /k "cd account-service && mvn spring-boot:run -DskipTests"

echo Waiting 5 seconds...
timeout /t 5 /nobreak > nul

echo.
echo Step 7: Starting Transaction Service...
start "Transaction Service" cmd /k "cd transaction-service && mvn spring-boot:run -DskipTests"

echo Waiting 5 seconds...
timeout /t 5 /nobreak > nul

echo.
echo Step 8: Starting Notification Service...
start "Notification Service" cmd /k "cd notification-service && mvn spring-boot:run -DskipTests"

echo.
echo ========================================
echo ALL SERVICES STARTED!
echo ========================================
echo.
echo Access URLs:
echo   Eureka Dashboard: http://localhost:8761
echo   API Gateway: http://localhost:8080
echo   Customer Service Direct: http://localhost:8081
echo.
echo Eureka Login: eureka / password
echo.
echo Test Commands:
echo   curl -X POST http://localhost:8081/api/auth/register -H "Content-Type: application/json" -d "{\"firstName\":\"Test\",\"lastName\":\"User\",\"email\":\"test@test.com\",\"phoneNumber\":\"1234567890\",\"password\":\"test123\"}"
echo.
pause