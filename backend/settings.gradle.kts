rootProject.name = "calmvest-backend"

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}

include(":domain")
include(":application")
include(":infrastructure")
include(":api")
include(":app")
