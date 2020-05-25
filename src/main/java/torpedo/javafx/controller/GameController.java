package torpedo.javafx.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DurationFormatUtils;
import torpedo.results.GameResult;
import torpedo.results.GameResultDao;
import torpedo.state.Field;
import torpedo.state.Ship;
import torpedo.state.ShipState;

import javax.inject.Inject;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class GameController {

    @Inject
    private FXMLLoader fxmlLoader;

    @Inject
    private GameResultDao gameResultDao;

    private String playerOneName;
    private String playerTwoName;
    private Field playerOneField;
    private Field playerTwoField;
    private IntegerProperty steps = new SimpleIntegerProperty();
    private Instant startTime;

    private int playerTurn;

    private HashMap<ShipState, Image> images;

    @FXML
    private Label messageLabel;

    @FXML
    private GridPane leftGrid;

    @FXML
    private GridPane rightGrid;

    @FXML
    private Label stepsLabel;

    @FXML
    private Label stopWatchLabel;

    private Timeline stopWatchTimeline;

    @FXML
    private Button resetButton;

    @FXML
    private Button giveUpButton;

    @FXML
    private Button rotateButton;

    @FXML
    private Label notificationText;

    private BooleanProperty gameOver = new SimpleBooleanProperty();

    public void setPlayerName(String playerName, String otherPlayerName) {
        this.playerOneName = playerName;
        this.playerTwoName = otherPlayerName;
    }

    @FXML
    public void initialize() {
        stepsLabel.textProperty().bind(steps.asString());
        gameOver.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                log.info("Game is over");
                log.debug("Saving result to database...");
                gameResultDao.persist(createGameResult());
                stopWatchTimeline.stop();
            }
        });
        images = new HashMap<ShipState, Image>();

        images.put(ShipState.WHITE, new Image(this.getClass().getResource("/images/white.png").toExternalForm()));
        images.put(ShipState.BLACK, new Image(this.getClass().getResource("/images/black.png").toExternalForm()));
        images.put(ShipState.CROSS, new Image(this.getClass().getResource("/images/cross.png").toExternalForm()));
        images.put(ShipState.RED, new Image(this.getClass().getResource("/images/red.png").toExternalForm()));

        leftGrid.setOnMouseClicked(e -> handleClickOnGrid(e, 1));
        rightGrid.setOnMouseClicked(e -> handleClickOnGrid(e, 2));

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                ImageView view = new ImageView();
                view.setPickOnBounds(true);
                view.setPreserveRatio(true);
                view.setImage(images.get(ShipState.WHITE));
                leftGrid.add(view, i, j);

                ImageView view2 = new ImageView();
                view2.setPickOnBounds(true);
                view2.setPreserveRatio(true);
                view2.setImage(images.get(ShipState.WHITE));
                rightGrid.add(view2, i, j);
            }
        }

        resetGame();
    }

    private void resetGame() {
        playerOneField = new Field();
        playerTwoField = new Field();
        playerTurn = 1;

        steps.set(0);
        startTime = Instant.now();
        gameOver.setValue(false);
        displayGameState();
        createStopWatch();
        Platform.runLater(() -> messageLabel.setText("Battle: " + playerOneName + " vs " + playerTwoName + "!"));
    }

    private Field getCurrentPlayerField() {
        return playerTurn == 1 ? playerOneField : playerTwoField;
    }

    private void drawFieldShips(Field field, GridPane grid) {
        for (Ship s : field.getShips()) {
            for (int x = 0; x < s.getWidth(); x++) {
                for (int y = 0; y < s.getHeight(); y++) {
                    ImageView view = (ImageView)grid.getChildren().get((s.getY() + y) * 10 + s.getX() + x + 1);
                    view.setImage(images.get(s.getState()));
                }
            }
        }
    }

    private void displayGameState() {
        notificationText.setText("Place the next ship (" + playerOneField.getNextShipName() + ") with size " + playerOneField.getNextSize());

        // Clear grids (set image views to white)
        leftGrid.getChildren().stream().filter(s -> s instanceof ImageView).map(s -> (ImageView) s).forEach(s -> s.setImage(images.get(ShipState.WHITE)));
        rightGrid.getChildren().stream().filter(s -> s instanceof ImageView).map(s -> (ImageView) s).forEach(s -> s.setImage(images.get(ShipState.WHITE)));

        drawFieldShips(getCurrentPlayerField(), leftGrid);
    }

    public void handleClickOnGrid(MouseEvent mouseEvent, int target) {
        int row = GridPane.getRowIndex((Node) mouseEvent.getTarget());
        int col = GridPane.getColumnIndex((Node) mouseEvent.getTarget());
        log.debug("ImageView ({}, {}) is pressed", row, col);

        if (target == 1) {
            getCurrentPlayerField().addShip(row, col, rotateButton.getText().equals("Vertical"));
        } else {


            //playerTwoField.addShip(row, col, rotateButton.getText().equals("Vertical"));
        }

        displayGameState();
    }

    public void handleRotateButton(ActionEvent actionEvent)  {
        if (rotateButton.getText().equals("Vertical"))
            rotateButton.setText("Horizontal");
        else
            rotateButton.setText("Vertical");
    }

    public void handleResetButton(ActionEvent actionEvent)  {
        log.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        log.info("Resetting game...");
        stopWatchTimeline.stop();
        resetGame();
    }

    public void handleGiveUpButton(ActionEvent actionEvent) throws IOException {
        String buttonText = ((Button) actionEvent.getSource()).getText();
        log.debug("{} is pressed", buttonText);
        if (buttonText.equals("Give Up")) {
            log.info("The game has been given up");
        }
        gameOver.setValue(true);
        log.info("Loading high scores scene...");
        fxmlLoader.setLocation(getClass().getResource("/fxml/highscores.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private GameResult createGameResult() {
        GameResult result = GameResult.builder()
                .player(playerOneName)
                .solved(false)
                .duration(Duration.between(startTime, Instant.now()))
                .steps(steps.get())
                .build();
        return result;
    }

    private void createStopWatch() {
        stopWatchTimeline = new Timeline(new KeyFrame(javafx.util.Duration.ZERO, e -> {
            long millisElapsed = startTime.until(Instant.now(), ChronoUnit.MILLIS);
            stopWatchLabel.setText(DurationFormatUtils.formatDuration(millisElapsed, "HH:mm:ss"));
        }), new KeyFrame(javafx.util.Duration.seconds(1)));
        stopWatchTimeline.setCycleCount(Animation.INDEFINITE);
        stopWatchTimeline.play();
    }

}
