// build.gradle.kts
plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.vanniktech)
    jacoco
}

tasks.register<JacocoReport>("jacocoRootReport") {

    dependsOn(":envy-tests:test")

    executionData.from(
        project(":envy-tests").layout.buildDirectory.file("jacoco/test.exec"),
    )

    sourceDirectories.from(
        project(":envy").file("src/main/kotlin"),
        // project(":envy-ksp").file("src/main/kotlin"),
    )

    classDirectories.from(
        project(":envy").layout.buildDirectory.dir("classes/kotlin/main"),
        // project(":envy-ksp").layout.buildDirectory.dir("classes/kotlin/main"),
    )

    reports {
        html.required.set(true)
        xml.required.set(true)
    }
}

allprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
    }
}

subprojects {

    plugins.withId("org.jetbrains.kotlin.jvm") {

        extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension> {

            jvmToolchain(17)

            // explicitApi()

            compilerOptions {
                freeCompilerArgs.add("-Xjsr305=strict")
            }
        }
    }

    plugins.withType<JavaLibraryPlugin> {

        extensions.configure<JavaPluginExtension> {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates("io.github.allopensource", "envy", "0.1.0-SNAPSHOT")

    pom {
        name.set("envy")
        description.set("A Kotlin library for environment-based configuration.")
        inceptionYear.set("2024")
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

repositories {
    mavenCentral()
}