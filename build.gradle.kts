buildscript {
    dependencies {
        val nav_version = "2.7.7"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    //FireBase
    id("com.google.gms.google-services") version "4.4.2" apply false

    //FireBase Crashlytics
    id("com.google.firebase.crashlytics") version "3.0.2" apply false

}