plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    kotlin("kapt")
}

android {
    compileSdkVersion(deps.build.compileSdkVersion)
    buildToolsVersion = deps.build.buildToolsVersion

    defaultConfig {
        applicationId = "app.ticktock"
        minSdkVersion(deps.build.minSdkVersion)
        targetSdkVersion(deps.build.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    // androidx
    implementation(deps.android.androidx.appCompat)
    implementation(deps.android.androidx.coreKtx)
    implementation(deps.android.androidx.recyclerView)
    implementation(deps.android.androidx.lifecycle.viewModel)
    kapt(deps.android.androidx.hilt.compiler)
    implementation(deps.android.androidx.hilt.viewModel)
    implementation(deps.android.androidx.fragmentKtx)
    implementation(deps.android.androidx.constraint)

    // google
    implementation(deps.google.material)
    implementation(deps.google.hilt.android)
    kapt(deps.google.hilt.compiler)

    // kotlin
    implementation(deps.kotlin.stdlib)

    // moshi
    implementation(deps.moshi.core)
    implementation(deps.moshi.kotlin)
    kapt(deps.moshi.codegen)

    // room
    implementation(deps.room.runtime)
    implementation(deps.room.ktx)
    implementation(deps.room.rxJava2)
    kapt(deps.room.apt)

    // rx
    implementation(deps.rx.android)
    implementation(deps.rx.java)

    implementation(deps.timber)

    implementation(deps.timber)
    testImplementation(deps.junit)
}