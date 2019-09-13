@echo off
@echo ######################################
@echo #                                    #
@echo #                                    #
@echo #         DM MASTER程序恢复           #
@echo #                                    #
@echo #                                    #
@echo ######################################
@echo 正在恢复，请不要关闭此窗口
curl https://github.com/Het7230/DM_master/releases/download/1.3.2.0/DMmaster.exe -o %~dp0\\DMmaster.exe --progress
start DMmaster.exe