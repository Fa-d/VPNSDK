# PhoenixVPN implementation
This is a details examle of the usage of the VPNSDK.

# Prerequisites
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
implementation("com.pheonixLib.phoenixlib:phoenixlib:0.0.1")
implementation("com.pheonixLib.openvpn:openvpn:0.0.1")
implementation("com.pheonixLib.wgtunlib:wgtunlib:0.0.1")
implementation("com.pheonixLib.singbox:singbox:0.0.2")
```

# Usage
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
}
```
[N.B](#-browsing) All observers must be used inside the `coreSdk.funInvoker` block


