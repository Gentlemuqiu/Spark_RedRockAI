plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.redrock.module.video"
    compileSdk = 34
    viewBinding {
        enable = true
    }
    dataBinding{
        enable=true
    }

    defaultConfig {
       //  applicationId = "com.example.redrock.module.video"
        minSdk = 24
        targetSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}
kapt {
    arguments {
        arg("AROUTER_MODULE_NAME", project.getName())
    }
}


dependencies {
    implementation(project(":lib_api"))
    implementation(project(":lib_net"))
    implementation(project(":lib_utils"))


    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //导入视频播放的控件
    implementation("xyz.doikki.android.dkplayer:dkplayer-java:3.3.6")
    implementation("xyz.doikki.android.dkplayer:player-exo:3.3.6")
    implementation("xyz.doikki.android.dkplayer:player-ijk:3.3.6")
    implementation("xyz.doikki.android.dkplayer:dkplayer-ui:3.3.6")
    implementation("xyz.doikki.android.dkplayer:videocache:3.3.7")

    //解析图片
    implementation("com.github.bumptech.glide:glide:4.13.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.13.0")
    //解析网络
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //协程
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.5.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    //笔记
    implementation("jp.wasabeef:richeditor-android:2.0.0")

    //UC crop裁剪
    implementation("com.github.yalantis:ucrop:2.2.6")

    //语法糖kts插件
    implementation("androidx.fragment:fragment-ktx:1.5.5")

    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    kapt("com.alibaba:arouter-compiler:1.5.2")
    implementation("com.alibaba:arouter-api:1.5.2")

    //使用room
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    testImplementation("androidx.room:room-testing:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")


}