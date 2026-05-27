plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("boosters.minecraft-module-conventions")
}

architectury {
    common("neoforge", "fabric")
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())

    libs.bundles.commonModImplementation.get().forEach { dependency ->
        modImplementation(dependency.copy()) { isTransitive = false }
    }
    libs.bundles.commonImplementation.get().forEach { dependency ->
        implementation(dependency)
    }
}

tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        inputs.property("mod_name", project.property("mod_name").toString())
        filesMatching("pack.mcmeta") {
            expand(project.properties)
        }
    }
}
