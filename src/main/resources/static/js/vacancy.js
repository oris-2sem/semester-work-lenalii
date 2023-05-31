function addTagsToVacancy() {
    let vacancyId = document.getElementById("vacancyIdInput").value;
    let tagsString = document.getElementById("tagsInput").value;
    let tags = tagsString.split(",").map(tag => tag.trim());

    console.log("vacancyId ", vacancyId)
    console.log("tags ", tags)

    let requestBody = {
        vacancyId: vacancyId,
        tags: tags
    };
    fetch('/vacancy/tag', {
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
            console.log('Tags added successfully:', data);
            location.reload();
        })
        .catch(error => {
            console.error('Error adding tags to vacancy:', error);
        });
}

function deleteTagsFromVacancy() {
    let selectedElements = [];

    let checkboxes = document.querySelectorAll("#elementList input[type=checkbox]:checked");
    checkboxes.forEach(function (checkbox) {
        selectedElements.push(checkbox.value);
    });
    console.log("selected elements: ", selectedElements)

    let vacancyId = document.getElementById("vacancyId").value;

    let requestBody = {
        vacancyId: vacancyId,
        ids: selectedElements
    };
    console.log(vacancyId)
    fetch('/vacancy/tag', {
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

function getCompanyInformation(companyId) {
    document.querySelector('.getInformationAboutCompany').disabled = true

    fetch("/vacancy/by/company/" + companyId)
        .then(response => response.json())
        .then(data => {
            console.log(data.content)
            const vacancies = document.querySelector('.company-vacancies')
            vacancies.style.display = 'block';
            const vacancyItemTemplate = vacancies.querySelector('.vacancy-container-item')

            for (let i = 0; i < data.content.length; i++) {
                let newElement = vacancyItemTemplate.cloneNode(true)

                newElement.style.display = 'block';
                newElement.querySelector('#vacancyId').textContent = data.content[i].id;
                newElement.querySelector('#get-vacancy-link').href = data.content[i].id;
                newElement.querySelector('#vacancyName').textContent = data.content[i].name;

                vacancies.appendChild(newElement)
            }
            if (vacancies.querySelector('.vacancy-container-item') === null) {
                vacancies.querySelector('.vacancy-container-item').style.display = 'none';
            }
        });
}

function checkHrCompanyAndVacancyCompanyMatch(hrId, companyId) {

    fetch("/account/info/" + hrId)
        .then(response => response.json())
        .then(data => {
            let hrCompanyId = data.companyResponse.id;
            if (companyId === hrCompanyId) {
                document.querySelector('.change-vacancy-container').style.display = 'block'
            }

        });
}

function deleteReply(vacancyId) {

    let requestBody = {
        id: vacancyId
    };
    fetch("/vacancy/reply", {
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
            console.log('Tags deleted successfully:', data);
            let element = document.getElementById(data.message);
            element.remove()
        })
        .catch(error => {
            console.error('Error adding tags to vacancy:', error);
        });

}

function rejectReply(vacancyId, developerId){
    console.log("develoeprId", developerId)
    console.log("vacancyId", vacancyId);

    let requestBody = {
        vacancyId: vacancyId,
        developerId: developerId
    };

    fetch("/vacancy/reply/reject", {
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

function replyToVacancy(vacancyid){

    fetch("/vacancy/reply/"+ vacancyid, {
        method: 'POST',
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Failed');
            }

        })
        .then(data => {

            let str = "Ваша заявка успешно отправлена!"
            openPopup(str)
            
        })
        .catch(error => {
            let str = "Вы уже отправили заявку"
            openPopup(str)
        });
}
