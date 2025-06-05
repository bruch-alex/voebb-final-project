document.addEventListener("DOMContentLoaded", function () {
    const sidebar = document.querySelector(".offcanvas");
    const mainForm = document.getElementById("searchForm");

    // Visible elements
    const sidebarCreator = sidebar.querySelector("#creator");

    // Hidden form inputs
    const hiddenLibrary = document.getElementById("libraryHidden");
    const hiddenProductType = document.getElementById("productTypeHidden");
    const hiddenCreator = document.getElementById("creatorHidden");
    const hiddenLanguage = document.getElementById("languageHidden");
    const hiddenCountry = document.getElementById("countryHidden");

    setupDropdown(
        hiddenProductType,
        document.getElementById('productTypeDropdown').querySelectorAll('.dropdown-item'),
        document.getElementById('productTypeDropdownButton')
    );

    setupDropdown(
        hiddenLanguage,
        document.getElementById('languageDropdown').querySelectorAll('.dropdown-item'),
        document.getElementById('languageDropdownButton')
    );

    setupDropdown(
        hiddenCountry,
        document.getElementById('countryDropdown').querySelectorAll('.dropdown-item'),
        document.getElementById('countryDropdownButton')
    );

    setupDropdown(
        hiddenLibrary,
        document.getElementById('libraryDropdown').querySelectorAll('.dropdown-item'),
        document.getElementById('libraryDropdownButton')
    );

    function setupDropdown(hidden, items, button) {
        // Set button text if hidden has a pre-set value
        if (hidden && hidden.value) {
            items.forEach(item => {
                if (item.getAttribute('data-value') === hidden.value) {
                    button.textContent = item.textContent;
                }
            });
        }

        items.forEach(item => {
            item.addEventListener('click', function (e) {
                e.preventDefault(); // Correct use of preventDefault
                button.textContent = item.textContent;
                hidden.value = item.getAttribute('data-value');
            });
        });
    }

    // Handle Apply Filters
    const applyBtn = document.getElementById("applyFiltersBtn");
    if (applyBtn) {
        applyBtn.addEventListener("click", function (e) {
            e.preventDefault();
            if (sidebarCreator && hiddenCreator) {
                hiddenCreator.value = sidebarCreator.value;
            }
            mainForm.submit();
        });
    }

    // Handle Reset Filters
    const resetBtn = document.getElementById("resetFiltersBtn");
    if (resetBtn) {
        resetBtn.addEventListener("click", function (e) {
            e.preventDefault();
            if (hiddenProductType) hiddenProductType.value = '';
            if (hiddenCreator) hiddenCreator.value = '';
            if (hiddenLanguage) hiddenLanguage.value = '';
            if (hiddenCountry) hiddenCountry.value = '';
            mainForm.submit();
        });
    }
});
