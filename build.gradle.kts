import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.8"

    kotlin("jvm") version "1.7.20"
}

group = "ru.ezhov"
version = "1.0-SNAPSHOT"

javafx {
    version = "11.0.2"
    modules = listOf("javafx.controls", "javafx.graphics")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://packages.atlassian.com/maven/repository/public")
    }
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.kotlinx.ast:grammar-kotlin-parser-antlr-kotlin:0.1.0")

    implementation("no.tornado:tornadofx:1.7.20")

    implementation("org.commonmark:commonmark:0.20.0")
    implementation("org.commonmark:commonmark-ext-gfm-tables:0.20.0")

    implementation("io.github.microutils:kotlin-logging-jvm:2.0.10")

    implementation("com.atlassian.jira:jira-rest-java-client-core:5.2.4")
    implementation("io.atlassian.fugue:fugue:5.0.0")


    implementation("io.arrow-kt:arrow-core:0.13.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")

    implementation("org.yaml:snakeyaml:1.21")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.3")

    implementation("net.sourceforge.plantuml:plantuml:1.2022.8")

    implementation("com.github.spullara.mustache.java:compiler:0.9.10")

    implementation("org.apache.lucene:lucene-core:7.1.0")
    implementation("org.apache.lucene:lucene-queryparser:7.1.0")
    implementation("org.apache.lucene:lucene-analyzers-common:7.1.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}
