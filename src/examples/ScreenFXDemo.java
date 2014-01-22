/*
 * ScreenFX - A plugin library for JavaFX 8 adding features for resizing 
 * and arranging stage windows on a multiple screen hardware configuration
 * 
 * Copyright (C) 21014 Mario Voigt
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
package examples;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;

import com.vmario.screenfx.ScreenFX;

/**
 * @author vmario
 * 
 */
public class ScreenFXDemo implements Initializable {
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private AnchorPane anchorPaneRoot;

	@FXML
	private Button buttonShowScreenFX;

	@FXML
	private ToggleButton toggleButtonScreenFX;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// make a new ScreenFX object
		ScreenFX screenFX = new ScreenFX();

		// put some static properties to override the standard one's
		Properties sfxProperties = ScreenFX.getScreenFXProperties();
		sfxProperties.put("UIConventionOverride", new Boolean(false));
		sfxProperties.put("allowTooltips", new Boolean(true));
		sfxProperties.put("exitDelayTime", new Long(200l));
		List<Boolean> taskbarStandards = new ArrayList<Boolean>();
		taskbarStandards.add(new Boolean(false));
		taskbarStandards.add(new Boolean(true));
		sfxProperties.put("taskbarIncludeSelected", taskbarStandards);
		// sfxProperties.put("popupKeyCodeCombination", new
		// KeyCodeCombination(KeyCode.D,
		// KeyCombination.SHIFT_DOWN)); //example of a key combination
		// sfxProperties.remove("popupKeyCodeCombination");
		// sfxProperties.put("popupKeyCodeCombination", KeyCode.D); // example
		// for
		// // single
		// // key
		// sfxProperties.put("quickResizeKeyCodeCombination", KeyCode.B); //
		// example
		// for
		// single
		// key
		screenFX.setScreenFXProperties(sfxProperties);

		// install ScreenFX to some nodes
		screenFX.install(buttonShowScreenFX);
		screenFX.install(toggleButtonScreenFX);
	}

	@FXML
	private void handleScreenFX() {
		System.out.println("you pressed a button");
	}

}
