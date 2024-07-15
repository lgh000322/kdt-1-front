$(document).ready(function () {
    let text = ''; // Initialize text as an empty string

    let setText = function () {
        text += '<div class="col-sm-3">';
        text += '<div class="card">';
        //text += '<div class="card-body">';
        text += '<a class="card-img-top" href="http://www.naver.com" style="text-decoration: none; color: black;">';
        text += '<div class="image-container">';
        text += ' <img src="prod1.jpg" class="card-img-top justify-content-center">';
        text += ' <div class="overlay">게임하러가기</div>';
        text += '</div>';
        text += ' <div class="card-body">';
        text += '<h5>준비중 입니다..</h5>';
        text += '</div>';
        text += '</a>';
        text += '</div>';
        text += '</div>';
    };

    let apppendDocument = function () {
        for (var i = 0; i < 20; i++) {
            $('.row').append(text);
        }
    };

    $(window).scroll(function () {
        let scrollHeight = $(window).scrollTop() + $(window).height();
        let documentHeight = $(document).height();

        if (scrollHeight >= documentHeight - 100) {
            apppendDocument();
        }
    });

    setText(); 
});