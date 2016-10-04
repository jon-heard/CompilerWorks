@echo off
set /p notes="Enter commit notes: "

git add -A
git commit -m "%notes%"
pause
