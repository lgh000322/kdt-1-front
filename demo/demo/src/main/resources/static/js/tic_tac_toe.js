
$(document).ready(function () {
    //012
    //345
    //678

    $("#comment-submit-btn").click(function () {
        let content = $("#comment").val();
        let gamename = $("title").text();
        let username = $('#memberName').text();

        fetch("/comment/write", {
            method: "POST",
            body: JSON.stringify({ content: content, gamename: gamename }),
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    //댓글 작성 성공시 데이터를 추가해줘야함
                    let newComment = `
                    <div class="comments">
                        <p class="userName">${username}</p>
                        <p class="userComment">${content}</p>
                    </div>
                `;

                    $("#comments-aria").prepend(newComment);
                    $("#comment").val("");
                } else {
                    alert("댓글 작성 중 에러가 발생했습니다.");
                }
            })
            .catch(error => {
                console.error("Error: ", error);
                alert("댓글 작성 중 에러가 발생했습니다.");
            });
    });
});

$(document).ready(function () {
    let text = ''; // 댓글을 담을 텍스트

    let setText = function () {
        text += ' <div class="comments">';
        text += '  <p class="userName">id</p>';
        text += ' <p class="userComment"> comment......</p>';
        text += '</div>';
    };

    let appendComment = function () {
        for (var i = 0; i < 20; i++) {
            $('#comments-aria').append(text);
        }
    };


    $(window).scroll(function () {
        let scrollHeight = $(window).scrollTop() + $(window).height();
        let documentHeight = $(document).height();

        if (scrollHeight >= documentHeight - 100) {
            appendComment();
        }
    });

    setText();
})

$(document).ready(function () {
    //승리 조합
    let win_combination = [
        [0, 1, 2],
        [3, 4, 5],
        [6, 7, 8],
        [0, 3, 6],
        [1, 4, 7],
        [2, 5, 8],
        [0, 4, 8],
        [2, 4, 6]
    ];

    //O, X의 턴을 구분하기위한 불리언값
    var check_turn = true;
    //중복 체크, 슴, 무, 패 확인을 위한 해쉬맵
    let o_check_win = new Map();
    let x_check_win = new Map();
    var cnt = 0;



    // 마우스 올려놓을때 테이블 이미지 변환
    $("td").hover(function () {
        $(this).css("background-color", "wheat");
    }, function () {
        $(this).css("background-color", "white");
    });

    // td를 클릭하면 실행
    $("td").click(function () {

        // td에 값이 있으면 alert후 종료
        if ($(this).html() !== "") {
            alert("체크된 자리입니다. 다른 자리를 골라주세요.");
            return;
        }

        // tr인덱스값
        var trNum = $(this).parent().index();

        // 체크된 td값 확인 0 ~ 2
        var tdNum = $(this).index();




        cnt++;


        //첫시작턴 O턴
        if (check_turn) {


            $(this).html('<img src="../img/O.png" alt="" style="width: 100px; height: 100px;">');



            if (parseInt(trNum) == 1) {
                tdNum = parseInt(tdNum) + 3;
                console.log("tdNum = " + tdNum);
            }

            else if (parseInt(trNum) == 2) {
                tdNum = parseInt(tdNum) + 6;
                console.log("tdNum = " + tdNum);
            }

            // o 승리 확인을 위한 해쉬맵에 밸류로 o저장
            o_check_win.set(tdNum.toString(), "O");

            //승리 확인
            for (i = 0; i < win_combination.length; i++) {
                // 승리 확인을 위한 변수
                var value = "";

                for (j = 0; j < win_combination[i].length; j++) {
                    var key = win_combination[i][j];
                    //가져온 밸류를 합친다
                    var value = value + o_check_win.get(key.toString());


                    // 승리조합확인.
                    if (value === "OOO") {
                        $("#modal_txt").html("O's Win!!!");
                        $(".modal").css("display", "block");
                        $(".close").click(() => {
                            $(".modal").css("display", "none");
                        });
                        return;
                    }
                }

            }


            // 만약 위 포문에 걸리지 않고 cnt == 9가 되면 draw
            if (cnt === 9) {
                console.log("cnt = " + cnt);
                $("#modal_txt").html("Draw!!!");
                $(".modal").css("display", "block");
                $(".close").click(() => {
                    $(".modal").css("display", "none");
                });
                return;
            }



            $("#turn").text("X's Turn!");
            check_turn = false;




        }
        //X턴
        else {
            $(this).html('<img src="../img/X.png" alt="" style="width: 100px; height: 100px;">');

            //1행일경우 td에  + 3을해줌
            if (parseInt(trNum) == 1) {
                tdNum = parseInt(tdNum) + 3;
                console.log("tdNum = " + tdNum);
            }

            //2행일경우 td에 +6을 해줌
            else if (parseInt(trNum) == 2) {
                tdNum = parseInt(tdNum) + 6;
                console.log("tdNum = " + tdNum);
            }

            // o 승리 확인을 위한 해쉬맵에 밸류로 o저장
            x_check_win.set(tdNum.toString(), "X");

            //승리 확인
            for (i = 0; i < win_combination.length; i++) {
                // 승리 확인을 위한 변수
                var value = "";

                for (j = 0; j < win_combination[i].length; j++) {
                    var key = win_combination[i][j];
                    //가져온 밸류를 합친다
                    var value = value + x_check_win.get(key.toString());


                    // 승리조합확인.
                    if (value === "XXX") {
                        $("#modal_txt").html("X's Win!!!");
                        $(".modal").css("display", "block");
                        $(".close").click(() => {
                            $(".modal").css("display", "none");
                        });
                        return;
                    }
                }

            }


            //무승부 시 모달창 띄움
            if (cnt === 9) {
                console.log("cnt = " + cnt);
                $("#modal_txt").html("Draw!!!");
                $(".modal").css("display", "block");
                $(".close").click(() => {
                    $(".modal").css("display", "none");
                });
                return;
            }



            $("#turn").text("O's Turn!");


            check_turn = true;
        }
    });


    $("#sel-option > button").click(() => {
        //모든값 초기화

        $("#turn").html("O's Turn!");
        //O, X의 턴을 구분하기위한 불리언값
        check_turn = true;
        //중복 체크, 슴, 무, 패 확인을 위한 해쉬맵
        o_check_win = new Map();
        x_check_win = new Map();
        cnt = 0;
        $("td").html("");
    });
})





