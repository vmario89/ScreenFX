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

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;

/**
 * @author vmario
 * 
 */
class ScreenFXGridElement extends Button {
	private final int rowIndex;
	private final int columnIndex;
	private final ScreenFXPosition sfxPosition;

	/*
	 * a grid element - other values for this item as rowIndex, columnIndex and
	 * positionAction do not need a redudant getter/setter method
	 */

	/**
	 * @param rowIndex
	 *            the row index of the button in grid
	 * @param columnIndex
	 *            the column index of the button in grid
	 * @param buttonId
	 *            the button id
	 * @param buttonSize
	 *            the button size (width and height in pixel)
	 * @param buttonIcon
	 *            the button icon
	 * @param toolTipText
	 *            the text for tooltip
	 * @param positionAction
	 *            the action to perform
	 */
	public ScreenFXGridElement(int rowIndex, int columnIndex, String buttonId, int buttonSize,
			ImageView buttonIcon, String toolTipText, ScreenFXPosition positionAction) {
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
		this.sfxPosition = positionAction;
		setText("");

		if (ScreenFXConfig.isAllowTooltips()) {
			if (toolTipText != null & !toolTipText.isEmpty()) {
				setTooltip(new Tooltip(toolTipText));
			}
		}
		setBackground(null);
		if (buttonId != null && !buttonId.isEmpty()) {
			setId(buttonId);
		}
		setMinWidth(buttonSize);
		setMinHeight(buttonSize);
		setMaxWidth(buttonSize);
		setMaxHeight(buttonSize);
		setPadding(new Insets(0, 0, 0, 0));
		setGraphic(buttonIcon);

		Glow glow = new Glow(0.5);
		setOnMouseEntered(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				setEffect(glow);
			}
		});

		setOnMouseExited(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				setEffect(null);
			}
		});

	}

	/**
	 * @return the row index
	 */
	public int getRowIndex() {
		return rowIndex;
	}

	/**
	 * @return the column index
	 */
	public int getColumnIndex() {
		return columnIndex;
	}

	/**
	 * @return the ScreenFXPosition
	 */
	public ScreenFXPosition getSfxPosition() {
		return sfxPosition;
	}

}
