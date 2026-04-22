@echo off

echo ===============================
echo   Cleanup KeyStore Script
echo ===============================


set KEYSTORE_PATH=C:\secure\keys\keystore.p12
set KEYSTORE_DIR=C:\secure\keys


if exist "%KEYSTORE_PATH%" (
    echo Deleting keystore file...
    del "%KEYSTORE_PATH%"
) else (
    echo Keystore file not found.
)


if exist "%KEYSTORE_DIR%" (
    rmdir "%KEYSTORE_DIR%" 2>nul
)


echo Removing environment variables...

setx KEYSTORE_PASSWORD ""
setx KEYSTORE_PATH ""

REM === 5) إنهاء ===
echo.
echo ✅ Cleanup Completed!
echo ⚠️ Please restart IntelliJ or terminal
echo.

pause