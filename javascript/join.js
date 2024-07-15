var isIdValid = false;
var isNameValid = false;

//아이디 중복검사
function checkDuplicateById() {
    var idValue = document.getElementById("id").value;

    fetch("/member/idValidation", {
        method: "POST",
        body: JSON.stringify({ id: idValue }),
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
    })
        .then(response => response.json())
        .then(data => {
            if (!data.isValid) {
                alert("사용 가능한 아이디입니다.");
                isIdValid = true;
            } else {
                alert("이미 사용중인 아이디입니다.");
                isIdValid = false;
            }
            checkRegistrationButtonStatus();
        })
        .catch(error => {
            console.error("Error:", error);
        });
}

//닉네임 중복검사
function checkDuplicateByName() {
    var nameValue = document.getElementById("name").value;

    fetch("/member/nameValidation", {
        method: "POST",
        body: JSON.stringify({ name: nameValue }),
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
    })
        .then(response => response.json())
        .then(data => {
            if (!data.isValid) {
                alert("사용 가능한 닉네임입니다.");
                isNameValid = true;
            } else {
                alert("이미 사용중인 닉네임입니다.");
                isNameValid = false;
            }
            checkRegistrationButtonStatus();
        })
        .catch(error => {
            console.error("Error:", error);
        });
}

//회원추가 버튼 활성화
function checkRegistrationButtonStatus() {
    var registrationButton = document.getElementById("registrationButton");

    // ID와 닉네임이 모두 유효한 경우에만 등록 버튼을 활성화
    if (isIdValid && isNameValid) {
        registrationButton.disabled = false;
    } else {
        registrationButton.disabled = true;
    }
}