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

import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * @author vmario
 * 
 */
public class ScreenFXTaskbarCheckBox extends CheckBox {
	/**
	 * 
	 * @param screenNr
	 *            needed to know in which index of array you habe to save the
	 *            taskbar selected property
	 * @param buttonSize the button size (height and width in pixel)
	 */
	ScreenFXTaskbarCheckBox(int screenNr, int buttonSize) {
		ImageView taskBarCheckBoxIcon = new ImageView(this.getClass()
				.getResource(ScreenFX.getResourcePath() + "taskbar.png").toExternalForm());
		setSelected(true);
		setGraphic(taskBarCheckBoxIcon);
		setMinHeight(buttonSize);
		setMinWidth(buttonSize);
		setMaxHeight(buttonSize);
		setMaxWidth(buttonSize);
		setStyle("-fx-font-size:10;");

		Glow glow = new Glow(0.5);
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				setEffect(glow);
			}
		});
		setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				setEffect(null);
			}
		});
		// taskbarincludekey
		ResourceBundle resourceBundle = (ResourceBundle) ScreenFX.getScreenFXProperties().get(
				"resourceBundle");
		String taskbarIncludeKey = "";
		if ((boolean) ScreenFX.getScreenFXProperties().get("allowTooltips")) {

			if (ScreenFX.getScreenFXProperties().get("activateTaskbarKeyCodeCombination") instanceof KeyCodeCombination) {
				taskbarIncludeKey = "\n"
						+ java.text.MessageFormat.format(
								resourceBundle.getString("taskbarincludekey"),
								new Object[] { ((KeyCodeCombination) ScreenFX.getScreenFXProperties().get(
										"activateTaskbarKeyCodeCombination")).toString() });
			}
			if (ScreenFX.getScreenFXProperties().get("activateTaskbarKeyCodeCombination") instanceof KeyCode) {
				taskbarIncludeKey = "\n"
						+ java.text.MessageFormat.format(
								resourceBundle.getString("taskbarincludekey"),
								new Object[] { ((KeyCode) ScreenFX.getScreenFXProperties().get(
										"activateTaskbarKeyCodeCombination")).getName() });
			}
			setTooltip(new Tooltip(
					((ResourceBundle) ScreenFX.getScreenFXProperties().get("resourceBundle"))
							.getString("taskbar") + taskbarIncludeKey));
		}

		/*
		 * init selected value from standard properties
		 */
		setSelected(((List<Boolean>) ScreenFX.getScreenFXProperties().get("taskbarIncludeSelected"))
				.get(screenNr));
		selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
				((List<Boolean>) ScreenFX.getScreenFXProperties().get("taskbarIncludeSelected")).set(
						screenNr, newValue);
			}
		});
		indeterminateProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
				ScreenFXProperties.getTaskbarIncludeIndeterminateProperties().set(screenNr, newValue);
			}
		});
		/*
		 * add global handler for quick resize if a defined key is pressed and
		 * hold down. quick resize gets disabled if the user realeses the key
		 */

		if (ScreenFX.getScreenFXProperties().get("activateTaskbarKeyCodeCombination") != null) {
			ScreenFX.getStage().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					if (ScreenFX.getScreenFXPopup().isShowing()) {
						if (ScreenFX.getScreenFXProperties().get("activateTaskbarKeyCodeCombination") instanceof KeyCodeCombination) {
							if (((KeyCodeCombination) ScreenFX.getScreenFXProperties().get(
									"activateTaskbarKeyCodeCombination")).match(event)) {
								event.consume();
								setIndeterminate(true);
							}
						}
						if (ScreenFX.getScreenFXProperties().get("activateTaskbarKeyCodeCombination") instanceof KeyCode) {
							if (((KeyCode) ScreenFX.getScreenFXProperties().get(
									"activateTaskbarKeyCodeCombination")) == event.getCode()) {
								event.consume();
								setIndeterminate(true);
							}
						}
					}
				}
			});
			ScreenFX.getStage().addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					if (ScreenFX.getScreenFXPopup().isShowing()) {
						if (ScreenFX.getScreenFXProperties().get("activateTaskbarKeyCodeCombination") instanceof KeyCodeCombination) {
							if (((KeyCodeCombination) ScreenFX.getScreenFXProperties().get(
									"activateTaskbarKeyCodeCombination")).match(event)) {
								event.consume();
								setIndeterminate(false);
							}
						}
						if (ScreenFX.getScreenFXProperties().get("activateTaskbarKeyCodeCombination") instanceof KeyCode) {
							if (((KeyCode) ScreenFX.getScreenFXProperties().get(
									"activateTaskbarKeyCodeCombination")) == event.getCode()) {
								event.consume();
								setIndeterminate(false);
							}
						}
					}
				}
			});
		}
	}
}
