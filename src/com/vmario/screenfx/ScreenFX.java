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
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

//@formatter:off
/*
* ************************************************
* ******************* ScreenFX *******************
* ************************************************

* to do - source code:
* ************************************************
* - create algorithm to break the current 9 screen cubic layout to a more common flexible layout
* - fix the checkbox height not by font size  - instead find out how to style the box from checkbox in its width/height separately
* - create a stylesheet which contains all the styles, effects, filters and hoverings which are currently done with mouseEntered/mouseExited events, etc..
* - add some functionality to restore window positions
* - change time a tooltip is displayed before auto hiding
* - tool for automatically getting the current active stage/focusOwner
* - put some try-catch methods into code to grant stability
*/
//@formatter:on

/**
 * @author vmario
 * 
 */
public class ScreenFX {
	private static final Logger logger = Logger.getLogger(ScreenFX.class.getName());

	// one property set for all instances
	private static ScreenFXProperties screenFXProperties = new ScreenFXProperties();
	private static ScreenFXPopup screenFXPopup;
	private static Stage stage;
	private final static String resourcePath = "/com/vmario/screenfx/resource/";

	/**
	 * @param node
	 *            the only thing you need is a node (for example an AnchorPane)
	 *            for installation of ScreenFX. If the node is instance of
	 *            "ButtonBase" then an action event plus tooltip is
	 *            automatically tied to the node
	 */
	public void install(Node node) {
		final FutureTask<Object> screenFXInstallationTask = new FutureTask<Object>(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				ResourceBundle resourceBundle = (ResourceBundle) screenFXProperties.get("resourceBundle");
				stage = (Stage) node.getScene().getWindow();

				/*
				 * check if a single key or key combination for event handling
				 * is defined
				 */
				if (screenFXProperties.get("popupKeyCodeCombination") != null) {
					node.setOnKeyPressed(new EventHandler<KeyEvent>() {

						@Override
						public void handle(KeyEvent event) {
							if (screenFXProperties.get("popupKeyCodeCombination") instanceof KeyCodeCombination) {
								if (((KeyCodeCombination) screenFXProperties.get("popupKeyCodeCombination"))
										.match(event)) {
									event.consume();
									showScreenFX();
								}
							}
							if (screenFXProperties.get("popupKeyCodeCombination") instanceof KeyCode) {
								if (((KeyCode) screenFXProperties.get("popupKeyCodeCombination")) == event
										.getCode()) {

									event.consume();
									showScreenFX();
								}
							}
						}
					});
				}
				if (node instanceof ButtonBase) {
					/*
					 * set the tooltip - dependend on os the tooltip will expand
					 * for additional information
					 */
					if ((boolean) screenFXProperties.get("allowTooltips")) {
						String screenFXTooltipString = "";
						if (screenFXProperties.get("popupKeyCodeCombination") != null) {

							if (screenFXProperties.get("popupKeyCodeCombination") instanceof KeyCodeCombination) {
								screenFXTooltipString = java.text.MessageFormat.format(resourceBundle
										.getString("screenfxtooltipkey"),
										new Object[] { ((KeyCodeCombination) screenFXProperties
												.get("popupKeyCodeCombination")).toString() })
										+ "\n";
							}
							if (screenFXProperties.get("popupKeyCodeCombination") instanceof KeyCode) {
								screenFXTooltipString = java.text.MessageFormat.format(resourceBundle
										.getString("screenfxtooltipkey"),
										new Object[] { ((KeyCode) screenFXProperties
												.get("popupKeyCodeCombination")).getName() })
										+ "\n";
							}
						}
						screenFXTooltipString += resourceBundle.getString("screenfxtooltip");

						List<String> osNamesAdditionalHint = new ArrayList<String>();
						osNamesAdditionalHint.add("Windows 7");
						osNamesAdditionalHint.add("Windows 8");
						osNamesAdditionalHint.add("Windows 8.1");
						String osName = System.getProperty("os.name");
						for (String string : osNamesAdditionalHint) {
							if (string.toLowerCase().equals(osName.toLowerCase())) {
								screenFXTooltipString += "\n" + resourceBundle.getString("additionalhints");
							}
						}
						Tooltip screenFXTooltip = new Tooltip(screenFXTooltipString);
						((ButtonBase) node).setTooltip(screenFXTooltip);
					}
					((ButtonBase) node).setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							showScreenFX();
						}
					});
				}
				return null;
			}
		});
		Platform.runLater(screenFXInstallationTask);

	}

	private void showScreenFX() {
		try {
			if (screenFXPopup != null) {
				if (screenFXPopup.isShowing()) {
					screenFXPopup.hide();
				}
			}
			screenFXPopup = new ScreenFXPopup();
			screenFXPopup.show(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the ScreenFXProperties
	 */
	public static Properties getScreenFXProperties() {
		return (Properties) screenFXProperties;
	}

	/**
	 * @param screenFXProperties
	 *            the ScreenFXProperties
	 */
	public void setScreenFXProperties(Properties screenFXProperties) {
		ScreenFX.screenFXProperties = (ScreenFXProperties) screenFXProperties;
	}

	static ScreenFXPopup getScreenFXPopup() {
		return screenFXPopup;
	}

	/**
	 * @return the stage
	 */
	public static Stage getStage() {
		return stage;
	}

	/**
	 * @return the resource path
	 */
	public static String getResourcePath() {
		return resourcePath;
	}
}
