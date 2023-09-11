package service.base;

import java.util.HashMap;
import java.util.Map;

import beans.base.AbstractBean;

public abstract class BaseService<T extends AbstractBean> implements IBaseService<T> {

	private final Map<Integer, T> _lista;
	
	public BaseService( ) {
		_lista = new HashMap<>();
	}
	
	public Map<Integer, T> getAll() {
		return _lista;
	}
	
	@Override
	public void save(T newObject) {
		_lista.put(newObject.getId(), newObject);
	}
	
	@Override
	public T getById(Integer id) {
		return _lista.get(id);
	}
}
