import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

// 2-D moment invariants
public class Invariant {

    public static void main(String[] args) throws IOException {
        String imageName = args[0];

        File file = new File("images/" + imageName + ".bmp");
        BufferedImage img = ImageIO.read(file);
        double m00 = 0;
        double m01 = 0;
        double m10 = 0;
        Raster raster = img.getData();
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                double f = raster.getSample(i, j, 0);
                m00 += f;
                m01 += j * f;
                m10 += i * f;
            }
        }

        double xBar = m10 / m00;
        double yBar = m01 / m00;

        double mu20 = 0;
        double mu02 = 0;
        double mu00 = m00;
        double mu11 = 0;
        double mu12 = 0;
        double mu21 = 0;
        double mu30 = 0;
        double mu03 = 0;

        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                double f = raster.getSample(i, j, 0);
                mu02 += (j - yBar) * (j - yBar) * f;
                mu20 += (i - xBar) * (i - xBar) * f;
                mu11 += (i - xBar) * (j - yBar) * f;
                mu12 += (i - xBar) * (j - yBar) * (j - yBar) * f;
                mu21 += (i - xBar) * (j - yBar) * (i - xBar) * f;
                mu03 += (j - yBar) * (j - yBar) * (j - yBar) * f;
                mu30 += (i - xBar) * (i - xBar) * (i - xBar) * f;
            }
        }

        double eta02 = mu02 / mu00 / mu00;
        double eta20 = mu20 / mu00 / mu00;
        double eta11 = mu11 / mu00 / mu00;
        double eta12 = mu12 / Math.pow(mu00, 2.5);
        double eta21 = mu21 / Math.pow(mu00, 2.5);
        double eta03 = mu03 / Math.pow(mu00, 2.5);
        double eta30 = mu30 / Math.pow(mu00, 2.5);

        double phi1 = eta02 + eta20;
        double phi2 = (eta02 - eta20) * (eta02 - eta20) + 4 * eta11 * eta11;
        double phi3 = Math.pow(eta30 - 3 * eta12, 2) + Math.pow(3 * eta21 - eta03, 2);
        double phi4 = Math.pow(eta30 + eta12, 2) + Math.pow(eta21 + eta03, 2);
        double phi5 = (eta30 - 3 * eta12) * (eta30 + eta12) *
                (Math.pow(eta30 + eta12, 2) - 3 * Math.pow(eta21 + eta03, 2))
                - (3 * eta21 - eta03) * (eta21 + eta03) * (3 * Math.pow(eta30 + eta12, 2) - Math.pow(eta21 + eta03, 2));
        double phi6 = (eta20 - eta02) * (Math.pow(eta30 + eta12, 2) - Math.pow(eta21 + eta03, 3))
                + 4 * eta11 * (eta30 + eta12) * (eta21 + eta03);
        double phi7 = (3 * eta21 - eta03) * (eta30 + eta12) * (Math.pow(eta30 + eta12, 2) - 3 * Math.pow(eta21 + eta03, 2))
                + (3 * eta12 - eta30) * (eta21 + eta03) * (3 * Math.pow(eta30 + eta12, 2) - Math.pow(eta21 + eta03, 2));


        System.out.println("Image is " + imageName);
        System.out.println("phi1 = " + phi1);
        System.out.println("phi2 = " + phi2);
        System.out.println("phi3 = " + phi3);
        System.out.println("phi4 = " + phi4);
        System.out.println("phi5 = " + phi5);
        System.out.println("phi6 = " + phi6);
        System.out.println("phi7 = " + phi7);

        System.out.println("=========");
        System.out.println(phi1);
        System.out.println(phi2);
        System.out.println(phi3);
        System.out.println(phi4);
        System.out.println(phi5);
        System.out.println(phi6);
        System.out.println(phi7);

    }

}
