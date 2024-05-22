# PhoenixVPN implementation
There are diffrent protocols used for VPN connection. Diffrent protocols have differnt implementation. But all of them has some features in common. This project tries to ensamble the basic features of all protocols. This is still in the early phase of development. It describes as an examle of the usage of the VPNSDK.

# Featues  
- [x] Multi Protocols
- [x] VPN Status
- [x] Upload Download Speed delta
- [ ] Total upload download speed in a session.
- [x] Current IP
- [x] Ping
- [x] Connected Duration.
- [ ] Single package enable for VPN
- [x] Publish to private maven server
- [ ] Dynamic protocol enabling.

# VPN Types
As per latest implementation the sdk contains the following protocols. 
- [x] `OpenVPN`
- [x] `WireGuard`
- [x] `Singbox`
- [ ] `OpenConnect`
- [ ] `IPSec/IKEV2`
- [ ] `V2Ray`


# Prerequisites
You must have the private credentials thats been provided to fetch configs from server.
Go into `settings.gradle.kts` and add the following 

``` kotlin
dependencyResolutionManagement {
    repositories {

        maven {
            isAllowInsecureProtocol = true //if there is the protocol is secured, remove this line.
            name = "reposiliteRepository"
            url = uri("http://a.b.c.d/private")
            credentials {
                username = "provided username"
                password = "provided credentials"
            }
        }

    }
}
```


# Installation
For fetching the necessary dependencies add the following lines to your app level `build.gradle.kts`

``` Kotlin
dependencies {
    implementation("com.pheonixLib.phoenixlib:phoenixlib:0.0.1")
    implementation("com.pheonixLib.openvpn:openvpn:0.0.1")
    implementation("com.pheonixLib.wgtunlib:wgtunlib:0.0.1")
    implementation("com.pheonixLib.singbox:singbox:0.0.2")
}
```

## Further Installation
Using `Hilt` is must for this project. So you need to install it in your project.

* Add the following lines to your app level `build.gradle.kts`
```Kotlin
plugins {
    id("com.android.application") 
    id("org.jetbrains.kotlin.android")
    alias("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    alias("com.google.devtools.ksp")
}
```
* Add the following line in project level `build.gradle.kts`
```kotlin
plugins {
    alias("org.jetbrains.kotlin.android").version("1.9.22") apply false
    kotlin("plugin.serialization").version("1.9.22").apply(false)
    alias("com.google.devtools.ksp").version("1.9.22-1.0.16") apply false
    alias("org.jetbrains.kotlin.jvm").version("1.9.22") apply false
}
```

# Usage

### Injecting required dependencies

Annotate your application class with `@HiltAndroidApp`

``` kotlin
@HiltAndroidApp
class MainApp : Application() {}
```


Annotate your Activity/Fragment with `@AndroidEntryPoint` and inject the Core SDK like following.

``` kotlin
@AndroidEntryPoint
class MainActivityPro : AppCompatActivity() {

    @Inject
    lateinit var coreSdk: PhoenixVPN
}


@AndroidEntryPoint
class MainActivityPro : Fragment() {

    @Inject
    lateinit var coreSdk: PhoenixVPN
}
```



###  Attatch to Lifecycle  
```kotlin
override fun onPause() {
    super.onPause()
    coreSdk.onVPNPause()
}

override fun onResume() {
    super.onResume()
    coreSdk.onVPNResume()
}

override fun onDestroy() {
    coreSdk.onVPNDestroy()
    super.onDestroy()
}

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    coreSdk.onVpnCreate()
}
```

### Connecting Disconnecting

#### Create a VPN Profile 
* `vpnType` is the type of VPN you are trying to connect to. They may be as previously selected VPN types.
* `userName` must be used to log Into your app/OpenVPN server UserName. The Same  goes for the `password`
* `vpnConfig` is the server config you are trying to connect to. The Data should be plain text.
*  `serverIP` should be passed the IP of the server you are wanting to connect to.

```
val vpnProfile = VpnProfile(
    vpnType = VPNType.WIREGUARD, 
    userName = "userName",
    password = "password",
    vpnConfig = "VPNConfig",
    serverIP = "0.0.0.0"
)
```

Prepare VPN Service and connect.
```kotlin
binding.connectButton.setOnClickListener{
    startConnectDisconnectDecision()
}

fun startConnectDisconnectDecision(){
     if (coreSdk.isVpnServicePrepared()) {
        if (coreSdk.isVpnConnected()) {
            coreSdk.disconnect()
        } else {
            coreSdk.startConnect(context, vpnProfile)
        }
    } else {
        coreSdk.prepareVPNService(context, activityRes)
    }
}

```
Also declare this globally in your Fragment/Activity.
```kotlin
private val activityRes = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
    startConnectDisconnectDecision()
 }
```
### Observerving callbacks 
To fetch data from core, e.g. state, current IP, etc. Use as below.

```kotlin

coreSdk.funInvoker = {

    coreSdk.connectedStatus?.observe(this) { status ->
        when (status) {
            VPNStatus.DISCONNECTING -> {}
            VPNStatus.CONNECTED -> {}
            VPNStatus.CONNECTING -> {}
            VPNStatus.DISCONNECTED -> {}
        }
    }

    coreSdk.currentUploadSpeed?.observe(this) { uploadSpeed ->
                
    }

    coreSdk.currentDownloadSpeed?.observe(this) { downloadSpeed ->
    
    }

    coreSdk.connectedVpnTime.observe(this) { time ->

    }

    coreSdk.currentPing.observe(this) { ping ->
            
    }

    coreSdk.myCurrentIp?.observe(this) { ip ->

    }
}
```
[N.B](#-browsing) All observers must be used inside the `coreSdk.funInvoker` block


