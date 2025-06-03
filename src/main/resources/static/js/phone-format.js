
    const phoneInput = document.getElementById('phoneNumber');
    const form = document.getElementById('registrationForm');

    phoneInput.addEventListener('input', (e) => {
    let value = e.target.value.replace(/[^0-9]/g, ''); // Remove all non-digits

    if (value.length > 3) {
    value = value.slice(0, 3) + '-' + value.slice(3);
}

    e.target.value = value;
});

    // Before submitting form, remove dashes to send only digits
    form.addEventListener('submit', () => {
    phoneInput.value = phoneInput.value.replace(/-/g, '');
});
