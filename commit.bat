@echo off
set /p notes="Enter commit notes: "

git add -A
git commit -v -m "%notes%"
pause
