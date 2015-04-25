package application;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Set;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import resources.CruiseControl;
import backendCommunication.GPPDHandler;

import com.owlike.genson.Genson;
import com.owlike.genson.TransformationException;

import config.Settings;

public class Main extends Application {

	@FXML
	private TextArea cruiseObject = new TextArea();
	@FXML
	private TextArea headers = new TextArea();
	@FXML
	private Button button1 = new Button();
	@FXML
	private Button button2 = new Button();
	@FXML
	private Button button3 = new Button();
	@FXML
	private Button button4 = new Button();
	@FXML
	private Button button5 = new Button();
	@FXML
	private HBox hBox = new HBox();

	@FXML
	private Button[] buttons = new Button[4];

	// currentLinks stores all the links from the last server response. After
	// every server request we have to call the
	// getLinksFromHeader Method from ConnectionFilter to make shure
	// currentLinks is uptodate
	private HashMap<String, String> currentLinks = new HashMap<String, String>();
	private HashMap<String, String> addedHeaders = new HashMap<String, String>();
	private CruiseControl cc;
	private Genson gen = new Genson();

	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource(
					"../layout/MainLayout.fxml"));
			Scene scene = new Scene(root, 600, 450);
			scene.getStylesheets().add(
					getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("REST Example");
			// preparing starting process with the start button (Button5)

			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void startProgramm() {
		cc = new CruiseControl();
		String jsonBody;
		try {
			// prepare for upcomming server request
			fillButtonArray();
			addedHeaders.put("content-type", "application/json");
			jsonBody = gen.serialize(cc);

			// do server request and parse response
			HttpURLConnection con = GPPDHandler.sendPOST(Settings.base,
					jsonBody, addedHeaders);
			currentLinks = ConnectionFilter.getLinksFromHeader(con);
			cc = gen.deserialize(GPPDHandler.fetchingResponse(con),
					CruiseControl.class);

			// affect userinterface
			headers.setText(getTextForHeader());
			cruiseObject.setText(gen.serialize(cc));
			button5.setDisable(true);
			nameButtons();
		} catch (TransformationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void handleButtonPress(ActionEvent event) {
		Button source = (Button) event.getSource();
		String buttonText = source.getText();
		String currentURL = currentLinks.get(buttonText);
		String jsonBody = cruiseObject.getText();
		try {
			
			HttpURLConnection con = GPPDHandler.sendPOST(currentURL, jsonBody,
					addedHeaders);
			currentLinks = ConnectionFilter.getLinksFromHeader(con);
			cc = gen.deserialize(GPPDHandler.fetchingResponse(con),
					CruiseControl.class);

			// affect userinterface
			headers.setText(getTextForHeader());
			cruiseObject.setText(gen.serialize(cc));
			nameButtons();

		} catch (TransformationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

//	----------------------------------------------------------------------------
	private String getTextForHeader() {
		Set<String> keys = currentLinks.keySet();
		String returnValue = "";
		for (String key : keys) {
			returnValue += key + " : " + currentLinks.get(key) + "\n";
		}

		return returnValue;
	}

	private void nameButtons() {
		Set<String> keys = currentLinks.keySet();

		int i = 0;
		for (String relType : keys) {
			if (!relType.equals("self")) {
				buttons[i].setText(relType);
				buttons[i].setOpacity(1);
				++i;
			}
			
		}
		for (; i < buttons.length; ++i) {
			buttons[i].setOpacity(0);
		}
	}

	private void fillButtonArray() {

		buttons[0] = button1;
		buttons[1] = button2;
		buttons[2] = button3;
		buttons[3] = button4;

	}
}
