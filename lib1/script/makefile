SD_SAVE_PATH := /sdcard/
SRC_ROOT := E:\project_android\DexDemo\lib1
SRC_PATH := $(SRC_ROOT)\src\main\java\com\lpc\protect\Main.java
SRC_PATH_BUILD := $(SRC_ROOT)\script\build
SRC_PATH_BUILD_CLASS := $(SRC_ROOT)\script\build\lpc\protect\Main.class
SRC_EXE_MAIN := com.lpc.protect.Main

all:
	if exist "$(SRC_PATH_BUILD)" del /S /Q $(SRC_PATH_BUILD)
	if not exist "$(SRC_PATH_BUILD)" mkdir $(SRC_PATH_BUILD)
	javac -encoding utf8  -d  $(SRC_PATH_BUILD)  $(SRC_PATH)
	java -cp  $(SRC_PATH_BUILD) $(SRC_EXE_MAIN)

