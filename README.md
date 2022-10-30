## 상황 설명

- 전화 예약의 한계를 느껴 온라인 예약을 지원하기로 한다.
- 온라인 예약을 위해 로그인 기능을 제공한다.

## 요구사항

### 기능 요구사항

- [x] 로그인 기능
  - [x] 토큰을 발급
- [x] 내 정보 조회하기
  - [x] 토큰을 이용하여 본인 정보 응답하기

### API 설계

#### 토큰 발급

```
POST /login/token HTTP/1.1
accept: */*
content-type: application/json; charset=UTF-8

{
    "username": "username",
    "password": "password"
}
```

```
HTTP/1.1 200
Content-Type: application/json

{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk4NTEwLCJleHAiOjE2NjMzMDIxMTAsInJvbGUiOiJBRE1JTiJ9.7pxE1cjS51snIrfk21m2Nw0v08HCjgkRD2WSxTK318M"
}
```

#### 내 정보 조회

```
GET /members/me HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk4NTkwLCJleHAiOjE2NjMzMDIxOTAsInJvbGUiOiJBRE1JTiJ9.-OO1QxEpcKhmC34HpmuBhlnwhKdZ39U8q91QkTdH9i0
```

```
HTTP/1.1 200
Content-Type: application/json

{
    "id": 1,
    "username": "username", 
    "password": "password",
    "name": "name",
    "phone": "010-1234-5678",
    "role": "ADMIN"
}
```

---

### 기능 요구사항

- [x] 예약하기, 예약취소 개선
  - [x] 아래의 API 설계에 맞춰 API 스펙을 변경한다.
  - [x] 비로그인 사용자는 예약이 불가능하다.
  - [x] 자신의 예약이 아닌 경우 예약 취소가 불가능하다.

### API 설계

#### 예약 생성

```
POST /reservations HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk4NTkwLCJleHAiOjE2NjMzMDIxOTAsInJvbGUiOiJBRE1JTiJ9.-OO1QxEpcKhmC34HpmuBhlnwhKdZ39U8q91QkTdH9i0
content-type: application/json; charset=UTF-8
host: localhost:8080

{
  "scheduleId": 1
}
```

```
HTTP/1.1 201 Created
Location: /reservations/1
```

#### 예약 삭제

```
DELETE /reservations/1 HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk5MDcwLCJleHAiOjE2NjMzMDI2NzAsInJvbGUiOiJBRE1JTiJ9.zgz7h7lrKLNw4wP9I0W8apQnMUn3WHnmqQ1N2jNqwlQ
```

```
HTTP/1.1 204
``` 

--- 

### 기능 요구사항

- [x] 관리자 역할을 추가한다.
  - [x] 일반 멤버와 관리자 멤버를 구분한다.
- [x] 관리자 기능을 보호한다.
  - [x] 관리자 관련 기능 API는 /admin 붙이고 interceptor로 검증한다.
  - [x] 관리자 관련 기능 API는 authorization 헤더를 이용하여 인증과 인가를 진행한다.

### 프로그래밍 요구사항

- [ ] 관리자를 등록하도록 하기 보다는 애플리케이션이 동작할 때 관리자는 추가될 수 있도록 한다
  - [ ] DataLoader