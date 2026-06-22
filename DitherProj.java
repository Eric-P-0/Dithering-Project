
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.BufferOverflowException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;

/*
Dithering is a technique not many people know about or understand. This project is being made for not only me to understand the automation of the technique
but to also intro duce others to the technique and how it can be used in various applications. The main goal of this project is to create a user-friendly
 application that allows users to easily apply dithering to their images, and to understand the underlying principles of the technique. The application
  will be built using Java Swing for the user interface, and will utilize image processing libraries to perform the dithering algorithm on the images. 
  The project will also include documentation and tutorials to help users understand how to use the application and the principles behind dithering.
 */
public class DitherProj {

    public static void main(String[] args) {
//some accessable bool values for the user to check before analyzing photos
//this bool will be to set the photo to monochromatic displaying a better view of the reults for clearer understanding.
        boolean greyScale = false;
//Main Application Window
        JFrame frame = new JFrame("DitherProj");

//Label that displays text for user
        JLabel label = new JLabel("Hello, DitherProj! Please drag an image into the application.");

        JCheckBox checkBox = new JCheckBox("Enable Greyscale", false);

//simple function for setting up windows initial looks
        FramesetUp(frame, label, checkBox);
// Code found on StackOverflow to allow for files to be dragged into the program window
        DragandDrop(frame, label, checkBox);

    }

//Method to set up the main application window look
    private static void FramesetUp(JFrame frame, JLabel label, JCheckBox box) {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the background color of the frame to black
        frame.getContentPane().setBackground(Color.BLACK);
        // Set the minimum size of the frame to 400x500 pixels
        frame.setMinimumSize(new Dimension(400, 500));
        // Set the label's text color to white
        label.setForeground(Color.WHITE);

        frame.getContentPane().setLayout(new java.awt.FlowLayout());
        // Add the label to the frame's content pane
        frame.getContentPane().add(label);
        frame.getContentPane().add(box);
        box.setBackground(Color.BLACK);
        box.setForeground(Color.white);
        box.setOpaque(true);

        frame.getContentPane().setLayout(new javax.swing.BoxLayout(
                frame.getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        frame.pack();
        frame.setVisible(true);

    }

    //Method to set up the drag and drop functionality for the application
    private static void DragandDrop(JFrame frame, JLabel label, JCheckBox box) /*throws Exception*/ {

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
                    List<File> files = (List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

                    if (!files.isEmpty()) {
                        File droppedFile = files.get(0);

                        // Debug: Display the absolute directory path. If the file isnt an image, it just shows this message.
                        label.setText("<html>This is not an acceptable image.(.png, .jpg, .img...) <br> Path: </hmtml>" + droppedFile.getAbsolutePath());
                        if (box.isSelected()) {
                            //Greyscale image before processing
                            BWImageProcessing(droppedFile, label);
                        } else {
                            //RGB image processing
                            imageProcessing(droppedFile, label);
                        }
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    private static void BWImageProcessing(File droppedFile, JLabel label) throws Exception {
        File file = new File(droppedFile.getAbsolutePath());
        BufferedImage image = ImageIO.read(file);

        long startTime = System.nanoTime();
       
        
        // Convert to greyscale first
        BufferedImage dithered = floydSteinbergDither(ConvertToGreyscale(image));
        PostWindowBuild(droppedFile, dithered);
        
        long endTime = System.nanoTime();

        double elapsedTimeMS = (endTime -startTime) / 1_000_000.0;

         label.setText(DisplayMetadata(file, image, elapsedTimeMS));


    }
   

    private static void imageProcessing(File droppedFile, JLabel label) throws Exception {
        File file = new File(droppedFile.getAbsolutePath());
        BufferedImage image = ImageIO.read(file);
 long startTime = System.nanoTime();
       

       
        BufferedImage ditherred = floydSteinbergDither(image);
        PostWindowBuild(droppedFile, ditherred);

         long endTime = System.nanoTime();

        double elapsedTimeMS = (endTime -startTime) / 1_000_000.0;

         label.setText(DisplayMetadata(file, image, elapsedTimeMS));

    }
     private static String DisplayMetadata(File file, BufferedImage image, double time)
    {
        long fileSizeInBytes = file.length();
        double fileSizeInKB = fileSizeInBytes / 1024.0;
      

        return ("<html><br>" +file.getAbsolutePath()+ "</br> <br>Size(kbs): " + fileSizeInKB +"</br><br> "+ "width: " + image.getWidth() + "</br><br> height: "+ image.getHeight()+"</br><br> Processing Time(s): " +time/1000+ "</br></html>");
    }
private static BufferedImage ConvertToGreyscale( BufferedImage image)
{
      BufferedImage grey = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                // Standard luminance formula
                int lum = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                int greyPixel = (0xFF << 24) | (lum << 16) | (lum << 8) | lum;
                grey.setRGB(x, y, greyPixel);
            }
        }
        return grey;
}
    private static void PostWindowBuild(File droppedFile, BufferedImage dithered)
    {
        JFrame imageFrame = new JFrame("B&W Preview - " + droppedFile.getName());
        imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        imageFrame.getContentPane().setBackground(Color.BLACK);
        imageFrame.setLayout(new java.awt.BorderLayout());

        int maxWidth = 800, maxHeight = 600;
        double scale = Math.min(1.0, Math.min((double) maxWidth / dithered.getWidth(), (double) maxHeight / dithered.getHeight()));
        Image scaledImage = dithered.getScaledInstance((int) (dithered.getWidth() * scale), (int) (dithered.getHeight() * scale), Image.SCALE_SMOOTH);

        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton saveBtn = SaveButton(dithered, droppedFile.getName());

        imageFrame.getContentPane().add(imageLabel, java.awt.BorderLayout.CENTER);
        imageFrame.getContentPane().add(saveBtn, java.awt.BorderLayout.SOUTH);
        imageFrame.pack();
        imageFrame.setLocationRelativeTo(null);
        imageFrame.setVisible(true);
    }


    private static BufferedImage floydSteinbergDither(BufferedImage image) {

        int width = image.getWidth();
        int height = image.getHeight();

        // Initialize error buffers with original pixel channel values
        double[][] errR = new double[height][width];
        double[][] errG = new double[height][width];
        double[][] errB = new double[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                errR[y][x] = (rgb >> 16) & 0xFF;
                errG[y][x] = (rgb >> 8) & 0xFF;
                errB[y][x] = rgb & 0xFF;
            }
        }

        // Dithering loop
        for (int y = 0; y < height; y++) {
            if (y % 2 == 0) {
                // even row: left to right
                for (int x = 0; x < width; x++) {
                    ditherPixel(image, errR, errG, errB, x, y, 1, width, height);
                }
            } else {
                // odd row: right to left
                for (int x = width - 1; x >= 0; x--) {
                    ditherPixel(image, errR, errG, errB, x, y, -1, width, height);
                }
            }
        }

        return image;
    }

    private static void ditherPixel(BufferedImage image,
            double[][] errR, double[][] errG, double[][] errB,
            int x, int y, int dir, int width, int height) {
        int newR, newG, newB;
        int min = 0, max = 255, mid = max / 2;
        // Quantize each channel to nearest 0 or 255
        if (errR[y][x] < mid) {
            newR = min;
        } else {
            newR = max;
        }
        if (errG[y][x] < mid) {
            newG = min;
        } else {
            newG = max;
        }
        if (errB[y][x] < mid) {
            newB = min;
        } else {
            newB = max;
        }

        // Write quantized pixel back to image
        //read as A,R,G,B
        int quantized = (0xFF << 24) | (newR << 16) | (newG << 8) | newB;
        image.setRGB(x, y, quantized);

        // Calculate error per channel
        double eR = errR[y][x] - newR;
        double eG = errG[y][x] - newG;
        double eB = errB[y][x] - newB;

        // forward neighbor (7/16)     → [y][x + dir]
        if (x + dir >= 0 && x + dir < width) {
            errR[y][x + dir] += eR * (7.0 / 16);
            errG[y][x + dir] += eG * (7.0 / 16);
            errB[y][x + dir] += eB * (7.0 / 16);
        }

        if (y + 1 < height) {
            // below-forward (3/16)    → [y+1][x + dir]
            if (x + dir >= 0 && x + dir < width) {
                errR[y + 1][x + dir] += eR * (3.0 / 16);
                errG[y + 1][x + dir] += eG * (3.0 / 16);
                errB[y + 1][x + dir] += eB * (3.0 / 16);
            }

            // directly below (5/16)  → [y+1][x]
            errR[y + 1][x] += eR * (5.0 / 16);
            errG[y + 1][x] += eG * (5.0 / 16);
            errB[y + 1][x] += eB * (5.0 / 16);

            // below-backward (1/16)  → [y+1][x - dir]
            if (x - dir >= 0 && x - dir < width) {
                errR[y + 1][x - dir] += eR * (1.0 / 16);
                errG[y + 1][x - dir] += eG * (1.0 / 16);
                errB[y + 1][x - dir] += eB * (1.0 / 16);
            }
        }
    }

    private static JButton SaveButton(BufferedImage dithered, String filename) {

        JButton saveBtn = new JButton("Save Image");
        saveBtn.setBackground(Color.DARK_GRAY);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setOpaque(true);
        saveBtn.setBorderPainted(false);
        saveBtn.setFocusPainted(false);

        saveBtn.addActionListener(e -> {
            javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();

            // Restrict file types shown and selectable
            javax.swing.filechooser.FileNameExtensionFilter filter
                    = new javax.swing.filechooser.FileNameExtensionFilter(
                            "Image Files (*.png, *.jpg, *.bmp)", "png", "jpg", "bmp");
            fileChooser.setFileFilter(filter);
            fileChooser.setAcceptAllFileFilterUsed(false); // remove "All Files" option
            fileChooser.setSelectedFile(new File("dithered_" + filename));

            if (fileChooser.showSaveDialog(null) == javax.swing.JFileChooser.APPROVE_OPTION) {
                File saveFile = fileChooser.getSelectedFile();
                String saveName = saveFile.getName().toLowerCase();

                // Determine format, default to png if no valid extension typed
                String format;
                if (saveName.endsWith(".jpg")) {
                    format = "jpg";
                } else if (saveName.endsWith(".bmp")) {
                    format = "bmp";
                } else {
                    format = "png";
                    // Append .png if no recognized extension
                    if (!saveName.endsWith(".png")) {
                        saveFile = new File(saveFile.getAbsolutePath() + ".png");
                    }
                }

                try {
                    ImageIO.write(dithered, format, saveFile);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        return saveBtn;

    }

}
