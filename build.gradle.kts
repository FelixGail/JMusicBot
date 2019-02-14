import com.github.spotbugs.SpotBugsTask
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.ben-manes.versions") version Plugin.VERSIONS

    kotlin("jvm") version Plugin.KOTLIN
    `java-library`

    id("org.jetbrains.dokka") version Plugin.DOKKA
    idea

    signing
    `maven-publish`
    id("com.github.spotbugs") version Plugin.SPOTBUGS_PLUGIN
}

group = "com.github.bjoernpetersen"
version = "0.17.0-SNAPSHOT"

repositories {
    jcenter()
}

idea {
    module {
        isDownloadJavadoc = true
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val processResources by tasks.getting(ProcessResources::class) {
    filesMatching("**/version.properties") {
        filter {
            it.replace("%APP_VERSION%", version.toString())
        }
    }
}

spotbugs {
    isIgnoreFailures = true
    toolVersion = Plugin.SPOTBUGS_TOOL
}

tasks {
    "dokka"(DokkaTask::class) {
        outputFormat = "html"
        outputDirectory = "$buildDir/kdoc"
    }

    @Suppress("UNUSED_VARIABLE")
    val dokkaJavadoc by creating(DokkaTask::class) {
        outputFormat = "javadoc"
        outputDirectory = "$buildDir/javadoc"
    }

    @Suppress("UNUSED_VARIABLE")
    val javadocJar by creating(Jar::class) {
        dependsOn("dokkaJavadoc")
        classifier = "javadoc"
        from("$buildDir/javadoc")
    }

    @Suppress("UNUSED_VARIABLE")
    val sourcesJar by creating(Jar::class) {
        classifier = "sources"
        from(sourceSets["main"].allSource)
    }

    "compileKotlin"(KotlinCompile::class) {
        kotlinOptions.jvmTarget = "1.8"
    }

    "compileTestKotlin"(KotlinCompile::class) {
        kotlinOptions.jvmTarget = "1.8"
    }

    "test"(Test::class) {
        useJUnitPlatform()
    }

    withType(SpotBugsTask::class) {
        reports {
            xml.isEnabled = false
            html.isEnabled = true
        }
    }

    withType(Jar::class) {
        from(project.projectDir) {
            include("LICENSE")
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(
        group = "io.github.microutils",
        name = "kotlin-logging",
        version = Lib.KOTLIN_LOGGING)

    api(group = "org.slf4j", name = "slf4j-api", version = Lib.SLF4J)
    api(group = "com.google.guava", name = "guava", version = Lib.GUAVA)
    api(group = "com.google.inject", name = "guice", version = Lib.GUICE, classifier = "no_aop")

    implementation(group = "org.xerial", name = "sqlite-jdbc", version = Lib.SQLITE)

    implementation(group = "org.mindrot", name = "jbcrypt", version = Lib.JBCRYPT)
    implementation(
        group = "com.auth0",
        name = "java-jwt",
        version = Lib.JJWT)

    api(group = "com.github.zafarkhaja",
        name = "java-semver",
        version = Lib.JAVA_SEMVER)

    testImplementation(group = "org.slf4j", name = "slf4j-simple", version = Lib.SLF4J)
    testRuntime(
        group = "org.junit.jupiter",
        name = "junit-jupiter-engine",
        version = Lib.JUNIT)
    testImplementation(
        group = "org.junit.jupiter",
        name = "junit-jupiter-api",
        version = Lib.JUNIT)
    testImplementation(
        group = "name.falgout.jeffrey.testing.junit5",
        name = "guice-extension",
        version = Lib.JUNIT_GUICE)
    testImplementation(group = "io.mockk", name = "mockk", version = Lib.MOCK_K)
    testImplementation(group = "org.assertj", name = "assertj-core", version = Lib.ASSERT_J)
}

publishing {
    publications {
        create("Maven", MavenPublication::class) {
            from(components["java"])
            artifact(tasks.getByName("javadocJar"))
            artifact(tasks.getByName("sourcesJar"))

            pom {
                name.set("MusicBot")
                description
                    .set("Core library of MusicBot, which plays music from various providers.")
                url.set("https://github.com/BjoernPetersen/MusicBot")

                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                scm {
                    connection.set("scm:git:https://github.com/BjoernPetersen/MusicBot.git")
                    developerConnection.set("scm:git:git@github.com:BjoernPetersen/MusicBot.git")
                    url.set("https://github.com/BjoernPetersen/MusicBot")
                }

                developers {
                    developer {
                        id.set("BjoernPetersen")
                        name.set("Björn Petersen")
                        email.set("pheasn@gmail.com")
                        url.set("https://github.com/BjoernPetersen")
                    }
                }
            }
        }
        repositories {
            maven {
                val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2"
                val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots"
                // change to point to your repo, e.g. http://my.org/repo
                url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl
                else releasesRepoUrl)
                credentials {
                    username = project.properties["ossrh.username"]?.toString()
                    password = project.properties["ossrh.password"]?.toString()
                }
            }
        }
    }
}

signing {
    sign(publishing.publications.getByName("Maven"))
}
