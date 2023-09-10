package service.base;

import beans.base.AbstractBean;

public interface IBaseService<T extends AbstractBean> {
	public void save(T newBean);
	
	public T getById(Integer id);
}
