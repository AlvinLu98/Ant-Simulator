import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Display extends Application {
    public static AnimationTimer timer;
    public static Timeline timeline;
    public static long startTime = 0;
    public static long currentTime = 0;
    public static Label time;

    private Simulator sim = new AntSimulator(60); //Creates an ant simulator
    private GridPane grid;
    private Scene scene;
    private Stage menuStage;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        primaryStage.setResizable(false); //Set Window size to be not resizeable
        initialize(primaryStage);
        primaryStage.show();
    }

    //https://docs.oracle.com/javafx/2/get_started/form.htm
    public void initialize(Stage formStage){
        formStage.setTitle("Ant info");
        this.grid = new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25,25,25,25));

        createAntForm(formStage);

        this.scene = new Scene(grid, Simulator.WIDTH, Simulator.HEIGHT);
        formStage.setScene(scene);
    }

    private void createAntForm(Stage primaryStage){
        Text sceneTitle = new Text("Settings for ant");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2,1);

        Label antNo = new Label("No. of Ants:");
        grid.add(antNo, 0, 1);

        TextField antNoTextField = new TextField();
        grid.add(antNoTextField, 1, 1);

        Label evaporationRate = new Label("Evaporation rate:");
        grid.add(evaporationRate, 0, 2);

        TextField evaporationBox = new TextField();
        grid.add(evaporationBox, 1, 2);

        Label foodAmt = new Label("Number of food item:");
        grid.add(foodAmt, 0, 3);

        TextField foodAmtBox = new TextField();
        grid.add(foodAmtBox, 1, 3);


        Label obstacleLabel = new Label("Type of obstacle");
        grid.add(obstacleLabel, 0, 4);

        //https://docs.oracle.com/javafx/2/ui_controls/combo-box.htm
        ComboBox obstacleType = new ComboBox();
        obstacleType.getItems().addAll(
                "No Obstacle",
                "One Obstacle",
                "Two Obstacle"
        );
        obstacleType.setMinWidth(200.0);
        grid.add(obstacleType, 1, 4);

        Button btn = new Button("Start Simulation");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 5);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                boolean valid = true;
                Alert a = new Alert(Alert.AlertType.NONE);
                String errorMes = "";
                AntSimulator ant = (AntSimulator)sim;
                if(!antNoTextField.getText().isEmpty() && isDigit(antNoTextField.getText())){
                    ant.setNumAnt(Integer.parseInt(antNoTextField.getText()));
                }
                else{
                    a.setAlertType(Alert.AlertType.ERROR);
                    errorMes += "Please enter a valid number for ants\n";
                    antNoTextField.clear();
                    valid = false;
                }

                if(!evaporationBox.getText().isEmpty() && isDouble(evaporationBox.getText())){
                    double eva = Double.valueOf(evaporationBox.getText());
                    System.out.println(eva);
                    if(eva < 1) {
                        ant.setEvaporationRate(eva);
                    }
                }
                else{
                    a.setAlertType(Alert.AlertType.ERROR);
                    errorMes +="Please enter a valid number for evaporation constant" +
                            " (number must be smaller than 1)\n";
                    evaporationBox.clear();
                    valid = false;
                }

                if(!foodAmtBox.getText().isEmpty() && isDigit(foodAmtBox.getText())){
                   int amt = Integer.valueOf(foodAmtBox.getText());
                    if(amt <= 10 && amt >= 0) {
                       ant.setNumFood(amt);
                    }
                    else{
                        a.setAlertType(Alert.AlertType.ERROR);
                        errorMes += "Please enter a number between 0 - 10 for food!\n";
                        foodAmtBox.clear();
                        valid = false;
                    }
                }
                else{
                    a.setAlertType(Alert.AlertType.ERROR);
                    errorMes += "Please enter a valid number for food\n";
                    foodAmtBox.clear();
                    valid = false;
                }

                if(obstacleType.getValue() != null){
                    ant.setObstacleType(String.valueOf(obstacleType.getValue()));
                }
                else{
                    a.setAlertType(Alert.AlertType.ERROR);
                    errorMes += "Select an Obstacle type";
                    valid = false;
                }

                if(valid) {
                    beginSimulation(primaryStage);
                }
                else{
                    a.setContentText(errorMes);
                    a.show();
                }
            }
        });
    }

    private void beginSimulation(Stage primaryStage){
        sim.initialize(primaryStage);
        createMenu();

        sim.buildLoop();
        buildMenuLoop();

        primaryStage.show();
        menuStage.show();

        sim.beginSimulation();
        beginMenuTimer();
    }

    private void createMenu(){
        startTime = System.currentTimeMillis();

        //https://coderanch.com/t/620036/java/Stage-corner-screen
        Rectangle2D screenSize = Screen.getPrimary().getBounds();

        menuStage = new Stage();
        menuStage.setTitle("Settings");
        menuStage.setX(screenSize.getMinX() + screenSize.getMaxX() - 700);
        menuStage.setY(screenSize.getMinY() + 20);

        TabPane tabs = new TabPane();
        //https://stackoverflow.com/questions/31531059/how-to-remove-close-button-from-tabs-in-javafx
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Scene menuScene = new Scene(tabs, 500,500);

        Tab controls = new Tab();
        controls.setText("Controls");

        GridPane controlBox = new GridPane();
        controlBox.setHgap(10);
        controlBox.setVgap(10);
        controlBox.setPadding(new Insets(25,25,25,25));

        time = new Label(String.valueOf(currentTime));
        controlBox.add(time, 0,0);

        controls.setContent(controlBox);

        tabs.getTabs().add(controls);

        createMenuEvents();
        menuStage.setScene(menuScene);
    }

    private void buildSetUp(){
        //https://coderanch.com/t/620036/java/Stage-corner-screen
        Rectangle2D screenSize = Screen.getPrimary().getBounds();

        menuStage = new Stage();
        menuStage.setTitle("Set up");
        menuStage.setX(screenSize.getMinX() + screenSize.getMaxX() - 700);
        menuStage.setY(screenSize.getMinY() + 20);
    }

    private void buildMenuLoop(){
        //https://asgteach.com/2011/10/javafx-animation-and-binding-simple-countdown-timer-2/
        final Duration oneFrameAmt = Duration.millis(1000/60);
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
                event -> {
                        time.setText(String.valueOf(currentTime));
                });
        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.getKeyFrames().add(oneFrame);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                currentTime = System.currentTimeMillis() - startTime;
            }
        };
    }

    private void beginMenuTimer(){
        Display.timeline.play();
        Display.timer.start();
    }

    private void createMenuEvents(){
        //https://www.programcreek.com/java-api-examples/?class=javafx.stage.Stage&method=setOnCloseRequest
        menuStage.setOnCloseRequest(event -> {
            Platform.exit();
        });
    }

    //https://www.geeksforgeeks.org/program-check-input-integer-string/
    private boolean isDigit(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean isDouble(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                if(Character.compare(s.charAt(i), '.') != 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
