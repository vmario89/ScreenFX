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

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

/**
 * @author vmario
 * 
 */
public class ScreenFXKeyChecker {

	static String getStringRepresentation(String keyCodePropertyName) throws Exception {

		if (ScreenFXConfig.getInstance().get(keyCodePropertyName) instanceof KeyCodeCombination) {
			return ((KeyCodeCombination) ScreenFXConfig.getInstance().get(keyCodePropertyName)).getName();
		}

		if (ScreenFXConfig.getInstance().get(keyCodePropertyName) instanceof KeyCombination) {
			return ((KeyCombination) ScreenFXConfig.getInstance().get(keyCodePropertyName)).getName();
		}

		if (ScreenFXConfig.getInstance().get(keyCodePropertyName) instanceof KeyCode) {
			return ((KeyCode) ScreenFXConfig.getInstance().get(keyCodePropertyName)).getName();
		}

		return null;

	}

	/**
	 * @param keyCodePropertyName the keycode name from properties
	 * @param event the key event to check
	 * @return true or false
	 */
	public static boolean checkKeyEvent(String keyCodePropertyName, KeyEvent event) {
		if (ScreenFXConfig.getInstance().get(keyCodePropertyName) instanceof KeyCodeCombination) {
			if (((KeyCodeCombination) ScreenFXConfig.getInstance().get(keyCodePropertyName)).match(event)) {
				return true;
			}
		}
		if (ScreenFXConfig.getInstance().get(keyCodePropertyName) instanceof KeyCombination) {
			if (((KeyCombination) ScreenFXConfig.getInstance().get(keyCodePropertyName)).match(event)) {
				return true;
			}
		}
		if (ScreenFXConfig.getInstance().get(keyCodePropertyName) instanceof KeyCode) {
			if (((KeyCode) ScreenFXConfig.getInstance().get(keyCodePropertyName)) == event.getCode()) {
				return true;
			}
		}
		return false;

	}

}
