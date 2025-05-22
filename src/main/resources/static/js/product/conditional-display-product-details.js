const productTypeSelect = document.querySelector('[name="productTypeId"]');
const bookDetailsBlock = document.getElementById('bookDetailsBlock');
const emediaBlock = document.getElementById('emediaBlock');
const emediaInput = emediaBlock?.querySelector('input');

function toggleConditionalFields() {
    const selected = productTypeSelect.options[productTypeSelect.selectedIndex]?.textContent?.toLowerCase();
    const isBook = selected === "book" || selected === "ebook";
    const isEbook = selected === "ebook";

    // Book fields toggle
    bookDetailsBlock.classList.toggle("d-none", !isBook);
    bookDetailsBlock.querySelectorAll("input").forEach(input => {
        input.disabled = !isBook;
        input.required = isBook;
    });

    // eMedia link toggle
    emediaBlock.classList.toggle("d-none", !isEbook);
    if (emediaInput) {
        emediaInput.disabled = !isEbook;
        emediaInput.required = isEbook;
    }
}

productTypeSelect.addEventListener("change", toggleConditionalFields);
toggleConditionalFields();


