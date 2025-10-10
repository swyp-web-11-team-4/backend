# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring Boot 3.5.6 + Java 17 기반의 백엔드 프로젝트입니다. Gradle 빌드 시스템과 함께 Spotless, Jacoco, SonarQube를 활용한 코드 품질 관리가 설정되어 있습니다.

## Essential Commands

### Building and Running
```shell
# Build project and run tests
./gradlew build

# Run application locally
./gradlew bootRun

# Run tests only
./gradlew test
```

### Running a Single Test
```shell
# Run specific test class
./gradlew test --tests "com.swygbro.trip.example.application.ExampleServiceTest"

# Run specific test method
./gradlew test --tests "com.swygbro.trip.example.application.ExampleServiceTest.testMethodName"
```

### Code Quality
```shell
# Apply code formatting (MUST run before committing)
./gradlew spotlessApply

# Check code formatting compliance
./gradlew spotlessCheck

# Generate code coverage report
./gradlew jacocoTestReport
# Report location: build/reports/jacoco/test/html/index.html

# Verify coverage thresholds
./gradlew jacocoTestCoverageVerification

# Run all quality checks (coverage + formatting)
./gradlew check
```

### Code Analysis
```shell
# Run SonarQube analysis
./gradlew sonar
```

## Architecture

### Package Structure

프로젝트는 **모놀리식 아키텍처**로 구현되며, 도메인별로 계층화된 패키지 구조를 따릅니다:

```
com.swygbro.trip.<domain>/
├── presentation/     # Controllers (REST API endpoints)
├── application/      # Use cases and business logic services
├── domain/
│   ├── dto/         # Data Transfer Objects
│   └── entity/      # Domain Entities
└── infrastructure/   # Repositories and external integrations
```

**의존성 방향**: `presentation` → `application` → `domain` ← `infrastructure`

- **Presentation Layer**: REST API 엔드포인트를 정의하는 컨트롤러. UseCase 인터페이스에 의존
- **Application Layer**: 비즈니스 로직을 구현하는 서비스. UseCase 인터페이스를 구현
- **Domain Layer**: DTO와 비즈니스 엔티티를 포함. 데이터 구조와 도메인 모델 정의
- **Infrastructure Layer**: JPA 리포지토리와 외부 시스템 통합

### Key Patterns

1. **Use Case Interface Pattern**: 각 도메인의 `application` 패키지에 `*UseCase` 인터페이스를 정의하고, `*Service` 클래스에서 구현합니다. Presentation 계층은 인터페이스에 의존합니다.

2. **Example Package Reference**: 다음의 패키지 구조 예시를 참고하여 새로운 도메인을 구성하세요:
   - `ExampleController` (presentation)
   - `ExampleUseCase` (application interface)
   - `ExampleService` (application implementation)
   - `ExampleResponse` (domain/dto)
   - `Example` (domain/entity)
   - `ExampleRepository` (infrastructure)

## Build Configuration

### Custom Gradle Plugins

프로젝트는 `buildSrc/src/main/kotlin/plugin/` 디렉토리에 커스텀 Gradle 플러그인을 정의합니다:

- **`plugin.coverage`**: Jacoco 설정. 커버리지 제외 패턴 및 검증 규칙 포함
  - 전체 프로젝트: 60% 이상 (LINE/BRANCH)
  - 개별 클래스: 70% 이상 (LINE)
  - 제외 대상: `*Application*`, `*Config*`, `*Dto*`, `*Entity*`, `*Exception*`

- **`plugin.spotless`**: 코드 포맷팅 설정. Java, Kotlin, Gradle, JSON, YAML, Properties, XML 파일 포맷팅 규칙 포함
  - Java: `code-style/intellij-style.xml` 사용
  - Import 순서: `java`, `javax`, `jakarta`, `org`, `com`, `""`, `\#`

- **`plugin.sonar`**: SonarQube 연동 설정

### Version Catalog

의존성은 `gradle/libs.versions.toml`에서 중앙 관리됩니다. 새로운 의존성 추가 시 이 파일을 수정하세요.

## Code Style

- **IntelliJ 설정**: `code-style/intellij-style.xml`을 IDE에 import하여 일관된 스타일 유지
  - `File` > `Settings` > `Editor` > `Code Style` > `Import Scheme`

- **Spotless**: CI/CD에서 코드 스타일 검사. 커밋 전 반드시 `./gradlew spotlessApply` 실행

## Commit Conventions

프로젝트는 **Semantic Release**를 사용합니다 (`.releaserc.json` 참조).

커밋 메시지는 아래 형식을 따라야 합니다:
```
<type>: <subject>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
```

- **type**: 커밋의 종류를 나타냅니다. (필수)
- **subject**: 커밋에 대한 간결한 설명을 제공합니다. (필수)

주요 타입:
- `feat`: 새로운 기능 추가
- `fix`: 버그 수정
- `design`: CSS 등 사용자 UI 디자인 변경
- `docs`: 문서 변경 (README, 가이드, 주석 등)
- `chore`: 빌드 설정, 의존성, 환경 설정 변경
- `refactor`: 코드 리팩토링 혹은 성능 개선
- `test`: 테스트 코드 추가 또는 수정
- `comment`: 필요한 주석 추가 및 수정
- `style`: 코드 포맷팅, 세미콜론 등 (기능 변경 없음)
- `remove`: 파일, 기능, 의존성 제거
- `ci`: CI 설정 변경
- `cd`: CD 설정 변경

**Note**: `feat`, `fix` 타입은 CHANGELOG.md에 자동 포함되며 버전 업데이트를 트리거합니다.

## CI/CD Pipeline

`.github/workflows/ci.yml`에 정의된 파이프라인:

1. **Test & Build**: 테스트 실행, 커버리지 생성, PR에 커버리지 리포트 코멘트
2. **SonarQube Analysis**: 코드 품질 분석 및 Quality Gate 체크
3. **Semantic Release**: `main` 브랜치 푸시 시 자동 버전 관리 및 릴리즈
4. **Docker Build & Push**: 릴리즈 생성 시 Docker 이미지 빌드 및 ghcr.io에 푸시

## Testing

- **Test Location**: `src/test/java/com/swygbro/trip/`
- **Test Database**: H2 (in-memory)
- **Coverage Reports**: 테스트 실행 시 자동 생성 (`build/reports/jacoco/`)

## Swagger/OpenAPI

Swagger UI는 `spring-boot-openapi-starter-webmvc-ui`를 통해 자동 설정됩니다.
- Swagger 설정: `com.swygbro.trip.common.config.SwaggerConfig`
- 컨트롤러에 `@Tag`, `@Operation` 어노테이션 추가하여 API 문서화
- 개발 서버에서만 Swagger UI를 통해 api 확인이 가능합니다.
