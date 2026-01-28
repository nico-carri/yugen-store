package ar.org.centro8curos.service.implementacion;

import ar.org.centro8curos.model.Usuario;
import ar.org.centro8curos.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DetallesUsuarioServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public DetallesUsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println(">>> Intentando cargar usuario: " + email);
        
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println(">>> ERROR: Email no encontrado en la DB: " + email);
                    return new UsernameNotFoundException("No existe");
                });

        System.out.println(">>> Usuario encontrado: " + usuario.getEmail());
        System.out.println(">>> Rol en DB: " + usuario.getRole());
        System.out.println(">>> Hash en DB: " + usuario.getPassword());

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities(usuario.getRole().name())
                .build();
    }
}