@echo off

echo ===============================
echo   FULL CLEANUP (DANGEROUS)
echo ===============================

set KEYSTORE_PATH=C:\secure\keys\keystore.p12

echo.
echo  WARNING: This will delete:
echo - Keystore file
echo - JWT keys
echo - Pepper secret
echo - Environment variables
echo.

set /p CONFIRM=Are you sure? (yes/no): 

if /I NOT "%CONFIRM%"=="yes" (
    echo Operation cancelled.
    pause
    exit /b
)

echo.
echo Removing environment variables...

setx KEYSTORE_PATH ""
setx KEYSTORE_PASSWORD ""

echo.
echo Deleting keystore file...

if exist "%KEYSTORE_PATH%" (
    del "%KEYSTORE_PATH%"
    echo  Keystore deleted
) else (
    echo  Keystore not found
)

echo.
echo Cleaning directory (optional)...

set KEYSTORE_DIR=C:\secure\keys
if exist "%KEYSTORE_DIR%" (
    rmdir /s /q "%KEYSTORE_DIR%"
    echo  Directory removed
)

echo.
echo  FULL CLEANUP COMPLETED
echo  Restart terminal/IDE

pause