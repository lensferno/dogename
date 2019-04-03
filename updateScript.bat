@echo off
taskkill /im DMmaster.exe
del DM_master.exe
rename update.tmp DMmaster.exe
start DMmaster
exit
