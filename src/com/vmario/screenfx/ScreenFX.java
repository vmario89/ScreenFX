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
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * @author vmario
 * 
 */

/*
 * create a new ScreenFX for each stage you want to install to
 */
public class ScreenFX {
	private static final Logger logger = Logger.getLogger(ScreenFX.class.getName());
	private final String popupKeyPropertyName = "popupKeyCodeCombination"; // as
																			// defined
																			// in
																			// config
	private static ScreenFXPopup screenFXPopup;
	private Stage stage;

	/**
	 * @param stage
	 *            install ScreenFX globally to the stage so that each scene in
	 *            it can use ScreenFX via every single key (also modifiers like
	 *            shift work but it is not recommended) or key combination
	 */
	public void installOn(Stage stage) {
		try {
			this.stage = stage;
			logger.log(Level.INFO, "installing ScreenFX on stage " + this.stage.getTitle());
			if (ScreenFXConfig.getInstance().get(popupKeyPropertyName) != null) {
				stage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
					@Override
					public void handle(KeyEvent event) {
						if (ScreenFXKeyChecker.checkKeyEvent(popupKeyPropertyName, event)) {
							event.consume();
							showScreenFX();
						}
					}
				});
			}
		} catch (Exception e) {
		}
	}

	/**
	 * @param node
	 *            install ScreenFX to a single node. If the node is instance of
	 *            "ButtonBase" then an action event plus tooltip is
	 *            automatically tied to the node. otherwise you will only have a
	 *            node reacting on key combination if the node gets focused.
	 * @param installGlobal
	 *            if true ScreenFX gets automatically installed for the whole
	 *            stage so you can use the key combination when window gets the
	 *            foucs. If false you have to focus the node before your key
	 *            combination will work
	 */
	public void installOn(Node node, boolean installGlobal) {
		try {
			this.stage = (Stage) node.getScene().getWindow();
			logger.log(Level.INFO, "installing ScreenFX on node @ stage " + this.stage.getTitle());

			/*
			 * istall focus only listener or global listener for key / key
			 * combination
			 */if (ScreenFXConfig.getInstance().get(popupKeyPropertyName) != null) {
				if (installGlobal) {
					installOn(this.stage); // install automatically to whole
											// scene
				} else {
					// on key press - works only if component has focus!
					node.setOnKeyPressed(new EventHandler<KeyEvent>() {
						@Override
						public void handle(KeyEvent event) {
							if (ScreenFXKeyChecker.checkKeyEvent(popupKeyPropertyName, event)) {
								event.consume();
								showScreenFX();
							}
						}
					});
				}
			}

			if (node instanceof ButtonBase) {
				/*
				 * set the tooltip - depends on os; the tooltip will expand for
				 * additional information
				 */
				if (ScreenFXConfig.isAllowTooltips()) {
					Tooltip screenFXTooltip = new Tooltip(getTooltipText());
					((ButtonBase) node).setTooltip(screenFXTooltip);
				}
				((ButtonBase) node).setOnAction(new EventHandler<ActionEvent>() { // on
																					// mouse
																					// click
							@Override
							public void handle(ActionEvent event) {
								showScreenFX();
							}
						});
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "", e);
		}
	}

	/**
	 * @return the tooltip text
	 */
	public String getTooltipText() {
		String screenFXTooltipString = "";
		if (ScreenFXConfig.getInstance().get(popupKeyPropertyName) != null) {
			screenFXTooltipString = java.text.MessageFormat.format(ScreenFXConfig.getResourceBundle()
					.getString("screenfxtooltipkey"), new Object[] { ScreenFXKeyChecker
					.getStringRepresentation(popupKeyPropertyName) })
					+ "\n";

		}
		screenFXTooltipString += ScreenFXConfig.getResourceBundle().getString("screenfxtooltip");

		List<String> osNamesAdditionalHint = new ArrayList<String>();
		osNamesAdditionalHint.add("Windows 7");
		osNamesAdditionalHint.add("Windows 8");
		osNamesAdditionalHint.add("Windows 8.1");
		String osName = System.getProperty("os.name");
		for (String string : osNamesAdditionalHint) {
			if (string.toLowerCase().equals(osName.toLowerCase())) {
				screenFXTooltipString += "\n"
						+ ScreenFXConfig.getResourceBundle().getString("additionalhints");
			}
		}
		return screenFXTooltipString;
	}

	private void showScreenFX() {
		try {
			if (screenFXPopup != null) {
				if (screenFXPopup.isShowing()) {
					screenFXPopup.hide();
				}
			}
			screenFXPopup = new ScreenFXPopup(stage);
			screenFXPopup.show(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static ScreenFXPopup getScreenFXPopup() {
		return screenFXPopup;
	}

	/**
	 * @return the stage
	 */
	public Stage getStage() {
		return stage;
	}
}