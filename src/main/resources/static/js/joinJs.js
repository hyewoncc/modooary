
//회원가입 폼의 유효성 검증
function checkJoinForm() {

    //이전 검증 후 표시된 에러 문구를 일괄 삭제
    let errorSpans = Array.from(document.getElementsByClassName('error-span'));
    for (let i in errorSpans) {
        errorSpans[i].innerHTML = '';
        errorSpans[i].style.display = 'none';
    }
    let result = true;

    //입력값들의 유효성 검증
    if(!checkName(document.getElementById('name').value)){
        result = false;
    }

    if(!checkPassword(document.getElementById('password').value)) {
        result = false;
    }

    if(!checkPasswordCheck(document.getElementById('password').value,
        document.getElementById('password-check').value)) {
        result = false;
    }

    if(!checkEmail(document.getElementById('email').value)){
        result = false;
    }

    //입력 공란이 있는지 검증
    if(!checkBlank('name', '닉네임을')){
        result = false;
    }

    if(!checkBlank('email', '이메일을')){
        result = false;
    }

    if(!checkBlank('password', '비밀번호를')) {
        result = false;
    }

    if(!checkBlank('password-check', '비밀번호를 다시')){
        result = false;
    }

    return result;
}

//해당 칸이 비어있는지 확인
function checkBlank(id, name) {
    let str = document.getElementById(id).value;
    if(str.length == 0){
        document.getElementById(id + '-error').innerHTML = name + ' 입력하세요';
        document.getElementById(id + '-error').style.display = 'inline';
        return false;
    }
    return true;
}

//정규식으로 이메일 확인
function checkEmail(email) {
    let emailRegExp = /^[A-Za-z0-9_]+[A-Za-z0-9]*[@][A-Za-z0-9]+[A-Za-z0-9]*[.][A-Za-z]{1,3}$/;
    if(!emailRegExp.test(email)) {
        document.getElementById('email-error').innerHTML = '올바른 메일 주소를 입력하세요';
        document.getElementById('email-error').style.display = 'inline';
        return false;
    }
    return true;
}

//비밀번호의 길이, 형식 확인
function checkPassword(password) {
    let errorSpan = document.getElementById('password-error');

    //비밀번호 길이 확인
    if(password.length < 4) {
        errorSpan.innerHTML = '최소 4자리를 입력하세요';
        errorSpan.style.display = 'inline';
        return false;
    }
    if(password.length > 13) {
        errorSpan.innerHTML = '최대 12자리까지 입력하세요';
        errorSpan.style.display = 'inline';
        return false;
    }

    //비밀번호에 영문과 숫자만 있는지 확인
    let passwordRegExp = /^[a-zA-z0-9]/;
    if(!passwordRegExp.test(password)) {
        errorSpan.innerHTML = '영문,숫자만 사용 가능해요';
        errorSpan.style.display = 'inline';
        return false;
    }
    return true;
}

//닉네임 길이 확인
function checkName(name) {
    let errorSpan = document.getElementById('name-error');

    if(name.length > 16) {
        errorSpan.innerHTML = '이름은 최대 15자입니다';
        errorSpan.style.display = 'inline';
        return false;
    }
    return true;
}

//비밀번호 일치 확인
function checkPasswordCheck(password, passwordCheck) {
    let errorSpan = document.getElementById('password-check-error');
    if (!(password == passwordCheck)) {
        errorSpan.innerHTML = '비밀번호가 일치하지 않습니다';
        errorSpan.style.display = 'inline';
        return false;
    }
    return true;
}
