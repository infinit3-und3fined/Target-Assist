pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            // Plugins
            plugin("android-application", "com.android.application").version("8.2.0")
            plugin("kotlin-android", "org.jetbrains.kotlin.android").version("1.9.0")
            plugin("kotlin-compose", "org.jetbrains.kotlin.plugin.compose").version("1.5.3")

            // Core
            library("androidx-core-ktx", "androidx.core:core-ktx:1.12.0")
            library("androidx-lifecycle-runtime-ktx", "androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
            library("androidx-activity-compose", "androidx.activity:activity-compose:1.8.2")

            // Compose
            version("compose-bom", "2023.10.01")
            library("androidx-compose-bom", "androidx.compose:compose-bom").versionRef("compose-bom")
            library("androidx-ui", "androidx.compose.ui:ui")
            library("androidx-ui-graphics", "androidx.compose.ui:ui-graphics")
            library("androidx-ui-tooling", "androidx.compose.ui:ui-tooling")
            library("androidx-ui-tooling-preview", "androidx.compose.ui:ui-tooling-preview")
            library("androidx-ui-test-manifest", "androidx.compose.ui:ui-test-manifest")
            library("androidx-ui-test-junit4", "androidx.compose.ui:ui-test-junit4")
            library("androidx-material3", "androidx.compose.material3:material3")

            // Navigation
            library("androidx-navigation-compose", "androidx.navigation:navigation-compose:2.7.6")

            // Hilt
            version("hilt", "2.48")
            library("hilt-android", "com.google.dagger:hilt-android").versionRef("hilt")
            library("hilt-compiler", "com.google.dagger:hilt-android-compiler").versionRef("hilt")
            library("androidx-hilt-navigation-compose", "androidx.hilt:hilt-navigation-compose:1.1.0")

            // DataStore
            library("androidx-datastore-preferences", "androidx.datastore:datastore-preferences:1.0.0")

            // Coroutines
            library("kotlinx-coroutines-android", "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

            // Timber
            library("timber", "com.jakewharton.timber:timber:5.0.1")

            // CardView and ConstraintLayout
            library("androidx-cardview", "androidx.cardview:cardview:1.0.0")
            library("androidx-constraintlayout", "androidx.constraintlayout:constraintlayout:2.1.4")

            // Testing
            library("junit", "junit:junit:4.13.2")
            library("androidx-junit", "androidx.test.ext:junit:1.1.5")
            library("androidx-espresso-core", "androidx.test.espresso:espresso-core:3.5.1")
        }
    }
}

rootProject.name = "Target-Assist"
include(":app")
