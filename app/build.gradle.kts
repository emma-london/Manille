plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.abruce.manille"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.abruce.manille"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // The original project did not actually use any android.support.* APIs,
    // so no AndroidX dependencies are required. Uncomment if you later
    // need AppCompat features.
    // implementation(libs.androidx.appcompat)
}
