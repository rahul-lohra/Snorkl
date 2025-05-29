import java.util.Properties

apply(plugin = "maven-publish")

val artifactVersion = "0.0.1-kotlin1.9"
val artifactGroupId = "rahul.lohra.snorkl"
val repoUrl = uri("https://maven.pkg.github.com/rahul-lohra/API-WebSocket-Viewer")

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
                    username = localProperties.getProperty("gpr.user") as String? ?: System.getenv("GPR_USER")
                    password = localProperties.getProperty("gpr.token") as String? ?: System.getenv("GPR_TOKEN")
                }
            }
        }
    }
}
