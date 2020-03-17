cmake_minimum_required(VERSION 3.15)
project(launcher)

set(CMAKE_CXX_STANDARD 14)

add_executable(launcher main/main.cpp main/GetRegValue.h main/GetRegValue.cpp)