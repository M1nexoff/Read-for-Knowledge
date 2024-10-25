plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")

    id("com.google.gms.google-services")

    //Crashlytics
    id("com.google.firebase.crashlytics")

}

android {
    namespace = "uz.gita.readforknowledge"
    compileSdk = 34

    defaultConfig {
        applicationId = "uz.gita.readforknowledge"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
    buildFeatures{
        viewBinding = true
    }

}

dependencies {
    //PROGRESSBAR
    implementation("com.github.skydoves:progressview:1.1.3")

    //VIEW_BINDING
    implementation("com.github.kirich1409:viewbindingpropertydelegate-noreflection:1.5.9")

    //ROOM
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    //NAVIGATION
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    //GLIDE
    implementation("com.github.bumptech.glide:glide:4.16.0")

    //FIREBASE
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    //Analytics
    implementation("com.google.firebase:firebase-analytics")
    //Cloud Firestore
    implementation("com.google.firebase:firebase-firestore")
    //Cloud Storage
    implementation("com.google.firebase:firebase-storage")
    //Crashlytics
    implementation("com.google.firebase:firebase-crashlytics")

    //PDFVIEW
    implementation("io.github.afreakyelf:Pdf-Viewer:2.1.1")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}