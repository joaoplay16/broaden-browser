plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "com.playlab.broadenbrowser"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.playlab.broadenbrowser"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.playlab.broadenbrowser.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        /**
         * room.schemaLocation: Configures and enables exporting database schemas
         * into JSON files in the given directory
         * room.incremental: Enables Gradle incremental annotation processor.
         */
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    android.buildFeatures.buildConfig = true

    productFlavors {
        setFlavorDimensions(listOf("store"))

        create("google") {
            dimension = "store"
            applicationIdSuffix = ".google"

            buildConfigField(
                "String",
                "STORE_URI",
                "\"market://details?id=$namespace\""
            )
            buildConfigField(
                "String",
                "STORE_URL",
                "\"https://play.google.com/store/apps/details?id=$namespace\""
            )
            buildConfigField(
                "String",
                "STORE_NAME",
                "\"Google Play\""
            )
        }

        create("samsung") {
            dimension = "store"
            applicationIdSuffix = ".samsung"

            buildConfigField(
                "String",
                "STORE_URI",
                "\"samsungapps://ProductDetail/$namespace\""
            )
            buildConfigField(
                "String",
                "STORE_URL",
                "\"https://galaxystore.samsung.com/detail/$namespace\""
            )
            buildConfigField(
                "String",
                "STORE_NAME",
                "\"Galaxy Store\""
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.lifecycle.viewModelCompose)

    implementation(libs.androidx.activity.compose)

    implementation(libs.google.android.material)

    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.runtime)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(libs.accompanist.systemuicontroller)

    implementation(libs.coil.kt.compose)

    implementation(libs.accompanist.webview)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.ext.compiler)

    implementation(libs.androidx.datastore.preferences)

    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.ext.junit)
    testImplementation(libs.androidx.test.ext.truth)
    testImplementation(libs.androidx.test.runner)
    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.ext.truth)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}