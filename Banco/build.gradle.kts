// build.gradle.kts (Project-level)
plugins {
    // Aplicar o plugin de gerenciamento de versão do Android (não é necessário para Kotlin DSL)
    id("com.android.application") version "8.2.0" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.0") // Certifique-se de usar a versão compatível
    }
}
