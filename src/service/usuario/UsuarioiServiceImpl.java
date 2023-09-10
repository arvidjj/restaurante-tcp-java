package service.usuario;

import java.util.Map;

import beans.usuario.Usuario;
import service.base.BaseService;

public class UsuarioiServiceImpl extends BaseService<Usuario> implements IUsuarioService {

	@Override
	public Usuario findByName(String name) {
		final Map<Integer, Usuario> map = super.getAll();
		Usuario usuario = null;
		for (Usuario c : map.values()) {
			if (name.equalsIgnoreCase(c.getNombre()) || name.equalsIgnoreCase(c.getApellido())) {
				usuario = c;
				break;
			}
		}
		return usuario;
	}
}
