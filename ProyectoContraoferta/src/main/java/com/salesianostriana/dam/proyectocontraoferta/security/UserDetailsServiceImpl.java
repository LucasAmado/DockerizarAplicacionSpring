/**
 * 
 */
package com.salesianostriana.dam.proyectocontraoferta.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.salesianostriana.dam.proyectocontraoferta.model.Usuario;
import com.salesianostriana.dam.proyectocontraoferta.services.UsuarioServicio;

/**
 * Esta clase permite establecer los roles y por tanto los permisos que tiene un
 * usario en función de si es un admin o un cliente. Tanto el cliente como el
 * admin tendrán funciones en común (usuario), además de las que cada uno tendrá
 * como propias. A la aplicación sólo se podrá acceder si se está resgistrado,
 * por lo que solamente he distinguido entre los roles de aministrador y
 * cliente.
 * 
 * @author lamado
 *
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	UsuarioServicio usuarioServicio;

	public UserDetailsServiceImpl(UsuarioServicio servicio) {
		this.usuarioServicio = servicio;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario usuario = usuarioServicio.buscarPorEmail(username);

		UserBuilder userBuilder = null;

		if (usuario != null) {
			userBuilder = User.withUsername(usuario.getEmail());
			userBuilder.disabled(false);
			userBuilder.password(usuario.getPassword());
			if (usuario.isAdmin()) {
				userBuilder.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"));
			} else {
				userBuilder.authorities(new SimpleGrantedAuthority("ROLE_CLIENTE"));
			}
		} else {
			throw new UsernameNotFoundException("Usuario no encontrado");
		}

		return userBuilder.build();

	}

}
