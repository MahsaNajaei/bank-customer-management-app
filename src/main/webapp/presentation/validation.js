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

function showWrongPercentageAlert(input) {
    if (input.value == "")
        return showNullAlert(input);
    input.setCustomValidity("مقدار وارد شده اشتباه است! لطفا یک عدد بین 0 تا 100 در این فیلد وارد کنید! ");
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

function displayPositiveNumberValidationAlert(numericInputElement) {
    if (numericInputElement.value == "")
        return showNullAlert(numericInputElement);
    if (!numericInputElement.value.match(/^[1-9][0-9]*$/))
        return numericInputElement.setCustomValidity(" مقدار وارد شده برای این فیلد باید یک عدد بزرگتر از 0 باشد! ");
}

function checkConditionDataIntegrity() {

    let minContractDurationElement = document.getElementsByName("min-contract-duration")[0];
    let maxContractDurationElement = document.getElementsByName("max-contract-duration")[0];
    checkMaxMinValidation(maxContractDurationElement, minContractDurationElement, "مدت قرارداد");

    let minContractAmountElement = document.getElementsByName("min-contract-amount")[0];
    let maxContractAmountElement = document.getElementsByName("max-contract-amount")[0];
    checkMaxMinValidation(maxContractAmountElement, minContractAmountElement, "مبلغ قرارداد");
}

function checkMaxMinValidation(maxvalueElement, minvalueElement, valueName) {
    if (maxvalueElement.value != "" && minvalueElement.value != "" && parseFloat(maxvalueElement.value) < parseFloat(minvalueElement.value))
        return minvalueElement.setCustomValidity("حداقل " + valueName + " نباید از حداکثر مقدار وارد شده برای آن بیشتر باشد!");
}

function checkNationalCodeIntegrity() {
    let nationalCodeElement = document.getElementsByName("identity-number")[0];
    let nationalCode = nationalCodeElement.value;
    if (nationalCode == "")
        return showNullAlert(nationalCodeElement);
    if (!nationalCode.match(/[0-9]{10}/))
        return nationalCodeElement.setCustomValidity("کد ملی باید یک عدد ده رقمی باشد!");
    var sum = 0;
    for (var i = 0; i < 9; i++)
        sum += parseInt(nationalCode.substr(i, 1)) * (10 - i);
    var remainder = sum % 11;
    var rightMostDigit = parseInt(nationalCode.substr(9, 1));
    if (remainder != rightMostDigit && remainder != 11 - rightMostDigit)
        return nationalCodeElement.setCustomValidity("کد ملی وارد شده نامعتبر است!");
}