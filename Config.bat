@echo off

echo ===============================
echo   JWT KeyStore Setup Script
echo ===============================


set /p KEYSTORE_PASSWORD=Enter keystore password: 

set KEYSTORE_DIR=C:\secure\keys
set KEYSTORE_PATH=%KEYSTORE_DIR%\keystore.p12


if not exist "%KEYSTORE_DIR%" (
    echo Creating secure directory...
    mkdir "%KEYSTORE_DIR%"
)


echo Generating PKCS12 keystore...

keytool -genkeypair ^
 -alias jwt-key ^
 -keyalg RSA ^
 -keysize 2048 ^
 -storetype PKCS12 ^
 -keystore "%KEYSTORE_PATH%" ^
 -storepass %KEYSTORE_PASSWORD% ^
 -keypass %KEYSTORE_PASSWORD% ^
 -dname "CN=JWT, OU=Dev, O=MyApp, L=Cairo, S=Cairo, C=EG"

REM === 5) حفظ Environment Variables ===
echo Setting environment variables...

setx KEYSTORE_PATH "%KEYSTORE_PATH%"
setx KEYSTORE_PASSWORD "%KEYSTORE_PASSWORD%"

echo.
echo ✅ Setup Completed Successfully!
echo 📁 Keystore Location: %KEYSTORE_PATH%
echo ⚠️ Restart terminal or IDE to apply environment variables
echo.

pause