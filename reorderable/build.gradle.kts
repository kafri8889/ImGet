plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.anafthdev.reorderable"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
}

dependencies {

    val kotlin_version by extra("1.9.10")
    val compose_version by extra("1.5.3")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.compose.runtime:runtime:1.5.3")
    implementation("androidx.compose.runtime:runtime-livedata:${extra["compose_version"]}")
    implementation("androidx.navigation:navigation-compose:2.7.4")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0-beta01")
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Compose Common
    implementation("androidx.compose.ui:ui:${extra["compose_version"]}")
    implementation("androidx.compose.ui:ui-tooling-preview:${extra["compose_version"]}")
    implementation("androidx.compose.foundation:foundation:${extra["compose_version"]}")
    implementation("androidx.compose.ui:ui-util:${extra["compose_version"]}")
    implementation("androidx.compose.animation:animation:${extra["compose_version"]}")

    // Compose Android
    implementation("androidx.compose.ui:ui-android:${extra["compose_version"]}")
    implementation("androidx.compose.ui:ui-tooling-preview-android:${extra["compose_version"]}")
    implementation("androidx.compose.foundation:foundation-android:${extra["compose_version"]}")
    implementation("androidx.compose.ui:ui-util-android:${extra["compose_version"]}")
    implementation("androidx.compose.animation:animation-android:${extra["compose_version"]}")

    // Material Design
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.compose.material:material:1.5.3")
    implementation("androidx.compose.material:material-icons-extended:1.5.3")
    implementation("androidx.compose.material3:material3-android:1.2.0-alpha09")
    implementation("androidx.compose.material3:material3-window-size-class:1.1.2")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}