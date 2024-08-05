plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.redrockai"
    compileSdk = 34


    viewBinding {
        enable = true
    }

    defaultConfig {
        applicationId = "com.example.redrockai"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk {
            abiFilters.apply {
//                add("armeabi")
//                add("armeabi-v7a")
//                add("x86")
//                add("mips")
            }
        }


        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        kotlinOptions {
            jvmTarget = "1.8"
        }
        sourceSets {
            named("main") {
                jniLibs.srcDirs("libs")
            }
        }
    }

    dependencies {
        implementation(project(":lib_utils"))
        implementation(project(":lib_api"))
        implementation(project(":module_video"))
        implementation(project(":module_schoolroom"))
        implementation(project(":module_playground"))
        implementation(project(":module_mine"))
        implementation(project(":module_teacher"))
        implementation(project(":module_message"))
        implementation("com.hjq:xxpermissions:8.2")
        implementation("com.airbnb.android:lottie:3.1.0")

        implementation("androidx.core:core-ktx:1.12.0")
        implementation("androidx.appcompat:appcompat:1.6.1")
        implementation("com.google.android.material:material:1.11.0")
        //每日签到
        implementation("com.github.prolificinteractive:material-calendarview:2.0.1")
        implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


        //网络请求相关库
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")


    }
}
