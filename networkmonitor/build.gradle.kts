import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("plugin.serialization") version "1.9.10"
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.com.google.devtools.ksp)
}
// Add this at the top of the file
val localProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

android {
    namespace = "rahul.lohra.networkmonitor"
    compileSdk = 35

    defaultConfig {
//        applicationId = "rahul.lohra.networkmonitor"
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
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
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
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.material3)

    var paging_version = "3.1.1"
    implementation ("androidx.paging:paging-runtime:$paging_version")
    implementation("androidx.paging:paging-compose:3.3.6")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.navigation:navigation-compose:2.8.6")

    implementation(project(":data"))
    debugImplementation(project(":network"))
    releaseImplementation(project(":network-noop"))
    implementation(libs.room.runtime)
}


afterEvaluate {
    val artifactVersion = "0.0.15-kotlin1.9"

    publishing {
        publications {
            create<MavenPublication>("kotlin2.0") {
                from(components["release"])
                groupId = "rahul.lohra.networkmonitor"
                artifactId = "networkmonitor"
                version = artifactVersion
            }
            create<MavenPublication>("kotlin1.9") {
                from(components["release"])
                groupId = "rahul.lohra.networkmonitor"
                artifactId = "networkmonitor"
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