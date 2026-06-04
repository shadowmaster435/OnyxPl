plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "OnyxPl"
include("KLLVM")
project(":KLLVM").projectDir = file("/home/shadowmaste435/IdeaProjects/KLLVM")
