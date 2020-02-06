package com.salesianostriana.dam.proyectocontraoferta.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.salesianostriana.dam.proyectocontraoferta.excepciones.ExcepcionMismoEmail;

@ControllerAdvice
public class ControllerExcepcionEmail {
	
	@ExceptionHandler (ExcepcionMismoEmail.class)	
	public String excepcioncarrito (Model model, ExcepcionMismoEmail ecv) {
		
		model.addAttribute("excep", ecv);
		return "comun/errores/mismoEmail.html";
	}

}