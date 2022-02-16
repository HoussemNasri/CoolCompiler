cmake_minimum_required(VERSION 3.19)
project(PredictiveParsingInC C)

set(CMAKE_C_STANDARD 99)

add_executable(PredictiveParsingInC main.c Stack.c Stack.h Parser.c Parser.h)