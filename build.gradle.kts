plugins {
    application
    id("com.google.devtools.ksp") version "1.9.21-1.0.15"
    kotlin("jvm") version "1.9.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Komapperの設定
    platform("org.komapper:komapper-platform:1.15.0").let {
        implementation(it)
        ksp(it)
    }
    implementation("org.komapper:komapper-starter-jdbc")
    implementation("org.komapper:komapper-dialect-postgresql-jdbc")
    ksp("org.komapper:komapper-processor")

    // Testcontainersの設定
    testImplementation(platform("org.testcontainers:testcontainers-bom:1.19.3"))
    testRuntimeOnly("org.testcontainers:postgresql")

    // JUnitの設定
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()

    // GradleのプロパティをJavaのプロパティに引き継ぐ
    val jdbcUrl = project.property("jdbc.url") ?: error("jdbc.url not found")
    systemProperty("jdbc.url", jdbcUrl)
}