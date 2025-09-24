package org.itzixi.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;



public class QrCodeUtils {

    public static String generateQRCode(String data) {
        String filePath = "D:/work/photo/qrcode/";
        return QrCodeUtils.generateQRCode(data, filePath);
    }

    public static String generateQRCode(String data, String filePath) {
        return QrCodeUtils.generateQRCode(data, 300, 300, filePath);
    }

    // 生成二维码
    public static String generateQRCode(String data, int width, int height, String filePath) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // 设置字符编码
            hints.put(EncodeHintType.ERROR_CORRECTION, com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H); // 错误纠正级别
            hints.put(EncodeHintType.MARGIN, 1); // 二维码边距

            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, width, height, hints);

            // 创建BufferedImage对象来表示二维码
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
            }

            // 将QR码保存到文件
            File qrCodeFile = new File(filePath);
            ImageIO.write(image, "png", qrCodeFile);
            // MultipartFile multipartFile = new MockMultipartFile(
            //                                     "file",
            //                                     qrCodeFile.getName(),
            //                                     null,
            //                                     new FileInputStream(qrCodeFile));

            System.out.println("二维码已生成并保存到: " + filePath);
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
