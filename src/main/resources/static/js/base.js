<!-- Enable Bootstrap tooltips globally -->
document.addEventListener('DOMContentLoaded', function () {
    const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    tooltipTriggerList.forEach(el => new bootstrap.Tooltip(el));
});

<!-- Delete empty fields from forms -->
function cleanForm(form) {
    const inputs = form.querySelectorAll("input, select");
    inputs.forEach(input => {
        if (!input.value || input.value.trim() === "") {
            input.disabled = true;
        }
    });
    return true;
}

<!-- Add current url to redirect -->
document.addEventListener("DOMContentLoaded", function () {
    const redirect = document.getElementById("redirectInput");
    if (redirect) {
        redirect.value = window.location.pathname + window.location.search;
        console.log(redirect.value);
    }
});