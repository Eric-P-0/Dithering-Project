
import java.awt.Color;
import java.awt.Dimension;
import java.awt.datatransfer.*;
import java.io.File;
import java.util.List;
import javax.swing.*;

public class DitherProj {

    public static void main(String[] args) {
        //Main Application Window
        JFrame frame = new JFrame("DitherProj");

        //Label that displays text for user
        JLabel label = new JLabel("Hello, DitherProj!");

        FramesetUp(frame, label);
        DragandDrop(frame, label);

        //SwingUtilities.invokeLater(() -> new DitherProj().setVisible(true));
    }

    //Method to set up the main application window look
    private static void FramesetUp(JFrame frame, JLabel label) {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the background color of the frame to black
        frame.getContentPane().setBackground(Color.BLACK);
        // Set the minimum size of the frame to 400x500 pixels
        frame.setMinimumSize(new Dimension(400, 500));
        // Set the label's text color to white
        label.setForeground(Color.WHITE);

        // Add the label to the frame's content pane
        frame.getContentPane().add(label);

        frame.pack();
        frame.setVisible(true);
    }

    //Method to set up the drag and drop functionality for the application
    private static void DragandDrop(JFrame frame, JLabel label) {

        frame.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                // Check if the dropped content is a list of files
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            @Override
            public boolean importData(TransferSupport support) {
                try {
                    // Extract the list of files
                    List<File> files = (List<File>) support.getTransferable()
                            .getTransferData(DataFlavor.javaFileListFlavor);

                    if (!files.isEmpty()) {
                        File droppedFile = files.get(0);

                        // Debug: Display the absolute directory path
                        label.setText("Path: " + droppedFile.getAbsolutePath());

                        //method needed to process image
                        //imageProcessing(droppedFile);
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    private static void imageProcessing(File droppedFile) {
        // Placeholder for image processing logic
        // This method will handle the dithering algorithm and image manipulation
    }
}
