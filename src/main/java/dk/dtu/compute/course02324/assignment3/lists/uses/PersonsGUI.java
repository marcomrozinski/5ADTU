package dk.dtu.compute.course02324.assignment3.lists.uses;
import dk.dtu.compute.course02324.assignment3.lists.implementations.GenericComparator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;


public class PersonsGUI extends GridPane {

    final private List<Person> persons;
    private GridPane personsPane;
    private GridPane exeptionPane;

    Label avgWeightLabel = new Label("Average weight: -");
    Label mostFrequentNameLabel = new Label("Most common name: -");

    public PersonsGUI(@NotNull List<Person> persons) {
        this.persons = persons;

        this.setVgap(5.0);
        this.setHgap(5.0);

        TextField field = new TextField();
        field.setPrefColumnCount(5);
        field.setText("Name");

        TextField weightField = new TextField();
        weightField.setPrefColumnCount(5);
        weightField.setText("Weight");

        TextField ageField = new TextField();
        ageField.setPrefColumnCount(3);
        ageField.setText("Age");

        TextField indexField = new TextField();
        indexField.setPrefColumnCount(3);
        indexField.setText("Index");

        Button addButton = new Button("Add");
        addButton.setOnAction(
                e -> {
                    try {
                        String name = field.getText();
                        if(name.isEmpty()){
                            addExeptionInPane("Person have to have a name");
                            return;
                        }
                        int weight = Integer.parseInt(weightField.getText());
                        if (weight <= 0) {
                            addExeptionInPane("Weight must be positive.");
                        }
                        int age = Integer.parseInt(ageField.getText());
                        if (age <= 0) {
                            addExeptionInPane("Age must be positive.");
                        }
                        Person person = new Person(name, weight, age);
                        persons.add(person);
                        update();
                    } catch (NumberFormatException ex) {
                        addExeptionInPane("Invalid weight! Please enter a positive number.");
                    }
                });

        Comparator<Person> comparator = new GenericComparator<>();

        Button sortButton = new Button("Sort");
        sortButton.setOnAction(
                e -> {
                    persons.sort(comparator);
                    update();
                });

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(
                e -> {
                    persons.clear();
                    exeptionPane.getChildren().clear();
                });


        Button indexButton = new Button("Add at index");
        indexButton.setOnAction(e -> {
            try {
                String name = field.getText();
                int weight = Integer.parseInt(weightField.getText());
                int age = Integer.parseInt(ageField.getText());
                int index = Integer.parseInt(indexField.getText());
                if (weight <= 0) {
                    addExeptionInPane("Weight must be positive.");
                }
                if (index < 0 || index > persons.size()) {
                    addExeptionInPane("Invalid index! Must be between 0 and " + persons.size());
                }

                Person person = new Person(name, weight, age);
                persons.add(index, person);
                update();
            } catch (NumberFormatException ex) {
                addExeptionInPane("Invalid index! Please enter positive numbers.");
            } catch (IndexOutOfBoundsException ex) {
                addExeptionInPane("Index out of bounds");
            }
        });

        Label nametag = new Label("Name:");
        Label weighttag = new Label("Weight:");
        Label agetag = new Label("Age:");

        HBox wbox = new HBox(field,weightField,ageField,indexField);
        wbox.setSpacing(5.0);
        HBox ibox = new HBox(indexButton,indexField);
        ibox.setSpacing(5.0);
        HBox fbox = new HBox(nametag,weighttag, agetag);
        fbox.setSpacing(45.0);

        VBox actionBox = new VBox(fbox,wbox, addButton,ibox, sortButton, clearButton,avgWeightLabel,mostFrequentNameLabel);
        actionBox.setSpacing(5.0);

        this.add(actionBox, 0, 0);

        Label labelPersonsList = new Label("Persons:");

        personsPane = new GridPane();
        personsPane.setPadding(new Insets(5));
        personsPane.setHgap(5);
        personsPane.setVgap(5);

        ScrollPane scrollPane = new ScrollPane(personsPane);
        scrollPane.setMinWidth(300);
        scrollPane.setMaxWidth(300);
        scrollPane.setMinHeight(300);
        scrollPane.setMaxHeight(300);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        exeptionPane = new GridPane();
        exeptionPane.setPadding(new Insets(5));
        exeptionPane.setHgap(5);
        exeptionPane.setVgap(5);

        ScrollPane exeptionScrollPane = new ScrollPane(exeptionPane);
        exeptionScrollPane.setMinWidth(300);
        exeptionScrollPane.setMaxWidth(300);
        exeptionScrollPane.setMinHeight(100);
        exeptionScrollPane.setMaxHeight(100);
        exeptionScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        exeptionScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);


        VBox personsList = new VBox(labelPersonsList, scrollPane, exeptionScrollPane);
        personsList.setSpacing(5.0);
        this.add(personsList, 1, 0);
        update();
    }

    private void update() {
        personsPane.getChildren().clear();

        for (int i=0; i < persons.size(); i++) {
            Person person = persons.get(i);
            Label personLabel = new Label(i + ": " + person.toString());
            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(
                    e -> {
                        persons.remove(person);
                        update();
                    }
            );

            HBox entry = new HBox(deleteButton, personLabel);
            entry.setSpacing(5.0);
            entry.setAlignment(Pos.BASELINE_LEFT);
            personsPane.add(entry, 0, i);
        }
        updateStatistics();
    }

    private void updateStatistics() {
        if (persons == null || persons.isEmpty()) {
            avgWeightLabel.setText("Average weight: -");
            mostFrequentNameLabel.setText("Most occurring name: -");
            return;
        }
        double totalWeight = 0;
        String mostFrequent = "";
        int maxCount = 0;


        for (int i = 0; i < persons.size(); i++) {
            Person person = persons.get(i);
            totalWeight += person.weight;

            int count = 0;
            for (int j = 0; j < persons.size(); j++) {
                if (persons.get(j).name.equals(person.name)) {
                    count++;
                }
            }
            if (count > maxCount) {
                maxCount = count;
                mostFrequent = person.name;
            }
        }

        double avgWeight = totalWeight / persons.size();

        avgWeightLabel.setText(String.format("Average Weight: %.2f kg", avgWeight));
        mostFrequentNameLabel.setText("Most occurring Name: " + maxCount + "x" + mostFrequent);
    }
    private void addExeptionInPane(String exeptionMessage){
        int rowCount = exeptionPane.getChildren().size();
        Label exeptionLabel = new Label(exeptionMessage);
        exeptionPane.add(exeptionLabel, 0, rowCount);
    }
}
