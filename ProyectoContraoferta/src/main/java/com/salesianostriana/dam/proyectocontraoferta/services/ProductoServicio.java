package com.salesianostriana.dam.proyectocontraoferta.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.salesianostriana.dam.proyectocontraoferta.model.Producto;
import com.salesianostriana.dam.proyectocontraoferta.repo.ProductoRepository;
import com.salesianostriana.dam.proyectocontraoferta.services.base.BaseService;
import com.salesianostriana.dam.proyectocontraoferta.storage.StorageService;

/**
* Clase que contiene los métodos crud de los productos
 * 
 * @author lamado
 *
 */

@Service
public class ProductoServicio extends BaseService<Producto, Long, ProductoRepository> {

	@Autowired
	StorageService storageService;

	/**
	 * Productos que no están caducados
	 * 
	 * @param pageable
	 * @return productos no caducados pageable
	 */
	public Page<Producto> findProductosNoBorrados(Pageable pageable) {
		Page<Producto> todos = repositorio.findNoBorrados(pageable);
		for (Producto p : todos) {
			if (p.getFechaCaducidad().isBefore(LocalDate.now())) {
				p.setCaducado(true);
				repositorio.save(p);
			}
		}

		return todos;
	}

	/**
	 * Productos no caducados que contengan el parámetro nnombre
	 * 
	 * @param nombre nombre buscado
	 * @param pageable
	 * @return productos no caducados con ese nombre
	 */
	public Page<Producto> findProductosNoBorradosByNombre(String nombre, Pageable pageable) {

		Page<Producto> todos = repositorio.findNoBorradosByNombre(nombre, pageable);
		for (Producto p : todos) {
			if (p.getFechaCaducidad().isBefore(LocalDate.now())) {
				p.setCaducado(true);
				repositorio.save(p);
			}
		}

		return todos;
	}

	/**
	 * Productos caducados
	 * 
	 * @param pageable
	 * @return productos caducados paegable
	 */
	public Page<Producto> findProductosCaducados(Pageable pageable) {
		Page<Producto> caducados = repositorio.findCaducados(pageable);
		for (Producto p : caducados) {
			p.setCaducado(true);
			repositorio.save(p);
		}

		return caducados;
	}

	/**
	 * Productos caducados encontrados por nombre
	 * 
	 * @param nombre nombre buscado
	 * @param pageable
	 * @return productos que contengan ese nombre y estén caducados
	 */
	public Page<Producto> findProductosCaducadosByNombre(String nombre, Pageable pageable) {
		return repositorio.findCaducadosByNombre(nombre, pageable);
	}

	public Page<Producto> findProductosSinCaducar(Pageable pageable) {
		Page<Producto> sinCaducar = repositorio.findSinCaducar(pageable);
		for (Producto p : repositorio.findAll()) {
			if (p.getFechaCaducidad().isBefore(LocalDate.now())) {
				p.setCaducado(true);
				repositorio.save(p);
			}
		}
		return sinCaducar;
	}

	/**
	 * Productos sin caducar encontrados por nombre
	 * 
	 * @param nombre nombre buscado
	 * @param pageable
	 * @return productos que contengan ese nombre y no estén caducados
	 */
	public Page<Producto> findProductosSinCaducarByNombre(String nombre, Pageable pageable) {
		Page<Producto> sinCaducarByNombre = repositorio.findSinCaducarByNombre(nombre, pageable);
		for (Producto p : repositorio.findAll()) {
			if (p.getFechaCaducidad().isBefore(LocalDate.now())) {
				p.setCaducado(true);
				repositorio.save(p);
			}
		}
		return sinCaducarByNombre;
	}

	/**
	 * Productos borrados
	 * 
	 * @param pageable
	 * @return productos borrados pageable
	 */
	public Page<Producto> findAllBorrados(Pageable pageable) {
		return repositorio.findAllBorrados(pageable);
	}

	/**
	 * Productos borrados que contengan el nombre buscado
	 * 
	 * @param nombre nombre buscado
	 * @param pageable
	 * @return productos con ese nombre pageable
	 */
	public Page<Producto> findAllBorradosByNombre(String nombre, Pageable pageable) {
		return repositorio.findAllBorradosByNombre(nombre, pageable);
	}

	/**
	 * Productos que pertenezcan a una categoría buscada por su nombre y que no hayan caducado
	 * 
	 * @param nombre
	 * @param pageable
	 * @return
	 */
	public Page<Producto> findByCategoriaNombreSinCaducarPageable(String nombre, Pageable pageable) {
		Page<Producto> productosCategoria = repositorio.findByCategoriaNombrePageable(nombre, pageable);
		for (Producto p : repositorio.findAll()) {
			if (p.getFechaCaducidad().isBefore(LocalDate.now())) {
				p.setCaducado(true);
				repositorio.save(p);
			}
		}
		return productosCategoria;
	}

	/**
	 * 
	 * @param p producto
	 * @param file imagen
	 */
	public void add(Producto p, MultipartFile file) {
		String fileName = storageService.store(file);
		p.setFileUrl(fileName);
		repositorio.save(p);

	}

	/**
	 * 
	 * @param p producto
	 * @param file imagen
	 * @return
	 */
	public Producto edit(Producto p, @RequestParam("file") MultipartFile file) {
		String fileName = storageService.store(file);
		p.setFileUrl(fileName);
		return repositorio.save(p);

	};

}
