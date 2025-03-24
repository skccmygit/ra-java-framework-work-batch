# SKCC KMS AI - RA-JAVA-FRAMEWORK-WORK-BATCH

# 시작하기 가이드
## 소개

[RA-JAVA-FRAMEWORK-WORK-BATCH]에 오신 것을 환영합니다!
이 프로젝트 문서는 프로젝트 설정을 안내하여 빠르게 시작할 수 있도록 도와드립니다. 프로젝트에 기여하거나 사용하시는 분들에게 이 가이드가 도움이 될 것입니다.
이 프로젝트는 작업 배치를 위한 다음 기능들을 포함하고 있습니다: Job Scheduler, Batch.

## 사전 요구사항

- JDK 21
- Docker 및 Docker Compose
- Gradle 8.12.1 (또는 포함된 Gradle wrapper 사용)

## 프로젝트 구조

프로젝트는 다음과 같은 모듈들로 구성되어 있습니다:

- `com-batch` - 메인 서비스 구현
- `job-scheduler` - Job Scheduler 관련 API 포함
- `account-export` - 공유 DTO 및 인터페이스 포함
- `common-export` - 공통 유틸리티 및 공유 컴포넌트

## 설정 및 설치

1. 저장소 클론:

```bash
git clone <repository-url>
cd work-batch
```

2. Docker Compose를 사용하여 필요한 의존성 서비스 시작:

```bash
# 서비스 시작: zookeeper, kafka, kafka-ui, redis, mysql
docker-compose -f docker-compose.yml up -d
```

3. 데이터베이스 초기화:

```bash
# 모든 컨테이너 목록 확인
  docker ps

# mysql 컨테이너 실행 - <container_id> 값을 해당하는 값으로 변경
  docker exec -it <container_id> bash

# mysql 로그인
  mysql -u root -p

# MySql 비밀번호 입력

# SQL 파일 실행
# 데이터베이스 생성
  CREATE DATABASE OCO;
  CREATE DATABASE quartz;

# 사용자 생성
  CREATE USER 'com_dev'@'%' IDENTIFIED BY 'qwer1234!';

# 권한 부여
  GRANT ALL PRIVILEGES ON OCO.* TO 'com_dev'@'%';  
  GRANT ALL PRIVILEGES ON quartz.* TO 'com_dev'@'%';

  FLUSH PRIVILEGES;
```

4. IDE로 연결

MySQL Workbench나 MySQL을 지원하는 다른 IDE를 사용하여 MySQL 컨테이너에 연결할 수 있습니다.
설정 단계는 다음과 같습니다:

- MySQL Workbench 또는 사용하고자 하는 IDE를 엽니다.
- 다음 정보로 새 연결을 설정합니다:
    - **호스트명**: 127.0.0.1 (localhost)
    - **포트**: 3307
    - **사용자명**: com_dev
    - **비밀번호**: qwer1234!

- **연결 테스트**: MySQL 서버에 접근할 수 있는지 연결을 확인합니다.
  필요한 경우 'allowPublicKeyRetrieval=true'를 설정해야 할 수 있습니다.

연결이 성공하면 IDE의 그래픽 인터페이스를 사용하여 데이터베이스를 관리하고, SQL 쿼리를 실행하며, 데이터를 더 쉽게 조작할 수 있습니다.

5. 프로젝트 빌드:

```bash
./gradlew clean build
```

## 애플리케이션 실행

1. 서비스 시작:

```bash
./gradlew :com-batch:bootRun
```

2. 메인 서비스는 `http://localhost:8181/com-batch/health-check`에서 확인할 수 있습니다.
   ![swager.png](docs/imgs/HealthCheckComBatch.png)

3. Job Scheduler 서비스는 `http://localhost:8181/job-scheduler/health-check`에서 확인할 수 있습니다.
   ![swager.png](docs/imgs/HealthCheckJobScheduler.png)

## 개발

- `./gradlew build`를 사용하여 모든 모듈 빌드
- `./gradlew bootRun`을 사용하여 서비스를 로컬에서 실행

## 데이터베이스 설정

프로젝트는 `init-database` 폴더에 초기 설정을 위한 SQL 스크립트를 포함하고 있습니다:

- `menu.sql` 메뉴 관련 데이터
- `query.sql` 사용자 관련 데이터
- `query_batch.sql` Job Scheduler 관련 데이터

논리 ERD
![logic-erd.png](docs/imgs/logic-erd.png)

물리 ERD
![physic-erd.png](docs/imgs/physic-erd.png)

## 추가 리소스

- 자세한 정보는 README.md 파일을 확인하세요
- 특정 세부사항은 개별 모듈 문서를 참조하세요
- 인프라 설정은 docker-compose 파일을 참조하세요

## 문제 해결

- 데이터베이스 연결 문제가 발생하면 데이터베이스 컨테이너가 실행 중인지 확인하세요
- `logs` 폴더에서 자세한 오류 메시지를 확인하세요