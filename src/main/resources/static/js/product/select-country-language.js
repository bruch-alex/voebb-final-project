document.addEventListener("DOMContentLoaded", function () {
    const syncSelectedTags = (inputSelector, outputSelector) => {
        const checkboxes = document.querySelectorAll(inputSelector);
        const output = document.querySelector(outputSelector);

        const render = () => {
            output.innerHTML = "";
            checkboxes.forEach(cb => {
                if (cb.checked) {
                    const label = document.querySelector(`label[for="${cb.id}"]`);
                    const tag = document.createElement("span");
                    tag.className = "selected-tag";
                    tag.innerHTML = `
                            ${label?.textContent || "?"}
                            <button type="button" onclick="document.getElementById('${cb.id}').click()">&times;</button>
                        `;
                    output.appendChild(tag);
                }
            });
        };

        checkboxes.forEach(cb => cb.addEventListener("change", render));
        render();
    };

    syncSelectedTags("#country_list input[type=checkbox]", "#selectedCountries");
    syncSelectedTags("#language_list input[type=checkbox]", "#selectedLanguages");

    const setupSearchFilter = (inputId, listId) => {
        const listItems = document.querySelectorAll(`#${listId} li`);
        const input = document.getElementById(inputId);

        if (!input || listItems.length === 0) return;

        input.addEventListener("input", function () {
            const filter = this.value.toLowerCase();
            let lastVisibleItem = null;

            listItems.forEach(function (item) {
                const label = item.querySelector("label");
                const text = label.textContent.toLowerCase();

                if (text.includes(filter)) {
                    item.style.display = "list-item";
                    lastVisibleItem = item;
                } else {
                    item.style.display = "none";
                }
            });

            listItems.forEach(function (item) {
                if (item.style.display === "list-item") {
                    item.style.borderBottom = "1px solid #eee";
                }
            });

            if (lastVisibleItem) {
                lastVisibleItem.style.borderBottom = "none";
            }
        });
    };

    setupSearchFilter("searchCountryInput", "country_list");
    setupSearchFilter("searchLanguageInput", "language_list");
});