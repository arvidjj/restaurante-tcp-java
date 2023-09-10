package service.cheff;

import java.util.Map;

import beans.cheff.Cheff;
import service.base.BaseService;

public class CheffiServiceImpl extends BaseService<Cheff> implements ICheffService {
	@Override
	public Cheff findByName(String name) {
		final Map<Integer, Cheff> map = super.getAll();
		Cheff cheff = null;
		for (Cheff c : map.values()) {
			if (name.equalsIgnoreCase(c.getNombre())) {
				cheff = c;
				break;
			}
		}
		return cheff;
	}
}
