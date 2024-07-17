$(document).ready(function () {
    let text = ''; // Initialize text as an empty string

    let setText = function () {
        for (let i = 0; i < 20; i++) {
            text += '<div class="col-sm-3">';
            text += '<div class="card">';

            text += '<a class="card-img-top" href="http://www.naver.com" style="text-decoration: none; color: black;">';
            text += '<div class="image-container">';
            text += ` <img src="https://picsum.photos/200?random=${Math.random()}" class="card-img-top justify-content-center">`;
            text += ' <div class="overlay">게임하러가기</div>';
            text += '</div>';
            text += ' <div class="card-body">';
            text += '<h5>준비중 입니다..</h5>';
            text += '</div>';
            text += '</a>';
            text += '</div>';
            text += '</div>';
        }
    };

    let appendDocument = function () {
        $('.row').append(text);
        text = ''; // 텍스트를 비워줘야 다음 추가 시 중복되지 않습니다.
    };

    $(window).scroll(function () {
        let scrollHeight = $(window).scrollTop() + $(window).height();
        let documentHeight = $(document).height();

        if (scrollHeight >= documentHeight - 100) {
            setText(); // 스크롤 시 새로운 텍스트 생성
            appendDocument();
        }
    });

    setText();
    appendDocument(); // 초기 카드 로드
});