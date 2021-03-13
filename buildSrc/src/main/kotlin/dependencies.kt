@file:Suppress("ClassName", "unused")

object deps {
    object versions {
        const val kotlin = "1.4.20-RC"
        const val retrofit = "2.9.0"
        const val apollo = "2.5.4"
        const val nav_version = "2.3.3"
    }

    object android {
        const val gradlePlugin = "com.android.tools.build:gradle:4.1.1"

        object androidx {
            const val appCompat = "androidx.appcompat:appcompat:1.2.0"
            const val coreKtx = "androidx.core:core-ktx:1.3.2"
            const val recyclerView = "androidx.recyclerview:recyclerview:1.1.0"
            const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:2.3.1"

            object lifecycle {
                const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0"
            }

            object navigation {
                const val fragment =
                    "androidx.navigation:navigation-fragment-ktx:${versions.nav_version}"
                const val ui = "androidx.navigation:navigation-ui-ktx:${versions.nav_version}"
                const val gradlePlugin =
                    "androidx.navigation:navigation-safe-args-gradle-plugin:${versions.nav_version}"
            }

            object hilt {
                const val compiler = "androidx.hilt:hilt-compiler:1.0.0-alpha02"
                const val viewModel = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha01"
            }
        }
    }

    object build {
        const val compileSdkVersion = 30
        const val minSdkVersion = 26
        const val targetSdkVersion = 30
        const val buildToolsVersion = "30.0.2"
    }

    object google {
        object hilt {
            const val gradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:2.28-alpha"
            const val android = "com.google.dagger:hilt-android:2.28-alpha"
            const val compiler = "com.google.dagger:hilt-android-compiler:2.28-alpha"
        }

        const val material = "com.google.android.material:material:1.2.1"
    }

    object kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${versions.kotlin}"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${versions.kotlin}"
    }

    object moshi {
        const val core = "com.squareup.moshi:moshi:1.11.0"
        const val kotlin = "com.squareup.moshi:moshi-kotlin:1.11.0"
        const val codegen = "com.squareup.moshi:moshi-kotlin-codegen:1.11.0"
    }

    object room {
        private const val version = "2.3.0-alpha02"
        const val apt = "androidx.room:room-compiler:$version"
        const val ktx = "androidx.room:room-ktx:$version"
        const val runtime = "androidx.room:room-runtime:$version"
        const val rxJava2 = "androidx.room:room-rxjava2:$version"
    }

    object rx {
        const val android = "io.reactivex.rxjava2:rxandroid:2.1.1"
        const val java = "io.reactivex.rxjava2:rxjava:2.2.19"
    }


    const val timber = "com.jakewharton.timber:timber:4.7.1"

    const val junit = "junit:junit:4.13.2"
}