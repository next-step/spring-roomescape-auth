## 상황 설명

- 전화 예약의 한계를 느껴 온라인 예약을 지원하기로 한다.
- 온라인 예약을 위해 로그인 기능을 제공한다.

## 요구사항

### 기능 요구사항

- [ ] 로그인 기능
  - [ ] 토큰을 발급
- [ ] 내 정보 조회하기
  - [ ] 토큰을 이용하여 본인 정보 응답하기

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
