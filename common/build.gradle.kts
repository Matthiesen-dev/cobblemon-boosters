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
    modImplementation(libs.bundles.commonModImplementation) { isTransitive = false }
    implementation(libs.bundles.commonImplementation)
    compileOnly(libs.bundles.commonCompileOnly)
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
