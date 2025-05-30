document.addEventListener("DOMContentLoaded", function () {
    const sidebar = document.querySelector(".filters-sidebar");
    const mainForm = document.getElementById("searchForm");

    // Visible elements
    const sidebarCreator = sidebar.querySelector("#creator");

    // Hidden form inputs
    const hiddenLibrary = document.getElementById("libraryHidden");
    const hiddenProductType = document.getElementById("productTypeHidden");
    const hiddenCreator = document.getElementById("creatorHidden");
    const hiddenLanguage = document.getElementById("languageHidden");
    const hiddenCountry = document.getElementById("countryHidden");



    // Library dropdown
    setupDropdown(
        hiddenLibrary,
        document.getElementById("libraryDropdown").querySelectorAll('.dropdown-item'),
        document.getElementById('libraryDropdownButton'))

    // Product type dropdown
    setupDropdown(
        hiddenProductType,
        document.getElementById("productTypeDropdown").querySelectorAll('.dropdown-item'),
        document.getElementById('productTypeDropdownButton'))

    // Language dropdown
    setupDropdown(
        hiddenLanguage,
        document.getElementById("languageDropdown").querySelectorAll('.dropdown-item'),
        document.getElementById('languageDropdownButton'))

    // Country dropdown
    setupDropdown(
        hiddenCountry,
        document.getElementById("countryDropdown").querySelectorAll('.dropdown-item'),
        document.getElementById('countryDropdownButton'))

    function setupDropdown(hidden, items, button) {
        if (hidden.value) {
            items.forEach(item => {
                if (item.getAttribute('data-value') === hidden.value) {
                    button.textContent = item.textContent;
                }
            });
        }

        items.forEach(item => {
            item.addEventListener('click', function (e) {
                //e.preventDefault();

                // Update dropdown button text
                button.textContent = this.textContent;

                // Update hidden select value
                hidden.value = this.getAttribute('data-value');
            });
        });
    }

    // Handle apply
    document.getElementById("applyFiltersBtn").addEventListener("click", function (e) {
        //e.preventDefault();
        // Copy sidebar values to hidden fields
        hiddenCreator.value = sidebarCreator.value;
        // Submit the main form
        mainForm.submit();
    });

    // Handle reset
    document.getElementById("resetFiltersBtn").addEventListener("click", function (e) {
        //e.preventDefault();
        hiddenProductType.value = '';
        hiddenCreator.value = '';
        hiddenLanguage.value = '';
        hiddenCountry.value = '';
        mainForm.submit();
    });

});