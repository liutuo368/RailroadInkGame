package comp1110.ass2.gui;

import comp1110.ass2.Board;
import comp1110.ass2.Tile;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static comp1110.ass2.RailroadInk.*;


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

    protected static String CURRENT_PLACEMENT = "";
    private String ROLL_MEMBERS = "";
    private String SPECIAL_MEMBERS = "S0S1S2S3S4S5";
    private int specialCount1 = 0;
    private int specialCount2 = 0;
    private int round = 0;
    private int tilesLeft = 0;
    private boolean roundSpecialUsed = false;
    private boolean gameOver = false;
    private boolean multiPlayer = false;
    private int currentPlayer = 1;

    private static final String URI_BASE = "assets/";

    private final Group root = new Group();
    private final Group tiles = new Group();
    private final Group board = new Group();
    private final Group controls = new Group();
    private final Group diceRolls = new Group();
    private final Group specialTiles = new Group();

    public static void main(String[] args) {
        launch(args);
    }

    class Grid {
        private double x;
        private double y;

        public Grid(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }
    }

    Grid[][] grids = new Grid[7][7];

    private void initGrids() {
        for(int i = 0; i < 7; i++) {
            for(int j = 0; j < 7; j++) {
                grids[i][j] = new Grid(Tile_START_X + Tile_WIDTH * j, Tile_START_Y + Tile_WIDTH * (i+1));
            }
        }
    }

    List<TileImage> specialTiles1 = new ArrayList<>();
    List<TileImage> specialTiles2 = new ArrayList<>();

    class TileImage extends ImageView {
        double mouseX, mouseY;
        double homeX, homeY;
        String name;
        int orientation;
        int row, col;
        boolean isPlaced, dragable, isSpecial;

        TileImage(Image image, String name, double startX, double startY, int orientation) {
            super(image);
            this.row = -1;
            this.col = -1;
            this.isPlaced = false;
            this.dragable = true;
            this.name = name;
            this.orientation = orientation;
            this.homeX = startX;
            this.homeY = startY;
            if(name.charAt(0) == 'S') {
                this.isSpecial = true;
            } else {
                this.isSpecial = false;
            }

            setFitHeight(Tile_WIDTH);
            setFitWidth(Tile_WIDTH);
            setLayoutX(startX);
            setLayoutY(startY);
            setOrientation();

            setOnMousePressed(event -> {
                if(!this.isPlaced && !gameOver) {
                    mouseX = event.getSceneX();
                    mouseY = event.getSceneY();
                }
            });

            setOnMouseDragged(event -> {
                if(!this.isPlaced && !gameOver){
                    toFront();
                    double movementX = event.getSceneX() - mouseX;
                    double movementY = event.getSceneY() - mouseY;
                    setLayoutX(getLayoutX() + movementX);
                    setLayoutY(getLayoutY() + movementY);
                    mouseX = event.getSceneX();
                    mouseY = event.getSceneY();
                }
                event.consume();
            });

            setOnMouseReleased(event -> {
                if(!this.isPlaced && !gameOver) {
                    if(!snapToGrid())
                        snapToHome();
                }
            });

            setOnScroll(event -> {
                if (!this.isPlaced && !gameOver)
                {
                    //double deltaY = event.getDeltaY();
                  //  if (deltaY < 0)
                  //      this.orientation = (this.orientation - 1) % 8;
                  //  else
                        this.orientation = (this.orientation + 1) % 8;
                    setOrientation();
//                if(!this.isPlaced){
//                    this.orientation = (this.orientation + 1) % 8;
//                    setOrientation();
//                }
                    event.consume();
                }
            });

        }

        @Override
        public String toString() {
            return this.name + rowtoString(this.row) + this.col + this.orientation;
        }

        private void setOrientation() {
            if(this.orientation >= 4) {
                setScaleX(-1);
                setRotate(this.orientation * 90);
            } else {
                setScaleX(1);
                setRotate(this.orientation * 90);
            }

        }


        private void snapToHome() {
                setLayoutX(homeX);
                setLayoutY(homeY);
        }

        private String rowtoString(int row) {
            String value = "";
            switch (row) {
                case 0: value = "A"; break;
                case 1: value =  "B"; break;
                case 2: value =  "C"; break;
                case 3: value =  "D"; break;
                case 4: value =  "E"; break;
                case 5: value =  "F"; break;
                case 6: value =  "G"; break;
            }
            return value;
        }

        private boolean snapToGrid() {
            if(this.isSpecial) {
                if(currentPlayer == 1) {
                    if(specialCount1 >= 3 || roundSpecialUsed) {
                        return false;
                    }
                } else {
                    if(specialCount2 >= 3 || roundSpecialUsed) {
                        return false;
                    }
                }

            }
            for(int i = 0; i < 7; i++) {
                for(int j = 0; j < 7; j++) {
                    if((Math.abs(getLayoutX() - grids[i][j].getX()) < (Tile_WIDTH / 4)) && (Math.abs(getLayoutY() - grids[i][j].getY()) < (Tile_WIDTH / 4))) {
                        if(isValidPlacementSequence(CURRENT_PLACEMENT + this.name + rowtoString(i) + j + this.orientation)){
                            if(this.isSpecial) {
                                if(currentPlayer == 1) {
                                    specialCount1 += 1;
                                    specialTiles1.remove(this);
                                } else {
                                    specialCount2 += 1;
                                    specialTiles2.remove(this);
                                }

                                roundSpecialUsed = true;
                                specialLabel.setText("Special tiles used: " + (currentPlayer == 1 ? specialCount1 : specialCount2));
                            } else {
                                tilesLeft -= 1;
                            }
                            setLayoutX(grids[i][j].getX());
                            setLayoutY(grids[i][j].getY());
                            this.row = i;
                            this.col = j;
                            CURRENT_PLACEMENT += this.toString();
                            this.isPlaced = true;
                            tiles.getChildren().add(this);
                            if(this.isSpecial) {
                                specialTiles.getChildren().remove(this);
                                SPECIAL_MEMBERS = SPECIAL_MEMBERS.replaceFirst(this.name, "");
                            } else {
                                diceRolls.getChildren().remove(this);
                                ROLL_MEMBERS = ROLL_MEMBERS.replaceFirst(this.name, "");
                            }
//                            /*
//                            if(generateMove(CURRENT_PLACEMENT, ROLL_MEMBERS) == "") {
//                                gameOver();
//                            }
//                            */
                            if(tilesLeft == 0) {
                                if(round < 7) {
                                    rollDice();
                                } else {
                                    gameOver();
                                }
                            }
                            return true;
                        }

                    }
                }
            }
            return false;
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

                TileImage tileImage = new TileImage(image, img, Tile_START_X + Tile_WIDTH * col, Tile_START_Y + Tile_WIDTH * row, orientation);

                tiles.getChildren().add(tileImage);

            }
        }

    }


    void makeBoard() {
        initGrids();
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

    private void initSpecialTiles() {
        double x = Tile_START_X + Tile_WIDTH;
        for(int i = 0; i < 6; i++) {
            String img = "S" + i;
            Image image = new Image(Viewer.class.getResource(URI_BASE + img + ".png").toString());
            specialTiles1.add(new TileImage(image, img, x,Tile_START_Y / 3,0));
            specialTiles2.add(new TileImage(image, img, x,Tile_START_Y / 3,0));
            x += Tile_WIDTH * 1.5;
        }
    }

    private void makeSpecialTiles() {
        specialTiles.getChildren().clear();
        if(currentPlayer == 1) {
            for(int i = 0; i < specialTiles1.size(); i++) {
                specialTiles.getChildren().add(specialTiles1.get(i));
            }
        } else {
            for(int i = 0; i < specialTiles2.size(); i++) {
                specialTiles.getChildren().add(specialTiles2.get(i));
            }
        }



        /*
        double x = Tile_START_X + Tile_WIDTH;
        for(int i=0; i<6; i++) {
            String img = "S" + i;
            Image image = new Image(Viewer.class.getResource(URI_BASE + img + ".png").toString());
            TileImage tileImage = new TileImage(image, img, x, Tile_START_Y / 3, 0);
            x += Tile_WIDTH * 1.5;

            specialTiles.getChildren().add(tileImage);
        }*/
        specialLabel.setText("Special tiles used: " + (currentPlayer == 1 ? specialCount1 : specialCount2));
    }

    private void rollDice() {
        if(round < 7) {
            round += 1;
            roundLabel.setText("Round: " + round);
            diceRolls.getChildren().clear();
            String diceRoll = generateDiceRoll();
            ROLL_MEMBERS = "";
            double y = Tile_START_Y + Tile_WIDTH * 1.5;
            for(int i=0; i<8; i+=2) {
                String img = diceRoll.substring(i, i+2);
                ROLL_MEMBERS += img;
                Image image =new Image(Viewer.class.getResource(URI_BASE + img + ".png").toString());
                TileImage tileImage = new TileImage(image, img, Tile_START_X + Tile_WIDTH * 9, y, 0);
           /* ImageView imageview = new ImageView();
            imageview.setImage(image);
            imageview.setFitWidth(Tile_WIDTH);
            imageview.setFitHeight(Tile_WIDTH);
            imageview.setX(Tile_START_X + Tile_WIDTH * 9);
            imageview.setY(y);*/
                y += Tile_WIDTH * 1.5;
                diceRolls.getChildren().add(tileImage);
            }
            tilesLeft = 4;
            roundSpecialUsed = false;

            if(round > 1 && multiPlayer) {
                currentPlayer = (currentPlayer == 1 ? 2 : 1);
                makeSpecialTiles();
                playerLabel.setText("Player: " + currentPlayer);
            }

        }
    }

    Label roundLabel = new Label("Round: " + round);
    Label playerLabel = new Label("Player: " + currentPlayer);
    Label specialLabel = new Label("Special tiles used: " + (currentPlayer == 1 ? specialCount1 : specialCount2));

    private void gameOver() {

        gameOver = true;

        Image img = new Image(Viewer.class.getResource(URI_BASE+"WOW.png").toString());
        ImageView imageview = new ImageView(img);
        imageview.setX(250);
        imageview.setY(10);
        imageview.setFitWidth(120);
        imageview.setFitHeight(120);

        Label label1 = new Label("CONGRATULATIONS... YOU BEAT THE GAME!");
        label1.setFont(Font.font("family", FontWeight.BLACK.EXTRA_BOLD, FontPosture.REGULAR,20));
        label1.setLayoutX(120);
        label1.setLayoutY(130);

        Label label2 = new Label("Your Score: " + getBasicScore(CURRENT_PLACEMENT));
        label2.setFont(Font.font("family", FontWeight.BLACK.BOLD, FontPosture.ITALIC,20));
        label2.setLayoutX(250);
        label2.setLayoutY(170);

        Group root = new Group();
        root.getChildren().addAll(label1,label2,imageview);

       // StackPane layout = new StackPane();
        //layout.getChildren().addAll(label1,label2);

        Scene scene = new Scene(root, 640, 300);

        //New window
        Stage gameOverWindow = new Stage();
        gameOverWindow.setTitle("Game Over");
        gameOverWindow.setScene(scene);

        gameOverWindow.setX(200);
        gameOverWindow.setY(100);

        gameOverWindow.show();
    }

    private void restartGame() {
        controls.getChildren().clear();
        board.getChildren().clear();
        specialTiles.getChildren().clear();
        diceRolls.getChildren().clear();;
        tiles.getChildren().clear();
        CURRENT_PLACEMENT = "";
        specialCount1 = 0;
        specialCount2 = 0;
        round = 0;
        tilesLeft = 0;
        roundSpecialUsed = false;
        currentPlayer = 1;
        makeControls();
        gameOver = false;
    }

    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {

        Button nextRoundButton = new Button("Next Round");
        nextRoundButton.setOnAction(e -> {
            rollDice();
        });
        nextRoundButton.setLayoutX(Tile_START_X + Tile_WIDTH * 9);
        nextRoundButton.setLayoutY(Tile_START_X + Tile_WIDTH * 6);

        Label label = new Label("Special Tiles:");
        label.setFont(Font.font("Cambria", 24));
        label.setLayoutX(Tile_START_X / 5);
        label.setLayoutY(Tile_START_Y / 2);

        specialLabel.setFont(Font.font("Cambria", 15));
        specialLabel.setLayoutX(Tile_START_X / 5);
        specialLabel.setLayoutY(Tile_START_Y * 1.2);

        roundLabel.setFont(Font.font("Cambria", 20));
        roundLabel.setLayoutX(Tile_START_X / 5);
        roundLabel.setLayoutY(Tile_START_Y * 2);

        playerLabel.setFont(Font.font("Cambria", 20));
        playerLabel.setLayoutX(Tile_START_X / 5);
        playerLabel.setLayoutY(Tile_START_Y * 2.5);

        Button newGameButton = new Button("Restart Game");
        newGameButton.setOnAction(e -> {
            restartGame();
        });
        newGameButton.setLayoutX(Tile_START_X / 5);
        newGameButton.setLayoutY(Tile_START_Y * 3.5);


        controls.getChildren().addAll(label, roundLabel, specialLabel, playerLabel, nextRoundButton, newGameButton);

        initSpecialTiles();
        makeSpecialTiles();

        makeBoard();


        /*Label label1 = new Label("Placement:");
        textField = new TextField();
        textField.setPrefWidth(300);
        Button button = new Button("Refresh");
        button.setOnAction(e -> {
           makePlacement(textField.getText());
            textField.clear();
           //textField.setText(CURRENT_PLACEMENT);
        });

        Button button1 = new Button("gameover");
        button1.setLayoutX(0);
        button1.setLayoutY(0);
        button1.setOnAction(e -> {
           gameOver();
        });*/

        /*HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button);
        hb.setSpacing(10);
        hb.setLayoutX(130);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);
        controls.getChildren().add(button1);*/
        makeBoard();
        rollDice();

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
        primaryStage.setResizable(false);


        //choose mode
        Group modeChoosingGroup = new Group();
        Scene modeChoosingScene = new Scene(modeChoosingGroup, 400, 150);

        Label label1 = new Label("Please choose game mode: ");
        label1.setFont(Font.font("family", FontWeight.BLACK.EXTRA_BOLD, FontPosture.REGULAR,20));
        label1.setLayoutX(50);
        label1.setLayoutY(30);

        Stage modeChoosingWindow = new Stage();
        Button singleButton = new Button("Single player");
        singleButton.setOnAction(e -> {
            multiPlayer = false;
            primaryStage.show();
            modeChoosingWindow.close();
        });
        singleButton.setLayoutX(80);
        singleButton.setLayoutY(80);

        Button multiButton = new Button("Multi player");
        multiButton.setOnAction(e -> {
            multiPlayer = true;
            primaryStage.show();
            modeChoosingWindow.close();
        });
        multiButton.setLayoutX(200);
        multiButton.setLayoutY(80);

        modeChoosingGroup.getChildren().addAll(label1, singleButton, multiButton);
        modeChoosingWindow.setTitle("Mode Choosing");
        modeChoosingWindow.setScene(modeChoosingScene);
        modeChoosingWindow.setAlwaysOnTop(true);
        modeChoosingWindow.setResizable(false);

        modeChoosingWindow.show();

    }
}
