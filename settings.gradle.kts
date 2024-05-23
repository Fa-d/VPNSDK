pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        flatDir {
            dirs("libs")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        flatDir {
            dirs("libs")
        }
    }
}

rootProject.name = "VPNSDK"
include(":app")
include(":PhoenixLib")
include(":openvpnlib")
include(":wgtunlib")
include(":singbox")

//include(":openconnectlib")
//include(":strongswan")
