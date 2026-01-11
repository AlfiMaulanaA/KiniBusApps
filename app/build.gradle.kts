plugins {
    alias(libs.plugins.android.application)
    // Google Services plugin untuk Firebase
    id("com.google.gms.google-services")
}

android {
    namespace = "com.kinibus.apps"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.kinibus.apps"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Firebase dependencies
    // Firebase BoM (Bill of Materials) - menggunakan versi 32.7.0 untuk kompatibilitas
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))

    // Firebase Authentication - untuk login/register
    implementation("com.google.firebase:firebase-auth")

    // Cloud Firestore - untuk database
    implementation("com.google.firebase:firebase-firestore")

    // Firebase Analytics - optional untuk tracking
    implementation("com.google.firebase:firebase-analytics")

    // ZXing untuk QR Code
    implementation("com.google.zxing:core:3.4.1")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    // Dependensi standar lainnya
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}
