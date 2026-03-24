@echo off
echo =======================================================
echo iniciando limpieza total de la base de datos...
echo =======================================================
echo.

echo [paso 1 de 2] eliminando esquemas de tenants (clientes)...
call mvn liquibase:update -Dliquibase.changeLogFile=src/main/resources/db/changelog/utilities/dropAll.sql

if %errorlevel% neq 0 (
    echo.
    echo [error] el paso 1 fallo.
    pause
    exit /b %errorlevel%
)

echo.
echo [paso 2 de 2] ejecutando liquibase dropall para limpiar el esquema public...
call mvn liquibase:dropAll

if %errorlevel% neq 0 (
    echo.
    echo [error] el paso 2 fallo.
    pause
    exit /b %errorlevel%
)

echo.
echo =======================================================
echo ¡exito! la base de datos ha sido completamente limpiada.
echo =======================================================
pause