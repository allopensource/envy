// build.gradle.kts
plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktlint)
    jacoco
}

group = "io.github.allopensource"
version = "0.1.0"

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

            jvmToolchain(11)

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
