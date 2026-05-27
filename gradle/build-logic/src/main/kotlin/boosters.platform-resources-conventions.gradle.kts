import org.gradle.kotlin.dsl.named
import org.gradle.language.jvm.tasks.ProcessResources

plugins {
    id("boosters.minecraft-module-conventions")
}

tasks.named<ProcessResources>("processResources") {
    inputs.property("mod_id", project.property("mod_id").toString())
    inputs.property("version", project.version)
}

