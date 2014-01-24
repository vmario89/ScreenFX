/*
 * ScreenFX - A plugin library for JavaFX 8 adding features for resizing 
 * and arranging sfxPositioner.getStage() windows on a multiple screen hardware configuration
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

import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * @author vmario
 * 
 */
class ScreenFXGridAllocator {
	private static final Logger logger = Logger.getLogger(ScreenFXGridAllocator.class.getName());

	private ScreenFXPositioner positioner;
	private static boolean overwriteMouseSingleClickToDoubleClick;

	/**
	 * @param sfxPositioner
	 *            the positioner
	 * @param buttonGridPane
	 *            the button grid pane
	 * @param buttonSize
	 *            the button size for width and height in pixel
	 * @throws Exception push exceptions
	 */
	public ScreenFXGridAllocator(ScreenFXPositioner sfxPositioner, GridPane buttonGridPane, int buttonSize)
			throws Exception {
		this.positioner = sfxPositioner;

		/*
		 * qualify the buttons with an icon matrix
		 */
		List<ImageView> buttonImages = new ArrayList<ImageView>(12);
		try {
			//@formatter:off
			buttonImages.add(new ImageView(this.getClass().getResource(ScreenFXConfig.getResourcePath() + "screen-left-top.png").toExternalForm()));
			buttonImages.add(new ImageView(this.getClass().getResource(ScreenFXConfig.getResourcePath() + "screen-middle-top.png").toExternalForm()));
			buttonImages.add(new ImageView(this.getClass().getResource(ScreenFXConfig.getResourcePath() + "screen-right-top.png").toExternalForm()));
			buttonImages.add(new ImageView(this.getClass().getResource(ScreenFXConfig.getResourcePath() + "screen-left-middle.png").toExternalForm()));
			buttonImages.add(new ImageView(this.getClass().getResource(ScreenFXConfig.getResourcePath() + "screen-middle-middle.png").toExternalForm()));
			buttonImages.add(new ImageView(this.getClass().getResource(ScreenFXConfig.getResourcePath() + "screen-right-middle.png").toExternalForm()));
			buttonImages.add(new ImageView(this.getClass().getResource(ScreenFXConfig.getResourcePath() + "screen-left-bottom.png").toExternalForm()));
			buttonImages.add(new ImageView(this.getClass().getResource(ScreenFXConfig.getResourcePath() + "screen-middle-bottom.png").toExternalForm()));
			buttonImages.add(new ImageView(this.getClass().getResource(ScreenFXConfig.getResourcePath() + "screen-right-bottom.png").toExternalForm()));
			buttonImages.add(new ImageView(this.getClass().getResource(ScreenFXConfig.getResourcePath() + "screen-height.png").toExternalForm()));
			buttonImages.add(new ImageView(this.getClass().getResource(ScreenFXConfig.getResourcePath() + "screen-fullscreen.png").toExternalForm()));
			buttonImages.add(new ImageView(this.getClass().getResource(ScreenFXConfig.getResourcePath() + "screen-width.png").toExternalForm()));	
			//@formatter:on				

		} catch (Exception e) {
			logger.log(Level.WARNING,
					"one or more icon images could not be assigned. Maybe there is a file path issue");
		}

		List<ScreenFXGridElement> sfxGridElementList = new ArrayList<>();

		/*
		 * build tooltip - note that this will be only displayed if the setting
		 * "allowTooltips" is true in class ScreenFXGridElement
		 */

		String quickResizeKeyPropertyName = "quickResizeKeyCodeCombination";

		String commonButtonTooltip = ScreenFXConfig.getResourceBundle().getString("placing") + "\n"
				+ ScreenFXConfig.getResourceBundle().getString("resizing");

		String quickResizeKey = "";
		if (ScreenFXConfig.getInstance().get(quickResizeKeyPropertyName) != null) {

			quickResizeKey = java.text.MessageFormat.format(
					ScreenFXConfig.getResourceBundle().getString("screenfxquickresizekey"),
					new Object[] { ScreenFXKeyChecker.getStringRepresentation(quickResizeKeyPropertyName) })
					+ "\n";

			commonButtonTooltip += "\n" + quickResizeKey;
		}
		/*
		 * define the action event matrix so that every button gets it's own job
		 */
		//@formatter:off
		sfxGridElementList.add(new ScreenFXGridElement(0, 0, "0", buttonSize, buttonImages.get(0), commonButtonTooltip, ScreenFXPosition.LEFT_TOP));
		sfxGridElementList.add(new ScreenFXGridElement(0, 1, "1", buttonSize, buttonImages.get(1), commonButtonTooltip, ScreenFXPosition.MIDDLE_TOP)); 
		sfxGridElementList.add(new ScreenFXGridElement(0, 2, "2", buttonSize, buttonImages.get(2), commonButtonTooltip, ScreenFXPosition.RIGHT_TOP));
		sfxGridElementList.add(new ScreenFXGridElement(1, 0, "3", buttonSize, buttonImages.get(3), commonButtonTooltip, ScreenFXPosition.LEFT_MIDDLE));
		sfxGridElementList.add(new ScreenFXGridElement(1, 1, "4", buttonSize, buttonImages.get(4), commonButtonTooltip, ScreenFXPosition.MIDDLE_MIDDLE));
		sfxGridElementList.add(new ScreenFXGridElement(1, 2, "5", buttonSize, buttonImages.get(5), commonButtonTooltip, ScreenFXPosition.RIGHT_MIDDLE));
		sfxGridElementList.add(new ScreenFXGridElement(2, 0, "6", buttonSize, buttonImages.get(6), commonButtonTooltip, ScreenFXPosition.LEFT_BOTTOM));
		sfxGridElementList.add(new ScreenFXGridElement(2, 1, "7", buttonSize, buttonImages.get(7), commonButtonTooltip, ScreenFXPosition.MIDDLE_BOTTOM));
		sfxGridElementList.add(new ScreenFXGridElement(2, 2, "8", buttonSize, buttonImages.get(8), commonButtonTooltip, ScreenFXPosition.RIGHT_BOTTOM));
		sfxGridElementList.add(new ScreenFXGridElement(3, 0, "9", buttonSize, buttonImages.get(9),  ScreenFXConfig.getResourceBundle().getString("resize-height"), ScreenFXPosition.RESIZE_HEIGHT));
		sfxGridElementList.add(new ScreenFXGridElement(3, 1, "fullScreenButton", buttonSize, buttonImages.get(10), ScreenFXConfig.getResourceBundle().getString("fullscreen"), ScreenFXPosition.FULLSCREEN));
		sfxGridElementList.add(new ScreenFXGridElement(3, 2, "11", buttonSize, buttonImages.get(11), ScreenFXConfig.getResourceBundle().getString("resize-width"), ScreenFXPosition.RESIZE_WIDTH));
		//@formatter:on

		// Override the icon set if property is given
		if (ScreenFXConfig.getInstance().get("iconSet") != null) {
			try {
				ArrayList<ImageView> iconSet = (ArrayList<ImageView>) ScreenFXConfig.getInstance().get(
						"iconSet");
				for (int i = 0; i < iconSet.size(); i++) {
					sfxGridElementList.get(i).setGraphic(iconSet.get(i));
				}
			} catch (Exception e) {
				logger.log(Level.WARNING, "icon image could not be assigned", e);
			}

		}

		/*
		 * disable some buttons if window is not resizable
		 */
		if (!sfxPositioner.getStage().isResizable()) {
			sfxGridElementList.get(9).setDisable(true); // disable
														// RESIZE_HEIGHT
			sfxGridElementList.get(10).setDisable(true); // disable
															// FULLSCREEN
			sfxGridElementList.get(11).setDisable(true); // disable
															// RESIZE_WIDTH
		}

		/*
		 * add global handler for quick resize if a defined key is pressed and
		 * hold down. quick resize gets disabled if the user realeses the key
		 */

		if (ScreenFXConfig.getInstance().get(quickResizeKeyPropertyName) != null) {
			sfxPositioner.getStage().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					if (ScreenFX.getScreenFXPopup().isShowing()) {
						if (ScreenFXKeyChecker.checkKeyEvent(quickResizeKeyPropertyName, event)) {
							event.consume();
							overwriteMouseSingleClickToDoubleClick = true;
						}
					}
				}
			});
			sfxPositioner.getStage().addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					if (ScreenFX.getScreenFXPopup().isShowing()) {
						if (ScreenFXKeyChecker.checkKeyEvent(quickResizeKeyPropertyName, event)) {
							event.consume();
							overwriteMouseSingleClickToDoubleClick = false;
						}
					}
				}
			});
		}

		/*
		 * this event handler will set the actions for each button. if the
		 * button is index 10 then the sfxPositioner.getStage() has to be set to fullscreen or to
		 * be quit from - this action event cannot be processed by
		 * ScreenFXPositioner
		 */
		for (ScreenFXGridElement screenFXGridElement : sfxGridElementList) {
			buttonGridPane.add(screenFXGridElement, screenFXGridElement.getColumnIndex(),
					screenFXGridElement.getRowIndex());
			screenFXGridElement.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					try {
						// if sfxPositioner.getStage() is in fullscreen and a resize button is
						// pressed disable the fullscreen properly. do not do
						// this action for the fullscreen button itself
						if (sfxPositioner.getStage().isFullScreen()) {
							if (screenFXGridElement.getId() != null
									&& !screenFXGridElement.getId().equals("fullScreenButton")) {
								sfxPositioner.getStage().setFullScreen(false);
							}
						}

						if (event.getClickCount() == 2) {
							if (screenFXGridElement.getId() != null
									&& screenFXGridElement.getId().equals("fullScreenButton")) {
								sfxPositioner.getStage().setFullScreen(false);
							}
							if (overwriteMouseSingleClickToDoubleClick) {
								sfxPositioner.setPosition(screenFXGridElement.getSfxPosition(), true);
							} else {
								sfxPositioner.setPosition(screenFXGridElement.getSfxPosition(), true);
							}
						} else {
							if (screenFXGridElement.getId() != null
									&& screenFXGridElement.getId().equals("fullScreenButton")) {
								sfxPositioner.getStage().setFullScreen(true);
							}
							if (overwriteMouseSingleClickToDoubleClick) {
								sfxPositioner.setPosition(screenFXGridElement.getSfxPosition(), true);
							} else {
								sfxPositioner.setPosition(screenFXGridElement.getSfxPosition(), false);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});

			/*
			 * at this place keyboard key processing would also be possible
			 */
			screenFXGridElement.setOnKeyPressed(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					// nothing
				}
			});
		}
	}

	/**
	 * @return the ScreenFXPositioner
	 */
	public ScreenFXPositioner getPositioner() {
		return positioner;
	}

	/**
	 * @param positioner
	 *            the ScreenFXPositioner
	 */
	public void setPositioner(ScreenFXPositioner positioner) {
		this.positioner = positioner;
	}

}
