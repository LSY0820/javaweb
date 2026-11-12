# 자바웹프로그래밍(2) 실습

## 10주차 - 페이징 + 검색 + 댓글 완료
- 제공 화면 복사: [board/list.html](demo/src/main/resources/templates/board/list.html) (페이지 번호 + 검색창), [board/detail.html](demo/src/main/resources/templates/board/detail.html) (댓글 영역), [board.css](demo/src/main/resources/static/css/board.css). 테스트 글 105개는 제공 SQL 로 DB 에 넣음 (깃에는 안 올림)
- **단계 1 페이징**: `PostService.findPage()` → `PageRequest.of(page, 10, id 내림차순)` + `findAll(pageable)` (리포지토리 수정 없음). 컨트롤러가 `posts`(이번 페이지 글) + `page`(페이지 정보)를 넘김. SQL 은 `limit ?,?` + `count` 2번
- **단계 2 검색**: [PostRepository.java](demo/src/main/java/com/example/demo/model/repository/PostRepository.java) JPQL `@Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%")` → 검색 결과도 10개씩, `keyword` 를 넘겨서 검색창·페이지 링크에 유지. 파라미터 바인딩(`like ?`)이라 `' OR 1=1 --` 는 그냥 글자 → 0개
- **단계 3 댓글 쓰기**: [Comment.java](demo/src/main/java/com/example/demo/model/domain/Comment.java) (`comments` 테이블, `@ManyToOne(LAZY)` post_id·member_id 외래 키 2개), [CommentRepository.java](demo/src/main/java/com/example/demo/model/repository/CommentRepository.java) (`findByPostOrderByIdAsc`), [CommentService.java](demo/src/main/java/com/example/demo/model/service/CommentService.java), [CommentController.java](demo/src/main/java/com/example/demo/controller/CommentController.java) `POST /board/{postId}/comments` → 성공·실패 모두 redirect (PRG, 새로고침해도 중복 등록 없음)
- **단계 4 댓글 삭제**: `POST /board/3/comments/7/delete` → 본인 또는 관리자 (`checkWriter` 403, 없는 댓글 404)
- **단계 5 글 삭제 정리**: [Post.java](demo/src/main/java/com/example/demo/model/domain/Post.java) `@OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)` + `@ToString.Exclude @EqualsAndHashCode.Exclude` → 글을 지우면 댓글 DELETE 가 먼저 나감. 목록 제목 옆 `[댓글 수]` 주석 해제
- **단계 6 회원 삭제 정리**: `existsByAuthor` (글·댓글) → [MemberService.java](demo/src/main/java/com/example/demo/model/service/MemberService.java) 에서 DB 가 거부하기 전에 먼저 확인 → 화면은 빨간 안내, API 는 500 대신 400 `{"error"}`

| 지우는 것 | 연결된 것 | 선택 | 이유 |
|---|---|---|---|
| 글 | 댓글 | 함께 삭제 (cascade) | 글이 없으면 댓글은 의미가 없다 |
| 회원 | 글 · 댓글 | 삭제 막기 | 남이 볼 글, 남이 단 댓글이 사라지면 안 된다 |

## 10주차 연습문제 - 내가 쓴 글 페이징 & 작성자로 검색
1. **내가 쓴 글 페이징**: `GET /board/my?page=0` → `findByAuthor_Username(String username, Pageable pageable)` (메서드 이름 조회 + Pageable = Page), 컨트롤러가 `page`, `pageUrl("/board/my")` 를 넘겨서 페이지 링크가 `/board/my?page=1`
2. **작성자로 검색**: 검색 범위 선택 (제목+내용 / 작성자). JPQL `p.author.name LIKE %:name%` (posts JOIN member 자동), 컨트롤러가 `type` 을 넘겨서 선택 상자가 나타나고 페이지 링크에 `type=writer` 유지

### 테스트 결과 (8099 포트, 임시 계정으로 확인 후 삭제)
| # | 하는 일 | 결과 |
|---|---|---|
| 1 | /board | 글 10개 + "1 / 11 페이지" |
| 2 | 페이지 2 클릭 | 다음 10개, 주소 page=1 |
| 3 | 마지막 페이지 / page=-1 / page=999 | 남은 글만 / 첫 페이지 / 빈 목록 (모두 오류 없음) |
| 4 | "스터디" 검색 → 2페이지 | 14개(테스트 13 + 직접 쓴 글), 2페이지에도 keyword 유지 |
| 5 | "파이썬" / 빈칸 / `' OR 1=1 --` | "검색 결과가 없습니다." / 전체 목록 / 0개 |
| 6 | 비로그인 상세 | 댓글 영역 + "로그인하면 댓글을 쓸 수 있습니다." |
| 7 | 학생 댓글 → 다른 학생 답글 → 빈 댓글 → 관리자 댓글 → F5 | 오래된 댓글이 위 / "댓글은 1~500자로 입력하세요." / 댓글 수 3, 새로고침해도 3 |
| 8 | 댓글 삭제 버튼 | 내 댓글만 "삭제", 관리자는 남의 댓글에 "관리자 삭제", 남의 댓글 삭제 POST 직접 전송은 **403** |
| 9 | 댓글 달린 글 삭제 | "글이 삭제되었습니다." (로그 : comments → posts 순서로 DELETE), DB 에 댓글도 없음 |
| 10 | 댓글 쓴 회원 삭제 (화면 / API) | 빨간 안내 "작성한 글이나 댓글이 있는 회원은 삭제할 수 없습니다." / 400 `{"error"}` |
| 11 | 글·댓글 없는 회원 삭제 | 삭제됨 |
| 12 | 연습 1 : 관리자 내가 쓴 글 / 비로그인 /board/my | 10개씩, 링크 /board/my?page=1 / 로그인 화면 |
| 13 | 연습 2 : 작성자 + "홍길" / 작성자 검색 2페이지 | 홍길동의 글만 / 주소에 type=writer 유지 |

## 9주차 - 게시판 CRUD와 본인 확인 완료
- [Post.java](demo/src/main/java/com/example/demo/model/domain/Post.java): 게시글 엔티티(`posts` 테이블). 작성자는 글자가 아니라 회원 객체로 연결 → `@ManyToOne(fetch = LAZY)` + `@JoinColumn(name = "member_id")` (DB 에서는 외래 키)
- [PostForm.java](demo/src/main/java/com/example/demo/model/dto/PostForm.java): 제목·내용만 받는 DTO → **작성자는 받지 않음** (남의 이름으로 글쓰기 차단, 작성자는 로그인한 사람으로 서버가 정함)
- [PostRepository.java](demo/src/main/java/com/example/demo/model/repository/PostRepository.java): `save`, `findAll`, `findById`, `delete` 는 JPA 가 이미 제공
- [PostService.java](demo/src/main/java/com/example/demo/model/service/PostService.java): 글쓰기(빈 값 검사 → 작성자 찾기 → 저장), 상세(없으면 404), 수정(변경 후 `save` → id 가 있으니 UPDATE, 수정 시간 기록), 삭제
- [BoardController.java](demo/src/main/java/com/example/demo/controller/BoardController.java): HTML 폼은 GET·POST 만 보낼 수 있어서 주소 끝에 할 일을 붙임 (`/board/3/edit`, `/board/3/delete`)
- [SecurityConfig.java](demo/src/main/java/com/example/demo/config/SecurityConfig.java): 목록·상세만 누구나 `"/board", "/board/{id:\\d+}"` (숫자만 → `/board/write` 는 로그인 필요)
- 화면(제공 코드): [board/](demo/src/main/resources/templates/board) 목록·상세·글쓰기(수정 공용), [error/404.html](demo/src/main/resources/templates/error/404.html), 메인 메뉴에 "게시판" 추가

| 기능 | SQL | JPA | 화면 주소 | 누가 |
|---|---|---|---|---|
| 쓰기 | INSERT | save() | POST /board/write | 로그인한 사람 |
| 목록 | SELECT | findAll() | GET /board | 누구나 |
| 상세 | SELECT … WHERE id | findById() | GET /board/3 | 누구나 |
| 수정 | UPDATE | save() | POST /board/3/edit | **글쓴이만** |
| 삭제 | DELETE | delete() | POST /board/3/delete | **글쓴이 또는 관리자** |

### 본인 확인 (단계 5)
- 버튼만 숨기면 주소창에 `/board/2/edit` 를 직접 쳐서 남의 글을 고칠 수 있다 (6주차 관리자 메뉴와 같은 문제, OWASP A01 접근 제어 실패)
- 글마다 주인이 달라서 URL 규칙으로는 못 막음 → **서비스 코드에서 확인**: `checkWriter()` (로그인한 사람 = 글쓴이? 아니면 `AccessDeniedException` → 403)
- 수정은 본인만, 삭제는 본인 또는 관리자(`isAdmin()`) → 화면(버튼 숨김) + 서버(본인 확인) **이중 확인**

## 9주차 연습문제 - 조회수 & 내가 쓴 글 모아 보기
1. **조회수**: 상세 화면을 열 때마다 `viewCount` 1 증가 → `PostService.read(id)` 를 컨트롤러 `detail()` 에서 사용
2. **내가 쓴 글**: `GET /board/my` → 로그인한 사람의 글만, 최신 글이 위로. 리포지토리에 메서드 이름만 추가 `findByAuthor_UsernameOrderByIdDesc` (= `JOIN member … WHERE m.username = ? ORDER BY p.id DESC`), 목록 화면 재사용 + `listTitle` 로 제목만 "내가 쓴 글"

### 테스트 결과
| # | 계정 | 하는 일 | 결과 |
|---|---|---|---|
| 1 | 없음 | /board 목록 | 200 (글쓰기 버튼 없음) |
| 2 | 없음 | 주소창 /board/write, /board/my | 302 → 로그인 화면 |
| 3 | 학생 A | 글쓰기 | 상세 화면으로 이동 |
| 4 | 학생 A | 제목 비우고 등록 | `제목과 내용을 모두 입력하세요.` |
| 5 | 없음 | /board/99999 | 404 화면 |
| 6 | 누구나 | 같은 글 상세 3번 열기 | 조회 3 |
| 7 | 학생 B | 남의 글 /edit 주소 직접 입력 · 수정 전송 | **403** |
| 8 | 학생 A | 내 글 수정 | 수정됨, "수정 시간" 표시 |
| 9 | 학생 A / B | 내가 쓴 글 | A: 제목 "내가 쓴 글" + 본인 글만 / B: A 의 글 안 보임 |
| 10 | 관리자 | 남의 글 /edit 주소 입력 | **403** (수정은 본인만) |
| 11 | 학생 B / 관리자 | 남의 글 삭제 | B: **403**, 관리자: 삭제 버튼 보임 → 삭제됨 (목록 + "글이 삭제되었습니다.") |

> 글은 DB 에 저장되고 깃에는 코드만 올라간다. 글 쓴 회원을 관리자가 삭제하면 외래 키 때문에 오류 → 10주차에 해결 (지금은 그 회원의 글을 먼저 삭제)

## 7주차 - 회원 관리 REST API(JSON) 완료
- 화면은 그대로 두고 데이터(JSON)만 주고받는 API 추가 (`@RestController` → 객체를 반환하면 JSON 으로 자동 변환)
- [MemberApiController.java](demo/src/main/java/com/example/demo/controller/MemberApiController.java): `GET /api/hello` (누구나), `GET /api/me` (내 정보)
- [MemberResponse.java](demo/src/main/java/com/example/demo/model/dto/MemberResponse.java) (record): 응답 DTO → **비밀번호(해시) 노출 차단**. 엔티티를 그대로 반환하면 getter 가 있는 값(password)이 전부 JSON 으로 나감
- [AdminApiController.java](demo/src/main/java/com/example/demo/controller/AdminApiController.java): 주소는 하나, 할 일은 HTTP 메서드로
- [RoleRequest.java](demo/src/main/java/com/example/demo/model/dto/RoleRequest.java): 권한 변경 요청 `{"role":"ADMIN"}`
- [SecurityConfig.java](demo/src/main/java/com/example/demo/config/SecurityConfig.java): `/api/admin/**` 관리자 전용, `httpBasic` (Postman 은 쿠키가 없어 요청마다 Basic 인증), `/api/**` CSRF 제외, 로그인 안 한 API 요청은 로그인 화면(302) 대신 **401**
- 화면: [admin/members-api.html](demo/src/main/resources/templates/admin/members-api.html) (제공 코드, fetch 로 API 호출 · 새로고침 없음), 회원 관리 화면에 "API 버전으로 보기" 링크
- [Postman 컬렉션](7주차_postman_collection.json): Import 해서 바로 테스트 (`baseUrl`, `memberId` 변수)

| 하고 싶은 일 | 6주차 화면 방식 | 7주차 REST 방식 | 성공 코드 |
|---|---|---|---|
| 회원 목록 | GET /admin/members | GET /api/admin/members | 200 |
| 회원 1명 | - | GET /api/admin/members/{id} | 200 / 없으면 404 |
| 회원 등록 | POST /signup | POST /api/admin/members | 201 + Location |
| 권한 변경 | POST /admin/members/{id}/role | PATCH /api/admin/members/{id}/role | 200 |
| 회원 삭제 | POST /admin/members/{id}/delete | DELETE /api/admin/members/{id} | 204 |

## 7주차 연습문제 - 회원 검색 API & 오류 처리 한 곳에 모으기
1. **회원 검색**: `GET /api/admin/members?keyword=student` → 아이디나 이름에 검색어가 들어간 회원만 (`@RequestParam(required = false)`, `findByUsernameContainingOrNameContaining`)
2. **오류 처리 한 곳에**: 등록·수정·삭제마다 반복되던 try-catch 를 지우고 [ApiExceptionHandler.java](demo/src/main/java/com/example/demo/controller/ApiExceptionHandler.java) (`@RestControllerAdvice`) 한 곳에서 `IllegalArgumentException → 400 {"error": "..."}` 처리 → 지운 뒤에도 상태 코드·메시지 동일
   - 추가: 오류 JSON 에 서버 내부 정보(trace)가 안 나가게 `server.error.include-stacktrace=never`

### API 테스트 결과 (curl / Postman 과 같은 요청)
| # | 요청 | 계정 | 결과 |
|---|---|---|---|
| 1 | GET /api/hello | 없음 | 200 `{"message":"안녕하세요, REST API!", "time":...}` |
| 2 | GET /api/me | 없음 | **401** (로그인 화면 HTML 아님) |
| 3 | GET /api/me | 학생 | 200 `{"id","username","name","role"}` — **password 없음** |
| 4 | GET /api/me | 비밀번호 틀림 | 401 |
| 5 | GET /api/admin/members?keyword=학생 | admin | 200, 이름에 "학생"이 들어간 회원만 |
| 6 | GET /api/admin/members/999999 | admin | 404 |
| 7 | GET /api/admin/members | 학생(USER) | 403 |
| 8 | POST /api/admin/members (`"role":"ADMIN"` 몰래 포함) | admin | 201 + `Location`, role 은 그대로 USER |
| 9 | 같은 아이디 다시 등록 / 아이디만 보내기 | admin | 400 `이미 사용 중인 아이디입니다.` / 400 `아이디, 비밀번호, 이름은 필수입니다.` |
| 10 | PATCH .../role `{"role":"ADMIN"}` | admin | 200 |
| 11 | PATCH .../role `{"role":"SUPER"}` | admin | 400 `허용되지 않는 권한입니다 : SUPER` |
| 12 | PATCH 본인 권한 | admin | 400 `자기 자신의 권한 변경·삭제는 할 수 없습니다.` |
| 13 | DELETE /api/admin/members/{id} | 학생(USER) | 403 |
| 14 | DELETE /api/admin/members/{id} | admin | 204 |
| 15 | 화면 /mypage (비로그인) | 없음 | 302 → 로그인 화면 (화면은 기존 그대로) |

## 실행 방법
1. `demo/src/main/resources/application-secret.properties` 작성 (깃에 안 올라감)
   ```
   spring.datasource.username=root
   spring.datasource.password=내비밀번호
   ```
2. `DemoApplication.java` → `Ctrl + F5` → http://localhost:8080 → 메뉴 "게시판"
3. (7주차 API) Postman → Import → `7주차_postman_collection.json`

## 이전 주차
- [2주차](https://github.com/LSY0820/springboot-week2) · [3주차](https://github.com/LSY0820/springboot-week3) · [4주차](https://github.com/LSY0820/springboot-week4) · [5주차](https://github.com/LSY0820/springboot-week5) · [6주차](https://github.com/LSY0820/springboot-week6) · [7주차](https://github.com/LSY0820/springboot-week7) · [9주차](https://github.com/LSY0820/springboot-week9)
