pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "Calmvest"

include(":app")
include(":core:domain")
include(":core:data")
include(":core:ui")
include(":feature:auth")
include(":feature:onboarding")
include(":feature:dashboard")
include(":feature:goals")
include(":feature:transactions")
include(":feature:portfolio")
include(":feature:settings")
