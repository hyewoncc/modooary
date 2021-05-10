
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
    /*
    document.getElementById('add-friend-close').onclick = function () {
        modalOff('add-friend-wrap');
    }
     */

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
        success: function(data){
            let reply = $('<div class="post-reply"><span>' + data.name + '</span> ' +
                '<span>' + data.content +'</span></div>')
            $('#reply-list' + postId).append(reply);
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
        success: function (){
            alert("초대장을 전송했습니다");
            modalOff('add-friend-wrap');
        }
    })
}