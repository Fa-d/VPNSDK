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
        aidl = true
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    implementation(fileTree("libs"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation("androidx.room:room-runtime:2.6.1")
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.preference:preference-ktx:1.2.1")
    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

}