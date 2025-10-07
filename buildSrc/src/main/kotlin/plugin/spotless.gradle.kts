package plugin

import com.diffplug.spotless.extra.wtp.EclipseWtpFormatterStep

plugins { id("com.diffplug.spotless") }

spotless {
    // Java 파일 포맷팅
    java {
        target("src/**/*.java")
        targetExclude("build/**")

        // IntelliJ/Eclipse code style 사용
        eclipse().configFile(rootProject.file("code-style/intellij-style.xml"))

        // Import 순서 정리
        importOrder("java", "javax", "jakarta", "org", "com", "", "\\#")

        // 불필요한 import 제거
        removeUnusedImports()
    }

    // Kotlin 파일 포맷팅
    kotlin {
        target("src/**/*.kt")
        targetExclude("build/**")

        // ktfmt 포맷터 사용
        ktfmt().kotlinlangStyle()

        // 들여쓰기 및 공백 정리
        indentWithSpaces(4)
        trimTrailingWhitespace()
        endWithNewline()
    }

    // Gradle 파일 포맷팅
    kotlinGradle {
        target("*.gradle.kts", "buildSrc/*.gradle.kts", "buildSrc/src/**/*.gradle.kts")

        ktfmt().kotlinlangStyle()
        indentWithSpaces(4)
        trimTrailingWhitespace()
        endWithNewline()
    }

    // JSON 파일 포맷팅
    json {
        target("src/**/*.json")
        targetExclude("build/**")

        gson().indentWithSpaces(2)
    }

    // YAML 파일 포맷팅
    format("yaml") {
        target("src/**/*.yml", "src/**/*.yaml")
        targetExclude("build/**")

        prettier()
    }

    // Properties 파일 포맷팅
    format("properties") {
        target("src/**/*.properties")
        targetExclude("build/**")

        trimTrailingWhitespace()
        endWithNewline()
    }

    // XML 파일 포맷팅
    format("xml") {
        target("src/**/*.xml")
        targetExclude("build/**")

        eclipseWtp(EclipseWtpFormatterStep.XML)
    }
}

// 커스텀 태스크
tasks.register("doFormat") {
    description = "모든 소스 코드를 Spotless 규칙에 따라 포맷팅합니다"
    dependsOn(tasks.spotlessApply)

    doLast { println("코드 포맷팅이 완료되었습니다!") }
}

tasks.register("checkFormat") {
    description = "코드 포맷팅 규칙 준수 여부를 검사합니다"
    dependsOn(tasks.spotlessCheck)

    doLast { println("코드 포맷팅 검사가 완료되었습니다!") }
}
