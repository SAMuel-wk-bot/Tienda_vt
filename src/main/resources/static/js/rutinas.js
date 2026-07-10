function mostrarImagen(input) {
    if (!input.files || !input.files[0]) return;
    const imagen = input.files[0];
    const maximo = 512 * 1024;
    if (imagen.size > maximo) {
        alert("La imagen no debe superar los 512 KB.");
        input.value = "";
        return;
    }
    const lector = new FileReader();
    lector.onload = function (evento) {
        const vistaPrevia = document.getElementById("blah");
        if (vistaPrevia) {
            vistaPrevia.src = evento.target.result;
            vistaPrevia.height = 200;
        }
    };
    lector.readAsDataURL(imagen);
}

document.addEventListener("DOMContentLoaded", function () {
    const confirmModal = document.getElementById("confirmModal");
    if (confirmModal) {
        confirmModal.addEventListener("show.bs.modal", function (event) {
            const button = event.relatedTarget;
            document.getElementById("modalId").value = button.getAttribute("data-bs-id");
            document.getElementById("modalDescripcion").textContent = button.getAttribute("data-bs-descripcion");
        });
    }
    setTimeout(function () {
        document.querySelectorAll(".toast").forEach(function (toast) { toast.classList.remove("show"); });
    }, 4000);
});
