@echo off
git status --porcelain
set /p notes="Enter commit notes: "

git add -A
git commit -v -m "%notes%"
pause
