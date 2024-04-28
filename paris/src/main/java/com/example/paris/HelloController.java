package com.example.paris;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

import static com.example.paris.Graph.addEdge;

public class HelloController implements Initializable {
    public static HelloController maincon;

    private  Graph graph = new Graph(); //Will be used to store the graph

    private Map<String, InterestPoints> points = new HashMap<>(); //Will be used to store all points

    @FXML
    public Button clearMap;

    @FXML
    public MenuButton waypointPoint;

    @FXML
    public AnchorPane mapPane;

    @FXML
    public Button bfsSearchButton;

    @FXML
    public ImageView mapImageView;

    @FXML
    public ListView routeOutput;

    @FXML
    public Button initialiseMapButton;

    @FXML
    public Button addAvoidPointButton;

    @FXML
    public MenuButton startPoint;

    @FXML
    public MenuButton destinationPoint;

    @FXML
    public MenuButton avoidPoint;


    private InterestPoints selectedWaypointPoint;
    private  Map<String, InterestLines> lines;

    private Circle firstPointRing;

    private Circle destinationPointCircle;

    private Circle firstPointCircle;

    private InterestPoints selectedDestinationPoint;

    private InterestPoints firstSelectedPoint;

    private Circle destinationPointOuterRing;

    private boolean isMapPopulated = false;

    private Map<String, GraphNode<String>> graphNodes;
    private AdjacencyMatrix am;

    public void initialiseMap(ActionEvent actionEvent) {
        // Check if the map is populated
        if (isMapPopulated) {
            System.out.println("Map already populated");
            return;
        }
        String csvData = "Eiffel Tower,Le Marais,#4287f5,73,83\n" +
                "Musee d'Orsay,Montmartre,#4287f5,295,36\n" +
                "Le Marais,Saint-Germain-des-Prés,#4287f5,601,291\n" +
                "Arc de Triomphe,Latin Quarter (Quartier Latin),#4287f5,413,411\n" +
                "Les Catacombes,Bastille,#4287f5,372,260\n" +
                "Canal Saint-Martin, Champs-Élysées,#4287f5,206,191\n" +
                "Galeries Lafayette,Le Quartier Latin,#4287f5,16,354\n" +
                "Jardin des Tuileries,Opéra,#4287f5,27,59\n" +
                "Sacre-Coeur Basilica,Place de la Concorde,#4287f5,284,300\n" +
                "The view from the Montparnasse,Le Trocadéro,#4287f5,474,260\n" +
                "Marche des Enfants Rouges,Place de la République,#4287f5,450,145\n" +
                "Cathedrale Notre-Dame de Paris,Place de la Bastille,#4287f5,595,5\n" +
                "Musee National Rodin,La Défense,#4287f5,258,162\n" +
                "The Louvre,Île de la Cité,#4287f5,241,371\n" +
                "La Coulee Verte,Île Saint-Louis,#4287f5,402,47\n" +
                "Sainte-Chapelle,Quartier des Invalides,#4287f5,225,252\n" +
                "Fondation Louis Vuitton,Quartier du Palais-Royal,#4287f5,649,90\n" +
                "Les Passages Couverts,Quartier de Bercy,#4287f5,566,428\n" +
                "The Centre Pompidou,Quartier Saint-Louis Versailles,#4287f5,248,455\n";
        createAndConnectGraphNodes(csvData);
        Map<String, InterestLines> lines = parseCSVData(csvData);
        // Iterate over the lines and print their points
        for (Map.Entry<String, InterestLines> entry : lines.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue().getLineColor());
            for (InterestPoints point : entry.getValue().getPoints()) {
                System.out.println("\t" + point.getPointName() + " (" + point.getXcoord() + "," + point.getYcoord() + ")");
            }
            System.out.println("\n");
        }

        populateMenuButtons(lines);


        //After filling the map, set the boolean to true.
        isMapPopulated = true;
    }

    private Map<String, InterestLines> parseCSVData(String csvData) {
        lines = new HashMap<>();
        InterestPoints lastPoint = null;  // This will hold the last point in each loop for connecting with the current point

        // Split the csvData to get each line
        String[] csvLines = csvData.split("\n");

        for (String line : csvLines) {
            String[] values = line.split(",");

            if (values.length != 5) {
                System.err.println("Invalid line in CSV: " + line);
                continue;
            }

            String lineName = values[0].trim();
            String pointName = values[1].trim();
            double x = Double.parseDouble(values[3].trim());
            double y = Double.parseDouble(values[4].trim());

            // Create or retrieve the Line object
            InterestLines lineObj = lines.get(lineName);
            if (lineObj == null) {
                lineObj = new InterestLines(lineName);
                lines.put(lineName, lineObj);
            }

            // Create or retrieve the Point object
            InterestPoints PointObj = points.get(pointName);
            if (PointObj == null) {
                PointObj = new InterestPoints(pointName, x, y);
                points.put(pointName, PointObj);
            }

            // Add the point to the Line object's point list
            lineObj.addPoint(PointObj);

            // Connect this point to the last point if it exists
            if (lastPoint != null) {
                addEdge(lastPoint, PointObj);  // Here is where you add edges

                // Calculate the distance between the current and previous points
                double distance = Math.sqrt(Math.pow(PointObj.getXcoord() - lastPoint.getXcoord(), 2)
                        + Math.pow(PointObj.getYcoord() - lastPoint.getYcoord(), 2));
                // Print the distances
                System.out.println("Calculating distance between " + lastPoint.getPointName() + " and " + PointObj.getPointName());
                System.out.println("Previous point coordinates: " + lastPoint.getXcoord() + ", " + lastPoint.getYcoord());
                System.out.println("Current point coordinates: " + PointObj.getXcoord() + ", " + PointObj.getYcoord());
                System.out.println("Calculated distance: " + distance);

                // Add the current point as a neighbor to the previous point, if they are not already neighbors
                if (!lastPoint.getNeighborPoints().containsKey(PointObj)) {
                    lastPoint.addNeighbor(PointObj);
                    System.out.println("Added " + PointObj.getPointName() + " as a neighbor to " + lastPoint.getPointName());
                }

                // Add the previous point as a neighbor to the current point, if they are not already neighbors
                if (!PointObj.getNeighborPoints().containsKey(lastPoint)) {
                    PointObj.addNeighbor(lastPoint);
                    System.out.println("Added " + lastPoint.getPointName() + " as a neighbor to " + PointObj.getPointName());
                }
            }

            lastPoint = PointObj;  // Update lastPoint to the current one for the next iteration
        }
        return lines;
    }

    private void handleMenuClick(ActionEvent event, InterestPoints point) {
        MenuItem clickedMenuItem = (MenuItem) event.getSource();
        MenuButton parentMenuButton = (MenuButton) clickedMenuItem.getParentPopup().getOwnerNode();

        // Set the selected point as the text of the parent MenuButton
        parentMenuButton.setText(point.getPointName());

        if (parentMenuButton == startPoint) {
            firstSelectedPoint = point;
            drawFirstCircle(point);
        } else if (parentMenuButton == destinationPoint) {
            selectedDestinationPoint = point;
            drawDestinationCircle(point);
        }
    }


    public void createAndConnectGraphNodes(String csvData) {
        graphNodes = new HashMap<>();
        am = new AdjacencyMatrix(100); // Adjust the size as needed

        String[] csvLines = csvData.split("\n");
        for (String line : csvLines) {
            String[] parts = line.split(",");
            if (parts.length != 7) {
                System.out.println("Skipping malformed line: " + line);
                continue;
            }

            String startLocation = parts[0].trim();
            String endLocation = parts[1].trim();
            int xStart = Integer.parseInt(parts[3].trim());
            int yStart = Integer.parseInt(parts[4].trim());
            int xEnd = Integer.parseInt(parts[5].trim());
            int yEnd = Integer.parseInt(parts[6].trim());

            // Create or get graph nodes with coordinates and connect them
            GraphNode<String> startNode = graphNodes.computeIfAbsent(startLocation,
                    name -> new GraphNode<>(name, xStart, yStart, am));
            GraphNode<String> endNode = graphNodes.computeIfAbsent(endLocation,
                    name -> new GraphNode<>(name, xEnd, yEnd, am));

            startNode.connectToNodeUndirected(endNode);
        }
    }








    // This method draws a line between each point on the path
    private void drawShortestPath(Path shortestRoute) {
        // Remove any existing lines from the mapPane
        mapPane.getChildren().removeIf(node -> node instanceof javafx.scene.shape.Line);
        // Get the list of points in the shortest path
        List<InterestPoints> shortestPath = shortestRoute.getPath();
        // Draw a line between each pair of adjacent points in the shortest path
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            InterestPoints start = shortestPath.get(i);
            InterestPoints end = shortestPath.get(i + 1);
            drawLineBetweenPoints(start, end, Color.BLACK);
        }
    }


    //Based on the scale of the map image and the relative coordinates, this method computes the precise coordinates of a point on the mapPane.
    private Point2D calculateActualCoordinates(InterestPoints point) {
        double scaleX = mapImageView.getBoundsInLocal().getWidth() / mapImageView.getImage().getWidth();
        double scaleY = mapImageView.getBoundsInLocal().getHeight() / mapImageView.getImage().getHeight();

        double actualX = point.getXcoord() * scaleX;
        double actualY = point.getYcoord() * scaleY;

        return new Point2D(actualX, actualY);
    }

    //By utilizing the point's precise coordinates, this method generates a colored circle to represent the point on the map.
    private void createPointCircle(Circle innerCircle, Circle outerRing, InterestPoints point, Color color) {
        // Calculate the coordinates of the point on the mapPane
        Point2D actualCoordinates = calculateActualCoordinates(point);

        // Set the radius and centre of the circle
        innerCircle.setCenterX(actualCoordinates.getX());
        innerCircle.setCenterY(actualCoordinates.getY());
        innerCircle.setRadius(5);
        innerCircle.setFill(color);

        outerRing.setCenterX(actualCoordinates.getX());
        outerRing.setCenterY(actualCoordinates.getY());
        outerRing.setRadius(5); // Slightly larger radius for the outer ring
        outerRing.setFill(Color.TRANSPARENT);
        outerRing.setStroke(color);
        outerRing.setStrokeWidth(2);
    }


    // this method draws the circle we have already on a starting point blue
    private void drawFirstCircle(InterestPoints point) {
        // Remove any existing circle
        if (firstPointCircle != null) {
            mapPane.getChildren().removeAll(firstPointCircle, firstPointRing);
        }
        // Makes a new circle on the point picked
        firstPointCircle = new Circle();
        firstPointRing = new Circle();
        createPointCircle(firstPointCircle, firstPointRing, point, Color.BLUE);
        // puts new circle on the map pane
        mapPane.getChildren().addAll(firstPointCircle, firstPointRing);
    }

    // This method draws an aqua circle on the selected destination
    private void drawDestinationCircle(InterestPoints point) {
        // Remove any existing end point circle and outer ring from the mapPane
        if (destinationPointCircle != null) {
            mapPane.getChildren().removeAll(destinationPointCircle, destinationPointOuterRing);
        }
        // Create a new circle and outer ring with the specified point and color
        destinationPointCircle = new Circle();
        destinationPointOuterRing = new Circle();
        createPointCircle(destinationPointCircle, destinationPointOuterRing, point, Color.RED);
        // Add the new circle and outer ring to the mapPane
        mapPane.getChildren().addAll(destinationPointCircle, destinationPointOuterRing);
    }

    private void populateMenuButtons(Map<String, InterestLines> lines) {
        Set<InterestPoints> uniquePointsSet = new HashSet<>();
        // Loop through each InterestPointsline object in the lines Map
        for (InterestLines line : lines.values()) {
            // Loop through each Point object in the Interestline's points
            // Add the point to the uniquePoints set
            uniquePointsSet.addAll(line.getPoints());
        }
        // Convert the Set to a List
        List<InterestPoints> uniquePointsList = new ArrayList<>(uniquePointsSet);
        // Sort list of points alphabetically
        uniquePointsList.sort(Comparator.comparing(InterestPoints::getPointName));
        // Add the points to the menu button
        avoidPoint.getItems().addAll(createPointMenuItems(uniquePointsList));
        waypointPoint.getItems().addAll(createPointMenuItems(uniquePointsList));
        startPoint.getItems().addAll(createPointMenuItems(uniquePointsList));
        destinationPoint.getItems().addAll(createPointMenuItems(uniquePointsList));
    }

    private List<MenuItem> createPointMenuItems(List<InterestPoints> points) {
        List<MenuItem> pointMenuItems = new ArrayList<>();
        for (InterestPoints point : points) {
            MenuItem menuItem = new MenuItem(point.getPointName());
            menuItem.setOnAction(e -> handleMenuClick(e, point));
            pointMenuItems.add(menuItem);
        }
        return pointMenuItems;
    }


    // This method draws a line between two points with a given color
    private void drawLineBetweenPoints(InterestPoints start, InterestPoints end, Color color) {
        // Calculate the actual coordinates of the start and end points on the mapPane
        Point2D startPoint = calculateActualCoordinates(start);
        Point2D endPoint = calculateActualCoordinates(end);
        // Create a new line with the calculated start and end points and set its color and width
        javafx.scene.shape.Line line = new javafx.scene.shape.Line(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
        line.setStroke(color);
        line.setStrokeWidth(6);
        // Add the line to the mapPane
        mapPane.getChildren().add(line);
    }


    public void bfsSearch(ActionEvent actionEvent) {
        if (firstSelectedPoint == null || selectedDestinationPoint == null) {
            System.out.println("Please select both start and end points.");
            return;
        }

        Path shortestRoute = graph.bfsAlgorithm(firstSelectedPoint, selectedDestinationPoint);
        if (shortestRoute == null || shortestRoute.getPath().isEmpty()) {
            System.out.println("No path found between the selected points");
            return;
        }

        List<InterestPoints> path = shortestRoute.getPath();
        System.out.println("Debug: Path from " + path.get(0).getPointName() + " to " + path.get(path.size()-1).getPointName() + " with " + shortestRoute.getStops() + " stops.");
        path.forEach(point -> System.out.println("Visited: " + point.getPointName()));  // Debug each point visited

        drawShortestPath(shortestRoute);

        ObservableList<String> items = FXCollections.observableArrayList();
        items.add("BFS Path: " + path.get(0).getPointName() + " to " + path.get(path.size()-1).getPointName());
        items.add("Edges (stops): " + (path.size() - 1));
        path.forEach(point -> items.add(point.getPointName()));
        routeOutput.setItems(items);
    }






    // Method to perform Dijkstra's search algorithm for finding the shortest path between two points.
// It also updates the GUI to display the results.
    public void dijkstraSearch(ActionEvent actionEvent) {
        // Check if both start and end points have been selected.
        if (firstSelectedPoint == null || selectedDestinationPoint == null) {
            System.out.println("Please select both start and end points.");
            return; // Exit the method if either point is not selected
        }

        // Retrieve all points from a collection holding the available points.
        Set<InterestPoints> allPoints = new HashSet<>(points.values());
        // Call the Dijkstra algorithm with all points, starting and destination points.
        Path shortestRoute = graph.dijkstraAlgorithm(allPoints, firstSelectedPoint, selectedDestinationPoint);

        // Check if a path was not found or is empty.
        if (shortestRoute == null || shortestRoute.getPath().isEmpty()) {
            System.out.println("No path found between the selected points.");
            return; // Exit the method if no path exists
        }

        // Retrieve the path and create a formatter for displaying the distance.
        List<InterestPoints> path = shortestRoute.getPath();
        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        // Print the path and the total distance in the console.
        System.out.println("Path from " + path.get(0).getPointName() + " to " + path.get(path.size() - 1).getPointName() +
                " with total distance: " + decimalFormat.format(shortestRoute.getDistance()) + " units.");
        // Print each point's name in the path.
        path.forEach(point -> System.out.print(point.getPointName() + " -> "));
        System.out.println();

        // Update the map visualization with the shortest path.
        drawShortestPath(shortestRoute);

        // Prepare the list for GUI display and add summary information.
        ObservableList<String> items = FXCollections.observableArrayList();
        items.add("Dijkstra Path: " + path.get(0).getPointName() + " to " + path.get(path.size()-1).getPointName());
        items.add("Total Distance: " + decimalFormat.format(shortestRoute.getDistance()) + " units");

        // Add each point in the path to the GUI list.
        path.forEach(point -> items.add(point.getPointName()));
        routeOutput.setItems(items);
    }




    private double calculateDistance(double x1, double y1, double x2, double y2) {
        //Use Euclidean Formula to calculate the distance between two points
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }



    public void clearMap(ActionEvent actionEvent) {
        //clear lines
        mapPane.getChildren().removeIf(node -> node instanceof javafx.scene.shape.Line);
        //clear listview
        routeOutput.getItems().clear();
    }


    public void initialize(URL url, ResourceBundle resourceBundle){
        maincon = this; // Set the maincon to this instance of the controller
    }


    public void addAvoidPoint(ActionEvent actionEvent) {

    }

    public void removePoint(ActionEvent event) {
    }

    public void removeWaypoint(ActionEvent event) {
    }

    public void addWaypoint(ActionEvent event) {
    }




















    public void dijkstraNoLineSearch(ActionEvent event) {
        if (firstSelectedPoint == null || selectedDestinationPoint == null) {
            System.out.println("Please select both start and end stations");
            return;
        }

        Set<InterestPoints> allPoints = new HashSet<>(points.values());
        Path shortestRoute = graph.dijkstraAlgorithm(allPoints, firstSelectedPoint, selectedDestinationPoint);

        if (shortestRoute == null || shortestRoute.getPath().isEmpty()) {
            System.out.println("No path found between the selected stations");
            return; // Exit the method early to avoid further processing
        }

        List<InterestPoints> path = shortestRoute.getPath();
        System.out.println(path.get(0).getPointName() + " to " + path.get(path.size()-1).getPointName());

        // Using the existing method to draw the path on the map
        drawShortestPath(shortestRoute);

        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        ObservableList<String> items = FXCollections.observableArrayList();
        items.add("Dijkstra: " + path.get(0).getPointName() + " to " + path.get(path.size()-1).getPointName());
        items.add("Total Time: " + decimalFormat.format(shortestRoute.getDistance()) + " seconds");

        path.forEach(point -> items.add(point.getPointName()));
        routeOutput.setItems(items);
    }





    public List<InterestLines> getCommonLines(InterestPoints point1, InterestPoints point2) {
        // Initialize a new list with the lines of the first point
        List<InterestLines> commonLines = new ArrayList<>(point1.getLines());

        // Keep only the lines that are also present in the lines of the second point
        commonLines.retainAll(point2.getLines());

        // Return the list of shared lines
        return commonLines;
    }

    private InterestPoints findNearestPoint(double x, double y) {
        // Initialize variables to store the nearest point and its distance
        InterestPoints nearestPoint = null;
        double nearestDistance = Double.MAX_VALUE;

        // Iterate over all points to find the nearest one
        for (InterestPoints point : points.values()) {
            // Calculate the distance between the given coordinates and the point's coordinates
            double distance = calculateDistance(x, y, point.getXcoord(), point.getYcoord());

            // If the calculated distance is smaller than the current nearest distance,
            // update the nearest point and nearest distance
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }

    private void selectMenuItem(MenuButton menuButton, String pointName) {
        // Iterate through menuButton's items list and select item with matching pointName
        for (MenuItem item : menuButton.getItems()) {
            if (item.getText().equals(pointName)) {
                // Set menuButton's text to selected item's text
                menuButton.setText(item.getText());
                break;
            }
        }
    }

}