plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()

    // This two for snapshot version we used
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

// Can't reconice version file so here we need to hardcode it
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.0")
    implementation("org.jetbrains.kotlin:kotlin-allopen:2.2.0")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:4.0.0-SNAPSHOT")
    implementation("io.spring.gradle:dependency-management-plugin:1.1.7")
}