import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

public class CoOccurrence {

    public static void main(String[] args) throws IOException {

        String imageName = args[0];

        File file = new File("images/" + imageName + ".bmp");
        BufferedImage img = ImageIO.read(file);
        int[][] coMatrix = new int[256][256];
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                coMatrix[i][j] = 0;
            }
        }
        Raster raster = img.getData();

        double max = 0;
        double sum = 0;
        for (int i = 0; i < img.getWidth() - 1; i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                int pixelLeft = raster.getSample(i, j, 0);
                int pixelRight = raster.getSample(i + 1, j, 0);
                coMatrix[pixelLeft][pixelRight]++;
                sum++;
                if (coMatrix[pixelLeft][pixelRight] > max) {
                    max = coMatrix[pixelLeft][pixelRight];
                }
            }
        }

        // System.out.println(max);
        BufferedImage destImg = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                int ranged = (int) (((double) coMatrix[i][j] / max) * 255.0);
                // int ranged = Math.min(coMatrix[i][j], 255);
                // ranged = Math.min(ranged, 255);
                ranged = (int) (Math.log1p(coMatrix[i][j]) / Math.log(max) * 255.0);
                ranged = Math.min(ranged, 255);
                ranged = Math.max(ranged, 0);
                // System.out.println(ranged);
                destImg.setRGB(i, j, new Color(ranged, ranged, ranged).getRGB());

            }
        }

        ImageIO.write(destImg, "png", new File("images/" + imageName + "_co.png"));

        double maxProbability = 0;
        double[] pi = new double[256];
        double[] pj = new double[256];

        double contrast = 0;
        double uniformity = 0;
        double homogeneity = 0;
        double entropy = 0;

        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                maxProbability = Math.max(maxProbability, coMatrix[i][j] / sum);
                pi[i] += coMatrix[i][j] / sum;
                pj[j] += coMatrix[i][j] / sum;
                contrast += (i - j) * (i - j) * coMatrix[i][j] / sum;
                uniformity += coMatrix[i][j] / sum * coMatrix[i][j] / sum;
                homogeneity += coMatrix[i][j] / sum / (1 + Math.abs(i - j));
                if (coMatrix[i][j] / sum > 0)
                    entropy += coMatrix[i][j] / sum * Math.log(coMatrix[i][j] / sum) / Math.log(2);
            }
        }

        double mr = 0;
        double mc = 0;

        for (int i = 0; i < 256; i++) {
            mr += i * pi[i];
            mc += i * pj[i];
        }

        double sigmaR = 0;
        double sigmaC = 0;

        for (int i = 0; i < 256; i++) {
            sigmaR += (i - mr) * (i - mr) * pi[i];
            sigmaC += (i - mc) * (i - mc) * pj[i];
        }

        double correlation = 0;
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                correlation += (i - mr) * (j - mc) * coMatrix[i][j] / sum
                        / Math.sqrt(sigmaR) / Math.sqrt(sigmaC);
            }
        }

        System.out.println("Image is " + imageName);
        System.out.println("MaxProbability = " + maxProbability);
        System.out.println("Correlation = " + correlation);
        System.out.println("Contrast = " + contrast);
        System.out.println("Uniformity = " + uniformity);
        System.out.println("Homogeneity = " + homogeneity);
        System.out.println("Entropy = " + (-entropy));
    }

}
