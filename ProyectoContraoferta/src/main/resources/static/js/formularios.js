var soloLetras = new RegExp('^[A-Za-zÑñÁÉÍÓÚáéíóú ]+$');

function comprobarName() {
	var nombre = document.getElementById("name");
	var mensajeNom = document.getElementById("mensajeNombre");

	if (soloLetras.test(nombre.value)) {
		nombre.style.borderColor = "green";
		mensajeNom.innerText = "";
	} else {
		nombre.style.borderColor = "red";
		mensajeNom.innerText = "El nombre introducido no es válido";
		mensajeNom.style.color = "red";
	}
}

function comprobarLastName() {
	var apellido = document.getElementById("lastName");
	var mensajeLastName = document.getElementById("mensajeApellidos");

	if (soloLetras.test(apellido.value)) {
		apellido.style.borderColor = "green";
		mensajeLastName.innerText = "";
	} else {
		apellido.style.borderColor = "red";
		mensajeLastName.innerText = "El apellido introducido no es válido";
		mensajeLastName.style.color = "red";
	}
}

function comprobarEmail() {
	var emails = document.getElementsByClassName("form-control email");
	var mensajeEmail = document.getElementById("mensajeEmail");
	var valores = /^([a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+(\.[a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+)*|"((([ \t]*\r\n)?[ \t]+)?([\x01-\x08\x0b\x0c\x0e-\x1f\x7f\x21\x23-\x5b\x5d-\x7e\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|\\[\x01-\x09\x0b\x0c\x0d-\x7f\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))*(([ \t]*\r\n)?[ \t]+)?")@(([a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.)+([a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.?$/i;

	if (emails[0].value != emails[1].value || emails[0].value === "") {
		for (var i = 0; i < emails.length; i++) {
			emails[i].style.borderColor = "red";
		}
		mensajeEmail.innerText = "Los correos no coinciden";
		mensajeEmail.style.color = "red";
	} else {
		for (var i = 0; i < emails.length; i++) {
			if (!valores.test(emails[i].value)) {
				emails[i].style.borderColor = "red";
				mensajeEmail.innerText = "Los correos no son válidos";
			} else {
				emails[i].style.borderColor = "green";
				mensajeEmail.innerText = "";
			}
		}
	}

}

function comprobarPassword() {
	var contrasenas = document.getElementsByClassName("form-control contra");
	var mensajeContra = document.getElementById("mensajeContra");

	if (contrasenas[0].value != contrasenas[1].value || contrasenas[0].value === "") {
		for (var i = 0; i < contrasenas.length; i++) {
			contrasenas[i].style.borderColor = "red";
		}
		mensajeContra.innerText = "Los correos no coinciden";
		mensajeContra.style.color = "red";
	} else {
		for (var i = 0; i < contrasenas.length; i++) {
			contrasenas[i].style.borderColor = "green";
			mensajeContra.innerText = "";
		}
	}

}


function comprobarPrecio() {
	var precioI = parseFloat(document.getElementById("precioI").value);
	var precioF = parseFloat(document.getElementById("precioF").value);
	var precios = document.getElementsByClassName("form-control precio");
	var mensajePrecio = document.getElementById("mensajePrecio");

	if (precioF >= precioI || precioF < 0 || precioI < 0) {
		for (var i = 0; i < precios.length; i++) {
			precios[i].style.borderColor = "red";
		}
		mensajePrecio.innerText = "Se recomienda poner un precio más bajo que el original";
		mensajePrecio.style.color = "red";
	} else {
		for (var i = 0; i < precios.length; i++) {
			precios[i].style.borderColor = "green";
			mensajePrecio.innerText = "";
		}
	}

}
