package com.salesianostriana.dam.proyectocontraoferta.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.salesianostriana.dam.proyectocontraoferta.model.LineaPedido;
import com.salesianostriana.dam.proyectocontraoferta.model.Producto;
import com.salesianostriana.dam.proyectocontraoferta.repo.LineaPedidoRepository;

import lombok.Getter;

/**
 * Esta clase contiene los métodos crud usados en el carrito
 * 
 * @author lamado
 *
 */

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CarritoServicio {

	@Autowired
	private LineaPedidoRepository lineaPedidoRepository;

	@Getter
	private List<LineaPedido> productosCarrito = new ArrayList<LineaPedido>();

	public CarritoServicio(LineaPedidoRepository lineaPedidoRepository) {
		this.lineaPedidoRepository = lineaPedidoRepository;
	}

	/**
	 * Si el producto ya está en el carrito se incrementará en uno la cantidad en la
	 * línea de pedido. Si por el contrario el producto no está en él se añadirá con
	 * cantidad 1.
	 * 
	 * @param p producto que se añade
	 */
	public void addProducto(Producto p) {
		Boolean lineaExistente = false;

		for (LineaPedido lp : productosCarrito) {
			if (lp.getProducto().getId() == p.getId()) {
				lp.setCantidad(lp.getCantidad() + 1);
				lp.setTotalLinea(lp.calcularPrecioLinea());
				lineaExistente = true;
			}
		}
		if (lineaExistente == false) {
			productosCarrito.add(new LineaPedido(p, 1));
		}

	}

	/**
	 * Método que disminuye en 1 la cantidad de productos de una línea de pedido. Si
	 * la cantidad de productos de esa línea es de 1 entonces línea se borra del
	 * carrito.
	 * 
	 * @param p producto a borrar
	 */

	public void borrarProducto(Producto p) {

		for (int i = 0; i < productosCarrito.size(); i++) {

			if (productosCarrito.get(i).getProducto().getId() == p.getId()) {
				if (productosCarrito.get(i).getCantidad() == 1) {
					productosCarrito.remove(productosCarrito.get(i));

				} else if (productosCarrito.get(i).getCantidad() > 1) {
					productosCarrito.get(i).setCantidad(productosCarrito.get(i).getCantidad() - 1);
					productosCarrito.get(i).setTotalLinea(productosCarrito.get(i).calcularPrecioLinea());
				}
			}

		}
	}

	/**
	 * Este método sirve para calcular el total. Se recorren todas las línea de
	 * pedido, cogiendo su precio total y sumándose.
	 * 
	 * @return double total de la compra
	 */
	public double calcularTotalCompra() {
		double total = 0;
		for (LineaPedido lp : productosCarrito) {
			total += lp.getTotalLinea();
		}
		return total;

	}

	/**
	 * Metodo que elimina todo el carrito
	 */
	public void borrarCarrito() {
		productosCarrito.clear();
	}

}