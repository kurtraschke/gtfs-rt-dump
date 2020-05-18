plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.72"
    kotlin("kapt") version "1.3.72"
    id("com.github.johnrengelman.shadow") version "5.2.0"
    application
}

group = "com.kurtraschke.gtfsrtdump"
version = "1.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.camsys-apps.com/releases/")
        mavenContent {
            releasesOnly()
        }
    }
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation(platform("com.fasterxml.jackson:jackson-bom:2.10.3"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("info.picocli:picocli:4.3.1")
    implementation("org.onebusaway:onebusaway-gtfs-realtime-api:1.2.12")
    implementation("com.jakewharton.picnic:picnic:0.3.1")
    implementation("com.google.guava:guava:29.0-jre")
    implementation("com.github.davidmoten:word-wrap:0.1.6")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv")
    implementation("com.hubspot.jackson:jackson-datatype-protobuf:0.9.10-jackson2.9-proto2") {
        exclude(group="com.google.protobuf", module="protobuf-java")
    }

    kapt("info.picocli:picocli-codegen:4.3.1")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}


kapt {
    arguments {
        arg("project", "${project.group}/${project.name}")
    }
}

application {
    mainClassName = "com.kurtraschke.gtfsrtdump.MainKt"
}