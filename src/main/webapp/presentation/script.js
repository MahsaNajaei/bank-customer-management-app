function showEconomicCodeAlert(event) {
    if (event.value == "")
        return showNullAlert(event);
    event.setCustomValidity("کد اقتصادی باید دوازده رقمی باشد!");
}

function showNationalCodeAlert(event) {
    if (event.value == "")
        return showNullAlert(event);
    event.setCustomValidity("کد ملی باید ده رقمی باشد!");
}

function showDayInvalidAlert(event) {
    if (event == "روز" || event == "")
        return showNullAlert(event);
    event.setCustomValidity("مقدار وارد شده برای روز نامعتبر است!");
}

function showMonthInvalidAlert(event) {
    if (event == "ماه" || event == "")
        return showNullAlert(event);
    event.setCustomValidity("مقدار وارد شده برای ماه نامعتبر است!");
}

function showYearInvalidAlert(event) {
    if (event == "سال" || event == "")
        return showNullAlert(event);
    event.setCustomValidity("مقدار وارد شده برای سال نامعتبر است!");
}

function showNullAlert(event) {
    event.setCustomValidity("لطفا فرم را کامل پر کنید!");
}

async function postData(form) {

    const formData = new FormData(form);
    const params = new URLSearchParams();
    params.append('company_name', formData.get("company_name").toString());
    params.append('economic_id', formData.get("economic_id").toString());
    params.append('registration_day', formData.get("registration_day").toString());
    params.append('registration_month', formData.get("registration_month").toString());
    params.append('registration_year', formData.get("registration_year").toString());

    const fetchSettings = {
        method: 'POST',
        body: params
    };
    let response = await fetch('legal-register', fetchSettings);
    let result = await response.text();
    alert(result.toString());
}

async function postNaturalData(form) {

    const formData = new FormData(form);
    const params = new URLSearchParams();
    params.append('name', formData.get("name").toString());
    params.append('surname', formData.get("surname").toString());
    params.append('fathers_name', formData.get("fathers_name").toString());
    params.append('birth_day', formData.get("birth_day").toString());
    params.append('birth_month', formData.get("birth_month").toString());
    params.append('birth_year', formData.get("birth_year").toString());
    params.append('identity_number', formData.get("identity_number").toString());

    const fetchSettings = {method: 'POST', body: params};
    let response = await fetch('../natural-register', fetchSettings);
    // alert("second");
    // let result = await response.text();
    // alert("third");
    // alert(result.toString());
}