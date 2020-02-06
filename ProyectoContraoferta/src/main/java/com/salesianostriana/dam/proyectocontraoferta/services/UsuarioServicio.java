package com.salesianostriana.dam.proyectocontraoferta.services;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.salesianostriana.dam.proyectocontraoferta.model.Usuario;
import com.salesianostriana.dam.proyectocontraoferta.repo.UsuarioRepository;
import com.salesianostriana.dam.proyectocontraoferta.services.base.BaseService;

@Service
public class UsuarioServicio extends BaseService<Usuario, Long, UsuarioRepository> {

	public Usuario buscarPorEmail(String email) {
		return repositorio.findFirstByEmail(email);
	}

	/**
	 * 
	 * @param model
	 * @param principal
	 * @return usuario que está logueado
	 */
	public Usuario buscarUsuarioLogeado(Model model, Principal principal) {

		if (principal != null) {
			String email = principal.getName();
			Usuario logeado = buscarPorEmail(email);
			model.addAttribute("logueado", logeado);

			return logeado;
		} else {
			return null;
		}
		
	}
	
	
	public Page<Usuario> findAllPageable(Pageable pageable) {
        return repositorio.findAll(pageable);
    }
	
	public Page<Usuario> findByNombre(String nombre, Pageable pageable) {
        return repositorio.findByNombre(nombre, pageable);
    }
	
}
