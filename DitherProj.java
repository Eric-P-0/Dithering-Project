
import java.awt.Color;
import java.awt.Dimension;
import java.awt.datatransfer.*;
import java.io.File;
import java.util.List;
import javax.swing.*;

public class DitherProj {

    public static void main(String[] args) {
        JFrame frame = new JFrame("DitherProj");
        JLabel label = new JLabel("Hello, DitherProj!");
        FramesetUp(frame, label);
        DragandDrop(frame, label);

        //SwingUtilities.invokeLater(() -> new DitherProj().setVisible(true));
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
                        // Display the absolute directory path
                        label.setText("Path: " + droppedFile.getAbsolutePath());
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }
}
