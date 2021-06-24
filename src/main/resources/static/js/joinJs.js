
//임시 비밀번호 설정 모달창 열고 닫기
window.onload = function () {

    if(document.getElementById('reset-password-open') != null){
        document.getElementById('reset-password-open').onclick = function () {
            resetPasswordOpen();
        }
        //임시 비밀번호 발송 취소 누르면 닫히기
        document.getElementById('button-reset-password-cancel').onclick = function () {
            document.getElementById('reset-password-wrap').classList.remove('show-modal');
            document.getElementById('reset-password-email').value = '';
            eraseErrors();
        }
    }

    //모달창 바깥을 클릭하면 닫히는 기능 추가
    Array.from(document.getElementsByClassName('modal-wrap')).forEach((w) => {
        window.addEventListener('click', (e) => {
            e.target === w ? w.classList.remove('show-modal') : false;
        })
    })


    //토스터 알림 설정
    toastr.options = {
        "progressBar": true,

    }

    if(document.getElementById('email-alert') != null) {
        if(document.getElementById('email-alert').value == 1){
            toastr.success('메일 링크로 가입을 완료하세요', '메일 발신 성공');
        }else if(document.getElementById('email-alert').value == -1){
            toastr.error('문제가 생겼어요, 다시 시도해보세요', '메일 발신 실패');
        }else{
        }
    }

    if(document.getElementById('password-alert') != null) {
        if(document.getElementById('password-alert').value == 'success'){
            toastr.success('이메일로 새 비밀번호를 보냈어요', '메일 발신 성공');
        }else if(document.getElementById('password-alert').value == 'fail'){
            toastr.error('문제가 생겼어요, 다시 시도해보세요', '메일 발신 실패');
        }else{
        }
    }
}

//임시 비밀번호 설정 창 열기
function resetPasswordOpen() {
    document.getElementById('reset-password-wrap').classList.add('show-modal');
}

//회원가입 폼의 유효성 검증
function checkJoinForm() {

    //이전 검증 후 표시된 에러 문구를 일괄 삭제
    eraseErrors();
    let result = true;

    //사용 가능한 메일인지 검증
    if(!checkEmailUsable(document.getElementById('email').value)){
        result = false;
    }

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

    if(!checkEmail('email-error',document.getElementById('email').value)){
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

    if(result){
        toastr.info('가입 메일을 전송하고 있어요');
    }else{
        toastr.warning('입력 양식을 확인해주세요');
    }

    return result;
}

//로그인 입력값 유효성 검증
function checkLoginForm() {

    //이전 검증 후 표시된 에러 문구를 일괄 삭제
    eraseErrors();
    let result = true;

    //이메일 형식 확인
    if(!checkEmail('email-error', document.getElementById('email').value)){
        result = false;
    }

    //이메일 칸이 비었는지 확인
    if(!checkBlank('email', '이메일을')){
        result = false;
    }

    if(!checkBlank('password', '비밀번호를')) {
        result = false;
    }

    return result;
}

//비밀번호 찾기 유효성 검증
function checkResetForm() {

    //이전 검증 후 표시된 에러 문구 일괄 삭제
    eraseErrors();
    let result = true;

    //가입된 이메일인지 확인
    if(!checkEmailMember(document.getElementById('reset-password-email').value)){
        result = false;
    }

    //이메일 형식 홗인
    if(!checkEmail('reset-password-email-error', document.getElementById('reset-password-email').value)){
        result = false;
    }

    //이메일 칸이 비었는지 확인
    if(!checkBlank('reset-password-email', '이메일을')){
        result = false;
    }

    if(result){
        toastr.info('메일을 전송하고 있어요');
    }else{
        toastr.warning('이메일 주소를 확인하세요');
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
function checkEmail(errorSpan, email) {
    let emailRegExp = /^[A-Za-z0-9_]+[A-Za-z0-9]*[@][A-Za-z0-9]+[A-Za-z0-9]*[.][A-Za-z]{1,3}$/;
    if(!emailRegExp.test(email)) {
        document.getElementById(errorSpan).innerHTML = '올바른 메일 주소를 입력하세요';
        document.getElementById(errorSpan).style.display = 'inline';
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

//사용 가능한 메일인지 확인
//사용 가능한 메일이면 true, 아니면 false 반환
function checkEmailUsable(email) {
    let data = {'email' : email};
    let checkResult = false;

    $.ajax({
        url: '/sign-in/check-email',
        data: data,
        type: 'post',
        async: false,
        success: function (result){
            if(result){
                checkResult = true;
            }else{
                document.getElementById('email-error').innerHTML = '이미 가입된 메일입니다';
                document.getElementById('email-error').style.display = 'inline';
            }
        }
    })

    return checkResult;
}

//가입된 메일인지 확인
//가입한 메일이면 true, 아니면 false 반환
function checkEmailMember(email) {
    let data = {'email' : email};
    let checkResult = false;

    $.ajax({
        url: '/reset-password/check-email',
        data: data,
        type: 'post',
        async: false,
        success: function (result){
            if(result){
                checkResult = true;
            }else{
                document.getElementById('reset-password-email-error').innerHTML = '아직 가입하지 않은 메일입니다';
                document.getElementById('reset-password-email-error').style.display = 'inline';
            }
        }
    })

    return checkResult;
}

//표기했던 에러 문구들을 일괄 삭제
function eraseErrors(){
    //이전 검증 후 표시된 에러 문구를 일괄 삭제
    let errorSpans = Array.from(document.getElementsByClassName('error-span'));
    for (let i in errorSpans) {
        errorSpans[i].innerHTML = '';
        errorSpans[i].style.display = 'none';
    }
}