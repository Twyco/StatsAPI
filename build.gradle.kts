plugins {
    id("java")
    id ("maven-publish")
}
group = "de.twyco"
version = "2.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.json:json:20231013")
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("org.mongodb:mongodb-driver-sync:4.11.1")
    implementation("org.yaml:snakeyaml:2.2")
}
publishing {
    repositories {
        maven {
            url = uri("https://repo.twyco.de/repository/StatsAPI/")

            credentials {
                println(System.getenv("NEXUS_USERNAME"))
                username = System.getenv("NEXUS_USERNAME")
                password = System.getenv("NEXUS_PASSWORD")
            }
        }
    }

    publications {
        create<MavenPublication>("mavenJava") {
            groupId = group.toString()
            artifactId = "StatsAPI"
            version = this.version
            from(components["java"])
        }
    }
}

tasks.test {
    useJUnitPlatform()
}