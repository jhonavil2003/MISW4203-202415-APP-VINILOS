plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.navigation.safe.args)
}

android {
    namespace = "com.example.app_vinilos_g17"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.app_vinilos_g17"
        minSdk = 26
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

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:${libs.versions.navigationVersion.get()}")
    implementation("androidx.navigation:navigation-ui-ktx:${libs.versions.navigationVersion.get()}")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:${libs.versions.navigationVersion.get()}")

    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:${libs.versions.navigationVersion.get()}")

    // Otros
    implementation(libs.volley)
    implementation(libs.gson)
    implementation(libs.converter.gson)
    implementation(libs.converter.scalars)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Jetpack Compose integration (si la usas)
    // implementation("androidx.navigation:navigation-compose:${libs.versions.navigationVersion.get()}")
}
