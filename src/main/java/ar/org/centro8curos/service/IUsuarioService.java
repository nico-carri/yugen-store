package ar.org.centro8curos.service;

import ar.org.centro8curos.dto.UsuarioRegistroDTO;
import ar.org.centro8curos.model.Usuario;

public interface IUsuarioService {
    Usuario save(UsuarioRegistroDTO registroDTO);

    Usuario getUsuarioById(Integer id);

    Usuario findByEmail(String email);
}