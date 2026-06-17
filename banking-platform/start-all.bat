@echo off
title Banking Platform Launcher
color 0A
echo ========================================
echo   Banking Platform Microservices
echo ========================================
echo.

echo [1/6] Starting Service Registry (Port 8761)...
start "Service Registry" cmd /k "cd service-registry && mvn spring-boot:run"

echo Waiting 15 seconds for Service Registry to initialize...
timeout /t 15 /nobreak > nul

echo [2/6] Starting API Gateway (Port 8080)...
start "API Gateway" cmd /k "cd api-gateway && mvn spring-boot:run"

timeout /t 8 /nobreak > nul

echo [3/6] Starting Customer Service (Port 8081)...
start "Customer Service" cmd /k "cd customer-service && mvn spring-boot:run"

timeout /t 8 /nobreak > nul

echo [4/6] Starting Account Service (Port 8082)...
start "Account Service" cmd /k "cd account-service && mvn spring-boot:run"

timeout /t 8 /nobreak > nul

echo [5/6] Starting Transaction Service (Port 8083)...
start "Transaction Service" cmd /k "cd transaction-service && mvn spring-boot:run"

timeout /t 8 /nobreak > nul

echo [6/6] Starting Notification Service (Port 8084)...
start "Notification Service" cmd /k "cd notification-service && mvn spring-boot:run"

echo.
echo ========================================
echo All services are starting!
echo ========================================
echo.
echo Access URLs:
echo   Eureka Dashboard: http://localhost:8761
echo   API Gateway: http://localhost:8080
echo.
echo Eureka Login: eureka / password
echo.
echo Check running services in Eureka dashboard
echo.
pause