plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.hilt.android)
    id("org.jetbrains.kotlin.plugin.serialization")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.faddy.phoenixlib"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34
    }
    ksp { arg("room.schemaLocation", "$projectDir/schemas") }

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
}

dependencies {
    implementation(project(":openvpnlib"))
    implementation(project(":wgtunlib")){
        isTransitive = true
    }
    implementation(project(":singbox"))
    //implementation(project(":openconnectlib"))
    //implementation(project(":strongswan"))

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")

    //wg
    implementation(libs.androidx.lifecycle.service)

    // logging
    implementation(libs.timber)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // accompanist
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.flowlayout)
    implementation(libs.accompanist.drawablepainter)

    // storage
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // lifecycle
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.process)

    // serialization
    implementation(libs.kotlinx.serialization.json)

    //wg
    implementation(libs.tunnel)
    implementation(libs.androidx.lifecycle.service)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}