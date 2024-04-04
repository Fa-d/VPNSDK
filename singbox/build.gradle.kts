plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    alias(libs.plugins.hilt.android)
    id("org.jetbrains.kotlin.plugin.serialization")
    alias(libs.plugins.ksp)
}

android {
    namespace = "io.nekohasekai.sfa"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34
    }

    ksp {
        arg("room.incremental", "true")
        arg("room.schemaLocation", "$projectDir/schemas")
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
        buildConfig = true
        aidl = true
    }
    packagingOptions {
        jniLibs {
            useLegacyPackaging = true
        }
    }
}

dependencies {
    //api(project(":singboxLib"))
    implementation("androidx.room:room-runtime:2.6.1")
    implementation(libs.androidx.work.runtime.ktx)
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.preference:preference-ktx:1.2.1")
    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

}