package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;

public class AffineTransformation extends JFrame {

    private static final int CANVAS_SIZE = 960;
    private static final Color POINT_COLOR = Color.BLUE;
    private static final String INPUT_FILE_PATH = "C:\\Users\\Downloads\\DS4.txt";
    private static final String OUTPUT_FILE_PATH = "C:\\Users\\Downloads\\output_dataset.txt";

    private BufferedImage canvas;

    public AffineTransformation() {
        canvas = new BufferedImage(CANVAS_SIZE, CANVAS_SIZE, BufferedImage.TYPE_INT_ARGB);
        initializeCanvas();
        loadAndTransformDataset(INPUT_FILE_PATH);
        displayResult();
        saveResultToImage("C:\\Users\\Downloads\\output_image.png");
        saveTransformedDataset(OUTPUT_FILE_PATH);
    }

    private void initializeCanvas() {
        Graphics2D g = canvas.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, CANVAS_SIZE, CANVAS_SIZE);
    }

    private void loadAndTransformDataset(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            AffineTransform transform = getAffineTransform(50, 480, 480);

            String line;
            Graphics2D g = canvas.createGraphics();
            g.setColor(POINT_COLOR);

            while ((line = br.readLine()) != null) {
                String[] coordinates = line.split("\\s+");
                if (coordinates.length == 2) {
                    double x = Double.parseDouble(coordinates[0]);
                    double y = Double.parseDouble(coordinates[1]);
                    double[] transformedPoint = transformPoint(transform, x, y);
                    g.drawLine((int) transformedPoint[0], (int) transformedPoint[1],
                            (int) transformedPoint[0], (int) transformedPoint[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private AffineTransform getAffineTransform(double angle, double cx, double cy) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(angle), cx, cy);
        return transform;
    }

    private double[] transformPoint(AffineTransform transform, double x, double y) {
        double[] result = new double[2];
        transform.transform(new double[] { x, y }, 0, result, 0, 1);
        return result;
    }

    private void displayResult() {
        ImageIcon imageIcon = new ImageIcon(canvas);
        JLabel jLabel = new JLabel(imageIcon);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(jLabel);
        setSize(CANVAS_SIZE, CANVAS_SIZE);
        setVisible(true);
    }

    private void saveResultToImage(String filePath) {
        try {
            javax.imageio.ImageIO.write(canvas, "png", new java.io.File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveTransformedDataset(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            AffineTransform transform = getAffineTransform(50, 480, 480);

            try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_PATH))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] coordinates = line.split("\\s+");
                    if (coordinates.length == 2) {
                        double x = Double.parseDouble(coordinates[0]);
                        double y = Double.parseDouble(coordinates[1]);
                        double[] transformedPoint = transformPoint(transform, x, y);
                        writer.println(transformedPoint[0] + " " + transformedPoint[1]);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new AffineTransformation());
    }
}