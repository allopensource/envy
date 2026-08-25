plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
    signing
    alias(libs.plugins.vanniktech)
}

// Must not depend on envy
dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:2.3.11")
    implementation("org.slf4j:slf4j-api:2.0.18")
}


mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates("io.github.allopensource", "envy-ksp", "0.3.0")

    pom {
        name.set("envy-ksp")
        description.set("Processor to generate type-safe environment loaders for envy.")
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