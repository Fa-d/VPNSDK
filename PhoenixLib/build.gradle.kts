plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.hilt.android)
    id("org.jetbrains.kotlin.plugin.serialization")
    alias(libs.plugins.ksp)
    id("maven-publish")
}

android {
    namespace = "com.faddy.phoenixlib"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        targetSdk = 35

        externalNativeBuild {
            cmake {
                cppFlags("")
            }
        }
        ndk {
            abiFilters.add("armeabi-v7a")
            abiFilters.add("arm64-v8a")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

publishing {
    publications {
        register<MavenPublication>("bar") {
            groupId = "com.pheonixLib.phoenixlib"
            artifactId = "phoenixlib"
            version = "0.0.2"
            artifact("$buildDir/outputs/aar/PhoenixLib-release.aar")
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
    implementation(project(":protocol:openvpnlib"))
    implementation(project(":protocol:wgtunlib"))
    //implementation(project(":protocol:SingBox"))
    //implementation(project(":strongswan"))

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")

    //wg
    implementation(libs.androidx.lifecycle.service)


    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // lifecycle
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.process)


    // serialization
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.lifecycle.service)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}