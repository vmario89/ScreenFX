/*
 * ScreenFX - A plugin library for JavaFX 8 adding features for resizing 
 * and arranging stage windows on a multiple screen hardware configuration
 * 
 * Copyright (C) 2014 Mario Voigt, Ulmenstr. 35, 09112 Chemnitz, Germany, vmario@hrz.tu-chemnitz.de
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.vmario.screenfx;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/*
 * Class for storing and loading ScreenFXContainer preferences
 */
/**
 * @author vmario
 * 
 */
public class ScreenFXConfig extends Properties {
	private static final long serialVersionUID = 1L;
	static ScreenFXConfig instance = null;

	static List<Boolean> taskbarIncludeIndeterminateProperties = new ArrayList<Boolean>(9);

	/*
	 * for public access through getters/setters
	 */
	private static String resourcePath = "";
	private static List<Boolean> taskbarIncludeSelectedProperties = new ArrayList<Boolean>(9);
	private static boolean allowTooltips = true;
	private static long exitDelayTime = 350l;
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle(
			"com.vmario.screenfx.resource.languages.lang", Locale.getDefault());

	/**
	 * @return get the singleton instance
	 */
	public static ScreenFXConfig getInstance() {
		if (instance == null) {
			instance = new ScreenFXConfig();
		}
		return instance;
	}

	private ScreenFXConfig() {
		for (int i = 0; i < 9; i++) {
			/*
			 * initialize taskbar selection standards
			 */
			taskbarIncludeSelectedProperties.add(new Boolean(true));
			taskbarIncludeIndeterminateProperties.add(new Boolean(false));
		}

		put("iconSet", new ArrayList<ImageView>());
		put("popupKeyCodeCombination", new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		put("quickResizeKeyCodeCombination", KeyCode.CONTROL);
		put("activateTaskbarKeyCodeCombination", KeyCode.SHIFT);
	}

	static String getResourcePath() {
		return resourcePath;
	}

	static List<Boolean> getTaskbarIncludeSelectedProperties() {
		return taskbarIncludeSelectedProperties;
	}

	static boolean isAllowTooltips() {
		return allowTooltips;
	}

	static long getExitDelayTime() {
		return exitDelayTime;
	}

	static ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	static void setResourcePath(String resourcePath) {
		ScreenFXConfig.resourcePath = resourcePath;
	}

	static void setTaskbarIncludeSelectedProperties(List<Boolean> taskbarIncludeSelectedProperties) {
		ScreenFXConfig.taskbarIncludeSelectedProperties = taskbarIncludeSelectedProperties;
	}

	static void setAllowTooltips(boolean allowTooltips) {
		ScreenFXConfig.allowTooltips = allowTooltips;
	}

	static void setExitDelayTime(long exitDelayTime) {
		ScreenFXConfig.exitDelayTime = exitDelayTime;
	}

	static void setResourceBundle(ResourceBundle resourceBundle) {
		ScreenFXConfig.resourceBundle = resourceBundle;
	}

}
