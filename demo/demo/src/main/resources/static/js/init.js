$(document).ready(function () {
    const body = document.querySelector('body');
    const colorButton = document.querySelector('#colorButton');

    let currentBackground = 'body1';

    colorButton.addEventListener('click', () => {
        console.log("버튼 눌림")
        if (currentBackground === 'body1') {
            body.classList.remove('body1');
            body.classList.add('body2');
            currentBackground = 'body2';
        } else {
            body.classList.remove('body2');
            body.classList.add('body1');
            currentBackground = 'body1';
        }
    });
})
