import java.util.Properties

apply(plugin = "maven-publish")

val artifactVersion = project.property("VERSION_NAME") as String
val artifactGroupId = project.property("GROUP_ID") as String

val repoUrl = uri("https://maven.pkg.github.com/rahul-lohra/API-WebSocket-Viewer")
val shouldSign =
    (System.getenv("CI") == "true") || (project.findProperty("enableSigning") == "true")

val localProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
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
                    name.set("API-WebSocket-Viewer")
                    description.set("A library to view and analyze API & WebSocket calls.")
                    url.set("https://github.com/rahul-lohra/API-WebSocket-Viewer")
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
                        connection.set("scm:git:git://github.com/rahul-lohra/API-WebSocket-Viewer.git")
                        developerConnection.set("scm:git:ssh://github.com:rahul-lohra/API-WebSocket-Viewer.git")
                        url.set("https://github.com/rahul-lohra/API-WebSocket-Viewer")
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
                url = repoUrl
                credentials {
                    username = localProperties.getProperty("gpr.user") as String?
                        ?: System.getenv("GPR_USER")
                    password = localProperties.getProperty("gpr.token") as String? ?: System.getenv(
                        "GPR_TOKEN"
                    )
                }
            }
        }
    }
}
