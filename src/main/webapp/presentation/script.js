async function postLegalRegistrationRequestOnSubmit() {
    let formElement = document.getElementById("legal_form");
    formElement.addEventListener("submit", async function (event) {
        const params = getFormParameters(formElement);
        event.preventDefault();
        const fetchSettings = {method: 'POST', body: params};
        let response = await fetch('../legal-register', fetchSettings);
        let message = await response.text();
        showPopUpMessage(message, "");
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
        showPopUpMessage(message, "presentation/");
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
        showPopUpMessage(message, "");
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
        showPopUpMessage(message, "presentation/");
    })
}

function loadSearchFormBasedOnSelection() {
    const domainSelectElement = document.getElementsByName("search-domain")[0];

    domainSelectElement.addEventListener("click", function (event) {
        if (event.target.value == "all") {
            $("#search_form_wrapper").load("reusableHTMLCodes/defaultSearchForm.html", function () {
                setSearchSubmitEvent(document.getElementById("search_form"), "all");
            });
        }

        if (event.target.value == "legal") {
            $("#search_form_wrapper").load("reusableHTMLCodes/legal-person-form.html", function () {
                setSearchSubmitEvent(document.getElementById("legal_form"), "legal")
                let innerHtml = document.getElementById("legal_form").innerHTML;
                document.getElementById("legal_form").innerHTML = "<lable> شماره مشتری <input type='text' pattern='[0-9]+' oninvalid='showCustomerIdAlert(this)' oninput=\"this.setCustomValidity('')\" name='customer-id' style=' margin-bottom:3%;'> </lable>" + innerHtml;
                document.getElementsByClassName("submit-button")[0].value = "جستجو";
            });
        }

        if (event.target.value == "real") {
            $("#search_form_wrapper").load("reusableHTMLCodes/real-person-form.html", function () {
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

async function postLoanType(conditionIsDefined) {
    let message = "خطا! \n تعیین حداقل یک شرط اعطا برای هر تسهیلات ضروری است!";
    let response = null;
    if(conditionIsDefined == "true"){
        const fetchSettings = {method: 'POST'};
        response = await fetch('../post-loan-type', fetchSettings);
        message = await response.text();
    }
    $("#popup").load("reusableHTMLCodes/popupBox.html", function () {
        document.getElementById("popup-message").innerHTML = message;
        document.getElementById("popup-button").addEventListener("click", function () {
            document.getElementById("popup-box").style.display = "none";
            if (response != null && response.status == 200) {
                window.location.assign("loanDefinition.jsp")
            }
        });
    });
}

function postLoanRequestOnSubmit() {
    let formElement = document.getElementById("loan-contract-request-form");
    formElement.addEventListener("submit", async function (event) {

        event.preventDefault();

        let customerId = document.getElementById("customer-id").value;
        if (customerId == null || customerId == "")
            return showPopUpMessage("لطفا ابتدا کد ملی مشتری را وارد کرده و گزینه ی بازیابی را انتخاب فرمایید!", "presentation/");

        const params = getFormParameters(formElement);
        const fetchSettings = {method: 'POST', body: params};
        let response = await fetch('register-loan-profile', fetchSettings);
        let message = await response.text();
        $("#popup").load("presentation/reusableHTMLCodes/popupBox.html", function () {
            document.getElementById("popup-message").innerHTML = message;
            document.getElementById("popup-button").addEventListener("click", function () {
                document.getElementById("popup-box").style.display = "none";
                window.location.assign("create-loan-profile");
            });
        });
    })
}

function showPopUpMessage(message, startingUrlPath){
    $("#popup").load( startingUrlPath + "reusableHTMLCodes/popupBox.html", function () {
        document.getElementById("popup-message").innerHTML = message;
        document.getElementById("popup-button").addEventListener("click", function () {
            document.getElementById("popup-box").style.display = "none";
        });
    });
}
