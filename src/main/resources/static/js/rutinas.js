function mostrarImagen(input) {
    if (!input.files || !input.files[0]) {
        return;
    }

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
