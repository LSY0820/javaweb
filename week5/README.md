# 자바웹프로그래밍(2) 실습

## 5주차 - 로그인/로그아웃, 암호화 완료
- [pom.xml](demo/pom.xml): `spring-boot-starter-security`, `thymeleaf-extras-springsecurity6` 추가
- 새로 만든 자바 파일 6개
  - `config` - [SecurityConfig.java](demo/src/main/java/com/example/demo/config/SecurityConfig.java): BCrypt 암호화 객체, URL 접근 규칙(인가), 폼 로그인(인증), 로그아웃, 로그인 상태 유지
  - `controller` - [MemberController.java](demo/src/main/java/com/example/demo/controller/MemberController.java): `/login`, `/signup` 화면, 회원가입 처리
  - `model/domain` - [Member.java](demo/src/main/java/com/example/demo/model/domain/Member.java): `member` 테이블 (아이디 중복 불가, 권한 USER)
  - `model/dto` - [MemberForm.java](demo/src/main/java/com/example/demo/model/dto/MemberForm.java): 가입 화면 → 컨트롤러 데이터 (role 없음 → 권한 조작 방지)
  - `model/repository` - [MemberRepository.java](demo/src/main/java/com/example/demo/model/repository/MemberRepository.java): `findByUsername`, `existsByUsername`
  - `model/service` - [MemberService.java](demo/src/main/java/com/example/demo/model/service/MemberService.java): 가입(BCrypt 암호화) + `UserDetailsService`(로그인 시 회원 조회)
- 화면: [login.html](demo/src/main/resources/templates/login.html), [signup.html](demo/src/main/resources/templates/signup.html), [index.html](demo/src/main/resources/templates/index.html) 네비게이션
  - `sec:authorize="isAnonymous()"` → 로그인 버튼 / `isAuthenticated()` → `아이디님` + 로그아웃
  - 모든 폼은 `th:action` 사용 → CSRF 토큰(`_csrf`) 자동 삽입. 로그아웃도 POST 폼
- 비로그인 상태로 `/testdb`(회원목록) 접근 → 로그인 화면으로 이동, 로그인 후 접근 가능
- **DB 비밀번호 분리**: 계정 정보는 `application-secret.properties`(깃에 안 올라감, `.gitignore`)로 옮기고 `spring.config.import=optional:application-secret.properties`로 읽음

- 화면(login.html, signup.html)은 **5주차 제공 코드** 사용, index.html 은 제공된 `index_네비게이션_교체부분.html`의 교체부분만 반영 (내 포트폴리오 유지)

## 5주차 연습문제 - 로그인 상태 유지, 비밀번호 확인
1. **로그인 상태 유지 (remember-me)**: `SecurityConfig`에 `.rememberMe()` 추가 (`key`, 7일). 로그인 화면 체크박스 `name="remember-me"` → 브라우저를 닫았다 열어도 로그인 유지
2. **비밀번호 확인 검증**: `MemberService.signup()`에서 `password`와 `passwordConfirm` 비교 → 다르면 "비밀번호가 일치하지 않습니다." 출력, DB에 저장하지 않음

### 테스트 결과
| 시나리오 | 결과 |
|---|---|
| 비로그인 `/testdb` | 로그인 화면으로 이동 (302) |
| 비밀번호 불일치 가입 | "비밀번호가 일치하지 않습니다." (저장 안 됨) |
| 정상 가입 | `/login?signup` + DB에 `$2a$10$...` (BCrypt 60자) 저장 |
| 같은 아이디 가입 | "이미 사용 중인 아이디입니다." |
| 틀린 비밀번호 로그인 | `/login?error` |
| 정상 로그인 (+유지 체크) | 메인 `아이디님` + 로그아웃, `JSESSIONID`·`remember-me` 쿠키 |
| 세션 없이 remember-me 쿠키만 | `/testdb` 접근 가능 |
| 로그아웃 | `/login?logout`, 다시 `/testdb` → 로그인 화면 |

## 실행 방법
1. `demo/src/main/resources/application-secret.properties` 파일을 만들고 아래 2줄 작성 (깃에 안 올라감)
   ```
   spring.datasource.username=root
   spring.datasource.password=내비밀번호
   ```
2. `DemoApplication.java` → `Ctrl + F5` → http://localhost:8080

## 이전 주차
- [2주차](../week2) · [3주차](../week3) · [4주차](../week4)
