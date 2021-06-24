
const colors = ["EA698B", "F15152", "EF8354", "F9C74F", "8cb369",
    "43AA8B", "4d908e", "277da1", "4d43ac", "7209b7"];

const pics = ["bear.png", "blackcat.png", "graycat.png", "parrot.png", "rabbit.png", "yellowcat.png"];

const postSize = 10;
let postPage = 1;
let postTotal = 10;

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
            $('#upload-picture-wrap').empty();
            $('#upload-picture-wrap').append('<label for="upload-picture">직접 업로드</label>'
            + '<input type="file" class="upload-picture" id="upload-picture" name="upload-picture" onchange="setUploadImg(); cancelSelection();" onerror="resetImg();">');
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
        document.getElementById('add-diary-title').innerHTML = '새 모두어리 생성';
        document.getElementById('new-diary-title').value = "";
        document.getElementById('form-purpose').value = "create";
        document.getElementById('add-diary-wrap').classList.add('show-modal');
        scrollLock();
    }
    document.getElementById('add-diary-close').onclick = function () {
        document.getElementById('add-diary-wrap').classList.remove('show-modal');
        scrollUnlock();
    }

    //친구 추가 창
    if(document.getElementById('add-friend-open') != null){
        document.getElementById('add-friend-open').onclick = function () {
            document.getElementById('search-result-span').innerHTML = '';
            document.getElementById('search-keyword').value = '';
            document.getElementById('search-result-list').innerHTML = '';
            document.getElementById('search-keyword-error').innerHTML = '';
            document.getElementById('search-keyword-error').style.display = 'none';
            document.getElementById('add-friend-wrap').classList.add('show-modal');
            scrollLock();
        }
    }
    document.getElementById('add-friend-close').onclick = function () {
        document.getElementById('search-keyword-error').innerHTML = '';
        document.getElementById('search-keyword-error').style.display = 'none';
        document.getElementById('add-friend-wrap').classList.remove('show-modal');
        document.getElementById('search-keyword').value = '';
        scrollUnlock();
    }

    //초대장 목록 창
    document.getElementById('show-invitation-open').onclick = function () {
        document.getElementById('show-invitation-wrap').classList.add('show-modal');
        checkInvitations();
        scrollLock();
    }
    document.getElementById('show-invitation-close').onclick = function () {
        document.getElementById('show-invitation-wrap').classList.remove('show-modal');
        location.reload();
        scrollUnlock();
    }

    //설정 창
    document.getElementById('show-info-open').onclick = function () {
        document.getElementById('show-info-wrap').classList.add('show-modal');
        scrollLock();
        $('#new-password-wrap').empty().append('<span>비밀번호</span>' +
            '<button type="button" class="clear-button" id="open-edit-password">변경하기</button>');
        $('#confirm-password-wrap').empty();
        if(document.getElementById('wrong-alert-wrap') != null){
            document.getElementById('wrong-alert-wrap').remove();
        }

        //비밀번호 변경 입력 영역 열기
        document.getElementById('open-edit-password').onclick = function () {
            document.getElementById('open-edit-password').remove();
            $('#new-password-wrap').append('<input type="password" class="input-box info-password" id="new-password" name="new-password">');
            $('#confirm-password-wrap').append('<span>재입력</span>' +
                '<input type="password" class="input-box info-password" id="confirm-password">');
        }
    }

    //설정 창 닫기
    document.getElementById('info-close').onclick = function () {
        document.getElementById('show-info-wrap').classList.remove('show-modal');
        scrollUnlock();
    }

    //모달창 바깥을 클릭하면 닫히는 기능 추가
    Array.from(document.getElementsByClassName('modal-wrap')).forEach((w) => {
        window.addEventListener('click', (e) => {
            e.target === w ? w.classList.remove('show-modal') : false;
            scrollUnlock();
        })
    })

    /* 친구 찾기 키워드 입력창에서 엔터키를 쳐도 검색이 되도록 이벤트 등록*/
    document.getElementById('search-keyword').addEventListener(
        'keyup', function (e) {
            if (e.key === 'Enter') {
                checkSearchKeyword();
            }
    })

    //내 설정 정보 버튼에 수정 가능 기능 추가
    document.getElementById('edit-info-name').onclick = function () {
        document.getElementById('info-name').removeAttribute('readonly');
        document.getElementById('info-name').setAttribute('class', 'input-box info-name');
        document.getElementById('info-name').focus();
    }

    //아이콘 색상 설정
    setHoverColor();

    //포스트 창 다이어리 항목에 맞춰 세로 조정
    let flags = document.getElementsByClassName('diary-flag').length;
    let flagsHeight = (flags * 52 + 20);
    let height = document.getElementById('diary-post-content-wrap').getBoundingClientRect().height;
    if(height < flagsHeight) {
        document.getElementById('diary-post-content-wrap').style.height = (flagsHeight + 'px');
    }

    //창 크기가 바뀔 때 작성중인 포스트의 입력창 크기도 재조정
    $(window).resize(function () {
        resize_postarea(document.getElementById('post_text'), 'new-post-submit');
    })

    //토스터 알림 설정
    toastr.options = {
        "progressBar": true
    }

    scrollTo(0, 0);
}

$(function () {
    if($(window).scrollTop() + $(window).height() == $(document).height()) {
        loadMorePosts();
    }

    $(window).scroll(function () {
        if($(window).scrollTop() + $(window).height() == $(document).height()) {
            loadMorePosts();
        }
    });
});

//다이어리 설정 창 열기
function editDiaryOpen(diaryId) {
    document.getElementById('add-diary-title').innerHTML = '모두어리 정보 수정';
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
function sendReply(postId) {
    let data = {'reply' : document.getElementById('reply-content' + postId).value,
                'postId' : postId};
    $.ajax({
        url: '/post-reply',
        data: data,
        type: 'post',
        success: function(replyList){
            $('#reply-list' + postId).empty();
            $.each(replyList, function (index, reply){
                let replyDiv = $('<div class="post-reply"><div class="reply-picture-wrap">' +
                    '<img class="reply-picture" src="/img/' + reply.picture +'"></div>' +
                    '<div class="reply-content"><span>' + reply.name + '</span> ' +
                    '<pre class="reply-text">' + reply.content +'</pre></div></div>');
                $('#reply-list' + postId).append(replyDiv);
            })
            $('#reply-content' + postId).val('');
            $('#reply-content' + postId).css('height', '30px');
            $('#new-reply-submit' + postId).css('marginTop', '6px');
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
            $('#search-result-list').empty();
            let result = '';

            //검색 결과가 있다면 친구 목록을, 없다면 안내문을 표기
            if(data.length > 0){
                document.getElementById('search-result-span').innerHTML = (data.length + '건이 검색되었어요');
                $.each(data, function (index, member){
                    result = $('<div onclick="sendInvitation(' + member.id + ')" class="friend-found">'
                        + '<img class="friend-picture" src="/img/' + member.picture + '">'
                        + '<span class="friend-name">' + member.name +'</span>'
                        + '<span class="friend-email">' + member.email +'</span></div>');
                    $('#search-result-list').append(result);
                })
            }else{
                $('#search-result-list').append($('<span>검색 결과가 없어요<br/>다른 키워드로 찾아보세요</span>'));
            }
        }
    })
}

//친구찾기 검색란의 유효성 검사, 비어있다면 재입력
function checkSearchKeyword(){
    let keywordError = document.getElementById('search-keyword-error');
    //경고 문구가 있다면 일단 비우기
    keywordError.style.display = 'none';
    keywordError.innerHTML = '';

    if((document.getElementById('search-keyword').value).length == 0){
        keywordError.innerHTML = '검색어를 입력하세요';
        keywordError.style.display = 'inline';
    }else {
        searchMember();
    }
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
                    toastr.info('이미 가입한 친구입니다');
                    break;
                case 'waiting':
                    toastr.warning('이미 초대장을 보낸 친구입니다');
                    break;
                case 'complete':
                    toastr.success('초대장을 전송했습니다');
                    break;
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
            toastr.success('가입했습니다');
            checkInvitations();
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
            toastr.info('가입을 거졀했습니다');
            checkInvitations();
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

//초대장이 있는지 확인하고 없을 시 없다는 문구를 보여주기 위해 클래스 변경
function checkInvitations() {
    if(document.getElementsByClassName('invitation-wrap').length == 1) {
        document.getElementById('no-invitation').classList.remove('invisible');
    }else {
        document.getElementById('no-invitation').classList.add('invisible');
    }
}

//포스트 쓰기 영역 리사이징
function resize_postarea(textarea, button) {
    textarea.style.height = "30px";
    textarea.style.height = (30 + (textarea.scrollHeight - 36)) + "px";
    document.getElementById(button).style.marginTop = (28 + (textarea.scrollHeight - 36)) + "px";
}

//댓글 쓰기 영역 리사이징
function resize_replyarea(textarea, button) {
    textarea.style.height = "30px";
    textarea.style.height = (30 + (textarea.scrollHeight - 36)) + "px";
    document.getElementById(button).style.marginTop = (6 + (textarea.scrollHeight - 36)) + "px";
}

//새 글 쓰기 전송
function sendPost() {
    if(checkBlank(document.getElementById('post_text').value)){
        document.getElementById('new-post-form').submit();
    }else {
        alert("내용을 입력하세요");
    }
}

//공백 입력 확인
function checkBlank(str) {
    if(str.replace(/\s| /gi, "").length == 0) {
        return false;
    }else{
        return true;
    }
}

//페이지네이션을 위한 함수
async function loadMorePosts() {
    let postData = '';
    let replyData = '';
    let diaryId = document.getElementById('diary-id').value;
    let memberPic = document.getElementById('past-picture').value;

    await $.get(`/diary/${diaryId}/load-post?page=${postPage}&size=${postSize}`, function (result){
        postData = result;
    })

    $.each(postData, function (index, post){
        $('#diary-post-content-wrap').append(toPostPage(post));

        $.ajax({
            url: '/load-reply?postId=' + post.id,
            type: 'get',
            success: function (result){
                replyData = result;
                $.each(replyData, function (index, reply){
                    $('#post-' + post.id).append(toReplyPage(reply));
                })
                $('#post-' + post.id).append(addReplyPage(post.id, memberPic));
            }
        })
    })

    postTotal += 5;
    postPage += 1;
    setHoverColor();
}

function toPostPage(post) {
    let result =
        $(
            '<div class="diary-post content-wrap note" id="post-' + post.id + '">'
                + '<div class="post-low post-header">'
                    + '<div class="post-picture-wrap">'
                        + '<img class="post-picture" src="/img/' + post.member_picture + '">'
                    + '</div>'
                    + '<div class="post-header-content">'
                        + '<a>' + post.member_name +'</a>'
                        + '<a>' + post.regdate + '</a>'
                    + '</div>'
                + '</div>'
                + '<div class="post-low">'
                    + '<pre class="post-content">' + post.content + '</pre>'
                + '</div>'
                + '<div class="post-low" id="reply-list' + post.id + '">'
                + '</div>'
            + '</div>')
        ;

    return result;
}

function toReplyPage(reply) {
    let result =
        $(
            '<div class="post-reply">'
                + '<div class="reply-picture-wrap">'
                    + '<img class="reply-picture" src="/img/' + reply.picture + '">'
                + '</div>'
                + '<div class="reply-content">'
                    + '<div class="reply-name-wrap">'
                        + '<span class="reply-name">' + reply.name + '</span>'
                    + '</div>'
                + '<div class="reply-content-wrap">'
                    + '<pre class="reply-text">' + reply.content + '</pre>'
                + '</div>'
                + '</div>'
            + '</div>'
        )

    return result;
}

function addReplyPage(postId, memberPic) {
    let result =
        $(
            '<div class="add-post-reply">'
                + '<div class="post-reply">'
                    + '<div class="reply-picture-wrap">'
                        + '<img class="reply-picture" src="/img/' + memberPic +'">'
                    + '</div>'
                    + '<div class="reply-content add-new-reply">'
                        + '<textarea class="text-input-clear reply reply-text" id="reply-content' + postId + '" '
                            + 'onkeyup="resize_replyarea(this, \'new-reply-submit' + postId + '\');"></textarea>'
                        + '<i class="fas fa-pen custom-icon reply-submit hover-diary-color" id="new-reply-submit' + postId + '"'
                            + 'onclick="sendReply(' + postId + ');"></i>'
                    + '</div>'
                + '</div>'
            + '</div>'
        )

    return result;
}

//아이콘 강조색을 다이어리 색으로 설정
function setHoverColor() {
    let iconList = Array.from(document.getElementsByClassName('hover-diary-color'));
    iconList.forEach(i => {i.addEventListener('mouseover', ()=>{
        i.style.color = document.getElementById('diary-title-wrap').style.backgroundColor;})
    })
    iconList.forEach(i => {i.addEventListener('mouseout', ()=>{
        i.style.color = '#9ba5b1';})
    })
}

//다이어리 이름 빈칸 유효성 검증
function checkTitleBlank() {
    let title = document.getElementById('new-diary-title').value;
    document.getElementById('diary-title-error').style.display = 'none';
    if(title.length < 1){
        document.getElementById('diary-title-error').style.display = 'inline';
    }else{
        return true;
    }
    return false;
}

//모달창 여닫을 시 스크롤 잠그고 풀기
function scrollLock(){
    $('html, body').css({'overflow': 'hidden', 'height': '100%'});
}
function scrollUnlock(){
    $('html, body').css({'overflow': 'auto', 'height': '100%'});
}