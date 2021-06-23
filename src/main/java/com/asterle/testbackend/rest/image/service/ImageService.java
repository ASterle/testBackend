package com.asterle.testbackend.rest.image.service;

import com.asterle.testbackend.rest.image.model.Image;
import java.awt.Color;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component
public class ImageService {

    public byte[] process(final Image image, final String method, final String parameter) {
        switch (method) {
            case "format":
                return convertFormat(image, parameter);
            case "color":
                final String[] split = parameter.split(",");
                final double[] doubleRgbs = Stream.of(split).mapToDouble(Double::parseDouble).toArray();

                return applyColor(image, doubleRgbs);
            case "grayscale":
                return convertToGrayscale(image);
        }
        return null;
    }

    private byte[] convertFormat(final Image image, final String filetype) {
        return image.getByteArray(filetype);
    }

    private byte[] applyColor(final Image image, final double... rgb) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                final Color pixel = image.getPixel(x, y);

                final double r = rgb[0];
                final double g = rgb[1];
                final double b = rgb[2];
                double a = 1.0;

                if (rgb.length == 4) {
                    a = rgb[3];
                }

                final Color result = new Color((int) (pixel.getRed() * r),
                                               (int) (pixel.getGreen() * g),
                                               (int) (pixel.getBlue() * b),
                                               (int) (pixel.getAlpha() * a));

                image.setPixel(x, y, result);
            }
        }

        return image.getByteArray();
    }

    private byte[] convertToGrayscale(final Image image) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                final Color pixel = image.getPixel(x, y);

                final int gray = (pixel.getRed() + pixel.getGreen() + pixel.getBlue()) / 3;

                image.setPixel(x, y, new Color(gray, gray, gray, pixel.getAlpha()));
            }
        }

        return image.getByteArray();
    }
}
