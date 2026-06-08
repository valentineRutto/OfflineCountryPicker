plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    `maven-publish`
}

group = "com.valentinerutto"
version = "1.0.0"

android {
    namespace = "com.valentinerutto.offlinecountrypicker"
    compileSdk {
        version = release(36)
    }
    defaultConfig {

        aarMetadata {
            minCompileSdk = 29
        }

        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material.icons.extended)

    implementation(libs.gson)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.valentinerutto"
            artifactId = "offline-country-picker"
            version = project.version.toString()

            afterEvaluate {
                from(components["release"])
            }

            pom {
                name.set("Offline Country Picker")
                description.set("A Jetpack Compose country and phone code picker that works fully offline.")
                url.set("https://github.com/valentinerutto/OfflineCountryPicker")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("valentinerutto")
                        name.set("Valentine Rutto")
                    }
                }

                scm {
                    connection.set("scm:git:https://github.com/valentinerutto/OfflineCountryPicker.git")
                    developerConnection.set("scm:git:ssh://git@github.com:valentinerutto/OfflineCountryPicker.git")
                    url.set("https://github.com/valentinerutto/OfflineCountryPicker")
                }
            }
        }
    }

    repositories {
        maven {
            name = "localBuild"
            url = uri(layout.buildDirectory.dir("repo"))
        }
    }
}
