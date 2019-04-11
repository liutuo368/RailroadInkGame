package comp1110.ass2.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.concurrent.TimeUnit;

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
    private static final double Tile_START_X = VIEWER_WIDTH / 10 * 3;
    private static final double Tile_START_Y = 60;

    private static final String URI_BASE = "assets/";

    private final Group root = new Group();
    private final Group tiles = new Group();
    private final Group lines = new Group();
    private final Group controls = new Group();
    TextField textField;

    public static void main(String[] args) {
        launch(args);
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
                ImageView imageview = new ImageView();
                imageview.setImage(image);
                imageview.setFitWidth(Tile_WIDTH);
                imageview.setFitHeight(Tile_WIDTH);
                imageview.setX(Tile_START_X + Tile_WIDTH * col);
                imageview.setY(Tile_START_Y + Tile_WIDTH * row);

                if(orientation >=4){
                    imageview.setScaleX(-1);
                }
                imageview.setRotate(orientation * 90);

                tiles.getChildren().add(imageview);

            }
        }

    }

    void makeLines() {
        for(int i = 0; i <= 7; i++) {
            Line l_vertical = new Line();
            l_vertical.setStartX(Tile_START_X + Tile_WIDTH * i);
            l_vertical.setStartY(Tile_START_Y + Tile_WIDTH);
            l_vertical.setEndX(Tile_START_X + Tile_WIDTH * i);
            l_vertical.setEndY(Tile_START_Y + Tile_WIDTH * 8);
            lines.getChildren().add(l_vertical);

            Line l_horizontal = new Line();
            l_horizontal.setStartX(Tile_START_X);
            l_horizontal.setStartY(Tile_START_Y + Tile_WIDTH * (i + 1));
            l_horizontal.setEndX(Tile_START_X + Tile_WIDTH * 7);
            l_horizontal.setEndY(Tile_START_Y + Tile_WIDTH * (i + 1));
            lines.getChildren().add(l_horizontal);
        }
    }

    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
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
        makeLines();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("StepsGame Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        root.getChildren().add(controls);
        root.getChildren().add(tiles);
        root.getChildren().add(lines);

        makeControls();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
