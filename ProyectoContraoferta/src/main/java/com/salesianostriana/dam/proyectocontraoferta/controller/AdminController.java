package com.salesianostriana.dam.proyectocontraoferta.controller;

/**
 * Esta claseSirve para restringir las rutas del administrador. Es decir, queSirve para gestionar aquellas acciones que sean sólo puedan realizar los administradores
 * 
 * @author lamado
 *
 */

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.salesianostriana.dam.proyectocontraoferta.model.Categoria;
import com.salesianostriana.dam.proyectocontraoferta.model.Cupon;
import com.salesianostriana.dam.proyectocontraoferta.model.Pager;
import com.salesianostriana.dam.proyectocontraoferta.model.Producto;
import com.salesianostriana.dam.proyectocontraoferta.model.Usuario;
import com.salesianostriana.dam.proyectocontraoferta.services.CategoriaServicio;
import com.salesianostriana.dam.proyectocontraoferta.services.CompraServicio;
import com.salesianostriana.dam.proyectocontraoferta.services.CuponServicio;
import com.salesianostriana.dam.proyectocontraoferta.services.ProductoServicio;
import com.salesianostriana.dam.proyectocontraoferta.services.UsuarioServicio;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	CategoriaServicio categoriaServicio;

	@Autowired
	ProductoServicio productoServicio;

	@Autowired
	UsuarioServicio usuarioServicio;

	@Autowired
	CompraServicio compraServicio;

	@Autowired
	CuponServicio cuponServicio;

	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;
	private static final int INITIAL_PAGE_SIZE = 6;
	private static final int[] PAGE_SIZES = { 6, 12, 21 };

	// PRODUCTOS

	/**
	 * Sirve para mostrar el menú de administración
	 * 
	 * @param model
	 * @param principal
	 * @return vista menú del admin
	 */
	@GetMapping({ "menu-gestion", "/" })
	public String mostrarMenuGestionProductos(Model model, Principal principal) {
		model.addAttribute("logeado", usuarioServicio.buscarUsuarioLogeado(model, principal));
		return "admin/menuGestion.html";
	}

	/**
	 * Sirve para mostrar la tabla de productos del admin en función del parámentro
	 * nombre. En caso de ser nulo muestra todos los productos. En el caso contrario
	 * devuelve los productos que contengan el nombre buscado
	 * 
	 * @param pageSize
	 * @param page
	 * @param nombre
	 * @param model
	 * @param principal
	 * @return vista tabla de los productos
	 */
	@GetMapping("/tabla-productos")
	public String mostrarGestionProductos(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page, @RequestParam("nombre") Optional<String> nombre, Model model,
			Principal principal) {
		model.addAttribute("logeado", usuarioServicio.buscarUsuarioLogeado(model, principal));
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		String evalNombre = nombre.orElse(null);

		Page<Producto> producto = null;
		if (evalNombre == null) {
			producto = productoServicio.findProductosSinCaducar(PageRequest.of(evalPage, evalPageSize));
		} else {
			producto = productoServicio.findProductosNoBorradosByNombre(evalNombre,
					PageRequest.of(evalPage, evalPageSize));
		}
		Pager pager = new Pager(producto.getTotalPages(), producto.getNumber(), BUTTONS_TO_SHOW);

		model.addAttribute("listaProductos", producto);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);

		return "admin/gestionProductos/caducidad/todos.html";
	}

	/**
	 * Sirve para mostrar la tabla de productos caducados del admin en función del
	 * parámentro nombre. En caso de ser nulo muestra todos los productos caducados.
	 * En el caso contrario devuelve los productos que contengan el nombre buscado
	 * 
	 * @param pageSize
	 * @param page
	 * @param nombre
	 * @param model
	 * @param principal
	 * @return vista tabla de productos caducados
	 */
	@GetMapping("/tabla-productos/caducados")
	public String mostrarGestionProductosCaducados(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page, @RequestParam("nombre") Optional<String> nombre, Model model,
			Principal principal) {
		model.addAttribute("logeado", usuarioServicio.buscarUsuarioLogeado(model, principal));
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		String evalNombre = nombre.orElse(null);

		Page<Producto> producto = null;

		if (evalNombre == null) {
			producto = productoServicio.findProductosCaducados(PageRequest.of(evalPage, evalPageSize));
		} else {
			producto = productoServicio.findProductosCaducadosByNombre(evalNombre,
					PageRequest.of(evalPage, evalPageSize));
		}
		Pager pager = new Pager(producto.getTotalPages(), producto.getNumber(), BUTTONS_TO_SHOW);

		model.addAttribute("listaProductos", producto);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);

		return "admin/gestionProductos/caducidad/caducados.html";
	}

	/**
	 * Sirve para mostrar la tabla de productos que no estén caducados en función
	 * del parámentro nombre. En caso de ser nulo muestra todos los productos. En el
	 * caso contrario devuelve los productos que contengan el nombre buscado
	 * 
	 * @param pageSize
	 * @param page
	 * @param nombre
	 * @param model
	 * @param principal
	 * @return vista tabla de productos no caducados
	 */
	@GetMapping("/tabla-productos/no-caducados")
	public String mostrarGestionProductosSinCaducar(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page, @RequestParam("nombre") Optional<String> nombre, Model model,
			Principal principal) {
		model.addAttribute("logeado", usuarioServicio.buscarUsuarioLogeado(model, principal));
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

		model.addAttribute("listaProductos", producto);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);

		return "admin/gestionProductos/caducidad/noCaducados.html";
	}

	/**
	 * Sirve para mostrar el formulario de creación de un nuevo producto
	 * 
	 * @param model
	 * @param principal
	 * @return vista formulario nuevo producto
	 */
	@GetMapping("/new-producto")
	public String mostrarFormularioProductos(Model model, Principal principal) {
		model.addAttribute("logeado", usuarioServicio.buscarUsuarioLogeado(model, principal));
		model.addAttribute("hoy", LocalDate.now());
		model.addAttribute("producto", new Producto());
		model.addAttribute("listaCategorias", categoriaServicio.findAll());
		model.addAttribute("listaCupones", cuponServicio.findAll());
		return "admin/gestionProductos/nuevo.html";
	}

	/**
	 * recibe la petición del admin al darle al botón de 'añadir producto' que
	 * aparece en el formulario de crear producto. Sólo crea el producto después de
	 * confirmar que no hay otro producto con el mismo nombre.
	 * 
	 * @param p    nuevo producto
	 * @param file
	 * @return redirección al controller /admin/tabla-productos
	 */
	@PostMapping("/addProducto")
	public String guardarProducto(@ModelAttribute("producto") Producto p, @RequestParam("file") MultipartFile file) {
		for (Producto prod : productoServicio.findAll()) {
			if (prod.getNombre().equalsIgnoreCase(p.getNombre())) {
				return "redirect:/admin/tabla-productos";
			}
		}
		productoServicio.add(p, file);
		productoServicio.save(p);
		return "redirect:/admin/tabla-productos";

	}

	/**
	 * comprueba si el producto (encontradop por su id) que se quiere editar existe
	 * y comprueba que no esté en la tabla de históricos. En caso de cumplir esas
	 * dos condiciones muestra el formulario de edición de productos
	 * 
	 * @param id        id del producto buscado
	 * @param model
	 * @param principal
	 * @return redirección al fomulario de edición del producto si cumple todos los
	 *         requisitos. EN caso de no hacerlo se manda al controller
	 *         /admin/tabla-productos
	 */
	@GetMapping("/editar-producto/{id}")
	public String mostrarFormularioEdicionProducto(@PathVariable("id") long id, Model model, Principal principal) {
		model.addAttribute("logeado", usuarioServicio.buscarUsuarioLogeado(model, principal));
		Producto p = productoServicio.findById(id);

		if (p != null && p.getEstado() != 0) {
			model.addAttribute("producto", p);
			model.addAttribute("listaCategorias", categoriaServicio.findAll());
			model.addAttribute("listaCupones", cuponServicio.findAll());
			return "admin/gestionProductos/editar.html";
		} else {
			return "redirect:/admin/tabla-productos";
		}

	}

	/**
	 * procesa la petición del admin de editar un producto y guarda el producto
	 * editado
	 * 
	 * @param p    producto que se va a editar
	 * @param file objeto tipo MultipartFile que se utiliza para subir una imagen
	 * @return redirección al controller /admin/tabla-productos
	 */
	@PostMapping("/edicion-producto")
	public String procesarEdicionProducto(@ModelAttribute("producto") Producto p,
			@RequestParam("file") MultipartFile file) {

		if (!file.isEmpty()) {
			productoServicio.add(p, file);
		} else {
			p.setFileUrl(productoServicio.findById(p.getId()).getFileUrl());
			productoServicio.edit(p);
		}

		return "redirect:/admin/tabla-productos";
	}

	/**
	 * Sirve para cambiar el estado del producto (encontrado por su id) a 0 para así
	 * mandarlo a la tabla de históricos. Aunque previamente se comprueba que el
	 * producto exista y que no tenga ya su estado a 0.
	 * 
	 * @param p producto que se quiere borrar
	 * @return redirección al controller /admin/tabla-productos
	 */
	@GetMapping("/borrar-producto/{id}")
	public String borrarProducto(@PathVariable("id") Producto p) {
		if (p != null && p.getEstado() != 0) {
			p.setEstado(0);
			productoServicio.edit(p);
		}

		return "redirect:/admin/tabla-productos";
	}

	/**
	 * Sirve para mostrar la tabla de productos que han sido borrados. En función de
	 * si el parámetro nombre es nulo o no muestra todos los productos borrados o
	 * solamente los que contengan ese nombres
	 * 
	 * @param pageSize
	 * @param page
	 * @param nombre
	 * @param model
	 * @param principal
	 * @return vista tabla de productos borrados/historico
	 */
	@GetMapping("tabla-productos/historico")
	public String verTablaHistoricoProductos(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page, @RequestParam("nombre") Optional<String> nombre, Model model,
			Principal principal) {
		model.addAttribute("logeado", usuarioServicio.buscarUsuarioLogeado(model, principal));
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);

		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		String evalNombre = nombre.orElse(null);

		Page<Producto> producto = null;

		if (evalNombre == null) {
			producto = productoServicio.findAllBorrados(PageRequest.of(evalPage, evalPageSize));
		} else {
			producto = productoServicio.findAllBorradosByNombre(evalNombre, PageRequest.of(evalPage, evalPageSize));
		}
		Pager pager = new Pager(producto.getTotalPages(), producto.getNumber(), BUTTONS_TO_SHOW);

		model.addAttribute("listaProductos", producto);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);

		return "admin/gestionProductos/historico.html";
	}

	/**
	 * Sirve para cambiar el estado de un producto, previa comprobación de que su
	 * estado es 0, es decir, que se encuentra borrado.
	 * 
	 * @param p producto al que se le quiere cambiar el estado
	 * @return redirección al controller /admin/tabla-productos/historico
	 */
	@GetMapping("/activar-producto/{id}")
	public String activarProducto(@PathVariable("id") Producto p) {
		if (p != null && p.getEstado() == 0) {
			p.setEstado(1);
			productoServicio.edit(p);
		}

		return "redirect:/admin/tabla-productos/historico";
	}

	// USUARIOS

	/**
	 * Sirve para mostrar una tabla con todos los usuarios registrados
	 * 
	 * @param pageSize
	 * @param page
	 * @param nombre
	 * @param model
	 * @param principal
	 * @return vista tabla de usuarios
	 */
	@GetMapping("/tabla-usuarios")
	public String mostrarGestionUsuarios(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page, @RequestParam("nombre") Optional<String> nombre, Model model,
			Principal principal) {
		model.addAttribute("logeado", usuarioServicio.buscarUsuarioLogeado(model, principal));
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		String evalNombre = nombre.orElse(null);

		Page<Usuario> usuarios = null;

		if (evalNombre == null) {
			usuarios = usuarioServicio.findAllPageable(PageRequest.of(evalPage, evalPageSize));
		} else {
			usuarios = usuarioServicio.findByNombre(evalNombre, PageRequest.of(evalPage, evalPageSize));
		}

		Pager pager = new Pager(usuarios.getTotalPages(), usuarios.getNumber(), BUTTONS_TO_SHOW);

		model.addAttribute("listaUsuarios", usuarios);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);

		return "admin/gestionUsuarios/gestion.html";
	}

	/**
	 * Sirve para mostrar el formulario de edición. Primero se comprueba si el
	 * usuario (a partir de su id) existe y si el usurio que esté logeado ne ese
	 * momento es administrados o no. En caso de ser adminsitrador puede editar
	 * cualquier usuario, pero si es un cliente sólo puede editar sus propios datos.
	 * 
	 * @param id        id del usuario que se quiere editar
	 * @param model
	 * @param principal
	 * @return formulario de edición de usuario si cumple los requisitos. En caso
	 *         contrario se redirecciona al controller /admin/tabla-usuarios
	 */
	@GetMapping("/editar-usuario/{id}")
	public String mostrarFormularioEdicionUsuario(@PathVariable("id") long id, Model model, Principal principal) {
		model.addAttribute("logeado", usuarioServicio.buscarUsuarioLogeado(model, principal));

		Usuario u = usuarioServicio.findById(id);
		if (u != null) {
			model.addAttribute("user", u);
			model.addAttribute("listaCategorias", categoriaServicio.findAll());
			return "comun/usuario/editar.html";
		} else {
			return "redirect:/admin/tabla-usuarios";
		}

	}

	// CATEGORÍAS

	/**
	 * Sirve para mostrar la tabla con todas las categorías. En fucnión de que el
	 * parámetro nombre sea nulo o no muestra todas las categorías y las categorías
	 * que contengan el nombre buscado.
	 * 
	 * @param pageSize
	 * @param page
	 * @param nombre
	 * @param model
	 * @param principal
	 * @return visa tabla de categorías
	 */
	@GetMapping("/tabla-categorias")
	public String mostrarTablaCategorias(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page, @RequestParam("nombre") Optional<String> nombre, Model model,
			Principal principal) {
		model.addAttribute("logeado", usuarioServicio.buscarUsuarioLogeado(model, principal));
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		String evalNombre = nombre.orElse(null);

		Page<Categoria> categorias = null;

		if (evalNombre == null) {
			categorias = categoriaServicio.findAllPageable(PageRequest.of(evalPage, evalPageSize));
		} else {
			categorias = categoriaServicio.findByNombrePageable(evalNombre, PageRequest.of(evalPage, evalPageSize));
		}

		Pager pager = new Pager(categorias.getTotalPages(), categorias.getNumber(), BUTTONS_TO_SHOW);

		model.addAttribute("listaCategorias", categorias);
		model.addAttribute("categoria", new Categoria());
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		return "admin/gestionCategorias/gestion.html";
	}

	/**
	 * Sirve para guardar una nueva categoría en caso de que no exista otra
	 * categoría con el mismo nombre
	 * 
	 * @param c     nueva categoría
	 * @param model
	 * @return redirección al controller /admin/tabla-categorias"
	 */
	@PostMapping("/addcategoria")
	public String guardarCategoria(@ModelAttribute("categoria") Categoria c, Model model) {
		for (Categoria lista : categoriaServicio.findAll()) {
			if (lista.getNombre().equalsIgnoreCase(c.getNombre())) {
				return "redirect:/admin/tabla-categorias";
			}
		}
		categoriaServicio.save(c);
		return "redirect:/admin/tabla-categorias";
	}

	/**
	 * Sirve para mostrar el formulario de edición de categorías en el caso de que
	 * la categoría exista
	 * 
	 * @param id        id de la categoría que se quiere editar
	 * @param model
	 * @param principal
	 * @return formulario de edición de categorías si el producto existe. En caso
	 *         contrario redirecciona al controller /admin/tabla-categorias
	 */
	@GetMapping("/editar-categoria/{id}")
	public String verFormularioEditarCategoria(@PathVariable("id") long id, Model model, Principal principal) {
		model.addAttribute("logeado", usuarioServicio.buscarUsuarioLogeado(model, principal));

		Categoria c = categoriaServicio.findById(id);
		if (c != null) {
			model.addAttribute("categoria", c);
			return "admin/gestionCategorias/editar.html";
		} else {
			return "redirect:/admin/tabla-categorias";
		}

	}

	/**
	 * Sirve para guardar la cetgoría editada
	 * 
	 * @param c categoría editada
	 * @return redirección al controller /admin/tabla-categorias
	 */
	@PostMapping("/edicion-categoria")
	public String procesarEdicionCategoria(@ModelAttribute("categoria") Categoria c) {
		categoriaServicio.edit(c);
		return "redirect:/admin/tabla-categorias";
	}

	/**
	 * Sirve para borrar una categoría a partir de su id. Primero se comprueba que
	 * esa categoría no sea "sin-categorizar" y que exista. En caso de cumplir ambos
	 * requisitos se recorren todas las categorías y se setea la categoría de todos
	 * los productos de esa categoría a sin-categorizar y posterioremente se elimina
	 * la categoría.
	 * 
	 * @param c categoría que se quiere borrar
	 * @return controller /admin/tabla-categorias
	 */
	@GetMapping("/borrar-categoria/{id}")
	public String borrarCategoria(@PathVariable("id") Categoria c) {
		Categoria sinCategorizar = categoriaServicio.findById((long) 0);
		if (c.getId() != sinCategorizar.getId() && c != null) {
			for (Categoria allCategorias : categoriaServicio.findAll()) {
				if (allCategorias.getId() == c.getId()) {
					List<Producto> productosCategoria = allCategorias.getListaProductos();
					for (Producto p : productosCategoria) {
						p.setCategoria(sinCategorizar);
					}
				}
			}
			categoriaServicio.edit(sinCategorizar);
			categoriaServicio.delete(c);
		}
		return "redirect:/admin/tabla-categorias";
	}

	// CUPONES

	/**
	 * Sirve para mostrar la tabla de cupones. En función de si el parátro nombre es
	 * nulo o no se muestran todos los cupones o solo los que tengan el nombre
	 * buscado
	 * 
	 * @param pageSize
	 * @param page
	 * @param nombre
	 * @param model
	 * @param principal
	 * @return vista de la tabla de cupones
	 */
	@GetMapping("/tabla-cupones")
	public String mostrarTbalaCupones(@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page, @RequestParam("nombre") Optional<String> nombre, Model model,
			Principal principal) {
		model.addAttribute("logeado", usuarioServicio.buscarUsuarioLogeado(model, principal));
		int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		String evalNombre = nombre.orElse(null);

		Page<Cupon> cupones = null;

		if (evalNombre == null) {
			cupones = cuponServicio.findAllPageable(PageRequest.of(evalPage, evalPageSize));
		} else {
			cupones = cuponServicio.findByNombre(evalNombre, PageRequest.of(evalPage, evalPageSize));
		}

		Pager pager = new Pager(cupones.getTotalPages(), cupones.getNumber(), BUTTONS_TO_SHOW);

		model.addAttribute("listaCupones", cupones);
		model.addAttribute("selectedPageSize", evalPageSize);
		model.addAttribute("pageSizes", PAGE_SIZES);
		model.addAttribute("pager", pager);
		return "admin/gestionCupones/gestion.html";
	}

	/**
	 * Sirve para mostrar el formulario para crear un nuevo cupón
	 * 
	 * @param model
	 * @param principal
	 * @return formulario para crear un cupón
	 */
	@GetMapping("/new-cupon")
	public String mostrarFormularioCupones(Model model, Principal principal) {
		model.addAttribute("logeado", usuarioServicio.buscarUsuarioLogeado(model, principal));
		model.addAttribute("cupon", new Cupon());
		return "admin/gestionCupones/formulario.html";
	}

	/**
	 * Sirve para guardar un nuevo cupón en caso de que no exista otro con el mismo
	 * nombre
	 * 
	 * @param c     cupón nuevo
	 * @param model
	 * @return redirección controller /admin/tabla-cupones
	 */
	@PostMapping("/addCupon")
	public String guardarCupon(@ModelAttribute("cupon") Cupon c, Model model) {
		for (Cupon lista : cuponServicio.findAll()) {
			if (lista.getNombre().equalsIgnoreCase(c.getNombre())) {
				return "redirect:/admin/tabla-cupones";
			}
		}
		cuponServicio.save(c);
		return "redirect:/admin/tabla-cupones";
	}

	/**
	 * Sirve para comprobar si el cupón que se quiere editar existe y que no sea el
	 * cupón "no". Si cumple ambos requisitos se muestra el formulario de edición
	 * del cupón, y en caso de no cumplir los requisitos redirecciona al controller
	 * /admin/tabla-cupones
	 * 
	 * @param id        id del cupón que se quiere editar
	 * @param model
	 * @param principal
	 * @return formulario de edición de cupones si cumples los requisitos. En caso
	 *         contrario redirección al controller /admin/tabla-cupones
	 */
	@GetMapping("/editar-cupon/{id}")
	public String verFormularioEditarCupon(@PathVariable("id") long id, Model model, Principal principal) {
		model.addAttribute("logeado", usuarioServicio.buscarUsuarioLogeado(model, principal));

		Cupon c = cuponServicio.findById(id);
		if (c != null && c.getId() != 1) {
			model.addAttribute("cupon", c);
			return "admin/gestionCupones/formulario.html";
		} else {
			return "redirect:/admin/tabla-cupones";
		}

	}

	/**
	 * Sirve para guardar los cambios del cupón editado
	 * 
	 * @param c cupón editado
	 * @return redirección controller /admin/tabla-cupones
	 */
	@PostMapping("/edicion-cupon")
	public String procesarEdicionCupon(@ModelAttribute("cupon") Cupon c) {
		cuponServicio.edit(c);
		return "redirect:/admin/tabla-cupones";
	}

	/**
	 * Sirve para borrar un cupón si existe y si es ditinto a 'no'. Si cumple ambos
	 * requisitos se recorrer todos los cupones, se cogen todos los productos a los
	 * que les aplica ese cupón y se setea al cupón 'no'.
	 * 
	 * @param c cupón a borrar
	 * @return redirección al controller /admin/tabla-cupones
	 */
	@GetMapping("/borrar-cupon/{id}")
	public String borrarCupon(@PathVariable("id") Cupon c) {
		Cupon sinCupon = cuponServicio.findById((long) 1);
		if (c.getId() != sinCupon.getId()) {
			for (Cupon allCupones : cuponServicio.findAll()) {
				if (allCupones.getId() == c.getId()) {
					List<Producto> productosCupon = allCupones.getListaProductos();
					for (Producto p : productosCupon) {
						p.setCupon(sinCupon);
					}
				}
			}
			cuponServicio.edit(sinCupon);
			cuponServicio.delete(c);
		}
		return "redirect:/admin/tabla-cupones";
	}

}