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
    if (input.value == ""){
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

function getLegalFormParameters(formElement) {
    const formData = new FormData(formElement);
    const params = new URLSearchParams();

    if (formData.has("customer-id")) {
        params.append("customer-id", formData.get("customer-id").toString());
    }
    params.append('company-name', formData.get("company-name").toString());
    params.append('economic-id', formData.get("economic-id").toString());
    params.append('registration-day', formData.get("registration-day").toString());
    params.append('registration-month', formData.get("registration-month").toString());
    params.append('registration-year', formData.get("registration-year").toString());

    return params;
}

async function postLegalRegistrationRequestOnSubmit() {
    let formElement = document.getElementById("legal_registration_form");
    formElement.addEventListener("submit", async function (event) {
        const params = getLegalFormParameters(formElement);
        event.preventDefault();
        const fetchSettings = {method: 'POST', body: params};
        let response = await fetch('../legal-register', fetchSettings);
        let message = await response.text();
        alert(message);
    });
}

async function postLegalUpdateRequestOnSubmit() {
    let formElement = document.getElementById("legal_registration_form");
    formElement.addEventListener("submit", async function (event) {
        const params = getLegalFormParameters(formElement);
        event.preventDefault();
        const fetchSettings = {method: 'POST', body: params};
        let response = await fetch('update?customer-type=legal', fetchSettings);
        let message = await response.text();
        alert(message);
    })
}

function getRealFormParameters(formElement) {
    const formData = new FormData(formElement);
    const params = new URLSearchParams();

    if (formData.has("customer-id")) {
        params.append("customer-id", formData.get("customer-id").toString());
    }
    params.append('customer-name', formData.get("customer-name").toString());
    params.append('surname', formData.get("surname").toString());
    params.append('fathers-name', formData.get("fathers-name").toString());
    params.append('birth-day', formData.get("birth-day").toString());
    params.append('birth-month', formData.get("birth-month").toString());
    params.append('birth-year', formData.get("birth-year").toString());
    params.append('identity-number', formData.get("identity-number").toString());

    return params;
}

async function postRealRegistrationRequestOnSubmit() {
    let formElement = document.getElementById("real_form");
    formElement.addEventListener("submit", async function (event) {
        event.preventDefault();
        const params = getRealFormParameters(formElement);
        const fetchSettings = {method: 'POST', body: params};
        let response = await fetch('../real-register', fetchSettings);
        let message = await response.text();
        alert(message);
    });
}
async function postRealUpdateRequestOnSubmit() {
    let formElement = document.getElementById("real_form");
    formElement.addEventListener("submit", async function (event) {
        const params = getRealFormParameters(formElement);
        event.preventDefault();
        const fetchSettings = {method: 'POST', body: params};
        let response = await fetch('update?customer-type=real', fetchSettings);
        let message = await response.text();
        alert(message);
    })
}
