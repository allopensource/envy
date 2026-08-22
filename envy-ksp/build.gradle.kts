plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
    signing
}

dependencies {
    implementation(project(":envy"))

    implementation("com.google.devtools.ksp:symbol-processing-api:2.3.11")
    implementation("org.slf4j:slf4j-api:2.0.18")

}