plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
    signing
    alias(libs.plugins.vanniktech)
}



mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates("io.github.allopensource", "envy", "0.1.0")

    pom {
        name.set("envy-ksp")
        description.set("Typesafe environment configuration for Kotlin.")
        inceptionYear.set("2026")
        url.set("https://github.com/allopensource/envy")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("sumitponia")
                name.set("Sumit Ponia")
                url.set("https://github.com/sumitponia")
            }
        }
        scm {
            url.set("https://github.com/allopensource/envy")
            connection.set("scm:git:https://github.com/allopensource/envy.git")
            developerConnection.set("scm:git:ssh://git@github.com/allopensource/envy.git")
        }
    }
}