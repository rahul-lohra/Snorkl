import java.util.Properties

apply(plugin = "com.vanniktech.maven.publish")
// Read version and group from gradle.properties

val artifactVersion = project.property("VERSION_NAME") as String
val artifactGroupId = project.property("GROUP_ID") as String
val repoUrl = uri("https://maven.pkg.github.com/rahul-lohra/Snorkl")

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")

if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { input ->
        localProperties.load(input)
    }
}


mavenPublishing {
    signAllPublications.set(
        System.getenv("CI") == "true" || project.findProperty("enableSigning") == "true"
    )

    localProperties.getProperty("signing.key")?.let { key ->
        useInMemoryPgpKeys(
            keyId = localProperties.getProperty("signing.keyId"),
            key = key,
            password = localProperties.getProperty("signing.password")
        )
    }

    publishToMavenCentral(SonatypeHost.S01) {
        username.set(localProperties.getProperty("ossrhUsername") ?: System.getenv("OSSRH_USERNAME"))
        password.set(localProperties.getProperty("ossrhPassword") ?: System.getenv("OSSRH_PASSWORD"))
    }

    pom {
        name.set("API WebSocket Viewer")
        description.set("A developer tool for inspecting APIs and WebSocket connections.")
        url.set("https://github.com/rahul-lohra/Snorkl")
        licenses {
            license {
                name.set("Apache License 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("rahul-lohra")
                name.set("Rahul Lohra")
                email.set("tgunix@gmail.com")
            }
        }
        scm {
            connection.set("scm:git:git://github.com/rahul-lohra/Snorkl.git")
            developerConnection.set("scm:git:ssh://github.com:rahul-lohra/Snorkl.git")
            url.set("https://github.com/rahul-lohra/Snorkl")
        }
    }
}