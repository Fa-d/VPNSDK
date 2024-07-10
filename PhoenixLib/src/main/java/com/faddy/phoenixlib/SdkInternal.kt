package com.faddy.phoenixlib

class SdkInternal() {
    init {
        System.loadLibrary("validator")
    }

    public external fun systemSetup(): Int

}