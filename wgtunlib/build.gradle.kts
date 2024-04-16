plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.hilt.android)
    id("org.jetbrains.kotlin.plugin.serialization")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.faddy.wgtunlib"
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
/*        packaging.jniLibs.keepDebugSymbols.addAll(
            listOf("libwg-go.so", "libwg-quick.so", "libwg.so"),
        )*/
    }
    packaging.jniLibs.keepDebugSymbols.addAll(
        listOf("libwg-go.so", "libwg-quick.so", "libwg.so"),
    )
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    //  sourceSets { main { jniLibs.srcDirs = ['libs'] } }
    /*    java.sourceSets["main"].java {
            srcDir("jniLibs")
        }*/
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.androidx.lifecycle.service)
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // logging
    implementation(libs.timber)

    // compose navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

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
    implementation(libs.androidx.datastore.preferences)

    // lifecycle
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.process)

    // icons
    implementation(libs.material.icons.extended)
    // serialization
    implementation(libs.kotlinx.serialization.json)
}
