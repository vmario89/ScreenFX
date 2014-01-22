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
class ScreenFXProperties extends Properties {
	private static final long serialVersionUID = 1L;

	private static List<Boolean> taskbarIncludeSelectedProperties = new ArrayList<Boolean>(9);
	private static List<Boolean> taskbarIncludeIndeterminateProperties = new ArrayList<Boolean>(9);

	/**
	 * 
	 */
	public ScreenFXProperties() {
		for (int i = 0; i < 9; i++) {
			/*
			 * initialize taskbar selection standards
			 */
			taskbarIncludeSelectedProperties.add(new Boolean(true));
			// do not put on changable properties!
			taskbarIncludeIndeterminateProperties.add(new Boolean(false));
		}
		put("resourceBundle",
				ResourceBundle.getBundle("com.vmario.screenfx.resource.languages.lang", Locale.getDefault()));
		put("UIConventionOverride", new Boolean(false));
		put("allowTooltips", new Boolean(true));
		put("exitDelayTime", new Long(350l));
		put("iconSet", new ArrayList<ImageView>());
		put("popupKeyCodeCombination", new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		/*
		 * just hold down ctrl when already activated the popup with ctrl + s
		 */
		put("quickResizeKeyCodeCombination", KeyCode.CONTROL);
		/*
		 * *just hold down ctrl when already activated the popup with ctrl + s
		 */
		put("activateTaskbarKeyCodeCombination", KeyCode.SHIFT);
		/*
		 * an array of size(9). Throw an error if array is too small
		 */
		put("taskbarIncludeSelected", taskbarIncludeSelectedProperties);
	}

	static List<Boolean> getTaskbarIncludeIndeterminateProperties() {
		return taskbarIncludeIndeterminateProperties;
	}
}
