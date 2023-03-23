plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("signing")
    id("org.jetbrains.dokka")
}

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 26
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-Xjvm-default=all" + "-Xopt-in=kotlin.RequiresOptIn"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = co.ycharts.dependency.Version.KOTLIN_COMPILER_EXT
    }
    packagingOptions {
        resources {
            exclude("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    namespace = "co.yml.charts.components"
}

dependencies {

    implementation(co.ycharts.dependency.YChartDependency.CORE_KTX)
    implementation(co.ycharts.dependency.YChartDependency.APPCOMPAT)
    implementation(co.ycharts.dependency.YChartDependency.MATERIAL)
    implementation(co.ycharts.dependency.YChartDependency.MATERIAL_3)
    implementation(co.ycharts.dependency.YChartDependency.COMPOSE_UI)
    implementation(co.ycharts.dependency.YChartDependency.COMPOSE_ACTIVITY)
    implementation(co.ycharts.dependency.YChartDependency.COMPOSE_MATERIAL)
    implementation(co.ycharts.dependency.YChartDependency.COMPOSE_TOOLING_PREVIEW)
    //implementation(co.ycharts.dependency.YChartDependency.RUNTIME_LIVEDATA)
    testImplementation(co.ycharts.dependency.YChartDependency.JUNIT)
    testImplementation(co.ycharts.dependency.YChartDependency.MOCKK)
    androidTestImplementation(co.ycharts.dependency.YChartDependency.COMPOSE_JUNIT)
    debugImplementation(co.ycharts.dependency.YChartDependency.COMPOSE_UI_TEST_MANIFEST)
    androidTestImplementation(co.ycharts.dependency.YChartDependency.TEST_EXTN)
    androidTestImplementation(co.ycharts.dependency.YChartDependency.ESPRESSO_CORE)

//    constraints {
//        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.10") {
//            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
//        }
//        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.20") {
//            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
//        }
//    }
}

configurations.implementation {
    exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
}

val dokkaOutputDir = "$buildDir/dokka"

tasks.dokkaHtml {
    outputDirectory.set(file(dokkaOutputDir))
}

val deleteDokkaOutputDir by tasks.register<Delete>("deleteDokkaOutputDirectory") {
    delete(dokkaOutputDir)
}
val javadocJar = tasks.register<Jar>("javadocJar") {
    dependsOn(deleteDokkaOutputDir, tasks.dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaOutputDir)
}
publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "co.yml"
            artifactId = "ycharts"
            version = "1.0.1"
            afterEvaluate {
                from(components["release"])
            }
            artifact(javadocJar)
            pom {
                name.set("YCharts")
                description.set("YCharts is a light and extensible chart library for Jetpack Compose system.")
                url.set("https://github.com/yml-org/YCharts")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("dkk009")
                        name.set("Deepak KK")
                        url.set("https://github.com/dkk009")
                    }
                    developer {
                        id.set("preetham1316")
                        name.set("Preetham Ivan Dsouza")
                        url.set("https://github.com/preetham1316")
                    }
                    developer {
                        id.set("kikoso")
                        name.set("Enrique López Mañas")
                        url.set("https://github.com/kikoso")
                    }
                }
                scm {
                    url.set("https://github.com/yml-org/YCharts")
                    connection.set("scm:git:git://github.com/yml-org/YCharts.git")
                    developerConnection.set("scm:git:ssh://git@github.com:yml-org/YCharts.git")
                }
            }
        }
    }
}
