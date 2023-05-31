function blockAccount(accountId) {

    let requestBody = {
        id: accountId
    };
    fetch('/account/block', {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Error: ' + response.status);
            }
        })
        .then(data => {

            let statusValue = document.getElementById('statusValue');
            statusValue.textContent = data.status


            let blockAccount = document.getElementById('block-account');
            blockAccount.style.display = 'none'
            let unblockAccount = document.getElementById('unblock-account');
            unblockAccount.style.display = 'block'


        }).catch(error => {

        openErrorBlock("Пользователь уже заблокирован!")
    })
}

function unblockAccount(accountId) {

    let requestBody = {
        id: accountId
    };
    console.log(accountId)
    fetch('/account/unblock', {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Error: ' + response.status);
            }
        })
        .then(data => {


            console.log('Account blocked:', data);
            let statusValue = document.getElementById('statusValue');
            statusValue.textContent = data.status

            let unblockAccount = document.getElementById('unblock-account');
            unblockAccount.style.display = 'none'
            let blockAccount = document.getElementById('block-account');
            blockAccount.style.display = 'block'

        })
        .catch(error => {

            openErrorBlock("Пользователь не заблокирован!")
        })
}

function searchAccounts(inputFindAccount) {

    let developerPage = document.getElementById('developers-all');
    developerPage.innerHTML = ''
    fetch('/account/search?query=' + inputFindAccount, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },

    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Error: ' + response.status);
            }
        })
        .then(data => {

            const container = document.getElementById('accounts');
            container.innerHTML = ''
            for (var i = 0; i < data.content.length; i++) {

                const template = document.getElementById('template-account');

                const account = template.content.cloneNode(true);

                account.querySelector("#developerFirstName").textContent = data.content[i].firstName;
                account.querySelector("#developerLastName").textContent = data.content[i].lastName;
                account.querySelector("#accountStatus").textContent = data.content[i].status;
                account.querySelector("#accountRole").textContent = data.content[i].role;
                account.querySelector("#accountEmail").textContent = data.content[i].email;

                if (data.content[i].photo != null) {
                    let photo = account.querySelector("#developerPhoto");
                    let imgTag = photo.getElementsByTagName('img');
                    imgTag.item(0).setAttribute('src', data.content[i].photo.link)
                }

                let developerLink = account.querySelector('#developerLink');
                developerLink.setAttribute('href', '/account/' + data.content[i].id)

                container.appendChild(account);

            }

            let pageNext = document.getElementById('pageNext');

        })
        .catch(error => {

            console.log("error", error)

        })
}
