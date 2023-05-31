function parseJwt(token) {
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}

function clearChildElements(parentElement) {

    const childElements = parentElement.children;
    console.log(parentElement.children.length)
    for (let i = 0; i < childElements.length; i++) {
        const childElement = childElements[i];
        clearChildElements(childElement);
        childElement.innerHTML = '';
    }
}


function getAllConfirmedCompanies(pageNumber) {
    console.log("getAllCompanies")
    fetch('/company/confirmed?number=' + pageNumber, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        }
    })
        .then(response => response.json())
        .then(data => {
            console.log('Companies:', data.content);
            pasteData(data)

            let nextPageBlock = document.getElementById('nextPageBlock');
            nextPageBlock.innerHTML = ''

            const template = document.getElementById('next-page-template')
            let newElement = template.content.cloneNode(true)

            newElement.querySelector('#nextPageValue').textContent = data.pageable.pageNumber + 1
            newElement.querySelector('#nextPage').addEventListener('click', function () {
                getAllConfirmedCompanies(data.pageable.pageNumber + 1)
            })

            nextPageBlock.appendChild(newElement)

            let backPageBlock = document.getElementById('backPageBlock');
            backPageBlock.innerHTML = ''

            const backtemplate = document.getElementById('back-page-template')
            let backElement = backtemplate.content.cloneNode(true)
            backElement.querySelector('#backPageValue').textContent = data.pageable.pageNumber - 1
            backElement.querySelector('#backPage').addEventListener('click', function () {
                getAllConfirmedCompanies(data.pageable.pageNumber - 1)
            })

            backPageBlock.appendChild(backElement)
        })
        .catch(error => {
            console.error('get companies error:', error);
        });
}

function getAllCompanies(pageNumber) {
    console.log("getAllCompanies")
    fetch('/company?number=' + pageNumber, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        }
    })
        .then(response => response.json())
        .then(data => {
            console.log('Companies:', data.content);
            pasteData(data)

            let nextPageBlock = document.getElementById('nextPageBlock');
            nextPageBlock.innerHTML = ''

            const template = document.getElementById('next-page-template')
            let newElement = template.content.cloneNode(true)

            newElement.querySelector('#nextPageValue').textContent = data.pageable.pageNumber + 1
            newElement.querySelector('#nextPage').addEventListener('click', function () {
                getAllCompanies(data.pageable.pageNumber + 1)
            })

            nextPageBlock.appendChild(newElement)

            let backPageBlock = document.getElementById('backPageBlock');
            backPageBlock.innerHTML = ''

            const backtemplate = document.getElementById('back-page-template')
            let backElement = backtemplate.content.cloneNode(true)
            backElement.querySelector('#backPageValue').textContent = data.pageable.pageNumber - 1
            backElement.querySelector('#backPage').addEventListener('click', function () {
                getAllCompanies(data.pageable.pageNumber - 1)
            })

            backPageBlock.appendChild(backElement)

        })
        .catch(error => {
            console.error('get companies error:', error);
        });
}

function deleteCompany(companyId) {

    fetch('/company', {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },

        body: JSON.stringify({
            id: companyId,
        })

    })
        .then(response => {

            let status = response.status;
            response = response.json()

            if (status === 202) {

                openRegistrationResult('Компания удалена')

                document.getElementById(companyId).innerHTML = ''
            } else {

                openRegistrationResult("Не удалось удалить компанию")
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });

}

function pasteData(data) {
    const container = document.getElementById('companies')
    container.innerHTML = ''

    for (let i = 0; i < data.content.length; i++) {
        let companyResponse = data.content[i];
        const template = document.getElementById('template-company')
        let newElement = template.content.cloneNode(true)

        newElement.querySelector('#companyId').textContent = companyResponse.id;
        newElement.querySelector('#companyName').textContent = companyResponse.name;
        newElement.querySelector('#websiteLink').textContent = companyResponse.websiteLink;
        newElement.querySelector('#companyEmail').textContent = companyResponse.email;
        newElement.querySelector('#companyBlock').id = companyResponse.id;
        newElement.querySelector('#companyLink').addEventListener('click', function () {

            getCompanyById(companyResponse.id)
            this.style.display = 'none'

        })

        let token = localStorage.getItem("accessToken");
        let parsedToken = parseJwt(token);
        let role = parsedToken.role;
        if (role === 'ROLE_ADMIN') {
            newElement.querySelector('#companyStatus').textContent = companyResponse.status;
            let updateCompanyInfo = newElement.querySelector('#updateCompanyInfo');
            updateCompanyInfo.style.display = 'block'

            updateCompanyInfo.addEventListener('click', function () {

                openUpdateForm(companyResponse);
            })

            let deleteCompanyButton = newElement.querySelector('#deleteCompany');
            deleteCompanyButton.style.display = 'block'

            deleteCompanyButton.addEventListener('click', function () {

                deleteCompany(companyResponse.id)
            })
        }

        if (role === 'ROLE_MODERATOR') {
            newElement.querySelector('#companyStatus').textContent = companyResponse.status;
            if (companyResponse.status === 'CONFIRMED') {

                let blockCompanyButton = newElement.querySelector('#blockCompany');
                blockCompanyButton.style.display = 'block'

                blockCompanyButton.addEventListener('click', function () {

                    blockCompany(companyResponse.id)

                })
            }

            if (companyResponse.status === 'MODERATED') {
                let confirmCompanyButton = newElement.querySelector('#confirmCompany');
                confirmCompanyButton.style.display = 'block'

                confirmCompanyButton.addEventListener('click', function () {

                    confirmCompany(companyResponse.id)


                })
            }
        }
        container.appendChild(newElement)
    }
}


function openUpdateForm(companyResponse) {
    let updateForm = document.getElementById('updateForm');
    updateForm.style.display = 'block'

    updateForm.querySelector('#updateCompanyId').value = companyResponse.id;
    updateForm.querySelector('#updateCompanyName').value = companyResponse.name;
    updateForm.querySelector('#updateCompanyWebsiteLink').value = companyResponse.websiteLink;
    updateForm.querySelector('#updateCompanyType').value = companyResponse.type;
    updateForm.querySelector('#updateCompanyDescription').value = companyResponse.description;
}

function getCompanyById(companyId) {

    let companyElement = document.getElementById(companyId);
    let element = companyElement.querySelector('#company-vacancies');

    let closeCompanyInfo = companyElement.querySelector('#closeCompanyInfo');
    closeCompanyInfo.style.display = 'block'
    closeCompanyInfo.addEventListener('click', function () {
        element.innerHTML = ''
        this.style.display = 'none'
        companyElement.querySelector('#companyLink').style.display = 'block'
    })

    fetch('/company/' + companyId)

        .then(response => {

            let status = response.status;

            let data = response.json()

            if (status === 200) {

                data.then((promise) => {

                    let description = companyElement.querySelector('#description');
                    description.textContent = promise.description;
                    description.style.display = 'block';
                })

            } else {

                data.then((promise) => {
                    let str = promise.message;
                    let element = document.getElementsByClassName('city');
                    openPopup(str)

                })
            }
        })
        .catch(error => {
            console.error(error);
        });


    fetch("/vacancy/by/company/" + companyId)
        .then(response => response.json())
        .then(data => {
            console.log(data.content)

            for (let i = 0; i < data.content.length; i++) {

                let vacancyResponse = data.content[i];
                const template = document.getElementById('vacancy-template')
                let newElement = template.content.cloneNode(true)

                newElement.querySelector('#vacancySalary').textContent = data.content[i].salary
                newElement.querySelector('#vacancyId').textContent = data.content[i].id;
                newElement.querySelector('#vacancyName').textContent = data.content[i].name;

                let tags = newElement.querySelector('#tagBlock');
                for (let j = 0; j < vacancyResponse.tags.length; j++) {
                    const tagTemplate = document.getElementById('tag-template')

                    let newTag = tagTemplate.content.cloneNode(true)
                    newTag.querySelector('#tagName').textContent = vacancyResponse.tags[j].tag

                }

                if (vacancyResponse.remote) {
                    newElement.querySelector('#vacancyRemote').style.display = 'block'
                }
                let vacancyLink = newElement.querySelector('#vacancyLink');
                vacancyLink.setAttribute('href', '/vacancy/' + data.content[i].id)
                element.appendChild(newElement)
            }
        });
}

function updateCompany(companyId, updateName, updateCompanyDescription, updateCompanyWebsiteLink, updateCompanyType) {

    fetch('/company', {
        method: 'PATCH',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },

        body: JSON.stringify({
            id: companyId,
            name: updateName,
            description: updateCompanyDescription,
            websiteLink: updateCompanyWebsiteLink,
            type: updateCompanyType
        })

    })
        .then(response => {
            let status = response.status;
            response = response.json()

            if (status === 202) {
                let popup = document.getElementById("updateForm");
                popup.style.display = "none";
                openRegistrationResult('Новые данные сохранены')
            } else {
                console.log("Нет")
                let popup = document.getElementById("updateForm");
                popup.style.display = "none";
                openRegistrationResult(response.message)
            }
        })
        .catch(error => {
            console.error('Registration error:', error);
        });

}

function addCompany(name, description, websiteLink, email, type) {

    fetch('/company', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },

        body: JSON.stringify({
            name: name,
            description: description,
            websiteLink: websiteLink,
            email: email,
            type: type
        })

    })
        .then(response => {

            let status = response.status;
            response = response.json()
            if (status === 201) {
                response.then((promise) => {

                    let popup = document.getElementById("popup");
                    popup.style.display = "none";
                    openRegistrationResult('Регистрация прошла успешно. Компания проходит модерацию, подождите немножко и все у вас будет хорошо.')
                })

            } else {
                response.then((promise) => {

                    let popup = document.getElementById("popup");
                    popup.style.display = "none";
                    openRegistrationResult(promise.message)
                })

            }

        })
        .catch(error => {
            console.error('Registration error:', error);
        });
}


function getCompanyByHr() {

    let token = localStorage.getItem("accessToken");

    console.log("token ", token)
    console.log(parseJwt(token))
    let hrId = parseJwt(token).sub;

    let companyId;
    console.log("hrId ", hrId)
    fetch('/hr/' + hrId, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        }
    })
        .then(response => response.json())
        .then(data => {
            console.log('Get hr', data);
            console.log('Hr company is ', data.companyResponse.id)
            companyId = data.companyResponse.id;
            window.location.href = "/company/company-page-old.html";
        })
        .catch(error => {
            console.error('Registration error:', error);
        });
    console.log("companyId ", companyId)

    fetch('/company/' + companyId, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',

        }

    })
        .then(response => response.json())
        .then(data => {
            console.log('Get company data', data);
        })
        .catch(error => {
            console.error('Registration error:', error);
        });
}

function searchCompany(companyName, pageNumber) {

    fetch('/company/by/name?query=' + companyName + '&number=' + pageNumber, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        }
    })
        .then(response => response.json())
        .then(data => {
            console.log('Companies:', data.content);
            pasteData(data)
            let nextPageBlock = document.getElementById('nextPageBlock');
            nextPageBlock.innerHTML = ''

            const template = document.getElementById('next-page-template')
            let newElement = template.content.cloneNode(true)

            newElement.querySelector('#nextPageValue').textContent = data.pageable.pageNumber + 1
            newElement.querySelector('#nextPage').addEventListener('click', function () {
                searchCompany(companyName, data.pageable.pageNumber + 1)
            })

            nextPageBlock.appendChild(newElement)

            let backPageBlock = document.getElementById('backPageBlock');
            backPageBlock.innerHTML = ''

            const backtemplate = document.getElementById('back-page-template')
            let backElement = backtemplate.content.cloneNode(true)
            backElement.querySelector('#backPageValue').textContent = data.pageable.pageNumber + 1
            backElement.querySelector('#backPage').addEventListener('click', function () {
                searchCompany(companyName, data.pageable.pageNumber - 1)
            })

            backPageBlock.appendChild(backElement)

        })
        .catch(error => {
            console.error('get companies error:', error);
        });

}

function getAllModeratedCompanies(pageNumber) {

    fetch('/company/moderated?number=' + pageNumber, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        }
    })
        .then(response => response.json())
        .then(data => {
            console.log('Companies:', data.content);
            pasteData(data)

            let nextPageBlock = document.getElementById('nextPageBlock');
            nextPageBlock.innerHTML = ''

            const template = document.getElementById('next-page-template')
            let newElement = template.content.cloneNode(true)

            newElement.querySelector('#nextPageValue').textContent = data.pageable.pageNumber + 1
            newElement.querySelector('#nextPage').addEventListener('click', function () {
                getAllModeratedCompanies(data.pageable.pageNumber + 1)
            })

            nextPageBlock.appendChild(newElement)

            let backPageBlock = document.getElementById('backPageBlock');
            backPageBlock.innerHTML = ''

            const backtemplate = document.getElementById('back-page-template')
            let backElement = backtemplate.content.cloneNode(true)
            backElement.querySelector('#backPageValue').textContent = data.pageable.pageNumber + 1
            backElement.querySelector('#backPage').addEventListener('click', function () {
                getAllModeratedCompanies(data.pageable.pageNumber - 1)
            })

            backPageBlock.appendChild(backElement)
        })
        .catch(error => {
            console.error('get companies error:', error);
        });
}

function confirmCompany(companyId) {
    fetch('/company/confirm', {
        method: 'PATCH',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            id: companyId
        })
    })
        .then(response => response.json())
        .then(data => {
            getAllModeratedCompanies(0)


        })
        .catch(error => {
            console.error('get companies error:', error);
        });

}

function blockCompany(companyId) {
    fetch('/company/block', {
        method: 'PATCH',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            id: companyId
        })
    })
        .then(response => response.json())
        .then(data => {
            console.log('Companies:', data.content);
            getAllCompanies(0)

        })
        .catch(error => {
            console.error('get companies error:', error);
        });
}