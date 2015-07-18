package cn.studease.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import javax.imageio.ImageIO;


public class QRCode {
    private static final String PNG = "png";
    private static final String MAIL_TO = "mailto:";
    private static final String URL = "://";
    private static final String TEL = "tel:";
    private static final String SMS = "smsto:";
    private static final String GPS = "geo:";
    private static final String COLON = ":";
    private String logo;
    private int size = 146;

    private Color forecolor = Color.BLACK;

    private Color backcolor = Color.WHITE;


    public static QRCode instance() {
        return new QRCode();
    }

    public static QRCode instance(int size) {
        QRCode instance = new QRCode();
        instance.size = size;
        return instance;
    }

    public static QRCode instance(String logo) {
        QRCode instance = new QRCode();
        instance.logo = logo;
        return instance;
    }

    public static QRCode instance(int size, String logo) {
        QRCode instance = new QRCode();
        instance.size = size;
        instance.logo = logo;
        return instance;
    }

    public QRCode setSize(int size) {
        this.size = size;
        return this;
    }

    public QRCode setLogo(String logo) {
        this.logo = logo;
        return this;
    }

    public QRCode setColor(Color forecolor, Color backcolor) {
        this.forecolor = forecolor;
        this.backcolor = backcolor;
        return this;
    }


    public void email(String email, OutputStream out)
            throws IOException, WriterException {
        write(bitMatrix("mailto:" + email), out);
    }


    public void url(String url, OutputStream out)
            throws IOException, WriterException {
        write(bitMatrix((url.toLowerCase().contains("://") ? "" : "http://") + url), out);
    }


    public void telephone(String telephone, OutputStream out)
            throws IOException, WriterException {
        write(bitMatrix("tel:" + telephone), out);
    }


    public void sms(String telephone, String content, OutputStream out)
            throws IOException, WriterException {
        write(bitMatrix("smsto:" + telephone + ":" + content), out);
    }


    public void gps(String latitude, String longitude, OutputStream out)
            throws IOException, WriterException {
        write(bitMatrix("geo:" + latitude + "," + longitude), out);
    }


    public void vCard(String name, String telephone, OutputStream out)
            throws IOException, WriterException {
        StringBuilder sb = new StringBuilder();
        sb.append("BEGIN:VCARD\n");
        sb.append("VERSION:2.1\n");
        sb.append("N:").append(name).append("\n");
        sb.append("TEL:").append(telephone).append("\n");
        sb.append("END:VCARD");

        write(bitMatrix(sb.toString()), out);
    }


    public void vCard(String name, String telephone, String address, OutputStream out)
            throws IOException, WriterException {
        StringBuilder sb = new StringBuilder();
        sb.append("BEGIN:VCARD\n");
        sb.append("VERSION:2.1\n");
        sb.append("N:").append(name).append("\n");
        sb.append("TEL:").append(telephone).append("\n");
        sb.append("ADR:").append(address).append("\n");
        sb.append("END:VCARD");

        write(bitMatrix(sb.toString()), out);
    }


    public void vCard(String name, String telephone, String email, String org, String duty, String address, String note, OutputStream out)
            throws IOException, WriterException {
        StringBuilder sb = new StringBuilder();
        sb.append("BEGIN:VCARD\n");
        sb.append("VERSION:2.1\n");
        sb.append("N:").append(name).append("\n");
        sb.append("ORG:").append(org).append("\n");
        sb.append("TITLE:").append(duty).append("\n");
        sb.append("TEL:").append(telephone).append("\n");
        sb.append("ADR:").append(address).append("\n");
        sb.append("EMAIL:").append(email).append("\n");
        sb.append("NOTE:").append(note).append("\n");
        sb.append("END:VCARD");

        write(bitMatrix(sb.toString()), out);
    }


    public void text(String text, OutputStream out)
            throws WriterException, IOException {
        write(bitMatrix(text), out);
    }


    private BitMatrix bitMatrix(String content)
            throws WriterException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, Integer.valueOf(1));
        return new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, this.size, this.size, hints);
    }


    private BufferedImage toBufferedImage(BitMatrix matrix)
            throws IOException {
        int width = matrix.getWidth();
        int height = matrix.getHeight();


        BufferedImage matrixImage = new BufferedImage(width, height, 1);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                matrixImage.setRGB(x, y, matrix.get(x, y) ? this.forecolor.getRGB() : this.backcolor.getRGB());
            }
        }


        if (this.logo != null && this.logo.trim().length() > 0) {
            BufferedImage logoFile = ImageIO.read(new File(this.logo));
            Graphics2D g = matrixImage.createGraphics();
            width = matrixImage.getWidth() / 5;
            height = matrixImage.getHeight() / 5;
            int x = (matrixImage.getWidth() - width) / 2;
            int y = (matrixImage.getHeight() - height) / 2;
            g.drawImage(logoFile, x, y, width, height, null);
            g.dispose();
        }

        return matrixImage;
    }


    private void write(BitMatrix matrix, OutputStream stream)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        ImageIO.write(image, "png", stream);
    }
}


