function openMenu() {
    console.log("call open menu")
    var menu = document.getElementById("menu");
    if (menu.style.display === "none") {
        menu.style.display = "block"; // Показываем меню
    } else {
        menu.style.display = "none"; // Скрываем меню
    }
}

function addSpecializationsToVacancy() {

    let specializationsString = document.getElementById("specializationsInput").value;
    let specializations = specializationsString.split(",").map(tag => tag.trim());

    let requestBody = {
        specializations: specializations
    };
    fetch('/developer/spec', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Failed to add tags to vacancy.');
            }
        })
        .then(data => {
            location.reload();
        })
        .catch(error => {
            console.error('Error adding tags to vacancy:', error);
        });

}

function deleteSpecializationsFromVacancy() {

    let selectedElements = [];
    let checkboxes = document.querySelectorAll("#elementList input[type=checkbox]:checked");
    checkboxes.forEach(function (checkbox) {
        selectedElements.push(checkbox.value);
    });
    console.log("selected elements: ", selectedElements)

    let requestBody = {
        ids: selectedElements
    };
    fetch('/developer/spec', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Failed to delete tags to vacancy.');
            }
        })
        .then(data => {
            location.reload();
        })
        .catch(error => {
            console.error('Error adding tags to vacancy:', error);
        });

}

function getDevelopersWithDocuments() {
    fetch('/developer/by/document')
        .then(function (response) {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Error: ' + response.status);
            }
        })
        .then(function (data) {

            let elements = document.getElementsByClassName('developers-page');

            let str = 'developer : ' + data.content;

            if (elements.length > data.content.length) {
                let x = elements.length - data.content.length;
                for (let i = elements.length - 1; i >= data.content.length; i--) {
                    let element = elements[i];
                    element.remove()

                }
            }
            for (var i = 0; i < elements.length; i++) {
                var element = elements[i];

                element.querySelector("#developerFirstName").textContent = data.content[i].firstName;
                element.querySelector("#developerWorkExperience").textContent = data.content[i].workExperience;
                element.querySelector("#developerCity").textContent = data.content[i].city
                element.querySelector("#developerLastName").textContent = data.content[i].lastName;
                let specializations = element.querySelector('#specializations');
                specializations.innerHTML = ''
                for (var j = 0; j < data.content[i].specializations.length; j++) {
                    specializations.innerHTML += '<span class="tag-list">' + data.content[i].specializations[j].name + '</span>'
                }
                element.querySelector("#developerCity").textContent = data.content[i].city;

                if (data.content[i].photo != null) {
                    let photo = element.querySelector("#developerPhoto");
                    let imgTag = photo.getElementsByTagName('img');
                    imgTag.item(0).setAttribute('src', data.content[i].photo.link)
                }

                let developerLink = element.querySelector('#developerLink');
                developerLink.setAttribute('href', '/developer/' + data.content[i].id)

            }

            let pageNext = document.getElementById('pageNext');
        })
        .catch(function (error) {

        });
}

function signUp(firstName, lastName, username, dateOfBirth, workExperience, city, email, password) {
    fetch("/developer/sign-up", {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            firstName: firstName,
            lastName: lastName,
            username: username,
            dateOfBirth: dateOfBirth,
            workExperience: workExperience,
            city: city,
            email: email,
            password: password
        })
    }).then(response => {

        let status = response.status;

        let data = response.json()

        if (status === 201) {

            data.then((promise) => {
                console.log(promise.email)
                let str = "Письмо для подтверждения регистрации отправлено на почту " + promise.email;
                openPopup(str)
            })

        } else {

            data.then((promise) => {
                let str=promise.message;
                let element = document.getElementsByClassName('city');
                element.item(0).textContent=str

                let errors = promise.errors

                if(errors!=null){
                for (let i = 0; i < errors.length; i++) {
                    let element = document.getElementsByClassName(errors[i].fieldName);

                    element.item(0).textContent=errors[i].detailMessage

                }}

            })
        }
    }).catch(error => {
        console.error('Registration error:', error);
    });

}