package service.menu;

import beans.menu.MenuApp;
import service.base.IBaseService;


	public interface IMenuService extends IBaseService<MenuApp> { 
		public MenuApp findByName(String name);
	}
