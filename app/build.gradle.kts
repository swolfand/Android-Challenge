plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    compileSdkVersion(30)
    buildToolsVersion = "30.0.2"

    defaultConfig {
        applicationId = "app.ticktock"
        minSdkVersion("23")
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.20-RC")
    implementation("androidx.core:core-ktx:1.3.0")
    implementation("com.google.android.material:material:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("com.squareup.moshi:moshi-kotlin:1.9.3")
    implementation("androidx.activity:activity-ktx:1.1.0")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation("io.reactivex.rxjava3:rxjava:3.0.4")

    // Dagger Core
    implementation("com.google.dagger:dagger:2.28")
    kapt("com.google.dagger:dagger-compiler:2.28")

    // Dagger Android
    api("com.google.dagger:dagger-android:2.27")
    api("com.google.dagger:dagger-android-support:2.27")
    kapt("com.google.dagger:dagger-android-processor:2.27")

//Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.28-alpha")
    kapt("com.google.dagger:hilt-android-compiler:2.28-alpha")

    val room_version = "2.2.5"
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    implementation("androidx.room:room-ktx:$room_version")

    // optional - RxJava support for Room
    implementation("androidx.room:room-rxjava2:$room_version")
    implementation("com.jakewharton.rxrelay3:rxrelay:3.0.0")
    implementation("com.jakewharton.timber:timber:4.7.1")

    implementation(" com.google.dagger:hilt-android:2.28-alpha")
    kapt("com.google.dagger:hilt-android-compiler:2.28-alpha")
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha01")
    kapt("androidx.hilt:hilt-compiler:1.0.0-alpha01")

    val lifecycle_version = "2.2.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycle_version")

    implementation("com.github.akarnokd:rxjava3-bridge:3.0.0")

    debugImplementation("com.facebook.flipper:flipper:0.49.0")
    debugImplementation("com.facebook.soloader:soloader:0.9.0")

    releaseImplementation("com.facebook.flipper:flipper-noop:0.49.0")
}