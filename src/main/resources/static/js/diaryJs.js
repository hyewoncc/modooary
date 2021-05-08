
//모달창 열고 닫기 등록
function modalOn(wrap){
    document.getElementById(wrap).style.display = 'flex';
}

function modalOff(wrap){
    document.getElementById(wrap).style.display = 'none';
}

window.onload = function () {
    document.getElementById('add-diary-open').onclick = function () {
        modalOn('add-diary-wrap');
    }
    document.getElementById('add-diary-close').onclick = function () {
        modalOff('add-diary-wrap');
    }
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