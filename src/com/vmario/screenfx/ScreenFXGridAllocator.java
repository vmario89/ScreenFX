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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
	String resourcePath;

	private ScreenFXPositioner positioner;

	/**
	 * @param sfxPositioner
	 * @param resourcePath
	 * @param buttonGridPane
	 * @param buttonSize
	 */
	public ScreenFXGridAllocator(ScreenFXPositioner sfxPositioner, String resourcePath,
			GridPane buttonGridPane, int buttonSize) {
		this.positioner = sfxPositioner;

		/*
		 * qualify the buttons with an icon matrix
		 */
		List<ImageView> buttonImages = new ArrayList<ImageView>(12);
		try {
			//@formatter:off
			buttonImages.add(new ImageView(this.getClass().getResource(resourcePath + "screen-left-top.png").toExternalForm()));
			buttonImages.add(new ImageView(this.getClass().getResource(resourcePath + "screen-middle-top.png").toExternalForm()));
			buttonImages.add(new ImageView(this.getClass().getResource(resourcePath + "screen-right-top.png").toExternalForm()));
			buttonImages.add(new ImageView(this.getClass().getResource(resourcePath + "screen-left-middle.png").toExternalForm()));
			buttonImages.add(new ImageView(this.getClass().getResource(resourcePath + "screen-middle-middle.png").toExternalForm()));
			buttonImages.add(new ImageView(this.getClass().getResource(resourcePath + "screen-right-middle.png").toExternalForm()));
			buttonImages.add(new ImageView(this.getClass().getResource(resourcePath + "screen-left-bottom.png").toExternalForm()));
			buttonImages.add(new ImageView(this.getClass().getResource(resourcePath + "screen-middle-bottom.png").toExternalForm()));
			buttonImages.add(new ImageView(this.getClass().getResource(resourcePath + "screen-right-bottom.png").toExternalForm()));
			buttonImages.add(new ImageView(this.getClass().getResource(resourcePath + "screen-height.png").toExternalForm()));
			buttonImages.add(new ImageView(this.getClass().getResource(resourcePath + "screen-fullscreen.png").toExternalForm()));
			buttonImages.add(new ImageView(this.getClass().getResource(resourcePath + "screen-width.png").toExternalForm()));	
			//@formatter:on				

		} catch (Exception e) {
			logger.log(Level.WARNING,
					"one or more icon images could not be assigned. Maybe there is a file path issue");
		}

		List<ScreenFXGridElement> sfxGridElementList = new ArrayList<>();
		ResourceBundle resourceBundle = (ResourceBundle) ScreenFX.getScreenFXProperties().get(
				"resourceBundle");

		/*
		 * define the action event matrix so that every button gets it's own job
		 */String commonButtonTooltip = resourceBundle.getString("placing") + "\n"
				+ resourceBundle.getString("resizing");
		//@formatter:off
		sfxGridElementList.add(new ScreenFXGridElement(0, 0, null, buttonSize, buttonImages.get(0), commonButtonTooltip, ScreenFXPosition.LEFT_TOP));
		sfxGridElementList.add(new ScreenFXGridElement(0, 1, null, buttonSize, buttonImages.get(1), commonButtonTooltip, ScreenFXPosition.MIDDLE_TOP)); 
		sfxGridElementList.add(new ScreenFXGridElement(0, 2, null, buttonSize, buttonImages.get(2), commonButtonTooltip, ScreenFXPosition.RIGHT_TOP));
		sfxGridElementList.add(new ScreenFXGridElement(1, 0, null, buttonSize, buttonImages.get(3), commonButtonTooltip, ScreenFXPosition.LEFT_MIDDLE));
		sfxGridElementList.add(new ScreenFXGridElement(1, 1, null, buttonSize, buttonImages.get(4), commonButtonTooltip, ScreenFXPosition.MIDDLE_MIDDLE));
		sfxGridElementList.add(new ScreenFXGridElement(1, 2, null, buttonSize, buttonImages.get(5), commonButtonTooltip, ScreenFXPosition.RIGHT_MIDDLE));
		sfxGridElementList.add(new ScreenFXGridElement(2, 0, null, buttonSize, buttonImages.get(6), commonButtonTooltip, ScreenFXPosition.LEFT_BOTTOM));
		sfxGridElementList.add(new ScreenFXGridElement(2, 1, null, buttonSize, buttonImages.get(7), commonButtonTooltip, ScreenFXPosition.MIDDLE_BOTTOM));
		sfxGridElementList.add(new ScreenFXGridElement(2, 2, null, buttonSize, buttonImages.get(8), commonButtonTooltip, ScreenFXPosition.RIGHT_BOTTOM));
		sfxGridElementList.add(new ScreenFXGridElement(3, 0, null, buttonSize, buttonImages.get(9),  resourceBundle.getString("resize-height"), ScreenFXPosition.RESIZE_HEIGHT));
		sfxGridElementList.add(new ScreenFXGridElement(3, 1, "fullScreenButton", buttonSize, buttonImages.get(10), resourceBundle.getString("fullscreen"), ScreenFXPosition.FULLSCREEN));
		sfxGridElementList.add(new ScreenFXGridElement(3, 2, null, buttonSize, buttonImages.get(11), resourceBundle.getString("resize-width"), ScreenFXPosition.RESIZE_WIDTH));
		//@formatter:on

		// Override the icon set if property is given
		if (ScreenFX.getScreenFXProperties().get("iconSet") != null) {
			try {
				@SuppressWarnings("unchecked")
				ArrayList<ImageView> iconSet = (ArrayList<ImageView>) ScreenFX
						.getScreenFXProperties().get("iconSet");
				for (int i = 0; i < iconSet.size(); i++) {
					sfxGridElementList.get(i).setGraphic(iconSet.get(i));
				}
			} catch (Exception e) {
				logger.log(Level.WARNING, "icon image could not be assigned", e);
			}

		}

		/*
		 * disable some buttons if window is not resizable - a window is also
		 * resizable when it is set to isResizable(false) but it would violate
		 * the common ui behavior
		 */
		if (!(boolean) ScreenFX.getScreenFXProperties().get("UIConventionOverride")) {
			if (!sfxPositioner.getStage().isResizable()) {
				sfxGridElementList.get(9).setDisable(true); // disable
															// RESIZE_HEIGHT
				sfxGridElementList.get(10).setDisable(true); // disable
																// FULLSCREEN
				sfxGridElementList.get(11).setDisable(true); // disable
																// RESIZE_WIDTH
			}
			/*
			 * disable some buttons if the stage is currently in fullscreen.
			 * This should be done because if you put a stage to fullscreen
			 * you're able anyways to resize the window while staying in
			 * fullscreen mode. this is a malicous behavior which should be
			 * suppressed. implement the disable feature for current visible
			 * popup (ChangeListener()) and for a new popup too (when user
			 * closes popup and presses key combination again to show). It also
			 * must be used that way because the primary stage of application
			 * could be initialized in fullscreen moed
			 */
			setFullscreenButtonDependencies(sfxGridElementList, sfxPositioner.getStage().isFullScreen());
			sfxPositioner.getStage().fullScreenProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
						Boolean newValue) {
					setFullscreenButtonDependencies(sfxGridElementList, newValue);
				}
			});
		}

		/*
		 * this event handler will set the actions for each button. if the
		 * button is index 10 then the stage has to be set to fullscreen or to
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
						if (event.getClickCount() == 2) {
							if (screenFXGridElement.getId() != null
									&& screenFXGridElement.getId().equals("fullScreenButton")) {
								sfxPositioner.getStage().setFullScreen(false);
							}
							sfxPositioner.setPosition(screenFXGridElement.getSfxPosition(), true);
						} else {
							if (screenFXGridElement.getId() != null
									&& screenFXGridElement.getId().equals("fullScreenButton")) {
								sfxPositioner.getStage().setFullScreen(true);
							}
							sfxPositioner.setPosition(screenFXGridElement.getSfxPosition(), false);
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

	/*
	 * a helper method to enable/disable buttons dependent on fullscreen state
	 */
	/**
	 * @param sfxGridElementList
	 * @param fullScreenValue
	 */
	public void setFullscreenButtonDependencies(List<ScreenFXGridElement> sfxGridElementList,
			boolean fullScreenValue) {
		sfxGridElementList.get(0).setDisable(fullScreenValue);
		sfxGridElementList.get(1).setDisable(fullScreenValue);
		sfxGridElementList.get(2).setDisable(fullScreenValue);
		sfxGridElementList.get(3).setDisable(fullScreenValue);
		sfxGridElementList.get(4).setDisable(fullScreenValue);
		sfxGridElementList.get(5).setDisable(fullScreenValue);
		sfxGridElementList.get(6).setDisable(fullScreenValue);
		sfxGridElementList.get(7).setDisable(fullScreenValue);
		sfxGridElementList.get(8).setDisable(fullScreenValue);
		sfxGridElementList.get(9).setDisable(fullScreenValue);
		sfxGridElementList.get(11).setDisable(fullScreenValue);
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
