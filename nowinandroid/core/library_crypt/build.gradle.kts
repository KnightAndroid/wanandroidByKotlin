plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.knight.library.cryption"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        //这里指定cpu架构
        ndk {

            abiFilters.add("arm64-v8a")
            abiFilters.add("armeabi-v7a")
        }
//        externalNativeBuild {
//            cmake {
//                cppFlags("-std=c++11")
//            }
//        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
//            consumerProguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),"proguard-rules.pro")
        }
    }
    externalNativeBuild {
        cmake {
            // path = file("src/main/cpp/CMakeLists.txt")
            path = file("CMakeLists.txt")
            version = "3.6.0"


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
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}

//用作生成aar
