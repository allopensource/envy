plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
    jacoco
}

dependencies {
    implementation(project(":envy"))
    ksp(project(":envy-ksp"))

    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.14.11")

    tasks.test {

        useJUnitPlatform()
        testLogging {
            showStandardStreams = true
        }

        reports {
            html.required.set(true)
            junitXml.required.set(true)
        }

        systemProperty("buildDir", layout.buildDirectory.get().asFile.absolutePath)

        environment("string", "I am a string.")
        environment("int", 1)
        environment("long", 1L)
        environment("double", 1.0)
        environment("float", 1.0F)
        environment("byte", 127)
        environment("char", 'a')
        environment("short", 1)
        environment("boolean", true)
        environment("nullString", "I am not a null string.")
        environment("incorrectInt", "I should not be a string.")
    }

}