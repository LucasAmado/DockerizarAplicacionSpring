/**
 * 
 */
package com.salesianostriana.dam.proyectocontraoferta.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.salesianostriana.dam.proyectocontraoferta.model.Cupon;
import com.salesianostriana.dam.proyectocontraoferta.repo.CuponRepository;
import com.salesianostriana.dam.proyectocontraoferta.services.base.BaseService;

/**
 * Clase que contiene los métodos crud de los cupones
 * @author lamado
 *
 */
@Service
public class CuponServicio extends BaseService<Cupon, Long, CuponRepository> {

	@Autowired
	private CuponRepository cuponRepository;

	public CuponServicio(CuponRepository repo) {
		this.cuponRepository = repo;
	}

	/**
	 * Este método permite añadir un cupon nuevo
	 * 
	 * @param c cupon a añadir
	 * @return cupon guardado en la base de datos
	 */
	public Cupon add(Cupon c) {
		return cuponRepository.save(c);
	}

	/**
	 * Este método permite guardar un cupon editado
	 */
	public Cupon edit(Cupon c) {
		return cuponRepository.save(c);
	}

	/**
	 * Este método permite borrar un cupon
	 */
	public void delete(Cupon c) {
		cuponRepository.delete(c);
	}

	/**
	 * Este método permite encontrar todos los cupones
	 * 
	 * @param pageable
	 * @return cupones pageable
	 */
	public Page<Cupon> findAllPageable(Pageable pageable) {
		return repositorio.findAll(pageable);
	}

	/**
	 * Este método permite encontrar todos los cupones que contienen el nombre buscado
	 * 
	 * @param nombre nombre buscado
	 * @param pageable
	 * @return cupones que contienen ese nombre buscado
	 */
	public Page <Cupon> findByNombre(String nombre, Pageable pageable) {
		return repositorio.findByNombre(nombre, pageable);
	}
}
