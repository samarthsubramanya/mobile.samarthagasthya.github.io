import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Sync
import org.gradle.api.tasks.wrapper.Wrapper

plugins {
    kotlin("multiplatform") version "2.3.20"
    kotlin("plugin.compose") version "2.3.20"
}

group = "dev.samarthagasthya"
version = "1.0.0"

kotlin {
    js(IR) {
        outputModuleName.set("samarth-mobile-portfolio")
        browser {
            commonWebpackConfig {
                outputFileName = "portfolio-mobile.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        jsMain.dependencies {
            implementation("org.jetbrains.compose.html:html-core:1.11.1")
            implementation("org.jetbrains.compose.html:html-svg:1.11.1")
            implementation("org.jetbrains.compose.runtime:runtime:1.11.1")
        }
    }
}

tasks.named<Copy>("jsProcessResources") {
    exclude("**/.DS_Store")
    from("public")
}

tasks.register<Sync>("syncMobileDist") {
    dependsOn("jsBrowserDistribution")
    from(layout.buildDirectory.dir("dist/js/productionExecutable"))
    into(layout.projectDirectory.dir("dist"))
    exclude("**/.DS_Store")
    doFirst {
        delete(layout.projectDirectory.file("dist/.DS_Store"))
        delete(fileTree(layout.projectDirectory.dir("dist")) {
            include(".DS_Store")
            include("**/.DS_Store")
        })
    }
}

tasks.named("build") {
    finalizedBy("syncMobileDist")
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = "8.14.3"
    distributionType = Wrapper.DistributionType.BIN
}
