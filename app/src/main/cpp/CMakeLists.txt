# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

#编译过程中生成二进制码没有被剥除，需要在CMakeLists.txt中加入如下设置,可缩小体积
#DEBUG版本
set(CMAKE_C_FLAGS_DEBUG "${CMAKE_C_FLAGS_DEBUG} -s")
set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -s")
#RELEASE版本
set(CMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} -s")
set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -s")

#设置生成的so动态库最后输出的路径
#set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/../jni/${ANDROID_ABI})
#正确的 但是会同时加载c代码库和so导致冲突 这里先修改下路径
#set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI})


#添加子目录,将会调用子目录中的CMakeLists.txt
ADD_SUBDIRECTORY(static_register)
ADD_SUBDIRECTORY(dynamic_register)
