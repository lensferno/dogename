@echo off
@echo ############################################
@echo #                                          #
@echo #                                          #
@echo #              DM MASTER升级                #
@echo #                                          #
@echo #                                          #
@echo ############################################
@echo 
@echo DM master正在升级，请不要关闭此窗口.....
taskkill /im DMmaster.exe
del DM_master.exe
del DMmaster.exe
rename update.tmp DMmaster.exe
start DMmaster
exit
