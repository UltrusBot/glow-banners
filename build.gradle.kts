import me.ultrusmods.glowbanners.gradle.Properties

plugins {
    // Required for NeoGradle
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.7"
    alias(libs.plugins.fabric.loom) apply false
    alias(libs.plugins.neoforge.moddev) apply false
    alias(libs.plugins.publishing)
}



publishMods {
    changelog = rootProject.file("CHANGELOG.md").readText()
    version = "${Properties.MOD}+${libs.minecraft.get().version}"
    type = STABLE

    github {
        accessToken = providers.gradleProperty("GH_TOKEN")
        repository = Properties.GITHUB_REPO
        tagName = "${Properties.MOD}+${libs.minecraft.get().version}"
        commitish = Properties.GITHUB_COMMITISH

        allowEmptyFiles = true
    }
}