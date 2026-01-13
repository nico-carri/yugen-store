package ar.org.centro8curos.service.implementacion;

import ar.org.centro8curos.model.Usuario;
import ar.org.centro8curos.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Buscar el usuario en la base de datos por su email
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        // 2. "Traducimos" tu Usuario al User de Spring Security
        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword()) // Esta debe estar encriptada en la DB
                .roles(usuario.getRole().name()) // Convierte el Enum Role a String (ADMIN, CLIENTE)
                .build();
    }
}