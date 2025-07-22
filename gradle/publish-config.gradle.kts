import java.util.Properties

apply(plugin = "maven-publish")

val artifactVersion = project.property("VERSION_NAME") as String
val artifactGroupId = project.property("GROUP_ID") as String

val repoUrl = uri("https://maven.pkg.github.com/rahul-lohra/Snorkl")
val shouldSign =
    (System.getenv("CI") == "true") || (project.findProperty("enableSigning") == "true")

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")

if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { input ->
        localProperties.load(input)
    }
}


afterEvaluate {
    extensions.configure<PublishingExtension>("publishing") {
        publications {
            create<MavenPublication>("kotlin2.0") {
                from(components["release"])
                groupId = artifactGroupId
                artifactId = project.name
                version = artifactVersion

                pom {
                    name.set("Snorkl")
                    description.set("A library to view and analyze API & WebSocket calls.")
                    url.set("https://github.com/rahul-lohra/Snorkl")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
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
            create<MavenPublication>("kotlin1.9") {
                from(components["release"])
                groupId = artifactGroupId
                artifactId = project.name
                version = artifactVersion
            }
        }

        repositories {
            maven {
                name = "GitHubPackages"
                url = uri(repoUrl)

                val username = localProperties.getProperty("gpr.user")?.takeIf { it.isNotBlank() }
                    ?: System.getenv("GITHUB_ACTOR")?.takeIf { it.isNotBlank() }

                val password = localProperties.getProperty("gpr.token")?.takeIf { it.isNotBlank() }
                    ?: System.getenv("GITHUB_TOKEN")?.takeIf { it.isNotBlank() }

                if (!username.isNullOrBlank() && !password.isNullOrBlank()) {
                    credentials {
                        this.username = username
                        this.password = password
                    }
                }
            }
        }

    }
}
