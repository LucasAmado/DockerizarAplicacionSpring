package com.salesianostriana.dam.proyectocontraoferta;

/**
 * Clase principal del proyecto
 */

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.salesianostriana.dam.proyectocontraoferta.model.Usuario;
import com.salesianostriana.dam.proyectocontraoferta.services.UsuarioServicio;


@SpringBootApplication
public class ProyectoContraofertaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoContraofertaApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner init(UsuarioServicio servicio, BCryptPasswordEncoder passwordEncoder) {
		return args -> {
			
			for(Usuario u : servicio.findAll()) {
				u.setPassword(passwordEncoder.encode(u.getPassword()));
				servicio.save(u);
			}
			
		};
	}
	
	
}
