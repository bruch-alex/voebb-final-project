document.addEventListener('DOMContentLoaded', function () {
    const modalEl = document.getElementById('createItemModal');
    const searchInput = modalEl.querySelector('input[name="searchTitle"]');
    const searchBtn = modalEl.querySelector('#searchBtn');
    const clearBtn = modalEl.querySelector('#clearBtn');
    const clearBtnContainer = modalEl.querySelector('#clearBtnContainer');
    const paginationContainer = modalEl.querySelector('#paginationContainer');
    const productSelectionForm = modalEl.querySelector('#productSelectionForm');
    const continueBtn = modalEl.querySelector('#continueBtn');
    const selectedProductId = modalEl.querySelector('#selectedProductId');
    const searchTab = modalEl.querySelector('#search-tab');
    const detailsTab = modalEl.querySelector('#details-tab');
    const noMatchWarning = modalEl.querySelector('#noMatchWarning');
    const createNewProductContainer = modalEl.querySelector('#createNewProductContainer');

    //  Enable "Continue" and "Add Item Location Details" button once product is selected
    window.enableContinueBtn = function (radio) {
        if (continueBtn && selectedProductId) {
            continueBtn.disabled = false;
            selectedProductId.value = radio.value;

            if (detailsTab) {
                detailsTab.classList.remove('disabled');
                detailsTab.removeAttribute('aria-disabled');
                detailsTab.removeAttribute('tabindex');
                bootstrap.Tooltip.getInstance(detailsTab)?.hide();
            }
        }
    };

    // Move to item details tab
    window.goToDetailsTab = function () {
        if (detailsTab) new bootstrap.Tab(detailsTab).show();
    };

    //  Handle search input enable/disable
    if (searchInput && searchBtn) {
        const toggleSearchBtn = () => {
            searchBtn.disabled = searchInput.value.trim().length < 2;
        };
        searchInput.addEventListener('input', toggleSearchBtn);
        toggleSearchBtn();
    }

    //   Handle clear button click
    if (clearBtn) {
        clearBtn.addEventListener('click', function () {
            searchInput.value = '';
            if (productSelectionForm) productSelectionForm.innerHTML = '';
            if (continueBtn) continueBtn.disabled = true;
            if (selectedProductId) selectedProductId.value = '';
            if (searchBtn) searchBtn.disabled = true;
            if (clearBtnContainer) clearBtnContainer.remove();
            if (paginationContainer) paginationContainer.remove();
            if (noMatchWarning) noMatchWarning.remove();
            if (createNewProductContainer) createNewProductContainer.remove();
        });
    }

    // Modal reset on close
    if (modalEl) {
        modalEl.addEventListener('hidden.bs.modal', function () {
            if (searchInput) searchInput.value = '';
            if (productSelectionForm) productSelectionForm.innerHTML = '';
            if (continueBtn) continueBtn.disabled = true;
            if (selectedProductId) selectedProductId.value = '';
            if (searchTab) new bootstrap.Tab(searchTab).show();

            if (detailsTab) {
                detailsTab.classList.add('disabled');
                detailsTab.setAttribute('aria-disabled', 'true');
                detailsTab.setAttribute('tabindex', '-1');

                bootstrap.Tooltip.getOrCreateInstance(detailsTab).enable();
            }

            //  Clean up URL after closing Modal
            const url = new URL(window.location);
            if (url.pathname === '/admin/items/create') {
                url.searchParams.delete('action');
                url.searchParams.delete('searchTitle');
                url.searchParams.delete('page');
                window.history.replaceState({}, document.title, url.pathname);
            }
        });
    }
});