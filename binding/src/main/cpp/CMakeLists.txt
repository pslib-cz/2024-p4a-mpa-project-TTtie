# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html.
# For more examples on how to use CMake, see https://github.com/android/ndk-samples.

# Sets the minimum CMake version required for this project.
cmake_minimum_required(VERSION 3.22.1)

# Declares the project name. The project name can be accessed via ${ PROJECT_NAME},
# Since this is the top level CMakeLists.txt, the project name is also accessible
# with ${CMAKE_PROJECT_NAME} (both CMake variables are in-sync within the top level
# build script scope).
project("qalculate_binding")

include(ExternalProject)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
#
# In this top level CMakeLists.txt, ${CMAKE_PROJECT_NAME} is used to define
# the target library name; in the sub-module's CMakeLists.txt, ${PROJECT_NAME}
# is preferred for the same purpose.
#
# In order to load a library into your app from Java/Kotlin, you must call
# System.loadLibrary() and pass the name of the library defined here;
# for GameActivity/NativeActivity derived applications, the same library name must be
# used in the AndroidManifest.xml file.

add_library(${CMAKE_PROJECT_NAME} SHARED
        # List C/C++ source files with relative paths to this CMakeLists.txt.
        binding.cpp)

add_library(asprintf STATIC IMPORTED)
set_target_properties(asprintf PROPERTIES
    IMPORTED_LOCATION "${CMAKE_SOURCE_DIR}/libs/${CMAKE_ANDROID_ARCH_ABI}/libasprintf.a"
    INTERFACE_INCLUDE_DIRECTORIES "${CMAKE_SOURCE_DIR}/include")

add_library(gettextpo STATIC IMPORTED)
set_target_properties(gettextpo PROPERTIES
        IMPORTED_LOCATION "${CMAKE_SOURCE_DIR}/libs/${CMAKE_ANDROID_ARCH_ABI}/libgettextpo.a"
        INTERFACE_INCLUDE_DIRECTORIES "${CMAKE_SOURCE_DIR}/include")

add_library(gmp STATIC IMPORTED)
set_target_properties(gmp PROPERTIES
        IMPORTED_LOCATION "${CMAKE_SOURCE_DIR}/libs/${CMAKE_ANDROID_ARCH_ABI}/libgmp.a"
        INTERFACE_INCLUDE_DIRECTORIES "${CMAKE_SOURCE_DIR}/include")

add_library(icu_data STATIC IMPORTED)
set_target_properties(icu_data PROPERTIES
        IMPORTED_LOCATION "${CMAKE_SOURCE_DIR}/libs/${CMAKE_ANDROID_ARCH_ABI}/libicudata.a"
        INTERFACE_INCLUDE_DIRECTORIES "${CMAKE_SOURCE_DIR}/include")

add_library(icu_i18n STATIC IMPORTED)
set_target_properties(icu_i18n PROPERTIES
        IMPORTED_LOCATION "${CMAKE_SOURCE_DIR}/libs/${CMAKE_ANDROID_ARCH_ABI}/libicui18n.a"
        INTERFACE_INCLUDE_DIRECTORIES "${CMAKE_SOURCE_DIR}/include")

add_library(icu_io STATIC IMPORTED)
set_target_properties(icu_io PROPERTIES
        IMPORTED_LOCATION "${CMAKE_SOURCE_DIR}/libs/${CMAKE_ANDROID_ARCH_ABI}/libicuio.a"
        INTERFACE_INCLUDE_DIRECTORIES "${CMAKE_SOURCE_DIR}/include")

add_library(icu_uc STATIC IMPORTED)
set_target_properties(icu_uc PROPERTIES
        IMPORTED_LOCATION "${CMAKE_SOURCE_DIR}/libs/${CMAKE_ANDROID_ARCH_ABI}/libicuuc.a"
        INTERFACE_INCLUDE_DIRECTORIES "${CMAKE_SOURCE_DIR}/include")

add_library(intl STATIC IMPORTED)
set_target_properties(intl PROPERTIES
        IMPORTED_LOCATION "${CMAKE_SOURCE_DIR}/libs/${CMAKE_ANDROID_ARCH_ABI}/libintl.a"
        INTERFACE_INCLUDE_DIRECTORIES "${CMAKE_SOURCE_DIR}/include")

add_library(mpfr STATIC IMPORTED)
set_target_properties(mpfr PROPERTIES
        IMPORTED_LOCATION "${CMAKE_SOURCE_DIR}/libs/${CMAKE_ANDROID_ARCH_ABI}/libmpfr.a"
        INTERFACE_INCLUDE_DIRECTORIES "${CMAKE_SOURCE_DIR}/include")

add_library(qalculate STATIC IMPORTED)
set_target_properties(qalculate PROPERTIES
        IMPORTED_LOCATION "${CMAKE_SOURCE_DIR}/libs/${CMAKE_ANDROID_ARCH_ABI}/libqalculate.a"
        INTERFACE_INCLUDE_DIRECTORIES "${CMAKE_SOURCE_DIR}/include")

add_library(textstyle STATIC IMPORTED)
set_target_properties(textstyle PROPERTIES
        IMPORTED_LOCATION "${CMAKE_SOURCE_DIR}/libs/${CMAKE_ANDROID_ARCH_ABI}/libtextstyle.a"
        INTERFACE_INCLUDE_DIRECTORIES "${CMAKE_SOURCE_DIR}/include")

add_library(xml2 STATIC IMPORTED)
set_target_properties(xml2 PROPERTIES
        IMPORTED_LOCATION "${CMAKE_SOURCE_DIR}/libs/${CMAKE_ANDROID_ARCH_ABI}/libxml2.a"
        INTERFACE_INCLUDE_DIRECTORIES "${CMAKE_SOURCE_DIR}/include")

# Specifies libraries CMake should link to your target library. You
# can link libraries from various origins, such as libraries defined in this
# build script, prebuilt third-party libraries, or Android system libraries.

add_definitions(-DU_DISABLE_RENAMING=1) # ICU requires that this is disabled

target_link_libraries(${CMAKE_PROJECT_NAME}
        PUBLIC
        # List libraries link to the target library
        asprintf
        gettextpo
        gmp
        icu_data
        icu_i18n
        icu_io
        icu_uc
        intl
        mpfr
        qalculate
        textstyle
        xml2
        android
        log)