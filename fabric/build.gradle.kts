plugins {
    id("com.gradleup.shadow")
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("boosters.shadow-platform-conventions")
}

architectury {
    platformSetupLoomIde()
    fabric()
}

val shadowCommon: Configuration by configurations.creating

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())

    add("modImplementation", libs.fabric.loader)
    add("modImplementation", fabricApi.module("fabric-command-api-v2", "0.116.6+1.21.1"))
    add("modImplementation", fabricApi.module("fabric-lifecycle-events-v1", "0.116.6+1.21.1"))
    add("modImplementation", fabricApi.module("fabric-networking-api-v1", "0.116.6+1.21.1"))
    add("modImplementation", libs.fabric.language.kotlin)
    libs.bundles.fabricModImplementation.get().forEach { dependency ->
        modImplementation(dependency.copy()) { isTransitive = false }
    }

    add("modRuntimeOnly", libs.fabric.api)
    add("modRuntimeOnly", "dev.architectury:architectury-fabric:13.0.8") { isTransitive = false }
    libs.bundles.fabricModRuntimeOnly.get().forEach { dependency ->
        modRuntimeOnly(dependency)
    }

    implementation(libs.discord.webhook.client)
    shadowCommon(libs.discord.webhook.client) {
        isTransitive = true
    }

    implementation(project(":common", configuration = "namedElements"))
    "developmentFabric"(project(":common", configuration = "namedElements"))
    shadowCommon(project(":common", configuration = "transformProductionFabric"))
}

tasks {
    processResources {
        filesMatching("fabric.mod.json") {
            expand(project.properties)
        }
    }

    shadowJar {
        configurations = listOf(shadowCommon)
    }
}
