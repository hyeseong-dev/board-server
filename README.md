### 프로젝트 이름: **게시판 시스템**

## 소개
이 프로젝트는 **대규모 트래픽을 처리할 수 있는 게시판 시스템**을 개발하는 것을 목표로 하고 있습니다. 사용자 인증, 게시글 작성, 수정, 삭제, 댓글 및 태그 관리, 캐시 시스템을 활용한 검색 기능 등을 구현하였습니다. **Spring Boot**와 **MyBatis**를 기반으로 하며, **Redis** 및 **MySQL**을 연동하여 성능을 최적화했습니다.

## 주요 기능
- **게시글 관리**: CRUD(Create, Read, Update, Delete) 기능 제공
- **댓글 기능**: 게시글에 대한 댓글 작성, 수정, 삭제
- **태그 관리**: 게시글에 태그 추가, 수정, 삭제
- **검색 기능**: Redis 캐시를 활용한 빠른 게시글 검색
- **API 구성**: RESTful API 제공

## 설치 및 실행

### 1. 의존성 설치
프로젝트 실행을 위해 아래와 같은 의존성이 필요합니다:

- **Java 11** 이상
- **Maven** 혹은 **Gradle**
- **Docker** (MySQL, Redis, Jenkins 사용)

### 2. 데이터베이스 및 캐시 설정

게시판 시스템은 **MySQL**과 **Redis**를 사용합니다. 이를 Docker Compose를 통해 실행할 수 있습니다.

#### MySQL & Redis 실행 (Docker Compose)
```bash
docker-compose up -d
```

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    container_name: mysql_db
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: board_db
    ports:
      - "3306:3306"
    volumes:
      - ./mysql_data:/var/lib/mysql

  redis:
    image: redis:alpine
    container_name: redis_cache
    ports:
      - "6379:6379"
    volumes:
      - ./redis_data:/data
```

### 3. Jenkins 설정
CI/CD 파이프라인을 자동화하기 위해 **Jenkins**를 Docker로 설정할 수 있습니다.

```bash
docker-compose up -d jenkins
```

```yaml
  jenkins:
    image: jenkins/jenkins:lts
    container_name: jenkins
    ports:
      - "8081:8080"
      - "50000:50000"
    volumes:
      - ./jenkins_home:/var/jenkins_home
```

### 4. 애플리케이션 실행

```bash
# Maven 실행
mvn spring-boot:run

# 혹은 Gradle 실행
./gradlew bootRun
```

### 5. API 문서
- **Postman**를 통해 API 문서를 제공합니다. `https://documenter.getpostman.com/view/14042841/2sAXjRWVTN` 접속하여 API 명세를 확인할 수 있습니다.

## 사용 예시
게시글 조회 API를 호출하는 예시:
```bash
curl -X GET "http://localhost:8080/posts"
```

## 기여 방법
1. 이 레포지토리를 포크하세요.
2. 새로운 브랜치를 생성하세요: `git checkout -b feature/your-feature`
3. 변경사항을 커밋하세요: `git commit -m 'Add some feature'`
4. 브랜치에 푸시하세요: `git push origin feature/your-feature`
5. 풀 리퀘스트를 생성하세요.

## 라이센스
이 프로젝트는 MIT 라이센스에 따라 배포됩니다. [LICENSE](LICENSE) 파일을 참조하세요.

## 추가 정보
- 공식 문서 및 상세한 API 정보는 [Wiki](https://github.com/hyeseong-dev/board-server/wiki)에서 확인 가능합니다.
