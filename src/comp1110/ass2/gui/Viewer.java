package comp1110.ass2.gui;

import comp1110.ass2.Board;
import comp1110.ass2.Tile;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.concurrent.TimeUnit;

import static comp1110.ass2.RailroadInk.generateDiceRoll;
import static comp1110.ass2.RailroadInk.isBoardStringWellFormed;


/**
 * A very simple viewer for tile placements in the Railroad Ink game.
 * <p>
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various tile placements.
 */
public class Viewer extends Application {
    /* board layout */
    private static final int VIEWER_WIDTH = 1024;
    private static final int VIEWER_HEIGHT = 768;
    private static final double Tile_WIDTH = 70.0;
    private static final double Tile_START_X = VIEWER_WIDTH / 10 * 2;
    private static final double Tile_START_Y = 60;

    private static final String URI_BASE = "assets/";

    private final Group root = new Group();
    private final Group tiles = new Group();
    private final Group board = new Group();
    private final Group controls = new Group();
    private final Group diceRolls = new Group();
    private final Group specialTiles = new Group();
    TextField textField;

    public static void main(String[] args) {
        launch(args);
    }

    class TileImage extends ImageView {
        double mouseX, mouseY;
        double startX, startY;
        int orientation;

        TileImage(Image image, double startX, double startY, int orientation) {
            super(image);
            this.startX = startX;
            this.startY = startY;
            this.orientation = orientation;
            setFitHeight(Tile_WIDTH);
            setFitWidth(Tile_WIDTH);
            setX(startX);
            setY(startY);
            setOrientation();

            setOnScroll(event -> {
                double deltaY = event.getDeltaY();
                if (deltaY < 0)
                    this.orientation = (this.orientation - 1) % 8;
                else
                    this.orientation = (this.orientation + 1) % 8;
                setOrientation();
            });

        }

        void setOrientation() {
            if(this.orientation >= 4) {
                setScaleX(-1);
            }
            setRotate(this.orientation * 90);
        }
    }

    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement A valid placement string
     */
    void makePlacement(String placement) {
        // FIXME Task 4: implement the simple placement viewer
        if(isBoardStringWellFormed(placement)) {
            tiles.getChildren().clear();
            for(int i = 0; i < placement.length(); i+=5) {
                String img = placement.substring(i, i + 2);
                int row = placement.charAt(i + 2) - 65 + 1;
                int col = placement.charAt(i + 3) - 48;
                int orientation = placement.charAt(i + 4) - 48;

                Image image =new Image(Viewer.class.getResource(URI_BASE+img+".png").toString());
                /*ImageView imageview = new ImageView();
                imageview.setImage(image);
                imageview.setFitWidth(Tile_WIDTH);
                imageview.setFitHeight(Tile_WIDTH);
                imageview.setX(Tile_START_X + Tile_WIDTH * col);
                imageview.setY(Tile_START_Y + Tile_WIDTH * row);

                if(orientation >=4){
                    imageview.setScaleX(-1);
                }
                imageview.setRotate(orientation * 90);*/

                TileImage tileImage = new TileImage(image, Tile_START_X + Tile_WIDTH * col, Tile_START_Y + Tile_WIDTH * row, orientation);

                tiles.getChildren().add(tileImage);

            }
        }

    }


    void makeBoard() {
        for(int i = 0; i <= 7; i++) {
            Line l_vertical = new Line();
            l_vertical.setStartX(Tile_START_X + Tile_WIDTH * i);
            l_vertical.setStartY(Tile_START_Y + Tile_WIDTH);
            l_vertical.setEndX(Tile_START_X + Tile_WIDTH * i);
            l_vertical.setEndY(Tile_START_Y + Tile_WIDTH * 8);
            board.getChildren().add(l_vertical);

            Line l_horizontal = new Line();
            l_horizontal.setStartX(Tile_START_X);
            l_horizontal.setStartY(Tile_START_Y + Tile_WIDTH * (i + 1));
            l_horizontal.setEndX(Tile_START_X + Tile_WIDTH * 7);
            l_horizontal.setEndY(Tile_START_Y + Tile_WIDTH * (i + 1));
            board.getChildren().add(l_horizontal);
        }

        Line sl_vertical1 = new Line();
        sl_vertical1.setStroke(Color.RED);
        sl_vertical1.setStrokeWidth(2);
        sl_vertical1.setStartX(Tile_START_X + Tile_WIDTH * 2);
        sl_vertical1.setStartY(Tile_START_Y + Tile_WIDTH * 3);
        sl_vertical1.setEndX(Tile_START_X + Tile_WIDTH * 2);
        sl_vertical1.setEndY(Tile_START_Y + Tile_WIDTH * 6);
        board.getChildren().add(sl_vertical1);

        Line sl_vertical2 = new Line();
        sl_vertical2.setStroke(Color.RED);
        sl_vertical2.setStrokeWidth(2);
        sl_vertical2.setStartX(Tile_START_X + Tile_WIDTH * 5);
        sl_vertical2.setStartY(Tile_START_Y + Tile_WIDTH * 3);
        sl_vertical2.setEndX(Tile_START_X + Tile_WIDTH * 5);
        sl_vertical2.setEndY(Tile_START_Y + Tile_WIDTH * 6);
        board.getChildren().add(sl_vertical2);

        Line sl_horizontal1 = new Line();
        sl_horizontal1.setStroke(Color.RED);
        sl_horizontal1.setStrokeWidth(2);
        sl_horizontal1.setStartX(Tile_START_X + Tile_WIDTH * 2);
        sl_horizontal1.setStartY(Tile_START_Y + Tile_WIDTH * 3);
        sl_horizontal1.setEndX(Tile_START_X + Tile_WIDTH * 5);
        sl_horizontal1.setEndY(Tile_START_Y + Tile_WIDTH * 3);
        board.getChildren().add(sl_horizontal1);

        Line sl_horizontal2 = new Line();
        sl_horizontal2.setStroke(Color.RED);
        sl_horizontal2.setStrokeWidth(2);
        sl_horizontal2.setStartX(Tile_START_X + Tile_WIDTH * 2);
        sl_horizontal2.setStartY(Tile_START_Y + Tile_WIDTH * 6);
        sl_horizontal2.setEndX(Tile_START_X + Tile_WIDTH * 5);
        sl_horizontal2.setEndY(Tile_START_Y + Tile_WIDTH * 6);
        board.getChildren().add(sl_horizontal2);

        Image highExit = new Image(Viewer.class.getResource(URI_BASE + "HighExit.png").toString());
        Image railExit = new Image(Viewer.class.getResource(URI_BASE + "RailExit.png").toString());

        for(int i = 0; i < 12; i++) {
            double x = 0.0;
            double y = 0.0;
            int orientation = 0;
            switch (i) {
                case 0:
                    x = Tile_START_X + Tile_WIDTH * 1;
                    y = Tile_START_Y + Tile_WIDTH * 0.5;
                    orientation = 0;
                    break;
                case 1:
                    x = Tile_START_X + Tile_WIDTH * 3;
                    y = Tile_START_Y + Tile_WIDTH * 0.5;
                    orientation = 0;
                    break;
                case 2:
                    x = Tile_START_X + Tile_WIDTH * 5;
                    y = Tile_START_Y + Tile_WIDTH * 0.5;
                    orientation = 0;
                    break;
                case 3:
                    x = Tile_START_X + Tile_WIDTH * 6.5;
                    y = Tile_START_Y + Tile_WIDTH * 2;
                    orientation = 1;
                    break;
                case 4:
                    x = Tile_START_X + Tile_WIDTH * 6.5;
                    y = Tile_START_Y + Tile_WIDTH * 4;
                    orientation = 1;
                    break;
                case 5:
                    x = Tile_START_X + Tile_WIDTH * 6.5;
                    y = Tile_START_Y + Tile_WIDTH * 6;
                    orientation = 1;
                    break;
                case 6:
                    x = Tile_START_X + Tile_WIDTH * 5;
                    y = Tile_START_Y + Tile_WIDTH * 7.5;
                    orientation = 2;
                    break;
                case 7:
                    x = Tile_START_X + Tile_WIDTH * 3;
                    y = Tile_START_Y + Tile_WIDTH * 7.5;
                    orientation = 2;
                    break;
                case 8:
                    x = Tile_START_X + Tile_WIDTH * 1;
                    y = Tile_START_Y + Tile_WIDTH * 7.5;
                    orientation = 2;
                    break;
                case 9:
                    x = Tile_START_X + Tile_WIDTH * -0.5;
                    y = Tile_START_Y + Tile_WIDTH * 6;
                    orientation = 3;
                    break;
                case 10:
                    x = Tile_START_X + Tile_WIDTH * -0.5;
                    y = Tile_START_Y + Tile_WIDTH * 4;
                    orientation = 3;
                    break;
                case 11:
                    x = Tile_START_X + Tile_WIDTH * -0.5;
                    y = Tile_START_Y + Tile_WIDTH * 2;
                    orientation = 3;
                    break;
            }
            ImageView exitView = new ImageView();
            exitView.setFitWidth(Tile_WIDTH);
            exitView.setFitHeight(Tile_WIDTH);
            if(i%2 == 0) {
                exitView.setImage(highExit);

            } else {
                exitView.setImage(railExit);
            }
            exitView.setX(x);
            exitView.setY(y);
            exitView.setRotate(orientation * 90);
            board.getChildren().add(exitView);
        }

    }

    private void makeSpecialTiles() {
        specialTiles.getChildren().clear();
        double x = Tile_START_X + Tile_WIDTH;
        for(int i=0; i<6; i++) {
            String img = "S" + i;
            Image image = new Image(Viewer.class.getResource(URI_BASE + img + ".png").toString());
            /*ImageView imageview = new ImageView();
            imageview.setImage(image);
            imageview.setFitWidth(Tile_WIDTH);
            imageview.setFitHeight(Tile_WIDTH);
            imageview.setX(x);
            imageview.setY(Tile_START_Y / 3);*/
            TileImage tileImage = new TileImage(image, x, Tile_START_Y / 3, 0);
            x += Tile_WIDTH * 1.5;

            specialTiles.getChildren().add(tileImage);
        }
    }

    private void rollDice() {
        diceRolls.getChildren().clear();
        String diceRoll = generateDiceRoll();
        double y = Tile_START_Y + Tile_WIDTH * 1.5;
        for(int i=0; i<8; i+=2) {
            String img = diceRoll.substring(i, i+2);
            Image image =new Image(Viewer.class.getResource(URI_BASE + img + ".png").toString());
            TileImage tileImage = new TileImage(image, Tile_START_X + Tile_WIDTH * 9, y, 0);
           /* ImageView imageview = new ImageView();
            imageview.setImage(image);
            imageview.setFitWidth(Tile_WIDTH);
            imageview.setFitHeight(Tile_WIDTH);
            imageview.setX(Tile_START_X + Tile_WIDTH * 9);
            imageview.setY(y);*/
            y += Tile_WIDTH * 1.5;

            diceRolls.getChildren().add(tileImage);
        }
    }

    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {

        Button button1 = new Button("Roll Dice");
        button1.setOnAction(e -> {
            rollDice();
        });
        button1.setLayoutX(Tile_START_X + Tile_WIDTH * 9);
        button1.setLayoutY(Tile_START_X + Tile_WIDTH * 6);

        Label label = new Label("Special Tiles:");
        label.setFont(Font.font("Cambria", 24));
        label.setLayoutX(Tile_START_X / 5);
        label.setLayoutY(Tile_START_Y / 2);

        controls.getChildren().addAll(button1, label);

        makeSpecialTiles();

        makeBoard();

        /*
        Label label1 = new Label("Placement:");
        textField = new TextField();
        textField.setPrefWidth(300);
        Button button = new Button("Refresh");
        button.setOnAction(e -> {
            makePlacement(textField.getText());
            textField.clear();
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button);
        hb.setSpacing(10);
        hb.setLayoutX(130);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);
        makeBoard(); */

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("StepsGame Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        root.getChildren().add(controls);
        root.getChildren().add(tiles);
        root.getChildren().add(board);
        root.getChildren().add(diceRolls);
        root.getChildren().add(specialTiles);

        makeControls();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
