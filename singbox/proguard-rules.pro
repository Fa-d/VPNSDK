# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-ignorewarnings
-keep class * {
    public private *;
}
-keep class org.infradead.** { *; }
-keep class de.blinkt.openvpn.core.** { *; }
-keep class net.openvpn.ovpn3.** { *; }
-keep class com.google.** { *; }

-dontwarn com.google.**
-dontnote com.google.**
-keepnames class com.google.gson.** {*;}
-keepnames enum com.google.gson.** {*;}

