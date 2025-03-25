package dk.dtu.compute.course02324.assignment3.lists.uses;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class PersonsApp extends Application {

    private Stage stage;


    private Pane root;

    private PersonsGUI personsGUI = null;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        MenuBar menuBar = createMenuBar();
        root = new Pane();
        VBox box = new VBox(menuBar, root);
        Scene scene = new Scene(box);

        this.stage.setScene(scene);

        this.stage.setTitle("Persons List App");
        this.stage.setResizable(false);
        this.stage.sizeToScene();
        this.stage.show();
    }


    private MenuBar createMenuBar() {
        Menu selectMenu = new Menu("Choose Implementation");

        MenuItem unsortedListItem = new MenuItem("(unsorted) List");
        unsortedListItem.setOnAction(
                e -> {
                    List<Person> list = new ArrayList<>();
                    switchImpl(list);
                }
        );

        selectMenu.getItems().add(unsortedListItem);
/*
        MenuItem sortedListItem = new MenuItem("sorted List");
        sortedListItem.setOnAction(
                e -> {
                    List<Person> list = new SortedArrayList<>();
                    switchImpl(list);
                }
        );
        selectMenu.getItems().add(sortedListItem);
*/
        MenuItem noListItem = new MenuItem("No Implementation");
        noListItem.setOnAction(
                e -> { switchImpl(null); }
        );
        selectMenu.getItems().add(noListItem);

        MenuBar menubar = new MenuBar();
        menubar.setMinWidth(400);
        menubar.getMenus().add(selectMenu);

        return menubar;
    }

    private void switchImpl(List<Person> list) {
        // if there exists a GUI for some stack already, this GUI
        // is removed.
        if (personsGUI != null) {
            root.getChildren().remove(personsGUI);

        }

        if (list != null) {
            personsGUI = new PersonsGUI(list);

            root.getChildren().add(personsGUI);
            this.stage.setTitle("Persons list test: " + list.getClass().getSimpleName());
        } else {
            // otherwise only the title of the application window is changed
            personsGUI = null;
            this.stage.setTitle("Persons list test: no implementation");
        }

        stage.sizeToScene();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
