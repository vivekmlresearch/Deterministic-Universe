plugins { id("com.android.application") }

android {
    namespace = "com.vivekmlresearch.deterministicuniverse"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.vivekmlresearch.deterministicuniverse"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    signingConfigs {
        create("release") {
            val store = System.getenv("DU_KEYSTORE_FILE")
            if (!store.isNullOrBlank()) {
                storeFile = file(store)
                storePassword = System.getenv("DU_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("DU_KEY_ALIAS")
                keyPassword = System.getenv("DU_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        debug { applicationIdSuffix = ".debug"; versionNameSuffix = "-test" }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            if (!System.getenv("DU_KEYSTORE_FILE").isNullOrBlank()) signingConfig = signingConfigs.getByName("release")
        }
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
}
