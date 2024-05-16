pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://maven.aliyun.com/repository/gradle-plugin")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://maven.aliyun.com/repository/public")
    }
}

rootProject.name = "RedRockAI"
include(":app")

include(":lib_utils")
include(":lib_api")
include(":language")
include(":voice")
include(":identify")
include(":module_schoolroom")
