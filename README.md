# SWYP Web 11기 Team 4 Backend

SpringBoot 3.5.6 + Java 17 기반의 백엔드 프로젝트입니다.

## 🚀 기술 스택 (Technology Stack)

- **Language**: Java 17
- **Framework**: Spring Boot 3.5.6
- **Build Tool**: Gradle
- **Database**: JPA, H2 (Test), MySQL
- **Code Quality**:
  - Spotless (Code Formatting)
  - Jacoco (Code Coverage)
  - SonarQube (Code Analysis)

## 🏁 시작하기 (Getting Started)

### 사전 요구 사항 (Prerequisites)

- Java 17
- IDE (e.g., IntelliJ)

### 설치 및 실행 (Installation and Run)

1. **저장소 복제 (Clone the repository)**
   ```shell
   git clone https://github.com/swyp-web-11-team-4/backend.git
   ```

2. **프로젝트 빌드 (Build the project)**
   ```shell
   ./gradlew build
   ```

3. **애플리케이션 실행 (Run the application)**
   ```shell
   ./gradlew bootRun
   ```

## ✨ 코드 스타일 (Code Style)

이 프로젝트는 [Spotless](https://github.com/diffplug/spotless)를 사용하여 코드 스타일을 강제합니다.  
CI/CD 파이프라인에서 코드 스타일을 검사하므로, 커밋하기 전에 아래 명령어를 실행하여 코드를 포맷팅해주세요.

```shell
./gradlew spotlessApply
```

`code-style/intellij-style.xml` 파일을 IDE에 사용하여 인텔리제이 코드 스타일을 유지할 수 있습니다.

**IntelliJ에서 설정하는 방법:**
1. `File` > `Settings` > `Editor` > `Code Style`
2. `Scheme` 옆의 톱니바퀴 아이콘 클릭 > `Import Scheme` > `IntelliJ IDEA code style XML`
3. `code-style/intellij-style.xml` 파일 선택 후 적용

## 🛠️ 사용 가능한 명령어 (Available Commands)

- `./gradlew build`: 프로젝트를 빌드하고 테스트를 실행합니다.
- `./gradlew bootRun`: 애플리케이션을 로컬에서 실행합니다.
- `./gradlew test`: JUnit 테스트를 실행합니다.
- `./gradlew spotlessApply`: 코드 스타일을 적용합니다.
- `./gradlew spotlessCheck`: 코드 스타일 준수 여부를 확인합니다.
- `./gradlew jacocoTestReport`: 코드 커버리지 리포트를 생성합니다. (`build/reports/jacoco/test/html/index.html`)
- `./gradlew jacocoTestCoverageVerification`: 코드 커버리지를 검증합니다.
- `./gradlew check`: 코드 커버리지 및 포맷팅 검사를 모두 실행합니다.
