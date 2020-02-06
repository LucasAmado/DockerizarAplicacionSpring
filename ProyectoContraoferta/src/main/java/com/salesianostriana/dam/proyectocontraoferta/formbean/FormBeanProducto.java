/**
 * 
 */
package com.salesianostriana.dam.proyectocontraoferta.formbean;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.salesianostriana.dam.proyectocontraoferta.model.Categoria;
import com.salesianostriana.dam.proyectocontraoferta.model.Cupon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Esta clase sirve para poder coger la url de la img de los productos
 * 
 * @author lamado
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormBeanProducto {
	// Atributos
	private String nombre, descripcion;
	private double precioU, precioOriginal;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaCaducidad;
	private boolean caducado;
	private Categoria categoria;
	private Cupon cupon;


}
