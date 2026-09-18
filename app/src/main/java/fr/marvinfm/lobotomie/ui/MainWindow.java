package fr.marvinfm.lobotomie.ui;

import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
public class MainWindow {
    public void show(Stage stage) {
        TabPane tabs = new TabPane();
        // plus tard : tabs.getTabs().add(... CoursesTab, AlgoTab, etc.)

        
        Scene scene = new Scene(tabs, 1024, 720);
        stage.setTitle("Lobotomie");
        stage.setScene(scene);
        stage.show();
    }
}