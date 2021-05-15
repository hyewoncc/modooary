
const colors = ["EA698B", "F15152", "EF8354", "F9C74F", "8cb369",
    "43AA8B", "4d908e", "277da1", "4d43ac", "7209b7"];

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
        document.getElementById('color-code').value = c.id})
    })

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
    document.getElementById('add-friend-open').onclick = function () {
        document.getElementById('add-friend-wrap').classList.add('show-modal');
    }

    //초대장 목록 창
    document.getElementById('show-invitation-open').onclick = function () {
        document.getElementById('show-invitation-wrap').classList.add('show-modal');
    }
    document.getElementById('show-invitation-close').onclick = function () {
        document.getElementById('show-invitation-wrap').classList.remove('show-modal');
        location.reload();
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
                let result = $('<div onclick="sendInvitation(' + member.id + ')"><span>' + member.name +'</span> '
                    + '<span>' + member.email +'</span></div><br/>');
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