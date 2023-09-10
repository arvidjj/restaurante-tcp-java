package service.cheff;

import beans.cheff.Cheff;
import service.base.IBaseService;

public interface ICheffService extends IBaseService<Cheff> { 
	public Cheff findByName(String name);
}
