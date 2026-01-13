package ar.org.centro8curos.service.implementacion;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Importación necesaria
import org.springframework.stereotype.Service;
import ar.org.centro8curos.dto.UsuarioRegistroDTO;
import ar.org.centro8curos.model.Usuario;
import ar.org.centro8curos.model.enums.Role;
import ar.org.centro8curos.repository.UsuarioRepository;
import ar.org.centro8curos.service.IUsuarioService;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder; // Agregamos el encriptador

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario save(UsuarioRegistroDTO registroDTO) {

        // Validación de contraseñas iguales
        if (!registroDTO.getPassword().equals(registroDTO.getConfirmarPassword())) {
            throw new RuntimeException("Las contraseñas no coinciden.");
        }

        // Validación de email único
        if (usuarioRepository.findByEmail(registroDTO.getEmail()).isPresent()) {
            throw new RuntimeException("El email " + registroDTO.getEmail() + " ya está registrado.");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(registroDTO.getNombre());
        nuevoUsuario.setApellido(registroDTO.getApellido());
        nuevoUsuario.setEmail(registroDTO.getEmail());

        // VITAL: Encriptar la contraseña antes de guardar
        nuevoUsuario.setPassword(passwordEncoder.encode(registroDTO.getPassword()));

        nuevoUsuario.setRole(Role.CLIENTE);
        nuevoUsuario.setDireccion("Dirección pendiente");
        nuevoUsuario.setTelefono("0000000000");

        return usuarioRepository.save(nuevoUsuario);
    }

    @Override
    public Usuario getUsuarioById(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }
}