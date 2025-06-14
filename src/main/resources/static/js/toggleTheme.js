const html = document.documentElement;
const body = document.body;

const savedTheme = localStorage.getItem("theme") || "light";
html.setAttribute("data-bs-theme", savedTheme);
body.setAttribute("data-theme", savedTheme);

const updateThemeIcon = (theme) => {
    const icon = document.getElementById('themeIcon');
    if (!icon) return;

    const isLight = theme === 'light';
    if (isLight) {
        icon.style.transform = 'scale(1.5)';
    } else {
        icon.style.transform = '';
    }
    icon.classList.toggle('bi-brightness-low-fill', isLight);
    icon.classList.toggle('bi-moon-stars-fill', !isLight);
};

const toggleTheme = () => {
    const isDark = html.getAttribute('data-bs-theme') === 'dark';
    const newTheme = isDark ? 'light' : 'dark';

    html.setAttribute('data-bs-theme', newTheme);
    body.setAttribute('data-theme', newTheme);

    localStorage.setItem("theme", newTheme);
    updateThemeIcon(newTheme);
};

document.getElementById('themeToggle')?.addEventListener('click', toggleTheme);

document.addEventListener('DOMContentLoaded', () => {
    updateThemeIcon(savedTheme);
});