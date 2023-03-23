buildscript {

    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
//        classpath("com.android.tools.build:gradle:7.2.2")
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
//        classpath("org.jetbrains.kotlin:kotlin-android-extensions:1.8.10")
//        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.7.20")
        classpath("com.android.tools.build:gradle:7.2.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
        classpath("org.jetbrains.kotlin:kotlin-android-extensions:1.6.10")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.6.10")
    }

    tasks {
        register("clean", Delete::class) {
            delete(rootProject.buildDir)
        }
    }
}
