@echo off
taskkill /im DMmaster.exe
del /q *.exe
rename update.tmp DMmaster.exe
start DMmaster
exit
