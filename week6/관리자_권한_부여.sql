-- 6주차: 회원가입으로 admin 계정을 만든 다음 실행 (SQL INSERT 로 만들면 비밀번호가 BCrypt 가 아니라 로그인 불가)
USE spring;
SELECT id, username, name, role FROM member;
UPDATE member SET role = 'ADMIN' WHERE username = 'admin';
