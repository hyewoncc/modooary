
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