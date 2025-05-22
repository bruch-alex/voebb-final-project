const creatorSearchInput = document.getElementById("creatorSearchInput");
const creatorResultsBox = document.getElementById("creatorResultsBox");
const roleSearchInput = document.getElementById("roleSearchInput");
const roleResultsBox = document.getElementById("roleResultsBox");


// =============== CREATOR SEARCH ===============
creatorSearchInput.addEventListener("blur", () => {
    setTimeout(() => creatorResultsBox.classList.add("d-none"), 200);
});

creatorSearchInput.addEventListener("input", async function () {
    const query = this.value.trim();
    creatorResultsBox.innerHTML = "";

    if (!query) {
        creatorResultsBox.classList.add("d-none");
        return;
    }

    try {
        const res = await fetch(`/api/creators/searchCreator?lastName=${encodeURIComponent(query)}`);
        const creators = await res.json();

        if (creators.length > 0) {
            creators.forEach(creator => {
                const li = document.createElement("li");
                li.className = "list-group-item list-group-item-action";
                li.textContent = `${creator.firstName} ${creator.lastName}`;
                li.onclick = () => {
                    document.getElementById("creatorId").value = creator.id;
                    document.getElementById("creatorFirstName").value = creator.firstName;
                    document.getElementById("creatorLastName").value = creator.lastName;
                    creatorSearchInput.value = `${creator.firstName} ${creator.lastName}`;
                    creatorResultsBox.innerHTML = "";
                    creatorResultsBox.classList.add("d-none");
                };
                creatorResultsBox.appendChild(li);
            });
        } else {
            const li = document.createElement("li");
            li.className = "list-group-item d-flex justify-content-between align-items-center text-muted";
            li.innerHTML = `
                <span>No match found.</span>
                <button type="button" class="btn btn-sm btn-outline-primary" onclick="openNewCreatorModal()">Add New</button>
            `;
            creatorResultsBox.appendChild(li);
        }

        creatorResultsBox.classList.remove("d-none");
    } catch (error) {
        console.error("Creator search failed:", error);
        creatorResultsBox.classList.add("d-none");
    }
});

// =============== ROLE SEARCH ===============
roleSearchInput.addEventListener("blur", () => {
    setTimeout(() => roleResultsBox.classList.add("d-none"), 200);
});

roleSearchInput.addEventListener("input", async function () {
    const query = this.value.trim();
    roleResultsBox.innerHTML = "";

    if (!query) {
        roleResultsBox.classList.add("d-none");
        return;
    }

    try {
        const res = await fetch(`/api/creators/searchRole?roleName=${encodeURIComponent(query)}`);
        const roles = await res.json();

        if (roles.length > 0) {
            roles.slice(0, 5).forEach(role => {
                const li = document.createElement("li");
                li.className = "list-group-item list-group-item-action";
                li.textContent = role.creatorRoleName;
                li.onclick = () => {
                    document.getElementById("creatorRoleId").value = role.id;
                    document.getElementById("creatorRole").value = role.creatorRoleName;
                    roleSearchInput.value = role.creatorRoleName;
                    roleResultsBox.innerHTML = "";
                    roleResultsBox.classList.add("d-none");
                };
                roleResultsBox.appendChild(li);
            });
        } else {
            const li = document.createElement("li");
            li.className = "list-group-item d-flex justify-content-between align-items-center text-muted";
            li.innerHTML = `
                <span>No match found.</span>
                <button type="button" class="btn btn-sm btn-outline-primary" onclick="openNewRoleModal('${query}')">Add New</button>
            `;
            roleResultsBox.appendChild(li);
        }

        roleResultsBox.classList.remove("d-none");
    } catch (err) {
        console.error("Role search failed:", err);
        roleResultsBox.classList.add("d-none");
    }
});

// =============== ADD CREATOR WITH ROLE TO PREVIEW ===============
function addCreatorToPreview() {
    const creatorRole = roleSearchInput.value.trim();
    const firstName = document.getElementById("creatorFirstName").value;
    const lastName = document.getElementById("creatorLastName").value;
    const index = parseInt(document.getElementById("creatorIndex").value);

    if (!creatorRole || !firstName || !lastName) {
        alert("Please select a creator and provide a role.");
        return;
    }

    const wrapper = document.createElement("div");
    wrapper.className = "selected-tag";
    wrapper.innerHTML = `
        ${firstName} ${lastName} — ${creatorRole}
        <button type="button" onclick="this.parentElement.remove()">×</button>
        <input type="hidden" name="creators[${index}].firstName" value="${firstName}">
        <input type="hidden" name="creators[${index}].lastName" value="${lastName}">
        <input type="hidden" name="creators[${index}].role" value="${creatorRole}">
    `;

    document.getElementById("creatorsPreview").appendChild(wrapper);


    creatorSearchInput.value = "";
    roleSearchInput.value = "";
    document.getElementById("creatorId").value = "";
    document.getElementById("creatorFirstName").value = "";
    document.getElementById("creatorLastName").value = "";
    document.getElementById("creatorRoleId").value = "";
    document.getElementById("creatorRole").value = "";

    document.getElementById("creatorIndex").value = index + 1;

    creatorSearchInput.removeAttribute("required");
    roleSearchInput.removeAttribute("required");
}



// =============== MODALS ===============
function openNewCreatorModal() {
    document.getElementById("newFirstName").value = "";
    document.getElementById("newLastName").value = "";
    new bootstrap.Modal(document.getElementById("newCreatorModal")).show();
}

document.getElementById("newCreatorForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const firstName = document.getElementById("newFirstName").value.trim();
    const lastName = document.getElementById("newLastName").value.trim();
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

    if (!firstName || !lastName) return;

    try {
        const response = await fetch("/api/creators/newCreator", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify({firstName, lastName})
        });

        if (!response.ok) throw new Error("Failed to save new creator.");

        const newCreator = await response.json();
        document.getElementById("creatorId").value = newCreator.id;
        document.getElementById("creatorFirstName").value = newCreator.firstName;
        document.getElementById("creatorLastName").value = newCreator.lastName;
        creatorSearchInput.value = `${newCreator.firstName} ${newCreator.lastName}`;

        bootstrap.Modal.getInstance(document.getElementById("newCreatorModal")).hide();

    } catch (err) {
        alert("Error saving new creator.");
        console.error(err);
    }
});

function openNewRoleModal(defaultName = "") {
    document.getElementById("newRoleName").value = defaultName;
    new bootstrap.Modal(document.getElementById("newRoleModal")).show();
}

document.getElementById("newRoleForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const roleName = document.getElementById("newRoleName").value.trim();
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

    if (!roleName) return;

    try {
        const response = await fetch("/api/creators/newRole", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify({creatorRoleName: roleName})
        });

        if (!response.ok) throw new Error("Failed to save new role.");

        const newRole = await response.json();
        document.getElementById("creatorRoleId").value = newRole.id;
        document.getElementById("creatorRole").value = newRole.creatorRoleName;
        roleSearchInput.value = newRole.creatorRoleName;

        bootstrap.Modal.getInstance(document.getElementById("newRoleModal")).hide();
    } catch (err) {
        alert("Error saving new role.");
        console.error(err);
    }
});

// // TODO: proper UI for this validation
// // Warning pop-up: At least one creator should be added before submitting
// document.addEventListener('DOMContentLoaded', function () {
//     const form = document.querySelector('.needs-validation');
//     form.addEventListener('submit', function (event) {
//         const creatorFields = document.querySelectorAll('#creatorsPreview input[type="hidden"]');
//         if (creatorFields.length === 0) {
//             event.preventDefault();
//             event.stopPropagation();
//             alert("Please add at least one creator before submitting.");
//         }
//     });
// });
