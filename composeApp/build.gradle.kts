
import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.spotless)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.firebaseCrashlytics)
}

kotlin {
//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//        moduleName = "composeApp"
//        browser {
//            commonWebpackConfig {
//                outputFileName = "composeApp.js"
//                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
//                    static = (static ?: mutableListOf()).apply {
//                        // Serve sources to debug inside browser
//                        add(project.projectDir.path)
//                    }
//                }
//            }
//        }
//        binaries.executable()
//    }
    
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    
    jvm("desktop")
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            binaryOption("bundleId", "com.unwur.mabiaho.composeApp")
        }
    }
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.splashscreen)

            implementation(project.dependencies.platform("com.google.firebase:firebase-bom:32.7.3"))
            implementation("com.google.firebase:firebase-crashlytics")

            implementation(libs.lottie.compose)
        }
//        iosMain.dependencies {
//        }
        commonMain.dependencies {
            implementation(projects.database)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)

            implementation(libs.kotlinx.datetime)

            implementation(libs.haze)
            implementation(libs.haze.materials)

            implementation(libs.generativeai.google)
            implementation(libs.calf.filepicker)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            implementation("com.russhwolf:multiplatform-settings-no-arg:1.1.1")

            api(libs.precompose)
            api(libs.precompose.viewmodel)

            implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7")
            implementation("co.touchlab:stately-concurrent-collections:2.0.6")

        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

android {
    namespace = "com.unwur.mabiaho"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    val keyProperties =
        Properties().apply {
            val propsFile = rootProject.file("keystore.properties")
            if (propsFile.exists()) {
                load(propsFile.inputStream())
            }
        }

    signingConfigs {
        create("release") {
            keyAlias = keyProperties["keyAlias"].toString()
            keyPassword = keyProperties["keyPassword"].toString()
            storeFile = file(keyProperties["storeFile"].toString())
            storePassword = keyProperties["storePassword"].toString()
        }
    }

    defaultConfig {
        applicationId = "com.unwur.mabiaho"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 11
        versionName = "1.0.11"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = false
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    lint {
        quiet = true
        abortOnError = false
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.unwur.mabiaho"
            packageVersion = "1.0.0"
        }
    }
}

//compose.experimental {
//    web.application {}
//}

buildkonfig {
    packageName = "com.unwur.mabiaho"

    val localProperties =
        Properties().apply {
            val propsFile = rootProject.file("local.properties")
            if (propsFile.exists()) {
                load(propsFile.inputStream())
            }
        }

    defaultConfigs {
        buildConfigField(
            FieldSpec.Type.STRING,
            "GEMINI_API_KEY",
            localProperties["gemini_api_key"]?.toString() ?: "",
        )
    }
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    kotlin {
        target("**/*.kt")
        targetExclude("${layout.buildDirectory}/**/*.kt")
        targetExclude("bin/**/*.kt")
        ktlint().editorConfigOverride(
            mapOf(
                "ktlint_standard_filename" to "disabled",
                "ktlint_standard_function-naming" to "disabled",
            ),
        )
        licenseHeaderFile(rootProject.file("licenses/MIT"))
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint()
    }
}

task("testClasses").doLast {
    println("This is a dummy testClasses task")
}
