document.addEventListener('DOMContentLoaded', function () {
  updateUIBasedOnLogin();
});

/**
 * 로그아웃 버튼 클릭
 */
document.getElementById('logout-btn').addEventListener('click', function (event) {
  logout(event);
});

/**
 * 회원가입 버튼 클릭
 */
document.addEventListener('DOMContentLoaded', function () {
  const signUpForm = document.getElementById('signup-form');
  if (signUpForm) {
    signUpForm.addEventListener('submit', signUp);
  }
});

/**
 * 로그인 버튼 클릭
 */
document.addEventListener('DOMContentLoaded', function () {
  const loginForm = document.getElementById('login-form');
  if (loginForm) {
    loginForm.addEventListener('submit', login);
  }
});

/**
 * 회원가입 API 호출
 */
function signUp(event) {
  event.preventDefault();

  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;
  const name = document.getElementById('name').value;
  const role = document.getElementById('role').value;

  if (!email || !password || !name || !role) {
    alert('Please fill in all fields.');
    return;
  }

  const signUpRequestBody = {
    email: email,
    password: password,
    name: name,
    role: role
  };

  fetch('/api/signup', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(signUpRequestBody)
  })
      .then(response => {
        if (!response.ok) {
          alert('Signup request failed');
          throw new Error('Signup request failed');
        }

        console.log('Signup successful:');
        window.location.href = '/login';
      })
      .catch(error => {
        console.error('Error during signup:', error);
      });
}

/**
 * 로그인 API 호출
 */
function login(event) {
  event.preventDefault(); // 로그인 실패 시 email, password 입력란을 비우지 않기 위해 기본 이벤트 방지.

  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;

  // 입력 필드 검증
  if (!email || !password) {
    alert('Please fill in all fields.');
    return; // 필수 입력 필드가 비어있으면 여기서 함수 실행을 중단
  }

  const loginRequestBody = {
    email: email,
    password: password
  };

  fetch('/api/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(loginRequestBody)
  })
      .then(response => {
        return response.json().then(responseBody => {
          if (!response.ok) {
            alert('Login failed');
            throw new Error('Login failed: ' + responseBody);
          }
        })
      })
      .then(loginResponse => {
        return callLoginCheckApi();
      })
      .then(loginCheckResponse => {
        console.log('Login check response:', loginCheckResponse);
        if (loginCheckResponse.data.userRole === 'ADMIN') {
          window.location.href = '/admin/reservation';
          return;
        }
        if (loginCheckResponse.data.userRole === 'CUSTOMER') {
          window.location.href = '/customer/reservation';
          return;
        }
        window.location.href = '/';
      })
      .catch(error => {
        console.warn('Error during login:', error);
      });
}

function logout(event) {
  event.preventDefault();
  fetch('/api/logout', {
    method: 'POST',
    credentials: 'include' // 쿠키를 포함시키기 위해 필요
  })
      .then(response => {
        if (response.status === 200) {
          // 로그아웃 성공, 페이지 새로고침 또는 리다이렉트
          window.location.href = '/login';
        } else {
          // 로그아웃 실패 처리
          console.error('Logout failed');
        }
      })
      .catch(error => {
        console.error('Error:', error);
      });
}


function updateUIBasedOnLogin() {
  fetch('/api/login/check') // 로그인 상태 확인 API 호출
      .then(response => {
        if (response.status !== 200) { // 요청이 실패하거나 로그인 상태가 아닌 경우
          throw new Error('Not logged in or other error');
        }
        return response.json(); // 응답 본문을 JSON으로 파싱
      })
      .then(responseBody => {
        // 응답에서 사용자 이름을 추출하여 UI 업데이트
        document.getElementById('profile-name').textContent = responseBody.data.userName; // 프로필 이름 설정
        document.querySelector('.nav-item.dropdown').style.display = 'block'; // 드롭다운 메뉴 표시
        document.querySelector('.nav-item a[href="/login"]').parentElement.style.display = 'none'; // 로그인 버튼 숨김
      })
      .catch(error => {
        // 에러 처리 또는 로그아웃 상태일 때 UI 업데이트
        console.error('Error:', error);
        document.getElementById('profile-name').textContent = 'Profile'; // 기본 텍스트로 재설정
        document.querySelector('.nav-item.dropdown').style.display = 'none'; // 드롭다운 메뉴 숨김
        document.querySelector('.nav-item a[href="/login"]').parentElement.style.display = 'block'; // 로그인 버튼 표시
      });
}

// 드롭다운 메뉴 토글
document.getElementById("navbarDropdown").addEventListener('click', function (e) {
  e.preventDefault();
  const dropdownMenu = e.target.closest('.nav-item.dropdown').querySelector('.dropdown-menu');
  dropdownMenu.classList.toggle('show'); // Bootstrap 4에서는 data-toggle 사용, Bootstrap 5에서는 JS로 처리
});

function signup() {
  // Redirect to signup page
  window.location.href = '/signup';
}

function base64DecodeUnicode(str) {
  // Base64 디코딩
  const decodedBytes = atob(str);
  // UTF-8 바이트를 문자열로 변환
  const encodedUriComponent = decodedBytes.split('').map(function (c) {
    return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
  }).join('');
  return decodeURIComponent(encodedUriComponent);
}

function callLoginCheckApi() {
  return fetch('/api/login/check')
      .then(response => {
        return response.json().then(data => {
          if (!response.ok) {
            throw new Error('Login check failed: ' + JSON.stringify(data));
          }
          return data;
        });
      });
}
