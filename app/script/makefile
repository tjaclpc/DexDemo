SD_SAVE_PATH := /sdcard/
SRC_ROOT := E:\project_android\DexDemo\app
SRC_PATH := $(SRC_ROOT)\src\main\java\com\jdee\getpage\classinfo\Test.java
SRC_PATH_BUILD := $(SRC_ROOT)\script\build
SRC_PATH_BUILD_CLASS := $(SRC_ROOT)\script\build\com\jdee\getpage\classinfo\Test.class
SRC_PATH_BUILD_DEX := $(SRC_ROOT)\script\build\d8

all:
	if exist "$(SRC_PATH_BUILD)" del /S /Q $(SRC_PATH_BUILD)
	if not exist "$(SRC_PATH_BUILD)" mkdir $(SRC_PATH_BUILD)
	javac  -d  $(SRC_PATH_BUILD)  $(SRC_PATH)
	if not exist "$(SRC_PATH_BUILD_DEX)" mkdir $(SRC_PATH_BUILD_DEX)
	d8 --intermediate --output $(SRC_PATH_BUILD_DEX)  $(SRC_PATH_BUILD_CLASS)
	rename $(SRC_PATH_BUILD_DEX)\classes.dex Test.dex
	adb push $(SRC_PATH_BUILD_DEX)\Test.dex $(SD_SAVE_PATH)

