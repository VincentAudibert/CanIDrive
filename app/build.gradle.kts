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
        versionCode = 11
        versionName = "0.1.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility.isJava8
        targetCompatibility.isJava8
    }
    (kotlinOptions as org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions).apply {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Kotlin
    implementation( kotlin("stdlib-jdk8", version = "1.3.61"))
    implementation( "androidx.core:core-ktx:1.2.0")

    // Android
    implementation( "androidx.appcompat:appcompat:1.1.0")
    implementation( "androidx.constraintlayout:constraintlayout:1.1.3")
    implementation( "com.google.android.material:material:1.1.0")
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
    val navigationVersion = "2.2.0"
    implementation( "androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation( "androidx.navigation:navigation-ui-ktx:$navigationVersion")



    // ViewModel and LiveData
    val lifecycleVersion = "2.2.0"
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")

    kapt("androidx.lifecycle:lifecycle-compiler:$lifecycleVersion")

    // optional - ReactiveStreams support for LiveData
    // For Kotlin use lifecycle-reactivestreams-ktx
    implementation("androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycleVersion")

    // optional - Test helpers for LiveData
    val coreTestingVersion = "2.1.0"
    testImplementation("androidx.arch.core:core-testing:$coreTestingVersion")


    // Room
    val roomVersion = "2.2.3"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion") // For Kotlin use kapt instead of annotationProcessor

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$roomVersion")

}

tasks.withType<Test> {
    // Use the native JUnit support of Gradle.
    useJUnitPlatform()
}