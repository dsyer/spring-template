package org.springframework.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Application {

	private static Log logger = LogFactory.getLog(Application.class);

	private static Menu DEFAULT_MENU;

	static {
		DEFAULT_MENU = new Menu();
		DEFAULT_MENU.setName("Home");
		DEFAULT_MENU.setPath("/");
		DEFAULT_MENU.setActive(true);
	}

	private List<Menu> menus = new ArrayList<>();

	public void activate(String name) {
		for (Menu menu : menus) {
			menu.setActive(false);
		}
		Menu menu = getMenu(name);
		menu.setActive(true);
	}


	public List<Menu> getMenus() {
		return menus;
	}

	public static class Menu {

		private String name;

		private String glyph;

		private String path;

		private String title;

		public boolean active;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

		public String getGlyph() {
			return glyph;
		}

		public void setGlyph(String glyph) {
			this.glyph = glyph;
		}

	}

	public Menu getMenu(String name) {
		for (Menu menu : menus) {
			if (menu.getName().equalsIgnoreCase(name)) {
				return menu;
			}
		}
		logger.error("No menu found for " + name + " (" + menus + ")");
		return DEFAULT_MENU;
	}

}
