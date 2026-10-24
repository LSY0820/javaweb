# 자바웹프로그래밍(2) 실습

## 4주차 - 데이터베이스 연동 완료
- [pom.xml](demo/pom.xml): Spring Data JPA, MySQL 커넥터(`mysql-connector-j`) 주석 해제
- [application.properties](demo/src/main/resources/application.properties): MySQL 접속 정보 (`localhost:3306/spring`, `ddl-auto=update`, `show-sql=true`)
- 계층별 패키지 구조로 변경 (MVC + 서비스/리포지토리)
  - `controller` - [DemoController.java](demo/src/main/java/com/example/demo/controller/DemoController.java) (기존 파일 이동, `/testdb` 추가)
  - `model/domain` - [TestDB.java](demo/src/main/java/com/example/demo/model/domain/TestDB.java) (엔티티, `testdb` 테이블)
  - `model/repository` - [TestRepository.java](demo/src/main/java/com/example/demo/model/repository/TestRepository.java) (`JpaRepository` 상속)
  - `model/service` - [TestService.java](demo/src/main/java/com/example/demo/model/service/TestService.java) (`findAll()`)
  - View: `templates` 폴더 그대로 유지 - [testdb.html](demo/src/main/resources/templates/testdb.html) (`th:each`로 여러 사용자 출력)
- 흐름: 브라우저 → Controller → Service → Repository → JPA(Hibernate) → MySQL

## 4주차 연습문제 - 사용자 정보 수정 및 출력하기
- 엔티티에 **나이(age), 성별(gender)** 컬럼 추가 → 서버 실행 시 테이블에 자동 반영 (`ddl-auto=update`)
- 사용자 추가 INSERT 문: [testdb_insert.sql](testdb_insert.sql) (VS Code MySQL 확장에서 실행)
- `testdb.html`에서 아이디 / 이름 / 나이 / 성별을 표로 출력

| 아이디 | 이름 | 나이 | 성별 |
|---|---|---|---|
| 1 | 홍길동 | 25 | 남 |
| 2 | 김영희 | 22 | 여 |
| 3 | 이철수 | 30 | 남 |

## 실행 방법
1. MySQL 설치 후 `create database spring;`
2. DB 비밀번호는 공개 저장소에 올리지 않으려고 환경 변수 `DB_PASSWORD`로 넣는다
   - VS Code: `.vscode/launch.json`의 `"env": { "DB_PASSWORD": "내 비밀번호" }` (이 파일은 깃에 안 올라감)
   - 또는 터미널: `set DB_PASSWORD=내비밀번호` (윈도우) / `export DB_PASSWORD=내비밀번호` (맥)
3. `DemoApplication.java` → `Ctrl + F5` → http://localhost:8080/testdb

## 이전 주차
- [2주차 - 스프링 부트 개발환경](../week2)
- [3주차 - 개인 포트폴리오](../week3)
