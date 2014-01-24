package examples;

import java.net.URL;
import java.util.Locale;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
			stage1.setResizable(true);
			stage1.setWidth(300);
			stage1.setHeight(300);
			stage1.setX(200);
			stage1.setY(200);
			stage1.initStyle(StageStyle.DECORATED);
			stage1.setTitle("ScreenFX 1. stage");
			
			URL location = getClass().getResource("/examples/RunDemo.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader();
			Parent root = (Parent) fxmlLoader.load(location.openStream());
			Scene sc = new Scene(root);
			ScreenFXDemo fooController = (ScreenFXDemo) fxmlLoader.getController();
			stage1.setScene(sc);
			ScreenFX sfx0 = new ScreenFX();
			sfx0.installOn(fooController.getButtonShowScreenFX(), true);
			sfx0.installOn(fooController.getToggleButtonScreenFX(), false);
			stage1.show();

			Stage infoStage = new Stage(StageStyle.UNIFIED);
			ScreenFX sfx1 = new ScreenFX();
			sfx1.installOn(infoStage);
			infoStage.setTitle("ScreenFX info box");
			AnchorPane secondaryStageRoot = new AnchorPane();
			TextArea description = new TextArea(sfx1.getTooltipText());
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
			AnchorPane stage2root = new AnchorPane();
			stage2.initStyle(StageStyle.DECORATED);
//			stage2.setResizable(false);
			Button button = new Button("Show ScreenFX");
			stage2root.getChildren().add(button);
			stage2.setWidth(300);
			stage2.setHeight(300);
			stage2.setX(200);
			stage2.setY(500);
			Scene scene = new Scene(stage2root);
			stage2.setScene(scene);
			ScreenFX sfx2 = new ScreenFX();
			sfx2.installOn(button, false); // install the resize
													// controller
			// a stage to control another
			// stage
			stage2.show();

			Stage stage3 = new Stage();
			stage3.setTitle("ScreenFX 3. stage");
			AnchorPane stage3root = new AnchorPane();
			stage3root.getChildren().add(button);
			stage3.setWidth(300);
			stage3.setHeight(300);
			stage3.setX(500);
			stage3.setY(500);
			stage3.setScene(new Scene(stage3root));
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
