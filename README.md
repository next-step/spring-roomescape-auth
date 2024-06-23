# 🚀 예약 기능

## API

### 예약 조회
- Request
```
GET /reservations HTTP/1.1
```
- Response

```json
HTTP/1.1 200
Content-Type: application/json

[
  {
    "id": 1,
    "name": "브라운",
    "date": "2023-01-01",
    "time": "10:00",
    "themeName" : "레벨1 탈출"
  },
  {
    "id": 2,
    "name": "브라운",
    "date": "2023-01-02",
    "time": "11:00",
    "themeName" : "레벨2 탈출"
  }
]
```

### 예약 추가 - 관리자
- Request
```json
POST /admin/reservations HTTP/1.1
content-type: application/json
cookie: token=tokenvalue
host: localhost:8080

{
"date": "2024-03-01",
"themeId": 1,
"timeId": 1,
"memberId": 1
}
```

- Response
```json
HTTP/1.1 201
Content-Type: application/json

{
    "id": 1,
    "name": "브라운",
    "date": "2023-08-05",
    "time": "15:40",
    "themeName" : "레벨3 탈출"
}
```

### 예약 추가 - 회원
- Request
```json
POST /reservations HTTP/1.1
content-type: application/json
cookie: token=tokenvalue

{
    "date": "2023-08-05",
    "timeId": "1",
    "themeId" : "1"
}
```

- Response
```json
HTTP/1.1 200
Content-Type: application/json

{
    "id": 1,
    "name": "브라운",
    "date": "2023-08-05",
    "time": "15:40",
    "themeName" : "레벨3 탈출"
}
```

### 예약 취소
- Request
```
DELETE /reservations/1 HTTP/1.1
```
- Response
```
HTTP/1.1 200
```

### 가능 시간 추가
- request
```json
POST /times HTTP/1.1
content-type: application/json

{
  "startAt": "10:00"
}
```
- response
```json
HTTP/1.1 200
Content-Type: application/json

{
  "id": 1,
  "startAt": "10:00"
}
```
### 시간 조회 API
- request
```
GET /times HTTP/1.1
```
- response
```json
HTTP/1.1 200
Content-Type: application/json

[
  {
    "id": 1,
    "startAt": "10:00"
  }
]
```
### 시간 삭제 API
- request
```
DELETE /times/1 HTTP/1.1
```
- response
```
HTTP/1.1 200
```

### 예약 가능 시간 조회
- Request
```jsonpath
GET /times/available?date={date}&themeId={themeId} HTTP/1.1
```
- Response
```jsonpath
HTTP/1.1 200 
Content-Type: application/json

[
    {
        "id": 1,
        "startAt": "10:00"
    },
    {
        "id": 2,
        "startAt": "12:00"
    },
    {
        "id": 2,
        "startAt": "12:00"
    }
]

```


### 테마 조회 API
- request
```json
GET /themes HTTP/1.1
```

- response
```json
HTTP/1.1 200
Content-Type: application/json

[
  {
    "id": 1,
    "name": "레벨2 탈출",
    "description": "우테코 레벨2를 탈출하는 내용입니다.",
    "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
  }
]
```

### 테마 추가 API
- request
```json
POST /themes HTTP/1.1
content-type: application/json

{
  "name": "레벨2 탈출",
  "description": "우테코 레벨2를 탈출하는 내용입니다.",
  "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```
- response
```json
HTTP/1.1 201
Location: /themes/1
Content-Type: application/json

{
  "id": 1,
  "ame": "레벨2 탈출",
  "description": "우테코 레벨2를 탈출하는 내용입니다.",
  "thumbnail": "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
}
```

### 테마 삭제 API
- request
```
DELETE /themes/1 HTTP/1.1
```
- response
```
HTTP/1.1 204
```

### 회원 조회 API
- request
```json
GET /members HTTP/1.1
```

- response
```json
HTTP/1.1 200
Content-Type: application/json

[
  {
    "id": 1,
    "email" : "test1@test.com",
    "name" : "test1"
  },
  {
    "id": 2,
    "email" : "test2@test.com",
    "name" : "test2"
  }
]
```
