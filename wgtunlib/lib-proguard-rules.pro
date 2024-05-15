# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#-keepattributes SourceFile,LineNumberTable
#-ignorewarnings
#-keep class * {
#    public private *;
#}
#-keep class com.wireguard.android.backend.Tunnel.** { *; }
#-keep class com.faddy.wgtunlib.** { *; }
#-keep class com.wireguard.android.** { *; }
-keepattributes SourceFile,LineNumberTable, Exceptions, InnerClasses
-ignorewarnings
-keep class * {
    public private *;
}
-keep class com.wireguard.android.backend.*$* {
    *;
}
-keep class com.faddy.** { *; }
-keep class com.wireguard.** { *; }
-dontwarn com.google.**
-dontnote com.google.**
-keepnames class com.google.gson.** {*;}
-keepnames enum com.google.gson.** {*;}


