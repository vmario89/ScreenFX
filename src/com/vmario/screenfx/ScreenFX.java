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
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

import javafx.application.Platform;
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

	private static ScreenFXPopup screenFXPopup;
	private Stage stage;
	private final static String resourcePath = "/com/vmario/screenfx/resource/";

	/**
	 * @param node
	 *            the only thing you need is a node (for example an AnchorPane)
	 *            for installation of ScreenFX. If the node is instance of
	 *            "ButtonBase" then an action event plus tooltip is
	 *            automatically tied to the node
	 * @throws Exception push exceptions
	 */
	public void install(Node node) {

		final FutureTask<Object> screenFXInstallationTask = new FutureTask<Object>(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				stage = (Stage) node.getScene().getWindow();

				System.out.println("installed on " + stage.getTitle());

				String popupKeyPropertyName = "popupKeyCodeCombination";

				/*
				 * check if a single key or key combination for event handling
				 * is defined
				 */
				if (ScreenFXConfig.getInstance().get(popupKeyPropertyName) != null) {
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
				if (node instanceof ButtonBase) {
					/*
					 * set the tooltip - dependend on os the tooltip will expand
					 * for additional information
					 */
					if (ScreenFXConfig.isAllowTooltips()) {
						String screenFXTooltipString = "";

						if (ScreenFXConfig.getInstance().get(popupKeyPropertyName) != null) {
							screenFXTooltipString = java.text.MessageFormat.format(ScreenFXConfig
									.getResourceBundle().getString("screenfxtooltipkey"),
									new Object[] { ScreenFXKeyChecker
											.getStringRepresentation(popupKeyPropertyName) })
									+ "\n";

						}
						screenFXTooltipString += ScreenFXConfig.getResourceBundle().getString(
								"screenfxtooltip");

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
		System.out.println("showing on " + stage.getTitle());

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

	/**
	 * @return the resource path
	 */
	public static String getResourcePath() {
		return resourcePath;
	}
}
