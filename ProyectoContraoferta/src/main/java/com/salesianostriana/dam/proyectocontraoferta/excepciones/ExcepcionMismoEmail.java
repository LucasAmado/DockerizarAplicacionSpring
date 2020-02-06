/**
 * 
 */
package com.salesianostriana.dam.proyectocontraoferta.excepciones;

/**
 * @author lamado
 *
 */

public class ExcepcionMismoEmail extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExcepcionMismoEmail () {
		
	}

	public ExcepcionMismoEmail (String mensaje) {
		
		super ("Ese mail ya ha sido registrado");
	}

}
