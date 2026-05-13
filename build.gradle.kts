// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.androidx.navigation.safeargs) apply false
}

// THÊM BLOCK NÀY NẾU CẦN
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // Không cần thêm gì nếu đã dùng plugin management
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}