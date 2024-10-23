import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PngConverter {
    private PngConverter() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // Function to save a string as a PNG image
    public static void saveStringAsImage(String text, String fileName, int width, int fontSize) {
        // Set font and calculate text dimensions
        Font font = new Font("Monospaced", Font.PLAIN, fontSize);

        int height = calculateImageHeight(text, font, width); // Adjusted height

        // Create a buffered image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // Set background color and text properties
        g.setColor(Color.WHITE); // Background color
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK); // Text color
        g.setFont(font);

        // Draw the string onto the image
        drawString(g, text, 10, 25, width);

        // Dispose of the graphics context
        g.dispose();

        // Save the image as PNG
        try {
            ImageIO.write(image, "png", new File(fileName));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }


    // Helper method to calculate the required image height
    private static int calculateImageHeight(String text, Font font, int imageWidth) {
        FontMetrics metrics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
                .getGraphics().getFontMetrics(font);
        int lineHeight = metrics.getHeight();
        int lines = 0;
        for (String line : text.split("\n")) {
            lines += (metrics.stringWidth(line) / imageWidth) + 1;
        }
        return lines * lineHeight + 20;
    }

    private static void drawString(Graphics2D g, String text, int x, int y, int imageWidth) {
        FontMetrics metrics = g.getFontMetrics();
        int lineHeight = metrics.getHeight();

        for (String line : text.split("\n")) {
            if (metrics.stringWidth(line) > imageWidth) {
                // Wrap long lines
                StringBuilder sb = new StringBuilder();
                for (char c : line.toCharArray()) {
                    sb.append(c);
                    if (metrics.stringWidth(sb.toString()) >= imageWidth) {
                        g.drawString(sb.toString(), x, y);
                        y += lineHeight;
                        sb = new StringBuilder();
                    }
                }
                if (!sb.isEmpty()) {
                    g.drawString(sb.toString(), x, y);
                    y += lineHeight;
                }
            } else {
                g.drawString(line, x, y);
                y += lineHeight;
            }
        }
    }
}
