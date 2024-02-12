plugins {
    id("java")
    id ("maven-publish")
}
group = "de.twyco"
version = "1.0.1-BETA"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.json:json:20231013")
    implementation("org.jetbrains:annotations:24.0.0")

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