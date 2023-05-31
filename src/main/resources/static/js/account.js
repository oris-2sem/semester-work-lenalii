function parseJwt(token) {
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}


function login(email, password) {

    fetch("/login", {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            email: email,
            password: password
        })
    }).then(response => {

        let status = response.status;

        let data = response.json()

        if (status === 200) {

            data.then((promise) => {

                localStorage.setItem('accessToken', promise.accessToken)
                localStorage.setItem('refreshToken', promise.refreshToken)

                let role = parseJwt(promise.accessToken).role;
                let id = parseJwt(promise.accessToken).sub;

                setTimeout(refreshAccessToken, 70000)

                if (role === 'ROLE_DEVELOPER') {

                    window.location.replace("http://localhost:8080/developer");
                } else if (role === 'ROLE_HR') {
                    window.location.replace("http://localhost:8080/hr/account");
                } else if (role === 'ROLE_MODERATOR') {
                    window.location.replace("http://localhost:8080/company/all");
                } else if (role === 'ROLE_ADMIN') {
                    window.location.replace("http://localhost:8080/vacancy");
                }

            })
        } else {

            document.getElementById('login-error').style.display = 'block'

        }
    }).catch(error => {
        console.error('Registration error:', error);
    });
}


function refreshAccessToken() {

    let expirationTime = parseJwt(localStorage.getItem('accessToken')).exp;
    let s = Date.now().toString().slice(0, 10);

    if (expirationTime <= s) {
        console.log('пытается обновить токены')

        console.log("Старый токен ", localStorage.getItem("refreshToken"))
        fetch("/token/refresh", {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + localStorage.getItem('refreshToken')
            }
        })
            .then(response => response.json())
            .then(response => {

                localStorage.setItem('accessToken', response.accessToken)
                localStorage.setItem('refreshToken', response.refreshToken)
                setTimeout(refreshAccessToken, 30000)
            })

    } else {
        setTimeout(refreshAccessToken, 50000)
    }
}

function logout() {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");

    fetch("/logout", {
        method: "GET",
    }).then(e => {
        window.location.replace("/vacancy")
    }).catch(e => console.log("error::", e))
}

document.addEventListener('DOMContentLoaded', function () {

    let token = localStorage.getItem("accessToken");

    if (token !== null) {
        setTimeout(refreshAccessToken, 10000);
        document.getElementById('login-link').style.display = 'none'
        document.getElementById('logout-link').style.display = 'block'

        let parsedToken = parseJwt(token);
        let role = parsedToken.role;

        document.getElementById('browse').style.display = 'block'
        if (role === 'ROLE_ADMIN') {

            let element = document.getElementById('all-accounts');
            element.style.display = 'block'
            let allHrsLink = document.getElementById("all-hrs");
            allHrsLink.style.display = 'block'
        }
        if (role === 'ROLE_HR') {
            let element = document.getElementById('vacancy-replies');
            element.style.display = 'block'

            let myPage = document.getElementById('my-page');

            myPage.style.display = 'block'
            myPage.setAttribute('href', '/hr/account')

            let allDevelopers = document.getElementById('all-developers');
            allDevelopers.style.display = 'block'
        }
        if (role === 'ROLE_DEVELOPER') {
            let element = document.getElementById('my-replies');
            element.style.display = 'block'
            let myPage = document.getElementById('my-page');
            myPage.style.display = 'block'
            myPage.setAttribute('href', '/developer')
        }

    } else {
        document.getElementById('login-link').style.display = 'block'
        document.getElementById('logout-link').style.display = 'none'
    }
});