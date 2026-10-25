# 자바웹프로그래밍(2) 실습

## 6주차 - 권한(ROLE) 접근 제어, 관리자 페이지 분리 완료
- [SecurityConfig.java](demo/src/main/java/com/example/demo/config/SecurityConfig.java)
  - `@EnableMethodSecurity` (메서드 보안 ON → `@PreAuthorize` 사용)
  - `.requestMatchers("/admin/**").hasRole("ADMIN")` → 관리자만 (규칙은 위에서부터, `anyRequest()`는 항상 마지막)
- [AdminController.java](demo/src/main/java/com/example/demo/controller/AdminController.java) (신규): `@RequestMapping("/admin")`, 회원 목록 / 권한 변경 / 삭제 (POST + redirect + flash 메시지 = PRG 패턴)
- [MemberController.java](demo/src/main/java/com/example/demo/controller/MemberController.java): `/mypage` (Principal 로 로그인 아이디 조회 → `?id=` 번호 조작(IDOR) 차단)
- [MemberService.java](demo/src/main/java/com/example/demo/model/service/MemberService.java): `findByUsername`, `findAll(Sort.by("id"))`, `changeRole`(ROLES 화이트리스트), `deleteMember` + `@PreAuthorize("hasRole('ADMIN')")`
- 화면: [index.html](demo/src/main/resources/templates/index.html) (내 정보 · 빨간 관리자 메뉴 · 권한 배지), [mypage.html](demo/src/main/resources/templates/mypage.html) (DB 권한 vs 세션 권한), [admin/members.html](demo/src/main/resources/templates/admin/members.html), [error/403.html](demo/src/main/resources/templates/error/403.html)
- 관리자 계정: 회원가입 후 [관리자_권한_부여.sql](관리자_권한_부여.sql) 실행 → 다시 로그인 (권한은 로그인할 때 세션에 복사됨)

### 접근 제어 3단계
| 단계 | 위치 | 역할 |
|---|---|---|
| ① 화면 | `sec:authorize` | 보이기/숨기기만 (보안 아님) |
| ② URL | `requestMatchers(...).hasRole()` | 1차 잠금, 컨트롤러 도착 전 차단 |
| ③ 메서드 | `@PreAuthorize` | 2차 잠금, 서비스 실행 직전 차단 (URL 규칙 실수·누락 대비) |

## 6주차 연습문제 - 관리자 보호 & 매니저(MANAGER) 권한
1. **관리자 본인 보호**: `changeRole`, `deleteMember`에서 대상이 본인이면 거부 (`SecurityContextHolder...getName()`) → "자기 자신의 권한 변경·삭제는 할 수 없습니다."
2. **MANAGER 권한 추가**: ROLES 에 MANAGER 추가 (회원 관리에서 부여 가능), `/testdb`는 `hasAnyRole("ADMIN", "MANAGER")` → USER 는 403
3. **(추가 도전)** 회원목록 메뉴를 `sec:authorize="hasAnyRole('ADMIN','MANAGER')"`로 USER 에게 숨김

### 테스트 결과
| 시나리오 | 결과 |
|---|---|
| 비로그인 `/admin/members` | 302 → 로그인 화면 |
| USER `/admin/members` | 403 접근 거부 화면 (아이디 · [ROLE_USER] · 요청 주소) |
| USER 가 자기 권한을 ADMIN 으로 (POST 직접 전송) | 403, DB 그대로 USER |
| USER `/testdb` | 403 |
| ADMIN 회원 관리 | 200, USER → MANAGER 변경 성공 메시지 |
| 권한 값 조작 (SUPER) | "허용되지 않는 권한입니다 : SUPER" |
| ADMIN 본인 권한 변경 / 삭제 | "자기 자신의 권한 변경·삭제는 할 수 없습니다." |
| DB 를 MANAGER 로 바꾼 직후 | 세션은 아직 [ROLE_USER] → 다시 로그인하면 [ROLE_MANAGER], `/testdb` 200, `/admin/members` 403 |
| 회원 삭제 | "회원을 삭제했습니다." |

> 관리자 계정(admin/1234)은 실습용. 실제 서비스는 강한 비밀번호 + 별도 관리.

## 실행 방법
1. `demo/src/main/resources/application-secret.properties` 작성 (깃에 안 올라감)
   ```
   spring.datasource.username=root
   spring.datasource.password=내비밀번호
   ```
2. `DemoApplication.java` → `Ctrl + F5` → http://localhost:8080

## 이전 주차
- [2주차](https://github.com/LSY0820/springboot-week2) · [3주차](https://github.com/LSY0820/springboot-week3) · [4주차](https://github.com/LSY0820/springboot-week4) · [5주차](https://github.com/LSY0820/springboot-week5)
