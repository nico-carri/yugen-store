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
    public Usuario getUsuarioById(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Usuario save(UsuarioRegistroDTO registroDTO) {

        if (!registroDTO.getPassword().equals(registroDTO.getConfirmarPassword())) {
            throw new RuntimeException("Las contraseñas no coinciden.");
        }

        if (usuarioRepository.findByEmail(registroDTO.getEmail()).isPresent()) {
            throw new RuntimeException("El email " + registroDTO.getEmail() + " ya está registrado.");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(registroDTO.getNombre());
        nuevoUsuario.setApellido(registroDTO.getApellido());
        nuevoUsuario.setEmail(registroDTO.getEmail());
        nuevoUsuario.setPassword(passwordEncoder.encode(registroDTO.getPassword()));
        nuevoUsuario.setRole(Role.CLIENTE);

        nuevoUsuario.setCiudad(registroDTO.getCiudad());
        nuevoUsuario.setDireccion(registroDTO.getDireccion());
        nuevoUsuario.setTelefono(registroDTO.getTelefono());
        nuevoUsuario.setFechaNacimiento(registroDTO.getFechaNacimiento());
        nuevoUsuario.setGenero(registroDTO.getGenero());

        return usuarioRepository.save(nuevoUsuario);
    }
}