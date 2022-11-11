import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

import static java.lang.Math.sqrt;
import static java.lang.System.exit;

/*
 *  Program to simulate segregation.
 *  See : http://nifty.stanford.edu/2014/mccown-schelling-model-segregation/
 *
 * NOTE:
 * - JavaFX first calls method init() and then method start() far below.
 * - To test methods uncomment call to test() first in init() method!
 *
 */
// Extends Application because of JavaFX (just accept for now)
public class Neighbours extends Application {

    class Actor {
        final Color color;        // Color an existing JavaFX class
        boolean isSatisfied;      // false by default

        Actor(Color color) {      // Constructor to initialize
            this.color = color;
        }  // Constructor, used to initialize
    }

    // Below is the *only* accepted instance variable (i.e. variables outside any method)
    // This variable may *only* be used directly in methods init() and updateWorld()
    Actor[][] world;              // The world is a square matrix of Actors

    // This is the method called by the timer to update the world
    // (i.e move unsatisfied) approx each 1/60 sec.
    void updateWorld() {
        // % of surrounding neighbours that are like me
        double threshold = 0.7;
        world = swapUnsatisfied(world, threshold);
        // TODO

    }

    // This method initializes the world variable with a random distribution of Actors
    // Method automatically called by JavaFX runtime
    // That's why we must have "@Override" and "public" (just accept for now)
    @Override
    public void init() {
        //test();    // <---------------- Uncomment to TEST, see below!
        // %-distribution of RED, BLUE and NONE
        double[] dist = {0.25, 0.25, 0.50};
        // Number of locations (places) in world (must be a square)
        int nLocations = 90000;   // Should also try 90 000
        world = new Actor[(int) sqrt(nLocations)][(int) sqrt(nLocations)];
        // TODO
        int numOfRed = (int) (dist[0] * nLocations);
        int numOfBlue = numOfRed + (int) (dist[1] * nLocations);
        int counter = 0;
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[i].length; j++) {
                if (counter < numOfRed) {
                    world[i][j] = new Actor(Color.RED);
                } else if (counter < numOfBlue) {
                    world[i][j] = new Actor(Color.BLUE);
                } else {
                    world[i][j] = null;
                }
                counter++;
            }
        }
        world = shuffleMatrix(world);
        // Should be last
        fixScreenSize(nLocations);
    }

    // TODO Many methods here, break down of init() and updateWorld()
    boolean isHappy(int x, int y, double threshold, Actor[][] matrix) {
        Actor cell = matrix[x][y];
        if (cell == null) {
            return true;
        }
        Actor[] neighbours = getNeighbours(x, y, matrix);
        int amountOfBlue = 0;
        int amountOfRed = 0;
        for (int i = 0; i < neighbours.length; i++) {
            if (neighbours[i] != null && neighbours[i].color == Color.RED) {
                amountOfRed++;
            } else if (neighbours[i] != null && neighbours[i].color == Color.BLUE) {
                amountOfBlue++;
            }
        }
        if (amountOfBlue == 0 && amountOfRed == 0) {
            return true;
        } else if (cell.color == Color.RED) {
            if ((double) amountOfRed / (amountOfBlue + amountOfRed) >= threshold) {
                return true;
            } else {
                return false;
            }
        } else {
            if ((double) amountOfBlue / (amountOfBlue + amountOfRed) >= threshold) {
                return true;
            } else {
                return false;
            }
        }
    }

    Actor[] getNeighbours(int x, int y, Actor[][] matrix) {
        Actor[] neighbours = new Actor[8];
        int counter = 0;
        for (int a = x - 1; a <= x + 1; a++) {
            for (int b = y - 1; b <= y + 1; b++) {
                if (a != x || b != y) {
                    if (a >= 0 && a < matrix.length && b >= 0 && b < matrix[0].length) {
                        neighbours[counter] = matrix[a][b];
                        counter++;
                    }
                }
            }
        }
        return neighbours;
    }

    Actor[] getUnsatActors(Actor[][] matrix) {
        Actor[] partialActors = new Actor[matrix.length * matrix.length];
        int k = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != null && !matrix[i][j].isSatisfied) {
                    partialActors[k] = matrix[i][j];
                    k++;
                }
            }
        }
        Actor[] actors = new Actor[k];
        for (int i = 0; i < k; i++) {
            actors[i] = partialActors[i];
        }
        return actors;
    }

    int[] getNullIndexes(Actor[][] matrix) {
        int[] partialNulls = new int[(int) (matrix.length * matrix.length)];
        int c = 0;
        Actor[] matrixArray = fromMatrix(matrix);
        for (int i = 0; i < matrixArray.length; i++) {
            if (matrixArray[i] == null) {
                partialNulls[c] = i;
                c++;
            }
        }
        int[] nulls = new int[c];
        for (int i = 0; i < c; i++) {
            nulls[i] = partialNulls[i];
        }
        return nulls;
    }

    Actor[][] assignSafisfaction(Actor[][] matrix, double threshold) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != null && isHappy(i, j, threshold, matrix)) {
                    matrix[i][j].isSatisfied = true;
                }
            }
        }
        return matrix;
    }

    Actor[][] swapUnsatisfied(Actor[][] matrix, double threshold) {
        matrix = assignSafisfaction(matrix, threshold);
        Actor[] matrixAsArray = fromMatrix(matrix);
        Actor[] unsatActors = getUnsatActors(matrix);
        int[] nullIndexes = getNullIndexes(matrix);
        nullIndexes = shuffleIntArr(nullIndexes);
        for (int i = 0; i < matrixAsArray.length; i++) {
            if (matrixAsArray[i] != null && !matrixAsArray[i].isSatisfied) {
                matrixAsArray[i] = null;
            }
        }
        for (int i = 0; i < unsatActors.length; i++) {
            matrixAsArray[nullIndexes[i]] = unsatActors[i];
        }
        matrix = toMatrix(matrixAsArray);
        return matrix;
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Check if inside world
    boolean isValidLocation(int size, int row, int col) {
        return 0 <= row && row < size && 0 <= col && col < size;
    }

    Actor doIt(Actor[][] world) {
        return world[0][0];
    }

    // ----------- Utility methods -----------------

    // TODO Method to change format of data, generate random etc.

    Actor[] fromMatrix(Actor[][] matrix) {
        Actor[] arr = new Actor[matrix.length * matrix[0].length];
        int counter = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                arr[counter] = matrix[i][j];
                counter++;
            }
        }
        return arr;
    }

    Actor[][] toMatrix(Actor[] arr) {
        Actor[][] matrix = new Actor[(int) sqrt(arr.length)][(int) sqrt(arr.length)];
        int counter = 0;
        for (int i = 0; i < sqrt(arr.length); i++) {
            for (int j = 0; j < sqrt(arr.length); j++) {
                matrix[i][j] = arr[counter];
                counter++;
            }
        }
        return matrix;
    }

    Actor[][] shuffleMatrix(Actor[][] matrix) {
        Actor[] matrixAsArray = fromMatrix(matrix);
        matrixAsArray = shuffleActorArr(matrixAsArray);
        Actor[][] matrixShuffled = toMatrix(matrixAsArray);
        return matrixShuffled;
    }

    Actor[] shuffleActorArr(Actor[] arr) {
        Random rand = new Random();
        for (int i = 0; i < arr.length; i++) {
            int randNum = rand.nextInt(arr.length);
            Actor temp = arr[i];
            arr[i] = arr[randNum];
            arr[randNum] = temp;
        }
        return arr;
    }

    int[] shuffleIntArr(int[] arr) {
        Random rand = new Random();
        for (int i = 0; i < arr.length; i++) {
            int randNum = rand.nextInt(arr.length);
            int temp = arr[i];
            arr[i] = arr[randNum];
            arr[randNum] = temp;
        }
        return arr;
    }

    // ------- Testing -------------------------------------

    // Here you run your tests i.e. call your logic methods
    // to see that they really work. Important!!!!
    void test() {
        // A small hard coded world for testing
        Actor[][] testWorld = new Actor[][]{
                {new Actor(Color.RED), new Actor(Color.RED), null},
                {null, new Actor(Color.BLUE), null},
                {new Actor(Color.RED), null, new Actor(Color.BLUE)}
        };
        double th = 0.5;   // Simple threshold used for testing

        int size = testWorld.length;

        // TODO  More tests here. Implement and test one method at the time
        // TODO Always keep all tests! Easy to rerun if something happens


        exit(0);
    }

    // ******************** NOTHING to do below this row, it's JavaFX stuff  **************

    double width = 500;   // Size for window
    double height = 500;
    final double margin = 50;
    double dotSize;

    void fixScreenSize(int nLocations) {
        // Adjust screen window
        dotSize = (double) 9000 / nLocations;
        if (dotSize < 1) {
            dotSize = 2;
        }
        width = sqrt(nLocations) * dotSize + 2 * margin;
        height = width;
    }

    long lastUpdateTime;
    final long INTERVAL = 450_000_000;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Build a scene graph
        Group root = new Group();
        Canvas canvas = new Canvas(width, height);
        root.getChildren().addAll(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Create a timer
        AnimationTimer timer = new AnimationTimer() {
            // This method called by FX, parameter is the current time
            public void handle(long now) {
                long elapsedNanos = now - lastUpdateTime;
                if (elapsedNanos > INTERVAL) {
                    updateWorld();
                    renderWorld(gc);
                    lastUpdateTime = now;
                }
            }
        };

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation");
        primaryStage.show();

        timer.start();  // Start simulation
    }

    // Render the state of the world to the screen
    public void renderWorld(GraphicsContext g) {
        g.clearRect(0, 0, width, height);
        int size = world.length;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int x = (int) (dotSize * col + margin);
                int y = (int) (dotSize * row + margin);
                if (world[row][col] != null) {
                    g.setFill(world[row][col].color);
                    g.fillOval(x, y, dotSize, dotSize);
                }
            }
        }
    }
}
