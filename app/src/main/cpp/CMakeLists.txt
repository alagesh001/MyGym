cmake_minimum_required(VERSION 3.18.1)

# Set the project name
project("native-lib")

# Create the shared library
add_library(
        native-lib # Must match the name used in Kotlin's System.loadLibrary()
        SHARED
        native-lib.cpp)

# Find the log library
find_library(
        log-lib
        log)

# Link the libraries
target_link_libraries(
        native-lib
       )
