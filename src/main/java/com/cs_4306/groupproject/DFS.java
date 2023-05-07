package com.cs_4306.groupproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Stack;

public class DFS extends Application {

    private static final int[][] edge = {
            {0, 1}, {0, 3}, {0, 5},
            {1, 0}, {1, 2}, {1, 3},
            {2, 1}, {2, 3}, {2, 4}, {2, 10},
            {3, 0}, {3, 1}, {3, 2}, {3, 4}, {3, 5},
            {4, 2}, {4, 3}, {4, 5}, {4, 7}, {4, 8}, {4, 10},
            {5, 0}, {5, 3}, {5, 4}, {5, 6}, {5, 7},
            {6, 5}, {6, 7},
            {7, 4}, {7, 5}, {7, 6}, {7, 8},
            {8, 4}, {8, 7}, {8, 9}, {8, 10}, {8, 11},
            {9, 8}, {9, 11},
            {10, 2}, {10, 4}, {10, 8}, {10, 11},
            {11, 8}, {11, 9}, {11, 10}
    };

    private static final int NUM_NODES = 12;
    private static final int NODE_RADIUS = 30;
    private static final int GUI_WIDTH = 800;
    private static final int GUI_HEIGHT = 600;

    private int position = 0;

    @Override
    public void start(Stage primaryStage) {

        int[][] adjacencyMatrix = {
                {0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0},
                {1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 0},
                {1, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 1},
                {0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0}
        };

        String adjList = dfsTraversal(adjacencyMatrix, 0);
        String[] master = parseStringToArray(adjList);

        // create a pane to hold the circles and labels
        Pane root = new Pane();

        // create circles for each node and add to pane
        for (int i = 0; i < NUM_NODES; i++) {
            Circle circle = new Circle();
            circle.setCenterX((i % 4 + 1) * (GUI_WIDTH / 5));
            circle.setCenterY((i / 4 + 1) * (GUI_HEIGHT / 4));
            circle.setRadius(NODE_RADIUS);
            circle.setFill(javafx.scene.paint.Color.WHITE); // set fill color to white
            root.getChildren().add(circle);
        }

        // create circles for each node and add to pane
        for (int i = 0; i < NUM_NODES; i++) {
            Circle circle = new Circle();
            circle.setCenterX((i % 4 + 1) * (GUI_WIDTH / 5));
            circle.setCenterY((i / 4 + 1) * (GUI_HEIGHT / 4));
            circle.setRadius(NODE_RADIUS);
            circle.setFill(javafx.scene.paint.Color.WHITE); // set fill color to white
            circle.setStroke(javafx.scene.paint.Color.BLACK); // set stroke color to black
            circle.setStrokeWidth(2); // set stroke width to 2 pixels
            root.getChildren().add(circle);
        }

        // create labels for each node and add to pane
        for (int i = 0; i < NUM_NODES; i++) {
            Text label = new Text(Character.toString((char) ('A' + i)));
            label.setX((i % 4 + 1) * (GUI_WIDTH / 5) - NODE_RADIUS / 2);
            label.setY((i / 4 + 1) * (GUI_HEIGHT / 4) + NODE_RADIUS / 2);
            root.getChildren().add(label);
        }

        // draw edges between adjacent nodes
        for (int i = 0; i < edge.length; i++) {
            int node1 = edge[i][0];
            int node2 = edge[i][1];
            Circle circle1 = (Circle) root.getChildren().get(node1);
            Circle circle2 = (Circle) root.getChildren().get(node2);

            javafx.scene.shape.Line line = new javafx.scene.shape.Line();
            line.setStartX(circle1.getCenterX());
            line.setStartY(circle1.getCenterY());
            line.setEndX(circle2.getCenterX());
            line.setEndY(circle2.getCenterY());
            root.getChildren().add(line);
        }

        // create a label for the button and add it to the pane
        Text buttonLabel = new Text(""); // Button
        buttonLabel.setX(10);
        buttonLabel.setY(GUI_HEIGHT - 50);
        root.getChildren().add(buttonLabel);

        // create a button and add it to the pane
        javafx.scene.control.Button button = new javafx.scene.control.Button("Traverse");
        button.setLayoutX(GUI_WIDTH / 2 - NODE_RADIUS / 2);
        button.setLayoutY(GUI_HEIGHT - 30);
        root.getChildren().add(button);

        // create a new scene with the pane as the root node
        Scene scene = new Scene(root, GUI_WIDTH, GUI_HEIGHT);

        // set the button action to perform a BFS traversal of the graph and color the nodes
        button.setOnAction(event -> {

            buttonLabel.setText(formatTraversal(master, position));
            position = position + 1;
        });

// set the stage title and scene, and show the stage
        primaryStage.setTitle("Depth First Search");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static String dfsTraversal(int[][] adjacencyMatrix, int startNode) {
        Stack<Integer> stack = new Stack<Integer>();
        boolean[] visited = new boolean[adjacencyMatrix.length];
        stack.push(startNode);
        visited[startNode] = true;
        StringBuilder result = new StringBuilder();
        while (!stack.empty()) {
            int currentNode = stack.pop();
            result.append((char)('A' + currentNode)).append(" ");
            for (int i = 0; i < adjacencyMatrix[currentNode].length; i++) {
                if (adjacencyMatrix[currentNode][i] == 1 && !visited[i]) {
                    stack.push(i);
                    visited[i] = true;
                }
            }
        }
        return result.toString();
    }

    public static String[] parseStringToArray(String str) {
        // Split the string on spaces and store the resulting substrings in an array
        String[] arr = str.split(" ");
        return arr;
    }

    public static String formatTraversal(String[] traversal, int position) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < position; i++) {
            sb.append(traversal[i]);
            if (i < position - 1) {
                sb.append(" -> ");
            }
        }
        if (position == traversal.length) {
            sb.append(" : Traversal Complete");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        launch(args);
    }
}