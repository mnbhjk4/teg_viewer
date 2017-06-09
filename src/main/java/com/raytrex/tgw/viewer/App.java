package com.raytrex.tgw.viewer;

import java.io.File;
import java.util.List;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.jfree.data.function.Function2D;
import org.jfree.data.function.NormalDistributionFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * AUO data anlazy xxx with raw data file
 *
 */
public class App extends Application {
	private double [] values = new double[]{9.0,10.0,11.0,8.0,12.0,19.0,20.0};
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		double mean = new Mean().evaluate(values);
		double sd = new StandardDeviation().evaluate(values);
		NormalDistribution nd = new NormalDistribution(mean,sd);
		Function2D normal = new NormalDistributionFunction2D(mean, sd);
		XYSeriesCollection dataset =
			    (XYSeriesCollection) DatasetUtilities.sampleFunction2D(normal,-10.0,50,100,"Test");
		Pane root = new Pane();
		final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        //creating the chart
        final LineChart<Number,Number> lineChart = 
                new LineChart<Number,Number>(xAxis,yAxis);        
        lineChart.setTitle("Stock Monitoring, 2010");
     
        XYSeries data = dataset.getSeries("Test");
        List itemList = data.getItems();
        
        XYChart.Series series1 = new XYChart.Series();
        for(Object i : itemList){
        	XYDataItem item = (XYDataItem) i;
        	Data d = new XYChart.Data(item.getX(), item.getY());
        	Rectangle rect = new Rectangle(0, 0);
        	rect.setVisible(false);
        	d.setNode(rect);
        	series1.getData().add(d);
        }
        lineChart.getData().addAll(series1);
        //Instantiating the class CubicCurve x
        Scene scene  = new Scene(root,800,600);
        scene.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                } else {
                    event.consume();
                }
            }
        });
        
        // Dropping over surface
        scene.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;
                    String filePath = null;
                    for (File file:db.getFiles()) {
                        filePath = file.getAbsolutePath();
                        System.out.println(filePath);
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
        root.getChildren().add(lineChart);
        
        primaryStage.setScene(scene);
        primaryStage.show();
	}

}
