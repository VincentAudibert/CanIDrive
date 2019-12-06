
plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("androidx.navigation.safeargs.kotlin")

}

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "com.vaudibert.canidrive"
        minSdkVersion(19)
        targetSdkVersion(28)
        versionCode = 3
        versionName = "0.0.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation( "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.61")
    implementation( "androidx.appcompat:appcompat:1.1.0")
    implementation( "androidx.core:core-ktx:1.1.0")
    implementation( "androidx.constraintlayout:constraintlayout:1.1.3")
    implementation( "com.google.android.material:material:1.0.0")
    implementation( "androidx.legacy:legacy-support-v4:1.0.0")

    // JUnit
    testImplementation( "org.junit.jupiter:junit-jupiter-api:5.5.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.1")

    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

    // Kotlin
    implementation( "androidx.navigation:navigation-fragment-ktx:2.1.0")
    implementation( "androidx.navigation:navigation-ui-ktx:2.1.0")
}
