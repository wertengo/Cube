package task3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class CubeDrawernerObj extends JPanel implements MouseMotionListener {

    private Calculate calculate;
    private List<int[]> vertices;
    private List<int[]> edges;
    private int lastX, lastY;

    public CubeDrawernerObj(String objFilePath) {
        calculate = new Calculate();
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        readObjFile(objFilePath);
        addMouseMotionListener(this);
    }

    private void readObjFile(String objFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(objFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("v ")) {
                    String[] parts = line.split(" ");
                    int x = (int) (Double.parseDouble(parts[1]) * 100);
                    int y = (int) (Double.parseDouble(parts[2]) * 100);
                    int z = (int) (Double.parseDouble(parts[3]) * 100);
                    vertices.add(new int[]{x, y, z});
                } else if (line.startsWith("f ")) {
                    String[] parts = line.split(" ");
                    int v1 = Integer.parseInt(parts[1].split("/")[0]) - 1;
                    int v2 = Integer.parseInt(parts[2].split("/")[0]) - 1;
                    int v3 = Integer.parseInt(parts[3].split("/")[0]) - 1;
                    edges.add(new int[]{v1, v2});
                    edges.add(new int[]{v2, v3});
                    edges.add(new int[]{v3, v1});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawCube() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int[] edge : edges) {
            int x1 = (int) calculate.calculateX(vertices.get(edge[0])[0], vertices.get(edge[0])[1], vertices.get(edge[0])[2]);
            int y1 = (int) calculate.calculateY(vertices.get(edge[0])[0], vertices.get(edge[0])[1], vertices.get(edge[0])[2]);
            int x2 = (int) calculate.calculateX(vertices.get(edge[1])[0], vertices.get(edge[1])[1], vertices.get(edge[1])[2]);
            int y2 = (int) calculate.calculateY(vertices.get(edge[1])[0], vertices.get(edge[1])[1], vertices.get(edge[1])[2]);

            g2d.setColor(Color.BLACK);
            g2d.drawLine(x1 + getWidth() / 2, y1 + getHeight() / 2, x2 + getWidth() / 2, y2 + getHeight() / 2);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int dx = e.getX() - lastX;
        int dy = e.getY() - lastY;
        calculate.setA(calculate.getA() + dy * 0.01);
        calculate.setB(calculate.getB() + dx * 0.01);
        lastX = e.getX();
        lastY = e.getY();
        drawCube();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
    }

    private class Calculate {
        private double A, B, C;

        double calculateX(int i, int j, int k) {
            return j * sin(A) * sin(B) * cos(C) - k * cos(A) * sin(B) * cos(C) +
                    j * cos(A) * sin(C) + k * sin(A) * sin(C) + i * cos(B) * cos(C);
        }

        double calculateY(int i, int j, int k) {
            return j * cos(A) * cos(C) + k * sin(A) * cos(C) -
                    j * sin(A) * sin(B) * sin(C) + k * cos(A) * sin(B) * sin(C) -
                    i * cos(B) * sin(C);
        }

        double calculateZ(int i, int j, int k) {
            return k * cos(A) * cos(B) - j * sin(A) * cos(B) + i * sin(B);
        }

        public double getA() {
            return A;
        }

        public void setA(double A) {
            this.A = A;
        }

        public double getB() {
            return B;
        }

        public void setB(double B) {
            this.B = B;
        }

        public double getC() {
            return C;
        }

        public void setC(double C) {
            this.C = C;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("3D Cube Drawer");
        CubeDrawernerObj panel = new CubeDrawernerObj("D:\\ideaproject\\проекты\\task1_komp_graphics\\task1_komp_graphics\\src\\task3\\cube.obj"); // Укажите путь к вашему файлу cube.obj
        frame.add(panel);
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Пример использования функции drawCube
        panel.drawCube();
    }
}


