plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
}


android {
    namespace = "com.tntt.itsgrey"
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles = "consumer-rules.pro"
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.1"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = '1.8'
    }
}

dependencies {

    implementation deps.ktx.core
    implementation deps.appcompat
    implementation deps.material

    // Material Design 3
    implementation deps.compose.material3.material3
    implementation deps.compose.material3.windowSize
    // Android Studio Preview support
    implementation deps.compose.ui.preview
    debugImplementation deps.compose.ui.tooling
    // such as input and measurement/layout
    implementation deps.compose.ui.ui
    // UI Tests
    androidTestImplementation deps.compose.ui.testJunit4
    debugImplementation deps.compose.ui.textManifest
    // the icons but not the material library (e.g. when using Material3 or a
    // custom design system based on Foundation)
    implementation deps.compose.material.iconsCore
    // Optional - Add full set of material icons
    implementation deps.compose.material.iconsExtended
    // Optional - Add window size utils
    implementation deps.compose.material3.windowSize
    // Optional - Integration with activities
    implementation "androidx.activity:activity-compose:1.6.1"
    // Optional - Integration with ViewModels
    implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1"

    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
}