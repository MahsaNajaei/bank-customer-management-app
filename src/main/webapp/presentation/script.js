function showEconomicCodeAlert(input) {
    if (input.value == "")
        return showNullAlert(input);
    input.setCustomValidity("کد اقتصادی باید یک عدد دوازده رقمی باشد!");
}

function showNationalCodeAlert(input) {
    if (input.value == "")
        return showNullAlert(input);
    input.setCustomValidity("کد ملی باید یک عدد ده رقمی باشد!");
}

function showDayInvalidAlert(input) {
    if (input.value == "") {
        return showNullAlert(input);
    }
    input.setCustomValidity("مقدار وارد شده برای روز نامعتبر است!");
}

function showMonthInvalidAlert(input) {
    if (input.value == "ماه") {
        return showNullAlert(input);
    }
    input.setCustomValidity("مقدار وارد شده برای ماه نامعتبر است!");
}

function showYearInvalidAlert(input) {
    if (input.value == "سال" || input.value == "")
        return showNullAlert(input);
    input.setCustomValidity("مقدار وارد شده برای سال نامعتبر است! (لطفا یک عدد چهار رقمی وارد کنید.)");
}

function showNullAlert(input) {
    input.setCustomValidity("لطفا فرم را کامل پر کنید!");
}

function showCustomerIdAlert(input) {
    input.setCustomValidity("شماره مشتری از عدد تشکیل شده و حداکثر ده رقم است!");
}

function checkDateIntegrity() {
    let dayElement = document.querySelector('.date-wrapper input[name="day"]');
    let day = dayElement.value;
    let month = document.querySelector('.date-wrapper input[name="month"]').value;
    if (month > 6 && day == 31) {
        dayElement.setCustomValidity("ام دارای 30 روز می باشد!" + month + "تاریخ نادرست است. ماه")
    }
}

async function postLegalRegistrationRequestOnSubmit() {
    let formElement = document.getElementById("legal_form");
    formElement.addEventListener("submit", async function (event) {
        const params = getFormParameters(formElement);
        event.preventDefault();
        const fetchSettings = {method: 'POST', body: params};
        let response = await fetch('../legal-register', fetchSettings);
        let message = await response.text();
        alert(message);
    });
}

async function postLegalUpdateRequestOnSubmit() {
    let formElement = document.getElementById("legal_form");
    formElement.addEventListener("submit", async function (event) {
        const params = getFormParameters(formElement);
        event.preventDefault();
        const fetchSettings = {method: 'POST', body: params};
        let response = await fetch('update?customer-type=legal', fetchSettings);
        let message = await response.text();
        alert(message);
    })
}

function getFormParameters(formElement) {
    const formData = new FormData(formElement);
    const params = new URLSearchParams();
    for (const key of formData.keys()) {
        let value = formData.get(key);
        if (value.length > 0) {
            params.append(key, value.toString())
        }
    }
    return params;
}

async function postRealRegistrationRequestOnSubmit() {
    let formElement = document.getElementById("real_form");
    formElement.addEventListener("submit", async function (event) {
        event.preventDefault();
        const params = getFormParameters(formElement);
        const fetchSettings = {method: 'POST', body: params};
        let response = await fetch('../real-register', fetchSettings);
        let message = await response.text();
        alert(message);
    });
}

async function postRealUpdateRequestOnSubmit() {
    let formElement = document.getElementById("real_form");
    formElement.addEventListener("submit", async function (event) {
        const params = getFormParameters(formElement);
        event.preventDefault();
        const fetchSettings = {method: 'POST', body: params};
        let response = await fetch('update?customer-type=real', fetchSettings);
        let message = await response.text();
        alert(message);
    })
}

function loadSearchFormBasedOnSelection() {
    const domainSelectElement = document.getElementsByName("search-domain")[0];

    domainSelectElement.addEventListener("click", function (event) {
        if (event.target.value == "all") {
            $("#search_form_wrapper").load("defaultSearchForm.html", function () {
                setSearchSubmitEvent(document.getElementById("search_form"), "all");
            });
        }

        if (event.target.value == "legal") {
            $("#search_form_wrapper").load("legal-person-form.html", function () {
                setSearchSubmitEvent(document.getElementById("legal_form"), "legal")
                let innerHtml = document.getElementById("legal_form").innerHTML;
                document.getElementById("legal_form").innerHTML = "<lable> شماره مشتری <input type='text' pattern='[0-9]+' oninvalid='showCustomerIdAlert(this)' oninput=\"this.setCustomValidity('')\" name='customer-id' style=' margin-bottom:3%;'> </lable>" + innerHtml;
                document.getElementsByClassName("submit-button")[0].value = "جستجو";
            });
        }

        if (event.target.value == "real") {
            $("#search_form_wrapper").load("real-person-form.html", function () {
                setSearchSubmitEvent(document.getElementById("real_form"), "real");
                let innerHtml = document.getElementById("real_form").innerHTML;
                document.getElementById("real_form").innerHTML = "<lable> شماره مشتری <input type='text'  pattern='[0-9]+' oninvalid='showCustomerIdAlert(this)' oninput=\"this.setCustomValidity('')\" name='customer-id' style=' margin-bottom:3%;'> </lable>" + innerHtml;
                document.getElementsByClassName("submit-button")[0].value = "جستجو";
            });
        }
    })
}

function setSearchSubmitEvent(formElement, searchDomain) {
    const inputs = formElement.getElementsByTagName("input");
    for (const input of inputs) {
        input.removeAttribute("required");
        if (input.name == "identity-number" || input.name == "economic-id") {
            input.pattern = "[0-9]+";
        }
    }
    formElement.addEventListener("submit", async function (event) {
        let params = getFormParameters(formElement);
        formElement.action = "../search?search-domain=" + searchDomain + "&" + params;
    })
}
