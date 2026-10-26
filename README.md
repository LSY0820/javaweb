# 자바웹프로그래밍(2) - 스프링 부트 실습

이세영 20241401 · 주차별 실습 코드

| 주차 | 내용 | 폴더 |
|---|---|---|
| 2주차 | 스프링 부트 개발환경, URL 맵핑과 컨트롤러, 연습문제(hello2) | [week2](week2) |
| 3주차 | 개인 포트폴리오 템플릿, 프로필 수정, 기술 상세 페이지 | [week3](week3) |
| 4주차 | MySQL 데이터베이스 연동, 계층별 패키지 구조, 연습문제(나이/성별) | [week4](week4) |
| 5주차 | 스프링 시큐리티 로그인/로그아웃, BCrypt 암호화, 연습문제(로그인 유지/비밀번호 확인) | [week5](week5) |
| 6주차 | 권한(ROLE) 접근 제어, 관리자 페이지 분리, 연습문제(관리자 보호/MANAGER) | [week6](week6) |
| 7주차 | 회원 관리 REST API(JSON), Postman, 연습문제(검색/오류 처리 한 곳에) | [week7](week7) |



# 💻 자바 웹프로그래밍 (2) - Spring Boot 포트폴리오 &amp; 백엔드 개발 프로젝트

&gt; **강의명**: 자바 웹프로그래밍 (2)  
&gt; **담당 교수**: 최도현 교수님  
&gt; **작성자**: [본인 이름/학번]  
&gt; **저장소 주소**: GitHub 저장소 (Public)

---

## 📌 1\. 프로젝트 개요 (Project Overview)

본 프로젝트는 **자바 웹프로그래밍(2)** 수업의 1주차부터 7주차까지 진행된 실습 코드를 바탕으로, **Spring Boot**, **Thymeleaf**, **Spring Data JPA**, **MySQL**, **Spring Security**, 그리고 **REST API(JSON)** 기술 스택을 종합하여 구축한 백엔드 웹 애플리케이션 및 개인 포트폴리오 프로젝트입니다.

단순한 화면 구현에 그치지 않고, 3-계층 아키텍처(Controller-Service-Repository)의 분리, BCrypt 암호화와 권한 분리를 통한 보안 강화, DTO 패턴 기반의 RESTful API 설계 및 예외 처리 일원화까지 실무 중심의 백엔드 개발 프로세스를 반영하여 정성껏 작성하였습니다.

---

## 🛠 2\. 기술 스택 (Tech Stack)

### Backend &amp; Framework

* **Language**: Java 17 / 21
* **Framework**: Spring Boot
* **Security**: Spring Security (BCrypt Hashing, Role-based Access Control)
* **Data Access**: Spring Data JPA (Hibernate, ORM)
* **API Communication**: REST API, JSON, Jackson, DTO Pattern

### Frontend &amp; Database

* **Template Engine**: Thymeleaf (Server-Side Rendering)
* **UI Framework**: Bootstrap v5 (First Portfolio TemplateMo 578)
* **Database**: MySQL

### Tools &amp; Infrastructure

* **IDE**: VS Code / IntelliJ IDEA
* **API Testing**: Postman
* **Build Tool**: Maven ()
* **Version Control**: Git &amp; GitHub

---

## 📑 3\. 주차별 학습 및 개발 실습 기록 (Weekly Development Log)

### 🗓 1주차: 강의 소개 및 개발 로드맵 수립

* **주요 내용**: 2학기 자바 웹프로그래밍 강의 개요 및 평가 기준(출석 20%, 중간고사 30%, 기말고사 30%, 기말보고서 20%) 확인.
* **실습**: GitHub Public 저장소 생성, VS Code/IntelliJ 및 Git 연동 완료.

### 🗓 2주차: Spring Boot 개발 환경 구축 및 기본 구조

* **주요 내용**: Web Server(NGINX/Apache)와 WAS(Tomcat)의 차이점 이해 및 Spring Boot의 자동 설정 메커니즘 학습.
* **실습**: , , 을 활용한 URL 매핑 및 Thymeleaf 템플릿 엔진을 이용한 동적 데이터 전달 실습 (, ).

### 🗓 3주차: 프론트엔드 포트폴리오 작성

* **주요 내용**: Bootstrap v5 기반의 반응형 레이아웃(Grid System: Container, Row, Col, Gutter) 학습.
* **실습**: (TemplateMo 578) 단일 페이지 템플릿 적용, 정적 자원(, ) 배치, 네비게이션 앵커(#section\_1\~5) 및 프로필 키워드/기술 스택 커스텀.

### 🗓 4주차: Spring Data JPA &amp; MySQL DB 연동

* **주요 내용**: ORM(Object-Relational Mapping) 개념 및 영속성 컨텍스트(EntityManager) 이해.
* **실습**: 에 Spring Data JPA 및 MySQL 드라이버 설정, Entity - Repository - Service - Controller Layer 분리, VS Code MySQL 확장을 통한 DB 제어 및 Thymeleaf 를 활용한 회원 목록 출력 기능 구현.

### 🗓 5주차: Spring Security 로그인/로그아웃 및 암호화

* **주요 내용**: OWASP Top 10 보안 위협 이해 및 BCrypt 해시 함수를 활용한 Salt 결합 암호화 적용.
* **실습**: Spring Security 기반 로그인/로그아웃 및 Remember-Me 쿠키 설정. DB 접속 정보 노출 방지를 위해 와 를 활용한 보안 조치 적용.

### 🗓 6주차: 권한 관리 및 관리자 페이지 구축

* **주요 내용**: 인증(Authentication)과 인가(Authorization)의 구별 및 역할 기반 접근 제어(RBAC) 구현.
* **실습**: 와 권한 분리, 및 URL 패턴 기반 접근 제어 설정, 403 Forbidden 예외 처리 페이지 연결 및 관리자 계정 보호 로직 적용.

### 🗓 7주차: REST API 및 JSON 통신 구현

* **주요 내용**: SSR(Server-Side Rendering)과 CSR(Client-Side Rendering) 패러다임 비교 및 RESTful API 설계 규칙 학습.
* **실습**: , , 를 활용한 회원 관리 REST API 구축. 민감 정보 노출 방지를 위한 응답 DTO() 정의, Postman을 통한 HTTP 메서드(GET, POST, PATCH, DELETE) 테스트, 를 활용한 예외 처리 일원화 및 회원 검색 API 구현.

---

## 🔧 4\. 트러블슈팅 및 문제 해결 경험 (Troubleshooting)

### 🚨 1\. GitHub 업로드 시 DB 접속 정보(비밀번호) 노출 위험

* **문제**: 에 데이터베이스 계정 정보가 평문으로 저장된 상태에서 GitHub 원격 저장소에 Push할 경우 보안 사고 위험 발생.
* **해결**:  
  1. 파일을 별도 생성하여 DB 계정 정보 분리.
  2. 에 구문 추가.
  3. 파일에 를 등록하여 Git 추적에서 제외함으로써 안전하게 관리.

### 🚨 2\. REST API 응답 시 Entity 직접 반환으로 인한 민감 정보 노출

* **문제**: 에서 엔티티 객체를 직접 JSON으로 반환할 때, 보안 필드(비밀번호 등)가 그대로 클라이언트에 전달됨.
* **해결**:  
  * 응답 전용 DTO 클래스인 를 작성 (uid=1000(sandbox) gid=1000(sandbox) groups=1000(sandbox), , , 필드만 포함).
  * 엔티티를 DTO로 변환하여 반환함으로써 데이터 캡슐화 및 민감 정보 노출 차단.

### 🚨 3\. Controller 내 중복된 예외 처리 코드 ( 반복)

* **문제**: API 컨트롤러의 등록, 수정, 삭제 메서드마다 동일한 블록이 중복 작성되어 코드 가독성 저하.
* **해결**:  
  * 클래스를 작성하고 메서드를 등록.
  * 예외 발생 시 일관된 에러 JSON 메시지와 상태 코드를 반환하도록 일원화하여 컨트롤러 코드 단순화.

---

## 📝 5\. 중간고사 대비 학습 요약 및 소회 (Exam Summary &amp; Reflection)

1. **전체 백엔드 흐름 습득**: 단순한 웹 페이지 출력에서 시작하여 DB 연동(JPA), 보안(Security), API(REST/JSON)까지 현대 웹 백엔드 개발의 핵심 축을 단계별로 습득하였습니다.
2. **보안 및 코드 품질의 중요성**: 평문 계정 저장 금지, 를 활용한 비밀키 보호, DTO를 통한 데이터 보호 등 실무에서 필수적인 보안 관점의 코딩 습관을 형성했습니다.
3. **API 설계 및 테스트**: Postman을 활용해 상태 코드(200 OK, 201 Created, 400 Bad Request, 403 Forbidden)와 JSON 응답 구조를 직접 검증하며 RESTful 서비스 설계 역량을 키웠습니다.
