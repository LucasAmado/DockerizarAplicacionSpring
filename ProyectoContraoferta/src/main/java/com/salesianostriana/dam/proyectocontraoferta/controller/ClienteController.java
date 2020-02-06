package com.salesianostriana.dam.proyectocontraoferta.controller;

/**
 * Esta clase sirve para restringir las rutas de los clientes. Es decir,
 * queSirve para gestionar aquellas acciones que sean sólo puedan realizar los
 * clientes.
 * 
 * @author lamado
 *
 */

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.salesianostriana.dam.proyectocontraoferta.model.Compra;
import com.salesianostriana.dam.proyectocontraoferta.model.LineaPedido;
import com.salesianostriana.dam.proyectocontraoferta.model.Producto;
import com.salesianostriana.dam.proyectocontraoferta.model.Usuario;
import com.salesianostriana.dam.proyectocontraoferta.services.CarritoServicio;
import com.salesianostriana.dam.proyectocontraoferta.services.CompraServicio;
import com.salesianostriana.dam.proyectocontraoferta.services.ProductoServicio;
import com.salesianostriana.dam.proyectocontraoferta.services.UsuarioServicio;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

	@Autowired
	UsuarioServicio usuarioServicio;

	@Autowired
	private CarritoServicio carritoServicio;

	@Autowired
	private CompraServicio compraServicio;

	@Autowired
	private ProductoServicio productoServicio;

	
	/**
	 * Sirve para ver la página de contacto
	 * 
	 * @param model
	 * @param principal
	 * @return vista de contacto
	 */
	@GetMapping("/contacto")
	public String mostrarContacto(Model model, Principal principal) {
		model.addAttribute("logeado", usuarioServicio.buscarUsuarioLogeado(model, principal));
		return "cliente/contacto.html";
	}

	/**
	 * Sirve para poder la página con información sobre Contraoferta
	 * 
	 * @param model
	 * @param principal
	 * @return vista de la información
	 */
	@GetMapping("/informacion")
	public String mostrarPaginaInfromacion(Model model, Principal principal) {
		Usuario user = usuarioServicio.buscarUsuarioLogeado(model, principal);
		model.addAttribute("logeado", user);
		return "cliente/informacion.html";
	}

	/**
	 * Sirve para acceder al carrito de compra
	 * 
	 * @param model
	 * @param principal
	 * @return vista del carrito
	 */
	@GetMapping("/carrito")
	public String mostrarCarrito(Model model, Principal principal) {
		model.addAttribute("logeado", usuarioServicio.buscarUsuarioLogeado(model, principal));
		model.addAttribute("listaProductos", carritoServicio.getProductosCarrito());
		return "cliente/carrito.html";
	}

	/**
	 * Sirve para añadir un producto al carrito a partir de su id en caso de que
	 * exista, no esté borrado ni esté caducado. Si incumple alguna de estas
	 * condiciones no se añadirá el producto y se hace una redirección a controller
	 * /
	 * 
	 * @param id    id del producto que se quiere añadir al carrito
	 * @param model
	 * @return vista carrito si el producto cumple todos los requisitos
	 */
	@GetMapping("/anadir-carrito/{id}")
	public String meterproductoAlCarrito(@PathVariable("id") Long id, Model model) {
		Producto p = productoServicio.findById(id);

		if (p != null && p.isCaducado() == false && p.getEstado() != 0) {
			carritoServicio.addProducto(productoServicio.findById(id));
			return "redirect:/cliente/carrito";
		} else {
			return "redirect:/";
		}

	}

	/**
	 * Sirve para borrar un producto del carrito en caso de que exista
	 * 
	 * @param id id del producto que se quiere borrar
	 * @return redirección controller /cliente/carrito
	 */
	@GetMapping("/carrito/borrar-producto/{id}")
	public String removeProductFromCart(@PathVariable("id") Long id) {
		Producto p = productoServicio.findById(id);
		if (p != null) {
			carritoServicio.borrarProducto(p);
		}
		return "redirect:/cliente/carrito";
	}

	/**
	 * Sirve para mostrar el total de la compra del carrito. En caso de que uno de
	 * los productos de alguna línea de pedido tenga un cupón de descuento y haya al
	 * menos la cantidad mínima de ese producto establecida en el cupón se aplicará
	 * un descuento sobre el precio total de la línea
	 * 
	 * @return double total_carrito
	 */
	@ModelAttribute("total_carrito")
	public Double CalcularTotalCarrito() {
		List<LineaPedido> carrito = carritoServicio.getProductosCarrito();
		double total = 0;
		if (carrito != null) {
			for (LineaPedido lp : carrito) {
				total += lp.getProducto().getPrecioU() * lp.getCantidad();
				/**
				 * El descuento se hace sobre cada producto, es decir, sobre el precio unitario.
				 * Si se añaden más productos el descuento se incrementa proporcionalmente
				 */
				if (lp.getCantidad() >= lp.getProducto().getCupon().getUnidadesMin()
						&& lp.getProducto().getCupon().getId() != 1) {
					lp.setDescuentoLinea(total * lp.getProducto().getCupon().getDescuento() / 100);
					total -= lp.getDescuentoLinea();
				}
			}
		}

		return total;
	}

	/**
	 * Sirve para mostrar la vista de la compra, sólo en el caso de que en el
	 * carrito hay alguna línea de pedido. En este controller se crea una compra
	 * vacía, a las líneas de pedido se les setea esa compra, y a la compra se le
	 * setean los datos requeridos.
	 * 
	 * @param model
	 * @param principal
	 * @return si no hay productos redirección al controller /cliente/carrito. Si lo
	 *         hay se manda a la vista de compra
	 */
	@GetMapping("/tramitar-compra")
	public String tramitarCompra(Model model, Principal principal) {
		Usuario logeado = usuarioServicio.buscarUsuarioLogeado(model, principal);
		model.addAttribute("logeado", logeado);
		List<LineaPedido> lineasCarrito = carritoServicio.getProductosCarrito();

		if (lineasCarrito.size() > 0) { // No se crean compras vacías
			Compra compra = new Compra();

			/**
			 * Se le asigna una compra a cada línea del carrito
			 */
			for (LineaPedido lp : lineasCarrito) {
				lp.setCompra(compra);
			}

			compra.setTotalCompra(carritoServicio.calcularTotalCompra());
			compra.setListaLineas(lineasCarrito);
			compra.setUsuario(logeado);
			compra.setNombreComprador(logeado.getNombre());
			compra.setApellidosComprador(logeado.getApellidos());

			model.addAttribute("compra", compraServicio.save(compra));
			carritoServicio.borrarCarrito();
			return "cliente/compra.html";
		} else {
			return "redirect:/cliente/carrito";
		}

	}

}
