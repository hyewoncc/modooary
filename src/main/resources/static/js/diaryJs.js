
const colors = ["EA698B", "F15152", "EF8354", "F9C74F", "8cb369",
    "43AA8B", "4d908e", "277da1", "4d43ac", "7209b7"];

const pics = ["bear.png", "blackcat.png", "graycat.png", "parrot.png", "rabbit.png", "yellowcat.png"];

//모달창 열고 닫기 등록
window.onload = function () {

    //컬러파레트 생성
    for(let color of colors) {
        $('#color-palette').append('<div class="color-chip" style=background-color:#' + color + ';' +
            ' id="' + color +'"></div>');
    }
    //컬러칩 클릭 시 선택되는 기능 추가
    let colorChips = Array.from(document.getElementsByClassName("color-chip"));
    colorChips.forEach((c) => { c.addEventListener('click', ()=>{
        colorChips.forEach((cc) => {cc.classList.remove('color-selected')})
        c.classList.add('color-selected');
        document.getElementById('new-diary-title').style.backgroundColor = c.style.backgroundColor;
        document.getElementById('color-code').value = c.id;})
    })

    //사진 수정 클릭 시 기본 사진 목록 생성
    document.getElementById('edit-picture-open').onclick = function () {
        if(document.getElementById('edit-picture-open').getAttribute('class') == ''){
            for(let pic of pics) {
                $('#sample-pics').append('<div class="picture-chip" id="'+ pic +'">' +
                    '<img class="picture-chip-img" src="/img/' + pic + '"></div>');
            }
            $('#upload-picture-wrap').append('<label for="upload-picture">직접 업로드</label>'
            + '<input type="file" class="upload-picture" id="upload-picture" onchange="setUploadImg(); cancelSelection();" onerror="resetImg();">');
            document.getElementById('edit-picture-open').setAttribute('class', 'opened');

            //기본 사진 클릭 시 선택되는 기능 추가
            let basicPics = Array.from(document.getElementsByClassName('picture-chip'));
            basicPics.forEach((p) => { p.addEventListener('click',  ()=>{
                basicPics.forEach((pp) => {pp.classList.remove('pic-selected')})
                p.classList.add('pic-selected');
                setBasicImg(p.id);
            })})
        }
    }


    //다이어리 추가 창
    document.getElementById('add-diary-open').onclick = function () {
        //추가 창 로드 시 컬러칩 하나 랜덤 선택
        colorChips[Math.floor(Math.random() * 10)].click();
        document.getElementById('add-diary-title').innerHTML = '새 다이어리 생성';
        document.getElementById('new-diary-title').value = "";
        document.getElementById('form-purpose').value = "create";
        document.getElementById('add-diary-wrap').classList.add('show-modal');
    }
    document.getElementById('add-diary-close').onclick = function () {
        document.getElementById('add-diary-wrap').classList.remove('show-modal');
    }

    //친구 추가 창
    if(document.getElementById('add-friend-open') != null){
        document.getElementById('add-friend-open').onclick = function () {
            document.getElementById('add-friend-wrap').classList.add('show-modal');
        }
    }

    //초대장 목록 창
    document.getElementById('show-invitation-open').onclick = function () {
        document.getElementById('show-invitation-wrap').classList.add('show-modal');
    }
    document.getElementById('show-invitation-close').onclick = function () {
        document.getElementById('show-invitation-wrap').classList.remove('show-modal');
        location.reload();
    }

    //설정 창
    document.getElementById('show-info-open').onclick = function () {
        document.getElementById('show-info-wrap').classList.add('show-modal');
        $('#new-password-wrap').empty().append('<span>비밀번호</span>' +
            '<button type="button" class="clear-button" id="open-edit-password">변경하기</button>');
        $('#confirm-password-wrap').empty();
        if(document.getElementById('wrong-alert-wrap') != null){
            document.getElementById('wrong-alert-wrap').remove();
        }

        //비밀번호 변경 입력 영역 열기
        document.getElementById('open-edit-password').onclick = function () {
            document.getElementById('open-edit-password').remove();
            $('#new-password-wrap').append('<input type="password" class="input-box info-password" id="new-password">');
            $('#confirm-password-wrap').append('<span>재입력</span>' +
                '<input type="password" class="input-box info-password" id="confirm-password">');
        }
    }

    //모달창 바깥을 클릭하면 닫히는 기능 추가
    Array.from(document.getElementsByClassName('modal-wrap')).forEach((w) => {
        window.addEventListener('click', (e) => {
            e.target === w ? w.classList.remove('show-modal') : false;
        })
    })

    /* 친구 찾기 키워드 입력창에서 엔터키를 쳐도 검색이 되도록 이벤트 등록*/
    document.getElementById('search-keyword').addEventListener(
        'keyup', function (e) {
            if (e.key === 'Enter') {
                searchMember();
            }
    })

    //내 설정 정보 버튼에 수정 가능 기능 추가
    document.getElementById('edit-info-name').onclick = function () {
        document.getElementById('info-name').removeAttribute('readonly');
        document.getElementById('info-name').setAttribute('class', 'input-box info-name');
        document.getElementById('info-name').focus();
    }

}

//다이어리 설정 창 열기
function editDiaryOpen(diaryId) {
    document.getElementById('add-diary-title').innerHTML = '다이어리 정보 수정';
    document.getElementById('new-diary-title').value =
        document.getElementById('diary-title').text;
    document.getElementById('form-purpose').value = "edit";
    
    //설정 창 로드 시 현재 컬러칩 선택
    let colorChips = Array.from(document.getElementsByClassName("color-chip"));
    for(let color of colorChips) {
        if(color.id == document.getElementById('diary-color' + diaryId).value){
            color.click();
        }
    }
    document.getElementById('add-diary-wrap').classList.add('show-modal');
}


//댓글 ajax 통신
function registerReply(postId) {
    let data = {'reply' : document.getElementById('reply-content' + postId).value,
                'postId' : postId};
    $.ajax({
        url: '/reply',
        data: data,
        type: 'post',
        success: function(replyList){
            $('#reply-list' + postId).empty();
            $.each(replyList, function (index, reply){
                let replyDiv = $('<div class="post-reply"><div class="reply-picture-wrap">' +
                    '<img class="reply-picture" src="/img/' + reply.picture +'"></div>' +
                    '<div class="reply-content"><span>' + reply.name + '</span> ' +
                    '<span>' + reply.content +'</span></div></div>');
                $('#reply-list' + postId).append(replyDiv);
            })
            $('#reply-content' + postId).val('');
        }
    })
}

//친구 찾기 ajax 통신
function searchMember() {
    let keyword = {'keyword' : document.getElementById('search-keyword').value };

    $.ajax({
        url: '/search',
        data: keyword,
        type: 'get',
        success: function(data){
            $('#result-list').empty();
            $.each(data, function (index, member){
                let result = $('<div onclick="sendInvitation(' + member.id + ')" class="friend-found">'
                    + '<img class="friend-picture" src="/img/' + member.picture + '">'
                    + '<span class="friend-name">' + member.name +'</span>'
                    + '<span class="friend-email">' + member.email +'</span></div>');
                $('#result-list').append(result);
            })
        }
    })
}

//친구에게 초대장 보내기 ajax 통신
function sendInvitation(memberId) {
    $.ajax({
        url: '/send-invitation',
        data: {'memberId' : memberId},
        type: 'post',
        success: function (result){
            switch (result) {
                case 'already':
                    alert('이미 가입한 친구입니다'); break;
                case 'waiting':
                    alert('이미 초대장을 보낸 친구입니다'); break;
                case 'complete':
                    alert('초대장을 전송했습니다'); break;
                default: break;
            }
        }
    })
}

//초대장 수락하기 ajax 통신
function acceptInvitation(invitationId) {
    $.ajax({
        url: '/accept-invitation',
        data: {'invitationId': invitationId},
        type: 'post',
        success: function (){
            $('#invitation-' + invitationId).remove();
        }
    })
}

//초대장 거절하기 ajax 통신
function rejectInvitation(invitationId) {
    $.ajax({
        url: '/reject-invitation',
        data: {'invitationId': invitationId},
        type: 'post',
        success: function (){
            $('#invitation-' + invitationId).remove();
        }
    })
}

//사진 업로드 시 프로필 이미지에 바로 반영
function setUploadImg() {
    let reader = new FileReader();
    reader.onload = e => {
        let img = document.getElementById('info-picture');
        img.src = e.target.result;
    }
    try{
        reader.readAsDataURL(event.target.files[0]);
    }catch{
        resetImg();
    }
}

function resetImg(){
    document.getElementById('info-picture')
        .setAttribute('src', '/img/' + document.getElementById('past-picture').value);
}

function setBasicImg(img) {
    document.getElementById('info-picture')
        .setAttribute('src', '/img/' + img);
    document.getElementById('past-picture').value = img;
}

function cancelSelection() {
    let selection = document.getElementsByClassName('pic-selected');
    for(let s of selection){
        s.classList.remove('pic-selected');
    }
}

//개인정보 창 입력값 유효성 검사
function checkInfoForm() {
    if(document.getElementById('wrong-alert-wrap') != null){
        document.getElementById('wrong-alert-wrap').remove();
    }
    let newPassword = document.getElementById('new-password').value;
    let confirmPassword = document.getElementById('confirm-password').value;
    if(newPassword != confirmPassword){
        document.getElementById('confirm-password').setAttribute('class', 'input-box info-password wrong');
        $('#confirm-password').focus();
        $('#info-password-wrap').append('<div id="wrong-alert-wrap"><span class="wrong-alert">비밀번호가 일치하지 않습니다</span></div>');
        return false;
    }
    return true;
}