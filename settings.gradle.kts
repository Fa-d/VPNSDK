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
include(":TunnelKitLib")

include(":protocols:openvpnlib")
include(":protocols:wgtunlib")
include(":protocols:singbox")
include(":protocols:strongswan")
project(":protocols:openvpnlib").projectDir = file("protocols/openvpnlib")
project(":protocols:wgtunlib").projectDir = file("protocols/wgtunlib")
project(":protocols:singbox").projectDir = file("protocols/singbox")
project(":protocols:strongswan").projectDir = file("protocols/strongswan")

//include(":protocols:openconnect") // not yet imported — see plan Phase 1 follow-ups
