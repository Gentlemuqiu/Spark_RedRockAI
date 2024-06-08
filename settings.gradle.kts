pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://maven.aliyun.com/repository/gradle-plugin")
        maven(url = "https://jitpack.io") // 添加 JitPack 仓库

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://jitpack.io") // 添加 JitPack 仓库

    }
}

rootProject.name = "RedRockAI"
include(":app")

include(":lib_utils")
include(":lib_api")
include(":language")
include(":identify")
include(":module_schoolroom")
include(":lib_net")
include(":module_playVideo")
include(":module_video")
include(":module_life")
include(":module_mine")
include(":module_message")
