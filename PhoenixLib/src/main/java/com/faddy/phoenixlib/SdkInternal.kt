package com.faddy.phoenixlib

class SdkInternal {
    constructor() {
        System.loadLibrary("validator")
    }

    public external fun systemSetup(): Int

}