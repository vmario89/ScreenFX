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
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javafx.stage.Stage;

/**
 * @author vmario
 * 
 */
class ScreenFXPositioner {
	private final Stage stage;
	private final GraphicsDevice[] graphicsDevices;
	private final int screenNr;

	/**
	 * @param stage the stage to set on position
	 * @param graphicsDevices
	 *            the list of graphic devices
	 * @param screenNr
	 *            the id/number of screen
	 */
	public ScreenFXPositioner(Stage stage, GraphicsDevice[] graphicsDevices, int screenNr) {

		this.stage = stage;
		this.graphicsDevices = graphicsDevices;
		this.screenNr = screenNr;
	}

	/**
	 * 
	 * @param sfxPosition
	 *            the position to set
	 * @param doubleClick
	 *            boolean value if the user performed a single click (false) or
	 *            double click (true)
	 * @throws Exception push exceptions
	 */
	public void setPosition(ScreenFXPosition sfxPosition, boolean doubleClick) throws Exception {

		GraphicsConfiguration gc = graphicsDevices[screenNr].getDefaultConfiguration();
		Rectangle bounds = gc.getBounds();
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);

		int posX, posY, dimX, dimY;

		posX = bounds.x + screenInsets.left;
		posY = bounds.y + screenInsets.top;
		dimX = bounds.width - screenInsets.left - screenInsets.right;
		dimY = bounds.height - screenInsets.top - screenInsets.bottom;

		// overwrite values for full fit to screen

		if (!ScreenFXConfig.getTaskbarIncludeSelectedProperties().get(screenNr)
				& !ScreenFXConfig.taskbarIncludeIndeterminateProperties.get(screenNr)) {
			posX = bounds.x;
			posY = bounds.y;
			dimX = bounds.width;
			dimY = bounds.height;
		}

		// disable resize with double click if the stage is not resizable
		if (!stage.isResizable()) {
			doubleClick = false;
		}

		switch (sfxPosition) {
		case LEFT_TOP:
			if (doubleClick) {
				stage.setWidth(dimX / 2);
				stage.setHeight(dimY / 2);
				stage.setX(posX);
				stage.setY(posY);
			} else {
				stage.setX(posX);
				stage.setY(posY);
			}
			break;
		case MIDDLE_TOP:
			if (doubleClick) {
				stage.setWidth(dimX);
				stage.setHeight(dimY / 2);
				stage.setX(posX);
				stage.setY(posY);
			} else {
				stage.setX(posX + (dimX / 2) - (stage.getWidth() / 2));
				stage.setY(posY);
			}
			break;
		case RIGHT_TOP:
			if (doubleClick) {
				stage.setWidth(dimX / 2);
				stage.setHeight(dimY / 2);
				stage.setX(posX + dimX - stage.getWidth());
				stage.setY(posY);

			} else {
				stage.setX(posX + dimX - stage.getWidth());
				stage.setY(posY);
			}
			break;

		case LEFT_MIDDLE:
			if (doubleClick) {
				stage.setWidth(dimX / 2);
				stage.setHeight(dimY);
				stage.setX(posX);
				stage.setY(posY);
			} else {
				stage.setX(posX);
				stage.setY(posY + (dimY / 2) - (stage.getHeight() / 2));
			}
			break;
		case MIDDLE_MIDDLE:
			if (doubleClick) {
				stage.setWidth(dimX);
				stage.setHeight(dimY);
				stage.setX(posX);
				stage.setY(posY);
			} else {
				stage.setX(posX + (dimX / 2) - (stage.getWidth() / 2));
				stage.setY(posY + (dimY / 2) - (stage.getHeight() / 2));
			}
			break;
		case RIGHT_MIDDLE:
			if (doubleClick) {
				stage.setWidth(dimX / 2);
				stage.setHeight(dimY);
				stage.setX(posX + dimX - stage.getWidth());
				stage.setY(posY);
			} else {
				stage.setX(posX + dimX - stage.getWidth());
				stage.setY(posY + (dimY / 2) - (stage.getHeight() / 2));
			}
			break;

		case LEFT_BOTTOM:
			if (doubleClick) {
				stage.setWidth(dimX / 2);
				stage.setHeight(dimY / 2);
				stage.setX(posX);
				stage.setY(posY + dimY - stage.getHeight());
			} else {
				stage.setX(posX);
				stage.setY(posY + dimY - stage.getHeight());
			}
			break;
		case MIDDLE_BOTTOM:
			if (doubleClick) {
				stage.setWidth(dimX);
				stage.setHeight(dimY / 2);
				stage.setX(posX + (dimX / 2) - (stage.getWidth() / 2));
				stage.setY(posY + dimY - stage.getHeight());
			} else {
				stage.setX(posX + (dimX / 2) - (stage.getWidth() / 2));
				stage.setY(posY + dimY - stage.getHeight());
			}
			break;
		case RIGHT_BOTTOM:
			if (doubleClick) {
				stage.setWidth(dimX / 2);
				stage.setHeight(dimY / 2);
				stage.setX(posX + dimX - stage.getWidth());
				stage.setY(posY + dimY - stage.getHeight());
			} else {
				stage.setX(posX + dimX - stage.getWidth());
				stage.setY(posY + dimY - stage.getHeight());
			}
			break;

		case RESIZE_HEIGHT:
			stage.setHeight(dimY);
			stage.setX(posX);
			stage.setY(posY);
			break;

		case RESIZE_WIDTH:
			stage.setWidth(dimX);
			stage.setX(posX);
			stage.setY(posY);
			break;
		case FULLSCREEN:
			/*
			 * set correct screen for applying fullscreenmode - the fullscreen
			 * mode will be applied with ActionEvent in ScreenFXGridAllocator!
			 */
			stage.setWidth(dimX);
			stage.setHeight(dimY);
			stage.setX(posX);
			stage.setY(posY);
			break;

		default:
			break;
		}
		;
	}

	/**
	 * @return the screen number
	 */
	public int getScreenNr() {
		return screenNr;
	}

	Stage getStage() {
		return stage;
	}

}
