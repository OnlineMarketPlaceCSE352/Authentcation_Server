@echo off

echo ===============================
echo   JWT + Pepper KeyStore Setup
echo ===============================

set /p KEYSTORE_PASSWORD=Enter keystore password: 
set /p PEPPER_VALUE=Enter PEPPER value: 

set KEYSTORE_DIR=C:\secure\keys
set KEYSTORE_PATH=%KEYSTORE_DIR%\keystore.p12

if not exist "%KEYSTORE_DIR%" (
    echo Creating secure directory...
    mkdir "%KEYSTORE_DIR%"
)

echo.
echo Generating PKCS12 keystore...

keytool -genkeypair ^
 -alias jwt-key ^
 -keyalg RSA ^
 -keysize 2048 ^
 -storetype PKCS12 ^
 -keystore "%KEYSTORE_PATH%" ^
 -storepass %KEYSTORE_PASSWORD% ^
 -keypass %KEYSTORE_PASSWORD% ^
 -dname "CN=JWT, OU=Dev, O=MyApp, L=Cairo, ST=Cairo, C=EG"

echo.
echo Adding PEPPER as secret...


echo %PEPPER_VALUE% | keytool -importpass ^
 -alias pepper ^
 -keystore "%KEYSTORE_PATH%" ^
 -storetype PKCS12 ^
 -storepass %KEYSTORE_PASSWORD% ^
 -keypass %KEYSTORE_PASSWORD% ^
 -noprompt

echo.
echo Setting environment variables...

setx KEYSTORE_PATH "%KEYSTORE_PATH%"
setx KEYSTORE_PASSWORD "%KEYSTORE_PASSWORD%"

echo.
echo ✅ Setup Completed Successfully!
echo 📁 Keystore: %KEYSTORE_PATH%
echo 🔐 Pepper stored correctly (NOT the password)
echo ⚠️ Restart terminal or IDE

pause