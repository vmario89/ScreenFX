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

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

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

	private static final Logger logger = Logger.getLogger(ScreenFXProperties.class.getName());

	/**
	 * 
	 */
	public ScreenFXProperties() {
		put("resourceBundle",
				ResourceBundle.getBundle("com.vmario.screenfx.resource.languages.lang", Locale.getDefault()));
		put("UIConventionOverride", new Boolean(false));
		put("allowTooltips", new Boolean(true));
		put("exitDelayTime", new Long(350l));
		put("iconSet", new ArrayList<ImageView>());
		put("popupKeyCodeCombination", new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
	}

	/**
	 * 
	 * @param file
	 *            the target file to read in
	 * @return the properties. returns null if an error occures
	 */
	public Properties deserializeXML(File file) {
		try {
			logger.log(Level.INFO, "deserializing from " + file.getPath());
			XMLDecoder d = new XMLDecoder(new FileInputStream(file));
			Properties p = (Properties) d.readObject();
			d.close();
			return p;
		} catch (Exception e) {
			logger.log(Level.WARNING, "error while deserializing " + file.getName(), e);
			return null;
		}
	}

	/**
	 * 
	 * @param properties
	 *            the properties to write out to file
	 * @param file
	 *            the file
	 */
	public void serializeXML(Properties properties, File file) {
		try {
			logger.log(Level.INFO, "serializing to " + file.getPath());
			XMLEncoder e = new XMLEncoder(new FileOutputStream(file));
			e.writeObject(properties);
			e.close();
		} catch (Exception e) {
			logger.log(Level.WARNING, "error while seserializing " + file.getName(), e);
		}
	}

}
