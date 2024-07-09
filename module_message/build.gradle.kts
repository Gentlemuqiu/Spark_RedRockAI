plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.redrockai.module.message"
    compileSdk = 34

    viewBinding {
        enable = true
    }

    defaultConfig {
        minSdk = 24
        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk {
            abiFilters.apply {
                add("armeabi")
                add("armeabi-v7a")
                add("x86")
                add("mips")
            }
        }

        consumerProguardFiles("consumer-rules.pro")
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
    implementation ("com.hjq:xxpermissions:8.2")
    //implementation(files("libs/SparkChain1.aar"))

    implementation(project(":lib_net"))
    implementation(project(":lib_utils"))
    implementation(project(":lib_api"))
    implementation(files("libs/SparkChain1.aar"))
    implementation(files("libs\\Msc.jar"))
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}