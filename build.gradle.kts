// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.com.google.devtools.ksp) apply false
//    alias(libs.plugins.kotlin.compose) apply false //Enable when kotlin version is 2.0+
//    alias(libs.plugins.android.library) apply false
}

subprojects {
    // Optional: apply only to modules with publishing
    afterEvaluate {
        if (project.plugins.hasPlugin("com.android.library") || project.plugins.hasPlugin("org.jetbrains.kotlin.jvm")) {
            apply(from = "$rootDir/gradle/publish-config.gradle.kts")
        }
    }
}

tasks.register("publishAllModules") {
    dependsOn(":core:publish", ":data:publish", /* add more here */)
}
