import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("plugin.serialization") version "1.9.10"
    alias(libs.plugins.maven.publish)
}
// Add this at the top of the file
val localProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

android {
    namespace = "rahul.lohra.networkinspector"
    compileSdk = 35

    defaultConfig {
//        applicationId = "rahul.lohra.networkinspector"
        minSdk = 24
//        targetSdk = 35
//        versionCode = 1
//        versionName = "1.0"

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "11"
    }

    configurations.all {
        resolutionStrategy.force(
            "org.jetbrains.kotlin:kotlin-stdlib-common:1.9.25", // Replace with your desired 1.9.x version
            "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.25",   // If you are using JDK 8 stdlib
            "org.jetbrains.kotlin:kotlin-stdlib:1.9.25"      // If you are using the platform stdlib
        )
    }
}

dependencies {

    implementation(libs.okhttp)
    implementation(libs.gson)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.websockets)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.serialization.json)
}

afterEvaluate {
    val artifactVersion = "0.0.8Re-kotlin1.9"

    publishing {
        publications {
            create<MavenPublication>("kotlin2.0") {
                from(components["release"])
                groupId = "rahul.lohra.networkinspector"
                artifactId = "networkinspector"
                version = artifactVersion
            }
            create<MavenPublication>("kotlin1.9") {
                from(components["release"])
                groupId = "rahul.lohra.networkinspector"
                artifactId = "networkinspector"
                version = artifactVersion
            }

        }
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/rahul-lohra/API-WebSocket-Viewer")
                credentials {
                    username = localProperties.getProperty("gpr.user") as String? ?: System.getenv("GPR_USER")
                    password = localProperties.getProperty("gpr.token") as String? ?: System.getenv("GPR_TOKEN")
                }
            }
        }
    }
}