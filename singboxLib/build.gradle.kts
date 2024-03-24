plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "io.nekohasekai.sfa2"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34
    }



    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        aidl = true
        buildConfig = true
    }
}

dependencies {
    implementation(fileTree("libs"))
}