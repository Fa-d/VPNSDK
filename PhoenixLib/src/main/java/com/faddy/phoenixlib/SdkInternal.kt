package com.faddy.phoenixlib

class SdkInternal {
    constructor() {
        System.loadLibrary("validator")
        System.loadLibrary("openconnect")
        System.loadLibrary("stoken")
    }

    public external fun systemSetup(): Int

}