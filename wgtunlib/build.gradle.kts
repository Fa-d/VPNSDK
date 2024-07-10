plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.hilt.android)
    id("org.jetbrains.kotlin.plugin.serialization")
    alias(libs.plugins.ksp)
    id("maven-publish")
}

android {
    namespace = "com.faddy.wgtunlib"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        targetSdk = 34
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isJniDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "lib-proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = true
            isJniDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "lib-proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
        aidl = true
    }
    packaging {
        jniLibs {
            useLegacyPackaging = true
        }
    }
    buildToolsVersion = "34.0.0"
    lint {
        abortOnError = false
        absolutePaths = false
       // baseline = file("lint-baseline.xml")
    }
}

publishing {
    publications {
        register<MavenPublication>("bar") {
            groupId = "com.pheonixLib.wgtunlib"
            artifactId = "wgtunlib"
            version = "0.0.1"
            artifact("$buildDir/outputs/aar/wgtunlib-release.aar")
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
    implementation(project(":commonCore"))
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // lifecycle
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.service)

}
