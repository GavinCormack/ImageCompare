import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.image.DataBuffer;
import java.awt.Robot;
import java.awt.AWTException;


public class ImageCompare
{
	public static void main(String args[])
	{
		File f1 = new File("C:/Users/Gavin/Desktop/testing.PNG"); //big image
		File f2 = new File("C:/Users/Gavin/Desktop/button.PNG");  //small image
		//compareImage(f1,f2);

		try {
		BufferedImage i1 = ImageIO.read(f1);
		BufferedImage i2 = ImageIO.read(f2);

		findSubimage(i1,i2);

		}




		catch(Exception e)
		{System.out.println("Failed!!!");}



	}

	public static boolean compareImage(File fileA, File fileB) {
    try {
        // take buffer data from both image files

        BufferedImage biA = ImageIO.read(fileA);
        DataBuffer dbA = biA.getData().getDataBuffer();
        int sizeA = dbA.getSize();

        BufferedImage biB = ImageIO.read(fileB);
        DataBuffer dbB = biB.getData().getDataBuffer();
        int sizeB = dbB.getSize();

        // compare data-buffer objects

        if(sizeA == sizeB) {
            for(int i=0; i<sizeA; i++) {
                if(dbA.getElem(i) != dbB.getElem(i)) {
                	System.out.println("Different Image");
                    return false;
                }
            }
            System.out.println("Same Image");
            return true;
        }
        else {
        	System.out.println("Different Size Image");
            return false;
        }
    }
    catch (Exception e) {
        System.out.println("Failed to compare images");
        return  false;
    }
}




 /**
 * Finds the a region in one image that best matches another, smaller, image.
 */
 public static int[] findSubimage(BufferedImage im1, BufferedImage im2){
   int w1 = im1.getWidth();
   int h1 = im1.getHeight();
   int w2 = im2.getWidth();
   int h2 = im2.getHeight();
   assert(w2 <= w1 && h2 <= h1);
   // will keep track of best position found
   int bestX = 0;
   int bestY = 0;
   double lowestDiff = Double.POSITIVE_INFINITY;
   // brute-force search through whole image (slow...)
   for(int x = 0;x < w1-w2;x++){
     for(int y = 0;y < h1-h2;y++){
       double comp = compareImages(im1.getSubimage(x,y,w2,h2),im2);
       if(comp < lowestDiff){
         bestX = x; bestY = y; lowestDiff = comp;
       }
     }
   }
   // output similarity measure from 0 to 1, with 0 being identical
   System.out.println(lowestDiff);
   // return best location
   System.out.println(bestX + " x " + bestY);
   return new int[]{bestX,bestY};
 }

 /**
 * Determines how different two identically sized regions are.
 */
 public static double compareImages(BufferedImage im1, BufferedImage im2){
   assert(im1.getHeight() == im2.getHeight() && im1.getWidth() == im2.getWidth());
   double variation = 0.0;
   for(int x = 0;x < im1.getWidth();x++){
     for(int y = 0;y < im1.getHeight();y++){
        variation += compareARGB(im1.getRGB(x,y),im2.getRGB(x,y))/Math.sqrt(3);
     }
   }
   return variation/(im1.getWidth()*im1.getHeight());
 }

 /**
 * Calculates the difference between two ARGB colours (BufferedImage.TYPE_INT_ARGB).
 */
 public static double compareARGB(int rgb1, int rgb2){
   double r1 = ((rgb1 >> 16) & 0xFF)/255.0;
   double r2 = ((rgb2 >> 16) & 0xFF)/255.0;
   double g1 = ((rgb1 >> 8) & 0xFF)/255.0;
   double g2 = ((rgb2 >> 8) & 0xFF)/255.0;
   double b1 = (rgb1 & 0xFF)/255.0;
   double b2 = (rgb2 & 0xFF)/255.0;
   double a1 = ((rgb1 >> 24) & 0xFF)/255.0;
   double a2 = ((rgb2 >> 24) & 0xFF)/255.0;
   // if there is transparency, the alpha values will make difference smaller
   return a1*a2*Math.sqrt((r1-r2)*(r1-r2) + (g1-g2)*(g1-g2) + (b1-b2)*(b1-b2));
 }


}