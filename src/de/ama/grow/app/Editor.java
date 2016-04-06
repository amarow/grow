package de.ama.grow.app;


import de.ama.grow.body.Body;
import de.ama.grow.script.Sequence;
import de.ama.grow.util.Util;
import de.ama.grow.view_3d.Space;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;

public class Editor extends Application{

    @FXML
    public TextArea scriptText;

    @FXML
    public TextField fileName;

    @FXML
    private Label statusText;

    @FXML
    void incrementCellAmount(ActionEvent event) {
        Environment.get().incrementMaxCells();
    }

    private Stage stage = null;

    public Editor() {
    }

    @FXML
    void startPressed(ActionEvent event) {
        stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("3D View");
        Space space = new Space();
        stage.setScene(space.start());
        stage.show();

        startEnvironment(space);
    }


    private void startEnvironment(Space space) {

        Environment.get().stop();

        Sequence sequence =null;



        if (Util.isEmpty(scriptText.getText())) {
            fileName.setText("/Users/ama/dev/grow3d/src/de/ama/grow/script/grow.txt");
            scriptText.setText(Util.readFile(new File(fileName.getText())));
        }

        if (Util.isNotEmpty(scriptText.getText())) {
            Util.saveFile(scriptText.getText(), new File(fileName.getText()));
            ScriptStore.get().setScriptText(scriptText.getText());
            if(ScriptStore.get().getErrorText()!=null){
                System.out.println("ScriptStore.get().getErrorText() = " + ScriptStore.get().getErrorText());
            }
            sequence = ScriptStore.get().getSequence();
        }

        Body body = new Body(new Point3D(0,0,0) ,"B1", sequence);
        space.addGroup(body);
        body.setVisible(true);

        Environment.get().addBody(body);
        Environment.get().setEditor(this);
        Environment.get().start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            URL resource = Editor.class.getResource("gui.fxml");
            loader.setLocation(resource);
            BorderPane rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTitleText(String text){
        stage.setTitle(text);
    }
    public void setStatusText(String text){
        statusText.setText(text);
    }

    public void loadFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            scriptText.setText(Util.readFile(file));
            fileName.setText(file.getAbsolutePath());
        }
    }
}
