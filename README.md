<p align="center"><img src="src/main/resources/icon.png" alt="GregTech Post Modern logo" width="128"></p>
<h1 align="center">GregTech Post Modern</h1>
<p align="center">GregTech Post Modern for Minecraft 1.21.1 on NeoForge.</p>
<h1 align="center">
    <a href="https://github.com/ModularMCLib/GregTech-Post-Modern/releases"><img src="https://img.shields.io/badge/NeoForge-MC%201.21.1-informational?style=for-the-badge" alt="Supported Version"></a>
    <a href="https://github.com/ModularMCLib/GregTech-Post-Modern/blob/1.21/LICENSE"><img src="https://img.shields.io/github/license/ModularMCLib/GregTech-Post-Modern?style=for-the-badge" alt="License"></a>
    <a href="https://discord.gg/HwnF3RTPtH"><img src="https://img.shields.io/badge/Discord-ModularMC-5865F2?logo=discord&logoColor=white&style=for-the-badge" alt="ModularMC Discord"></a>
    <br>
    <a href="https://github.com/ModularMCLib/GregTech-Post-Modern/releases"><img src="https://img.shields.io/github/downloads/ModularMCLib/GregTech-Post-Modern/total?sort=semver&logo=github&label=&style=for-the-badge&color=2d2d2d&labelColor=545454&logoColor=FFFFFF" alt="GitHub"></a>
</h1>

### [Wiki](https://modularmclib.github.io/GregTech-Post-Modern/)

## Developers

GregTech Post Modern builds for Minecraft 1.21.1 are available as NeoForge JARs. [GitHub Releases](https://github.com/ModularMCLib/GregTech-Post-Modern/releases)

Use the ModularMCLib Maven repository for published builds and set `gtpm_version` to the version you want to use. For a checkout you publish locally, use `mavenLocal()` with the same coordinates.

```groovy
repositories {
    maven { url = uri("https://maven.fury.io/modularmclib/") }
}

dependencies {
    implementation "com.modularmclib.gtpm:gtpm-1.21.1:${gtpm_version}"
}
```

### IDE Requirements (when using IntelliJ IDEA)

For contributing to this mod, the [Lombok plugin](https://plugins.jetbrains.com/plugin/6317-lombok) for IntelliJ IDEA is strictly required.  
Additionally, the [Minecraft Development plugin](https://plugins.jetbrains.com/plugin/8327-minecraft-development) is recommended.


## Credited Works
- Most textures are originally from [Gregtech: Refreshed](https://modrinth.com/resourcepack/gregtech-refreshed) by @ULSTICK. With some consistency edits and additions by @Ghostipedia.
- Some textures are originally from the **[ZedTech GTCEu Resourcepack](https://github.com/brachy84/zedtech-ceu)**, with some changes made by the community.
- New material item textures by @TTFTCUTS and @Rosethorns.
- Wooden Forms, World Accelerators, and the Extreme Combustion Engine are from the **[GregTech: New Horizons Modpack](https://www.curseforge.com/minecraft/modpacks/gt-new-horizons)**.
- Primitive Water Pump is from the **[IMPACT: GREGTECH EDITION Modpack](https://gt-impact.github.io/#/)**.
- Ender Fluid Link Cover, Auto-Maintenance Hatch, Optical Fiber, and Data Bank Textures are from **[TecTech](https://github.com/Technus/TecTech)**.
- Steam Grinder is from **[GregTech++](https://www.curseforge.com/minecraft/mc-mods/gregtech-gt-gtplusplus)**.
- Certificate of Not Being a Noob Anymore is from **[Crops++](https://www.curseforge.com/minecraft/mc-mods/berries)**.

See something we forgot to credit? Let us know who to credit. [Discord](https://discord.gg/HwnF3RTPtH) · [Open an issue](https://github.com/ModularMCLib/GregTech-Post-Modern/issues)
