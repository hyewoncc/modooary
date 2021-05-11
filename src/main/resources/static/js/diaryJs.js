
//모달창 열고 닫기 등록
function modalOn(wrap){
    document.getElementById(wrap).style.display = 'flex';
}

function modalOff(wrap){
    document.getElementById(wrap).style.display = 'none';
}

window.onload = function () {
    /* 모달 창에 열고 닫기 등록 */
    document.getElementById('add-diary-open').onclick = function () {
        modalOn('add-diary-wrap');
    }
    document.getElementById('add-diary-close').onclick = function () {
        modalOff('add-diary-wrap');
    }
    document.getElementById('add-friend-open').onclick = function () {
        modalOn('add-friend-wrap');
    }
    document.getElementById('show-invitation-open').onclick = function () {
        modalOn('show-invitation-wrap');
    }
    Array.from(document.getElementsByClassName('modal-wrap')).forEach((w) => {
        w.onclick = function () {
            modalOff(w.getAttribute('id'));
        }
    })
    document.getElementById('show-invitation-close').onclick = function () {
        modalOff('show-invitation-wrap');
        location.reload();
    }


    /* 친구 찾기 키워드 입력창에서 엔터키를 쳐도 검색이 되도록 이벤트 등록*/
    document.getElementById('search-keyword').addEventListener(
        'keyup', function (e) {
            if (e.key === 'Enter') {
                searchMember();
            }
    })
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