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

import javafx.beans.property.BooleanProperty;

/**
 * @author vmario
 * 
 */  
class ScreenFXBindingsHolder {
	private static List<BooleanProperty> includeTaskbarPropertyList = new ArrayList<BooleanProperty>();

	/**
	 * @return the list which includes each BooleanProperty for the checkbox
	 *         value
	 */
	public static List<BooleanProperty> getIncludeTaskbarPropertyList() {
		return includeTaskbarPropertyList;
	}
}
