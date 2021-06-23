package com.asterle.testbackend.rest.image.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class Image {

    int width;
    int height;
    BufferedImage bufferedImage;

    public Image(final byte[] imageArray) {
        try {
            bufferedImage = ImageIO.read(new ByteArrayInputStream(imageArray));

            width = bufferedImage.getWidth();
            height = bufferedImage.getHeight();

        } catch (final IOException e) {
            log.error("Error while decoding image!", e);
        }
    }

    public Image(final BufferedImage image) {
        bufferedImage = image;

        width = bufferedImage.getWidth();
        height = bufferedImage.getHeight();
    }

    public Color getPixel(final int x, final int y) {
        final Color color = new Color(bufferedImage.getRGB(x, y));
        return color;
    }

    public void setPixel(final int x, final int y, final Color color) {
        bufferedImage.setRGB(x, y, color.getRGB());
    }

    public byte[] getByteArray() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", baos);
        } catch (final IOException e) {
            log.error("Error while encoding image!", e);
        }
        return baos.toByteArray();
    }

    public byte[] getByteArray(final String format) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, format, baos);
        } catch (final IOException e) {
            log.error("Error while encoding image!", e);
        }
        return baos.toByteArray();
    }
}
