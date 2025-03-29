import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    kotlin("android")
    id("jacoco")
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

        // Critical for instrumentation tests:
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        // Map/Secrets placeholders
        manifestPlaceholders["MAPS_API_KEY"] = properties["MAPS_API_KEY"] ?: ""
        manifestPlaceholders["WEB_CLIENT_ID"] = properties["WEB_CLIENT_ID"] ?: ""

        buildConfigField(
            "String",
            "MAPS_API_KEY",
            "\"${project.findProperty("MAPS_API_KEY") ?: ""}\""
        )
        buildConfigField(
            "String",
            "WEB_CLIENT_ID",
            "\"${project.findProperty("WEB_CLIENT_ID") ?: ""}\""
        )
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        buildConfig = true
    }

    // Enable Android resource support in local (Robolectric) tests
    testOptions {
        unitTests {
            // So Robolectric can inflate layouts/resources
            isIncludeAndroidResources = true
            // Optionally return default values for missing resources
            isReturnDefaultValues = true
        }
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    packagingOptions {
        resources.excludes.addAll(
            listOf(
                "META-INF/DEPENDENCIES",
                "META-INF/NOTICE",
                "META-INF/LICENSE"
            )
        )

    }

    dependencies {
        // --- Main (production) Dependencies ---
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.constraintlayout:constraintlayout:2.2.0")
        implementation("com.google.android.gms:play-services-maps:19.1.0")
        implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
        implementation("com.google.firebase:firebase-firestore")
        implementation("com.google.android.gms:play-services-location:21.3.0")
        implementation("com.google.maps.android:android-maps-utils:3.10.0")
        implementation("com.mappedin.sdk:mappedin:5.7.1")
        implementation("androidx.legacy:legacy-support-v4:1.0.0")
        implementation("androidx.recyclerview:recyclerview:1.3.0")
        implementation("androidx.core:core-ktx:1.15.0")

        // Google APIs / Calendar
        implementation("com.google.android.gms:play-services-auth:21.3.0")
        implementation("com.google.apis:google-api-services-calendar:v3-rev411-1.25.0")
        implementation("com.google.api-client:google-api-client-android:1.33.0") {
            exclude(group = "org.apache.httpcomponents")
        }
        implementation("com.google.http-client:google-http-client-gson:1.42.3") {
            exclude(module = "httpclient")
        }

        // --- Local (unit) testing (Robolectric, JUnit, Mockito, etc.) ---
        testImplementation("junit:junit:4.13.2")
        testImplementation("org.robolectric:robolectric:4.10")
        testImplementation("androidx.test:core:1.5.0")
        testImplementation("org.mockito:mockito-core:5.16.0")
        testImplementation("com.squareup.okhttp3:okhttp:4.9.3")
        testImplementation("com.google.code.gson:gson:2.10.1")
        testImplementation("org.json:json:20250107")

        // --- Instrumentation (UI) testing (Espresso, etc.) ---
        androidTestImplementation("androidx.test:runner:1.5.2")
        androidTestImplementation("androidx.test:rules:1.5.0")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
        androidTestImplementation("androidx.test.espresso:espresso-contrib:3.6.1")
        androidTestImplementation("androidx.test.espresso:espresso-intents:3.6.1")
        androidTestImplementation("androidx.fragment:fragment-testing:1.5.7")
    }

// Jacoco coverage task (for local tests)
    tasks.register<JacocoReport>("jacocoTestReport") {
        dependsOn("testDebugUnitTest")

        group = "Reporting"
        description = "Generate Jacoco coverage reports for local (Robolectric) tests."

        reports {
            xml.required.set(true)
            html.required.set(true)
        }

        val fileFilter = listOf(
            "**/R.class",
            "**/R\$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/*\$ViewInjector*.*",
            "**/*\$ViewBinder*.*",
            "**/BR.*",
            "**/androidx/**",
            "**/androidTest/**",
            "**/test/**"
        )

        val debugTree = fileTree("${buildDir}/intermediates/javac/debug/classes") {
            exclude(fileFilter)
        }

        sourceDirectories.setFrom(files("src/main/java"))
        classDirectories.setFrom(debugTree)

        executionData.setFrom(fileTree(project.buildDir) {
            include(
                "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
                "jacoco/testDebugUnitTest.exec"
            )
        })
    }

}