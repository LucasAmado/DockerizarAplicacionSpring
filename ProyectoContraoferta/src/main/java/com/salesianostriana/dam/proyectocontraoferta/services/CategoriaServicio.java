/**
 * 
 */
package com.salesianostriana.dam.proyectocontraoferta.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.salesianostriana.dam.proyectocontraoferta.model.Categoria;
import com.salesianostriana.dam.proyectocontraoferta.repo.CategoriaRepository;
import com.salesianostriana.dam.proyectocontraoferta.services.base.BaseService;
import com.salesianostriana.dam.proyectocontraoferta.storage.StorageService;

/**
 * Clase que contiene los métodos crud de las categorías
 * 
 * @author lamado
 *
 */
@Service
public class CategoriaServicio extends BaseService<Categoria, Long, CategoriaRepository> {

	@Autowired
	StorageService storageService;

	@Autowired
	private CategoriaRepository categoriaRepository;

	public CategoriaServicio(CategoriaRepository repo) {
		this.categoriaRepository = repo;
	}

	/**
	 * @param c Categoría a añadir
	 * @return categoria guardada en la base de datos.
	 */
	public Categoria add(Categoria c) {
		return categoriaRepository.save(c);
	}

	/**
	 * Este método sirve para guardar en la base de datos la categoría editada
	 */
	public Categoria edit(Categoria c) {
		return categoriaRepository.save(c);
	}

	/**
	 * Este método sirve para borrar una categoría
	 */
	public void delete(Categoria c) {
		categoriaRepository.delete(c);
	}

	/**
	 * @param pageable
	 * @return categorías pageable
	 */
	public Page<Categoria> findAllPageable(Pageable pageable) {
		return repositorio.findAll(pageable);
	}


	/**
	 * Este métoso sirve para encontrar una categoría a partir del nombre
	 * 
	 * @param nombre nombre de la categoría buscada
	 * @return categoría buscada
	 */
	public Categoria findCategoriaByNombre(String nombre) {
		return repositorio.findByNombre(nombre);
	}
	
	/**
	 * Este métoso sirve para encontrar las categorías que contengan el parámetro nombre
	 * 
	 * @param nombre nombre buscado
	 * @param pageable
	 * @return categorias encontradas por el nombre pageable
	 */
	public Page <Categoria> findByNombrePageable (String nombre, Pageable pageable){
		return repositorio.findByNombrePageable(nombre, pageable);
	}
}
