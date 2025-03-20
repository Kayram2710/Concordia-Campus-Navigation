plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "minicap.concordia.campusnav"
    compileSdk = 35

    defaultConfig {
        applicationId = "minicap.concordia.campusnav"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testApplicationId = "minicap.concordia.campusnav.tests"

        buildConfigField("String", "MAPS_API_KEY", "\"${project.findProperty("MAPS_API_KEY")}\"")
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
    buildFeatures {

        dataBinding = true   // added from my branch
        viewBinding = true
        buildConfig = true
    }
    testOptions{
        unitTests.isReturnDefaultValues = true
        unitTests.isIncludeAndroidResources = true
    }
    compileOptions{
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("com.google.android.gms:play-services-maps:19.1.0")
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.maps.android:android-maps-utils:3.10.0")
    implementation("org.jsoup:jsoup:1.14.3")
    implementation("com.mappedin.sdk:mappedin:5.7.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.test.espresso:espresso-intents:3.6.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    testImplementation("org.robolectric:robolectric:4.10")
    testImplementation("com.squareup.okhttp3:okhttp:4.9.3")
    testImplementation("com.google.code.gson:gson:2.10.1")
    testImplementation("org.mockito:mockito-core:5.16.0")
    testImplementation("org.json:json:20250107")

    androidTestImplementation( "androidx.test.espresso:espresso-contrib:3.6.1")
    androidTestImplementation ("androidx.test:runner:1.5.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.fragment:fragment-testing:1.5.7")


}