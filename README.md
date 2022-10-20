## 기능구현목록
### 1단계 - 로그인
- [x] 로그인 기능
    - [x] 토큰 발급
    - [x] 아이디/비밀번호 일치하지 않을 경우 예외처리
- [x] 내 정보 조회
    - [x] 토큰을 이용하여 본인정보 조회

### 2단계 - 로그인 리팩터링
- [x] 예약생성 개선
    - [x] 이름 대신 로그인정보로 예약하도록 리팩터링
    - [x] 비로그인 사용자는 예약 불가능
- [x] 예약삭제 개선
    - [x] 본인이 한 예약만 취소 가능하도록 예외처리
- [x] 예외처리
    - [x] 회원가입 시, 중복 username 검증
    - [x] 예약삭제 시, 존재하지 않는 예약 예외처리

### 3단계 - 관리자 기능 보호
- [ ] 관리자 역할 추가
  - [ ] Member에 Role 속성으로 회원과 관리자 구분
- [ ] 관리자 기능 보호
  - [ ] 관리자 관련 기능은 URI에 /admin을 붙이고 interceptor로 검증
    - [ ] 테마 추가/삭제
    - [ ] 스케줄 추가/삭제
- [ ] 어플리케이션이 동작할 때 관리자가 추가될 수 있도록 구현 (DataLoader)

### 4단계 - 플레이 이력
