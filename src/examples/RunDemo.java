package examples;


import java.util.Locale;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.vmario.screenfx.ScreenFX;

/**
 * @author vmario
 * 
 */
public class RunDemo extends Application {

	@Override
	public void start(final Stage stage) {
		try {
			Stage stage1 = stage;
			stage1.setWidth(300);
			stage1.setHeight(300);
			stage1.setX(200);
			stage1.setY(200);
			stage1.initStyle(StageStyle.DECORATED);
			stage1.setTitle("ScreenFX 1. stage");
			stage1.setScene(new Scene(FXMLLoader.load(getClass().getResource(
					"/examples/RunDemo.fxml"))));
			stage1.show();

			Stage infoStage = new Stage(StageStyle.UNIFIED);
			infoStage.setTitle("ScreenFX info box");
			AnchorPane secondaryStageRoot = new AnchorPane();
			TextArea description = new TextArea(
					"This stage does not react on ScreenFX events as you can recognize. Just press [Shift + D] as it is defined in the 1. stage and try it out on all the stages");
			description.setEditable(false);
			description.setWrapText(true);
			AnchorPane.setBottomAnchor(description, 0d);
			AnchorPane.setTopAnchor(description, 0d);
			AnchorPane.setLeftAnchor(description, 0d);
			AnchorPane.setRightAnchor(description, 0d);
			secondaryStageRoot.getChildren().add(description);
			infoStage.setWidth(300);
			infoStage.setHeight(300);
			infoStage.setX(500);
			infoStage.setY(200);
			infoStage.setScene(new Scene(secondaryStageRoot));
			infoStage.show();

			Stage stage2 = new Stage();
			stage2.setTitle("ScreenFX 2. stage");
			AnchorPane sage2root = new AnchorPane();
			stage2.setResizable(false);
			Button button = new Button("Show ScreenFX");
			ScreenFX screenFX = new ScreenFX();
			screenFX.install(button);
			sage2root.getChildren().add(button);
			stage2.setWidth(300);
			stage2.setHeight(300);
			stage2.setX(200);
			stage2.setY(500);
			stage2.setScene(new Scene(sage2root));
			stage2.show();

			Stage stage3 = new Stage(StageStyle.UTILITY);
			stage3.setTitle("ScreenFX 3. stage");
			AnchorPane stage4root = new AnchorPane();
			stage4root.getChildren().add(button);
			stage3.setWidth(300);
			stage3.setHeight(300);
			stage3.setX(500);
			stage3.setY(500);
			stage3.setScene(new Scene(stage4root));
			stage3.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param args
	 *            the main arguments - pass with null
	 */
	public static void main(String[] args) {
		Locale.setDefault(Locale.ENGLISH);
		Platform.setImplicitExit(true);
		launch(args);
	}

}
