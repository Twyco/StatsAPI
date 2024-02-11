plugins {
    id("java")
}

group = "de.twyco"
version = "1.0-BETA"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.json:json:20231013")

}

tasks.test {
    useJUnitPlatform()
}