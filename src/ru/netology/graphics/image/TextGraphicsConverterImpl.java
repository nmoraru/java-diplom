package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TextGraphicsConverterImpl implements TextGraphicsConverter {

    TextColorSchemaImpl schema = new TextColorSchemaImpl();

    private double maxRatio = 0;
    private int maxWidth = 0;
    private int maxHeight = 0;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));

        int width = img.getWidth();
        int height = img.getWidth();

        double widthToHeight = (double) width / height;
        double heightToWidth = (double) height / width;
        double ratio = Math.max(widthToHeight, heightToWidth);

        int newWidth = width;
        int newHeight = height;

        if (maxRatio != 0 && ratio > maxRatio) {
            throw new BadImageSizeException(ratio, maxRatio);
        }

        if (maxHeight != 0 && newHeight > maxHeight) {
            double k = newHeight / maxHeight;
            newHeight = maxHeight;
            newWidth = (int) (newWidth / k);
        }

        if (maxWidth != 0 && newWidth > maxWidth) {
            double k = newWidth / maxWidth;
            newWidth = maxWidth;
            newHeight = (int) (newHeight / k);
        }

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        ImageIO.write(bwImg, "png", new File("out.png"));
        WritableRaster bwRaster = bwImg.getRaster();

        StringBuilder str = new StringBuilder();
        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                str.append(c);
                str.append(c);
            }
            str.append("\n");
        }

        return str.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = (TextColorSchemaImpl) schema;
    }
}
