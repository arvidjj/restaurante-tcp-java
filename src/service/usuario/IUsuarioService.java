package service.usuario;

import beans.usuario.Usuario;
import service.base.IBaseService;

public interface IUsuarioService extends IBaseService<Usuario> {
	public Usuario findByName(String name);
}
