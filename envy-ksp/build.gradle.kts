plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
    signing
}

dependencies {
    implementation(project(":envy"))

    compileOnly("com.google.devtools.ksp:symbol-processing-api:2.2.0-2.0.2")
    implementation("org.slf4j:slf4j-api:2.0.18")

}