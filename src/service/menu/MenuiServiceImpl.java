package service.menu;

import java.util.Map;

import beans.menu.MenuApp;
import service.base.BaseService;

	public class MenuiServiceImpl extends BaseService<MenuApp> implements IMenuService {

		@Override
		public MenuApp findByName(String name) {
			final Map<Integer, MenuApp> map = super.getAll();
			MenuApp menu = null;
			for (MenuApp c : map.values()) {
				if (name.equalsIgnoreCase(c.getNombre())) {
					menu = c;
					break;
				}
			}
			return menu;
		}
	}
