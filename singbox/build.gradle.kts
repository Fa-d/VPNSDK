plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    alias(libs.plugins.hilt.android)
    id("org.jetbrains.kotlin.plugin.serialization")
    alias(libs.plugins.ksp)
    id("maven-publish")
}

android {
    namespace = "com.faddy.singbox"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        lint.targetSdk = 34
        multiDexEnabled = true
    }
    buildToolsVersion = "34.0.0"

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
publishing {
    publications {
        register<MavenPublication>("bar") {
            groupId = "com.pheonixLib.singbox"
            artifactId = "singbox"
            version = "0.0.1"
            artifact("$buildDir/outputs/aar/singbox-release.aar")
        }
    }

    repositories {
        maven {
            url = uri("http://188.34.191.126:8080/private")
            isAllowInsecureProtocol = true
            credentials {
                username = "name"
                password = "YG2InE1sWt47Omf1wFVXJzdjF9JxUKavv3JnUCU3anj87jHXb/AL0eQVeVahF19d"
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.preference.ktx)
    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(libs.androidx.multidex)


}