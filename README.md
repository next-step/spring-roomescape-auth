# 1단계

- [x] 로그인 기능
  - [x] 토큰을 발급
- [x] 내 정보 조회하기
  - [x] 토큰을 이용하여 본인 정보 응답하기
- [x] CORS 처리 설정 추가

### API

* 토큰 발급
```http request
POST /login/token HTTP/1.1
accept: */*
content-type: application/json; charset=UTF-8

{
    "username": "username",
    "password": "password"
}
```
```http request
HTTP/1.1 200 
Content-Type: application/json

{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk4NTEwLCJleHAiOjE2NjMzMDIxMTAsInJvbGUiOiJBRE1JTiJ9.7pxE1cjS51snIrfk21m2Nw0v08HCjgkRD2WSxTK318M"
}
```

* 내 정보조회
```http request
GET /members/me HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk4NTkwLCJleHAiOjE2NjMzMDIxOTAsInJvbGUiOiJBRE1JTiJ9.-OO1QxEpcKhmC34HpmuBhlnwhKdZ39U8q91QkTdH9i0
```
```http request
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

# 2단계 요구사항

- [x] 예약하기, 예약취소 개선
  - [x] 아래의 API 설계에 맞춰 API 스펙을 변경한다.
  - [x] 비로그인 사용자는 예약이 불가능하다.
  - [x] 자신의 예약이 아닌 경우 예약 취소가 불가능하다.
  - [x] HandlerMethodArgumentResolver를 활용한다.

### API
* 예약 생성
```http request
POST /reservations HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk4NTkwLCJleHAiOjE2NjMzMDIxOTAsInJvbGUiOiJBRE1JTiJ9.-OO1QxEpcKhmC34HpmuBhlnwhKdZ39U8q91QkTdH9i0
content-type: application/json; charset=UTF-8
host: localhost:8080

{
    "scheduleId": 1
}
```
```http request
HTTP/1.1 201 Created
Location: /reservations/1
```

* 예약 삭제
```http request
DELETE /reservations/1 HTTP/1.1
authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjYzMjk5MDcwLCJleHAiOjE2NjMzMDI2NzAsInJvbGUiOiJBRE1JTiJ9.zgz7h7lrKLNw4wP9I0W8apQnMUn3WHnmqQ1N2jNqwlQ
```
```http request
HTTP/1.1 204
```
