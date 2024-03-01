package com.faddy.wgtunlib.util

sealed class Event {

    abstract val message: String

    sealed class Error : Event() {

        data object None : Error() {
            override val message: String
                get() = "No error"
        }

        data object SsidConflict : Error() {
            override val message: String
                get() = "SSID already exists"
        }

        data object RootDenied : Error() {
            override val message: String
                get() = "Root shell denied"
        }

        data class General(val customMessage: String) : Error() {
            override val message: String
                get() = customMessage
        }

        data class Exception(val exception: kotlin.Exception) : Error() {
            override val message: String
                get() = exception.message ?: "Unknown error occurred"
        }

        data object InvalidQrCode : Error() {
            override val message: String
                get() = "Invalid QR code"
        }

        data object InvalidFileExtension : Error() {
            override val message: String
                get() = "File is not a .conf or .zip"
        }

        data object FileReadFailed : Error() {
            override val message: String
                get() = "File is not a .conf or .zip"
        }

        data object AuthenticationFailed : Error() {
            override val message: String
                get() = "Authentication failed"
        }

        data object AuthorizationFailed : Error() {
            override val message: String
                get() = "Failed to authorize"
        }

        data object BackgroundLocationRequired : Error() {
            override val message: String
                get() = "Background location required"
        }

        data object LocationServicesRequired : Error() {
            override val message: String
                get() = "Location services required"
        }

        data object PreciseLocationRequired : Error() {
            override val message: String
                get() = "Precise location required"
        }

        data object FileExplorerRequired : Error() {
            override val message: String
                get() = "No file explorer installed"
        }
    }

    sealed class Message : Event() {
        data object ConfigSaved : Message() {
            override val message: String
                get() = "Configuration changes saved."
        }

        data object ConfigsExported : Message() {
            override val message: String
                get() = "Exported configs to downloads"
        }

        data object TunnelOffAction : Message() {
            override val message: String
                get() = "Action requires tunnel off"
        }

        data object TunnelOnAction : Message() {
            override val message: String
                get() = "Action requires active tunnel"
        }

        data object AutoTunnelOffAction : Message() {
            override val message: String
                get() = "Action requires auto-tunnel disabled"
        }
    }
}
