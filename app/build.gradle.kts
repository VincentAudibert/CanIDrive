plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
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

    // Kotlin
    implementation( kotlin("stdlib-jdk8", version = "1.3.61"))
    implementation( "androidx.core:core-ktx:1.1.0")

    // Android
    implementation( "androidx.appcompat:appcompat:1.1.0")
    implementation( "androidx.constraintlayout:constraintlayout:1.1.3")
    implementation( "com.google.android.material:material:1.0.0")
    implementation( "androidx.legacy:legacy-support-v4:1.0.0")

    // JUnit
    testImplementation( "org.junit.jupiter:junit-jupiter-api:5.5.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.1")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

    // Kotlin needs for testing
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))

    // Navigation (Kotlin)
    implementation( "androidx.navigation:navigation-fragment-ktx:2.1.0")
    implementation( "androidx.navigation:navigation-ui-ktx:2.1.0")


    val lifecycle_version = "2.1.0"

    // ViewModel and LiveData
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0-rc03")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")

    kapt("androidx.lifecycle:lifecycle-compiler:$lifecycle_version") // For Kotlin use kapt instead of annotationProcessor

    // optional - ReactiveStreams support for LiveData
    implementation("androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycle_version") // For Kotlin use lifecycle-reactivestreams-ktx

    // optional - Test helpers for LiveData
    testImplementation("androidx.arch.core:core-testing:$lifecycle_version")

}

tasks.withType<Test> {
    // Use the native JUnit support of Gradle.
    useJUnitPlatform()
}