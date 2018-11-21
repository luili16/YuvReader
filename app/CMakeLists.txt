cmake_minimum_required(VERSION 3.4.1)

add_library(
            yuv-util

            SHARED

            src/main/cpp/yuv_to_rgb.cpp )

add_library (
            yuv

            SHARED

            IMPORTED
)

set_target_properties(
            yuv

            PROPERTIES IMPORTED_LOCATION

            ${PROJECT_SOURCE_DIR}/libs/${ANDROID_ABI}/libyuv.so
)

include_directories(src/main/cpp/include/)

find_library(
            log-lib

            log )


target_link_libraries(

            yuv-util

            yuv

            ${log-lib}
)