plugins {
    alias(libs.plugins.android.application)

}


android {
    namespace = "com.example.sellpicture"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.sellpicture"
        minSdk = 34
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation (libs.recyclerview.v130)
    implementation (libs.material.v190)  // Sử dụng version mới nhất
    implementation (libs.google.gson)
    implementation(libs.firebase.analytics)
    testImplementation(libs.junit)
    implementation (libs.glide)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.mysql.connector.java.v5149)
    implementation (libs.glide)
    annotationProcessor (libs.compiler)
}