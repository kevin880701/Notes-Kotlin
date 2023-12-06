import java.text.DateFormat

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-android")
    id ("kotlin-kapt")
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
    // Add the Crashlytics Gradle plugin
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.lhr.notes_kotlin"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.lhr.notes_kotlin"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    applicationVariants.all {
        val variant = this
        variant.outputs
            .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                val outputFileName = "NotesKotlin_${variant.baseName}_${variant.versionName}_${variant.versionCode}.apk"
                println("OutputFileName: $outputFileName")
                output.outputFileName = outputFileName
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
    viewBinding {
        enable = true
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    packagingOptions {
        resources.excludes.apply {
            add("META-INF/LICENSE")
            add("META-INF/*.properties")
            add("META-INF/AL2.0")
            add("META-INF/LGPL2.1")
            add("META-INF/DEPENDENCIES")
        }
    }
}

dependencies {
    val activity_version = "1.2.3"
    val lifecycle = "2.2.0"
    val room = "2.2.1"
    val fragment_version = "1.3.5"
    val okhttp3 = "4.9.0"
    val retrofit2 = "2.9.0"
    val glide = "4.12.0"

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.google.android.material:material:1.4.0")
    // viewModel
    implementation ("androidx.lifecycle:lifecycle-extensions:$lifecycle")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation ("androidx.activity:activity-ktx:$activity_version")
    implementation ("androidx.fragment:fragment-ktx:$fragment_version")
    //room
    implementation ("androidx.room:room-runtime:$room")
    annotationProcessor ("androidx.room:room-compiler:$room")
    testImplementation ("androidx.room:room-testing:$room")
    kapt ("androidx.room:room-compiler:$room")
    implementation ("androidx.room:room-ktx:$room")
    //CircleImageView
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    // Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    // 如有使用BoM，皆不需要指定版本
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    // Timber
    implementation ("com.jakewharton.timber:timber:5.0.1")
    //rxjava2
    implementation ("io.reactivex.rxjava2:rxjava:2.2.10")
    implementation ("io.reactivex.rxjava2:rxandroid:2.1.1")
    //barcode
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
    //okhttp3
    implementation ("com.squareup.okhttp3:okhttp:$okhttp3")
    implementation ("com.squareup.okhttp3:logging-interceptor:$okhttp3")
    //retrofit2
    implementation ("com.squareup.retrofit2:retrofit:$retrofit2")
    implementation ("com.squareup.retrofit2:converter-gson:$retrofit2")
    implementation ("com.squareup.retrofit2:adapter-rxjava2:2.4.0")
    //Glide
    implementation ("com.github.bumptech.glide:glide:$glide")
    annotationProcessor ("com.github.bumptech.glide:compiler:$glide")

    implementation ("com.google.code.gson:gson:2.9.0")
    //Google雲端硬碟
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.google.http-client:google-http-client-gson:1.42.1")
    implementation ("com.google.api-client:google-api-client-android:1.26.0")
    implementation ("com.google.android.gms:play-services-drive:17.0.0")

    implementation ("com.google.apis:google-api-services-drive:v3-rev20220815-2.0.0")

    implementation ("com.opencsv:opencsv:5.6")
}

