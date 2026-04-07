
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.*;

public class DitherProj {

    public static void main(String[] args) {
        JFrame frame = new JFrame("DitherProj");
        JLabel label = new JLabel("Hello, DitherProj!");
        FramesetUp(frame, label);
    }

    private static void FramesetUp(JFrame frame, JLabel label) {

        // JFrame frame = new JFrame("DitherProj");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setMinimumSize(new Dimension(400, 500));
        //JLabel label = new JLabel("Hello, DitherProj!");
        label.setForeground(Color.WHITE);

        frame.getContentPane().add(label);

        frame.pack();
        frame.setVisible(true);
    }

    private static void DragandDrop(JFrame frame, JLabel label) {

    }
}
