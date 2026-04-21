plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
}

architectury {
    common("neoforge", "fabric")
}

loom {
    silentMojangMappingsLicense()
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings(loom.officialMojangMappings())

    implementation("com.n1netails:n1netails-discord-webhook-client:${property("discord_webhook_client_version")}")

    modApi("dev.architectury:architectury:${property("architectury_version")}") { isTransitive = false }
    modImplementation("com.cobblemon:mod:${property("cobblemon_version")}") { isTransitive = false }
    modImplementation("ca.landonjw.gooeylibs:api:${property("gooeylibs_version")}")

    compileOnly("net.kyori:adventure-platform-mod-shared:${property("adventure_text_version")}")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${property("junit_version")}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${property("junit_version")}")
}

tasks {
    test {
        useJUnitPlatform()
    }

    remapSourcesJar {
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveVersion.set("${project.version}")
        archiveClassifier.set("sources")
    }
}
