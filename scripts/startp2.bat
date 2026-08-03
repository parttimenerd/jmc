@echo off

echo "======== Building p2 repo ==================="
cd releng\third-party
call mvn %MAVENPARAMS% p2:site || EXIT /B 1
echo "======== Starting p2 repo ==================="
start /B cmd /C "mvn %MAVENPARAMS% jetty:run"
echo "======== Waiting for p2 repo to become available ==================="
:wait_loop
curl --silent --fail http://localhost:8080/site/content.jar > nul 2>&1
if %ERRORLEVEL% neq 0 (
  timeout /T 2 /NOBREAK > nul
  goto wait_loop
)
echo "======== Done ==============================="
