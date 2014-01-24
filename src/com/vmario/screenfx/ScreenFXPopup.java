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

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Service;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author vmario
 * 
 */
public class ScreenFXPopup extends Popup {
	private final Service<Void> delayService;

	/**
	 * make a popup with a oneclick lifecycle to create always new popup
	 * 
	 * @throws Exception push exceptions
	 */
	public ScreenFXPopup(Stage stage) throws Exception {
		delayService = new ScreenFXDelayService(ScreenFXConfig.getExitDelayTime());

		// Popup is special window (no decorations, transparent null
		// fill). For
		// a proper rendering there is a rectangle needed to display the
		// background of the rectangle and popup have the same bounds
		Rectangle borderRectangle = new Rectangle();
		borderRectangle.setArcWidth(15);
		borderRectangle.setArcHeight(15);
		Stop[] stops = new Stop[] { new Stop(0, Color.rgb(255, 255, 255)),
				new Stop(1, Color.rgb(227, 227, 227)) };
		LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
		borderRectangle.setFill(lg1);
		borderRectangle.setStroke(Color.BLACK);
		borderRectangle.setStrokeType(StrokeType.OUTSIDE);
		getContent().addAll(borderRectangle);
		setAutoFix(true);
		setHideOnEscape(true);

		AnchorPane topAnchorPane = new AnchorPane();

		List<AnchorPane> screenAnchorPanes = new ArrayList<>();

		// create new screen grid
		GridPane screenGridPane = new GridPane();
		screenGridPane.setGridLinesVisible(false);
		screenGridPane.setHgap(2);
		screenGridPane.setVgap(2);

		GraphicsEnvironment graphicEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] graphicsDevices = graphicEnvironment.getScreenDevices(); // screen
																					// device
																					// list

		// get vaules of main screen - needed for alignment of other
		// available
		// screen devices around the main screen
		GraphicsConfiguration mainScreenGraphicsConfiguration = graphicsDevices[0].getDefaultConfiguration();
		java.awt.Rectangle boundsMain = mainScreenGraphicsConfiguration.getBounds();
		int posXmain = boundsMain.x;
		int posYmain = boundsMain.y;
		int dimXmain = boundsMain.width;
		int dimYmain = boundsMain.height;

		// apply grid layout for available screens
		for (int i = 0; i < graphicsDevices.length; i++) {
			try {
				// get physically positions of the other screen relative
				// to main
				// screen
				GraphicsConfiguration gc = graphicsDevices[i].getDefaultConfiguration();
				java.awt.Rectangle bounds = gc.getBounds();
				int posX = bounds.x; // x pos of a non main screen
				int posY = bounds.y; // y pos of a non main screen

				// grid init values
				int gridXval = 0;
				int gridYval = 0;

				if (posX < posXmain + posXmain) { // left x
					gridXval = 0;
					if (posY < posYmain) { // top y
						gridYval = 0;
					} else if (posY >= posYmain & posY < posYmain + dimYmain) { // mid
																				// y
						gridYval = 1;
					} else if (posY >= posYmain + dimYmain) { // bottom
																// y
						gridYval = 2;
					}
				} else if (posX >= posXmain & posX < posXmain + dimXmain) { // mid
																			// x
					gridXval = 1;
					if (posY < posYmain) { // top y
						gridYval = 0;
					} else if (posY >= posYmain & posY < posYmain + dimYmain) { // mid
																				// y
						gridYval = 1;
					} else if (posY >= posYmain + dimYmain) { // bottom
																// y
						gridYval = 2;
					}
				} else if (posX >= posXmain + dimXmain) { // right x
					gridXval = 2;
					if (posY < posYmain) { // top y
						gridYval = 0;
					} else if (posY >= posYmain & posY < posYmain + dimYmain) { // mid
																				// y
						gridYval = 1;
					} else if (posY >= posYmain + dimYmain) { // bottom
																// y
						gridYval = 2;
					}
				}

				AnchorPane screenAnchorPane = new AnchorPane();
				if (i != 0) {
					ColorAdjust colorAdjust = new ColorAdjust();
					colorAdjust.setHue(0.1d);
					screenAnchorPane.setEffect(colorAdjust);
				}
				GridPane.setMargin(screenAnchorPane, new Insets(5, 5, 5, 5));
				screenAnchorPanes.add(screenAnchorPane);
				screenGridPane.add(screenAnchorPane, gridXval, gridYval);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/*
		 * create a 12 button control grid + other controls for each
		 * screenGridPane using ScreenFXGridAllocator
		 */
		for (int screenNr = 0; screenNr < screenAnchorPanes.size(); screenNr++) {
			GridPane buttonGridPane = new GridPane();
			buttonGridPane.setGridLinesVisible(false);
			int buttonSize = 15;
			int buttonGap = 2;
			buttonGridPane.setHgap(buttonGap);
			buttonGridPane.setVgap(buttonGap);
			buttonGridPane.setMaxWidth(3 * (buttonGap + buttonSize));
			buttonGridPane.setMaxHeight(4 * (buttonGap + buttonSize));

			// taskbar checkbox
			ScreenFXTaskbarCheckBox taskBarCheckBox = new ScreenFXTaskbarCheckBox(stage, screenNr, buttonSize);

			// overgive positioner with event related data
			ScreenFXPositioner positioner = new ScreenFXPositioner(stage, graphicsDevices, screenNr);
			new ScreenFXGridAllocator(positioner, buttonGridPane, buttonSize);

			// the screen number of the iterating list - number #1 is main
			// screen
			Label screenLabel = new Label("#" + (screenNr + 1));
			ImageView screenIcon = new ImageView(this.getClass()
					.getResource(ScreenFXConfig.getResourcePath() + "screen.png").toExternalForm());
			screenLabel.setGraphic(screenIcon);
			screenLabel.setMaxWidth(Double.MAX_VALUE);
			screenLabel.setTextAlignment(TextAlignment.CENTER);
			screenLabel.setAlignment(Pos.CENTER);

			// a tooltip which shows some display information for each
			// device
			Tooltip screenInfoTooltip = new Tooltip(
					ScreenFXConfig.getResourceBundle().getString("screeninformation") + ":\n"
							+ graphicsDevices[screenNr].getDisplayMode().getWidth() + "x"
							+ graphicsDevices[screenNr].getDisplayMode().getHeight() + "px @"
							+ graphicsDevices[screenNr].getDisplayMode().getRefreshRate() + "Hz - "
							+ graphicsDevices[screenNr].getDisplayMode().getBitDepth() + "bit");

			screenLabel.setTooltip(screenInfoTooltip);
			HBox screenLabelCenteringHBox = new HBox(2);
			HBox.setHgrow(screenLabel, Priority.ALWAYS);
			screenLabelCenteringHBox.getChildren().add(screenLabel);

			VBox screenVBox = new VBox(2);
			screenVBox.getChildren().addAll(buttonGridPane, taskBarCheckBox, screenLabelCenteringHBox);
			screenAnchorPanes.get(screenNr).getChildren().add(screenVBox);
		}
		topAnchorPane.getChildren().add(screenGridPane);
		getContent().addAll(topAnchorPane);

		/*
		 * the following two event handlers check the user behavior of exiting
		 * and reentering the topAnchorPane. If the user exits while
		 * delayService runs and turns to state "SUCCEEDED". The popup will
		 * hide. If the user reenters topAnchorPane while delayService is
		 * running yet the Service will take the value "CANCELED". The popup
		 * will not hide.
		 */
		topAnchorPane.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// (re)start new delay service
				delayService.restart();
				delayService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent event) {
						hide();
					}
				});
			}
		});

		topAnchorPane.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (delayService.isRunning()) {
					delayService.cancel();
				}
			}
		});

		// a dirty resize method to align rectangle to popup
		setOnShown(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				PointerInfo mousePointer = MouseInfo.getPointerInfo();
				int x = (int) mousePointer.getLocation().getX() - (int) getWidth() / 2;
				int y = (int) mousePointer.getLocation().getY() - (int) getHeight() / 2;
				setX(x);
				setY(y);
				borderRectangle.setWidth(getWidth());
				borderRectangle.setHeight(getHeight());
			}
		});
	}
}
