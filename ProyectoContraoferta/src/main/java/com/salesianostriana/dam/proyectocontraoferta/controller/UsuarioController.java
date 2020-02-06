package com.salesianostriana.dam.proyectocontraoferta.controller;

/**
 * Esta clase sirve para especificar las acciones que pueden ser llevadas a cabo por los administradores y los clientes.
 */

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.salesianostriana.dam.proyectocontraoferta.excepciones.ExcepcionMismoEmail;
import com.salesianostriana.dam.proyectocontraoferta.model.Compra;
import com.salesianostriana.dam.proyectocontraoferta.model.Pager;
import com.salesianostriana.dam.proyectocontraoferta.model.Producto;
import com.salesianostriana.dam.proyectocontraoferta.model.Usuario;
import com.salesianostriana.dam.proyectocontraoferta.services.CategoriaServicio;
import com.salesianostriana.dam.proyectocontraoferta.services.CompraServicio;
import com.salesianostriana.dam.proyectocontraoferta.services.ProductoServicio;
import com.salesianostriana.dam.proyectocontraoferta.services.UsuarioServicio;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioServicio usuarioServicio;
	@Autowired
	private ProductoServicio productoServicio;

	@Autowired
	private CompraServicio compraServicio;

	@Autowired
	private CategoriaServicio categoriaServicio;

	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 6;
	private static final int[] PAGE_SIZES = { 6, 12, 21 };

	/**
	 * Sirve para mostrar todos los productos en el caso de que el parámetro nombre
	 * sea nulo. En el caso contario se muestran sólo aquellos productos que
	 * contengan el nombre buscado
	 * 
	 * @param pageSize
	 * @param page
	 * @param nombre
	 * @param model
	 * @param principal
	 * @return vista inicio
	 */
	@GetMapping({ "/productos-buscados", "/", "/inicio" })
	public String mostrarProductosPaginaInicio(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page, @RequestParam("nombre") Optional<String> nombre, Model model,
			Principal principal) {

		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		String evalNombre = nombre.orElse(null);

		Page<Producto> producto = null;
		if (evalNombre == null) {
			producto = productoServicio.findProductosSinCaducar(PageRequest.of(evalPage, evalPageSize));
		} else {
			producto = productoServicio.findProductosSinCaducarByNombre(evalNombre,
					PageRequest.of(evalPage, evalPageSize));
		}

		Pager pager = new Pager(producto.getTotalPages(), producto.getNumber(), BUTTONS_TO_SHOW);

		model.addAttribute("logeado", usuarioServicio.buscarUsuarioLogeado(model, principal));
		model.addAttribute("listaProductos", producto);
		model.addAttribute("listaCategorias", categoriaServicio.findAll());
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);

		return "comun/inicio.html";
	}

	/**
	 * 
	 * Sirve para poder filtrar los productos por el nombre de la categría a la que
	 * pertenezcan. El parámetro nombre corresponde con el nombre de la categoría
	 * 
	 * @param nombre
	 * @param pageSize
	 * @param page
	 * @param model
	 * @param principal
	 * @return vista categorias
	 */
	@GetMapping("/categoria/{nombre}")
	public String filtrarPorCategoriaPaginable(@PathVariable("nombre") String nombre,
			@RequestParam("pageSize") Optional<Integer> pageSize, @RequestParam("page") Optional<Integer> page,
			Model model, Principal principal) {
		model.addAttribute("logeado", usuarioServicio.buscarUsuarioLogeado(model, principal));

		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		Page<Producto> productosCategorias;

		productosCategorias = productoServicio.findByCategoriaNombreSinCaducarPageable(nombre,
				PageRequest.of(evalPage, evalPageSize));

		Pager pager = new Pager(productosCategorias.getTotalPages(), productosCategorias.getNumber(), BUTTONS_TO_SHOW);

		model.addAttribute("categoria", categoriaServicio.findCategoriaByNombre(nombre));
		model.addAttribute("listaCategorias", categoriaServicio.findAll());
		model.addAttribute("listaProductos", productosCategorias);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);

		return "comun/categorias.html";
	}

	/**
	 * Sirve para mostrar el formulario de registro de un nuevo usuario
	 * 
	 * @param model
	 * @return formulario de registro
	 */
	@GetMapping({ "/registrarse", "/admin/new-usuario" })
	public String mostrarFormularioRegistro(Model model, Principal principal) {

		model.addAttribute("logeado", usuarioServicio.buscarUsuarioLogeado(model, principal));

		model.addAttribute("user", new Usuario());
		return "comun/usuario/nuevo.html";
	}

	/**
	 * Sirve para guardar los datos del nuevo usuario. Si el usuario logeado es un
	 * admin se redirecciona al controller /admin/tabla-usuarios. En caso contrario
	 * se redirecciona a /login
	 * 
	 * @param user  usuario nuevo
	 * @param model model
	 * @return redirección al controller correspondiente (dependiendo de si es un
	 *         admin o un cliente) una vez añadido un usuario
	 */
	@PostMapping("/addUsuario")
	public String anadirUsuario(@ModelAttribute("user") Usuario u, Model model, Principal principal)
			throws ExcepcionMismoEmail {
		Usuario logeado = usuarioServicio.buscarUsuarioLogeado(model, principal);

		for (Usuario user : usuarioServicio.findAll()) {
			if (user.getEmail().equalsIgnoreCase(u.getEmail())) {
				throw new ExcepcionMismoEmail("mismoEmail");
			}
		}

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		u.setPassword(passwordEncoder.encode(u.getPassword()));
		usuarioServicio.save(u);

		if (logeado != null) {
			return "redirect:/admin/tabla-usuarios";
		} else {
			return "redirect:/login";
		}
	}

	/**
	 * Sirve para que el usuario logeado acceda al formulario de edición de sus
	 * datos
	 * 
	 * @param model
	 * @param principal
	 * @return vista fomrulario edición usuario
	 */
	@GetMapping("/editar-perfil")
	public String verFormularioEditarUsuario(Model model, Principal principal) {
		Usuario logeado = usuarioServicio.buscarUsuarioLogeado(model, principal);
		model.addAttribute("logeado", logeado);
		model.addAttribute("user", logeado);
		return "comun/usuario/editar.html";

	}

	/**
	 * Sirve para poder borrar a un usuario a partir de su id. Una vez que se
	 * comprueba que el usuario existe (en caso contrario se manda al controller /)
	 * se recorren todas las compras en busca de las que haya realizado el usuario,
	 * para setear el usuaio de esas compras a nulo. Después se compueba si el
	 * usuario logeado es admin. Si es así y elimina a un usuario distinto a sí
	 * mismo entonces se borra el usuario y se le redirecciona al controller
	 * /admin/tabla-usuarios. En caso de que no cumpla alguna de estas dos
	 * condiciones se borra el usuario y se redirecciona al controller /login
	 * 
	 * @param u         usuario que se quiere borrar
	 * @param model
	 * @param principal
	 * @return
	 */
	@GetMapping({ "/admin/borrar-usuario/{id}", "/darme-baja/{id}" })
	public String borrarUsuario(@PathVariable("id") Usuario u, Model model, Principal principal) {
		Usuario logeado = usuarioServicio.buscarUsuarioLogeado(model, principal);

		if (u != null) {
			for (Compra allCompras : compraServicio.findAll()) {
				if (allCompras.getUsuario().getId() == u.getId()) {
					allCompras.setUsuario(null);
				}
			}

			if (logeado != u && logeado.isAdmin()) {
				usuarioServicio.delete(u);
				return "redirect:/admin/tabla-usuarios";
			}
			usuarioServicio.delete(u);
			return "redirect:/login";

		}

		return "redirect:/";
	}

	/**
	 * Sirve para guardar los nuevos datos del usuario. Si el usuario logeado es un
	 * admin se redirecciona al controller /admin/tabla-usuarios y si es un cliente
	 * se redirecciona al controller /
	 * 
	 * @param u usuario
	 * @return redirección al controller de la gestión de usuarios
	 */
	@PostMapping("/edicion-usuario")
	public String procesarEdicionUsuario(@ModelAttribute("user") Usuario u, Model model, Principal principal) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		u.setPassword(passwordEncoder.encode(u.getPassword()));
		usuarioServicio.edit(u);
		if (usuarioServicio.buscarUsuarioLogeado(model, principal).isAdmin()) {
			return "redirect:/admin/tabla-usuarios";
		} else {
			return "redirect:/";
		}
	}

	/**
	 * Sirve para ver los datos más detalladamente de un producto en concreto. Pero
	 * sólo se ven si el producto existe y no está borrado.
	 * 
	 * @param id        id del producto del que se quieren ver los detalles
	 * @param model
	 * @param principal
	 * @return vista ficha del producto si cumple los requisitos. En caso contrario
	 *         redirección al controller /inicio
	 */
	@GetMapping("/datos-producto/{id}")
	public String verDetallesProducto(@PathVariable("id") long id, Model model, Principal principal) {
		model.addAttribute("logeado", usuarioServicio.buscarUsuarioLogeado(model, principal));
		Producto p = productoServicio.findById(id);

		if (p != null && p.getEstado() != 0) {
			model.addAttribute("producto", p);
			return "comun/fichaProducto.html";
		} else {
			return "redirect:/inicio";
		}

	}

	/**
	 * Sirve para ver el historial de compras realizadas. Si el usuario logeado es
	 * un administrador se le muestran todas las compras, pero si es un cliente sólo
	 * se le muestran sus propias compras
	 * 
	 * @param pageSize
	 * @param page
	 * @param model
	 * @param principal
	 * @return vista historial de compras
	 */
	@GetMapping("/historial-compras")
	public String mostrarCompras(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page, Model model, Principal principal) {
		Usuario logeado = usuarioServicio.buscarUsuarioLogeado(model, principal);
		model.addAttribute("logeado", logeado);
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		Page<Compra> compras;
		if (logeado.isAdmin()) {
			compras = compraServicio.findAllPageable(PageRequest.of(evalPage, evalPageSize));
		} else {
			compras = compraServicio.findComprasUsuarioConcreto(logeado, PageRequest.of(evalPage, evalPageSize));
		}

		Pager pager = new Pager(compras.getTotalPages(), compras.getNumber(), BUTTONS_TO_SHOW);

		model.addAttribute("listaCompras", compras);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);

		return "comun/compras/historial.html";
	}

	/**
	 * Sirve para ver todos los detalles de manera más concreta de una compra. Si el
	 * usuario logeado es un cliente que intenta acceder a los detalles de la compra
	 * de otro cliente se redirecciona al controller /historial-compras. Sólo un
	 * administrador puede ver cualquier compra.
	 * 
	 * @param id        id de la compra que se quiere ver
	 * @param model
	 * @param principal
	 * @return
	 */
	@GetMapping("/detalles-compra/{id}")
	public String mostrarDetallesVenta(@PathVariable("id") long id, Model model, Principal principal) {
		Usuario logeado = usuarioServicio.buscarUsuarioLogeado(model, principal);
		model.addAttribute("logeado", logeado);
		Compra compraBuscada = compraServicio.findById(id);
		model.addAttribute("compra", compraBuscada);
		model.addAttribute("listaLineas", compraBuscada.getListaLineas());

		if (!logeado.isAdmin() && compraBuscada.getNombreComprador() != logeado.getNombre()
				&& compraBuscada.getApellidosComprador() != logeado.getApellidos()) {
			return "redirect:/historial-compras";
		}
		return "comun/compras/factura.html";
	}
}
