
package filter;

import controller.PaddingStrategy;
import model.ImageDouble;
import model.ImageX;
import model.Pixel;
import model.PixelDouble;

public class PaddingCopyStrategy extends PaddingStrategy {
	
	private Pixel copyPixel = null;
	private PixelDouble copyPixelDouble = null;
	
	@Override
	public Pixel pixelAt(ImageX image, int x, int y) {
		
		copyPixel = new Pixel(0,0,0,0);
		
		if ((x >= 0) && (x < image.getImageWidth()) &&
				(y >= 0) && (y < image.getImageHeight())) {
			copyPixel = image.getPixel(x, y);
			return copyPixel;
		} else if ((x < 0) && (y >= 0) && (y < image.getImageHeight())) { //coté gauche
			//System.out.println("Coté gauche : " + x + " " + y);
			copyPixel = image.getPixel(image.getImageWidth() - 1, y);
			return copyPixel;
		} else if ((x < 0) && (y == -1)) { //coin haut gauche
			//System.out.println("Coin haut gauche : " + x + " " + y);
			copyPixel = image.getPixel(image.getImageWidth() - 1, image.getImageHeight() - 1);
			return copyPixel;
		} else if ((x == image.getImageWidth()) && (y == -1)) { //coin haut droite
			//System.out.println("Coin haut droite : " + x + " " + y);
			copyPixel = image.getPixel(0, image.getImageHeight() - 1);
			return copyPixel;
		} else if (x == image.getImageWidth() && (y >= 0) && (y < image.getImageHeight())) { //côté droite
			//System.out.println("Coté droite : " + x + " " + y);
			copyPixel = image.getPixel(0, y);
			return copyPixel;
		} else if (x == -1 && (y == image.getImageHeight())) { //coin bas gauche
			//System.out.println("Coin bas gauche : " + x + " " + y);
			copyPixel = image.getPixel(image.getImageWidth() - 1, 0);
			return copyPixel;
		} else if (x == image.getImageWidth() && (y == image.getImageHeight())) {//coin bas droite
			//System.out.println("Coin bas droite : " + x + " " + y);
			copyPixel = image.getPixel(0, 0);
			return copyPixel;
		} else if ((y == -1) && (x >= 0) && (x < image.getImageWidth())) { //côté haut
			//System.out.println("Coté haut : " + x + " " + y);
			copyPixel = image.getPixel(x, image.getImageHeight() - 1);
			return copyPixel;
		} else if ((y == image.getImageHeight()) && (x >= 0) && (x < image.getImageWidth())) { //côté bas
			//System.out.println("Coté bas : " + x + " " + y);
			copyPixel = image.getPixel(x, 0);
			return copyPixel;
		}
		//System.out.println("sortie : " + x + " " + y);
		return copyPixel;
	}

	@Override
	public PixelDouble pixelAt(ImageDouble image, int x, int y) {
		
		copyPixelDouble = new PixelDouble(0,0,0,0);
		
		if ((x >= 0) && (x < image.getImageWidth()) &&
				(y >= 0) && (y < image.getImageHeight())) {
			copyPixelDouble = image.getPixel(x, y);
			return copyPixelDouble;
		} else if ((x < 0) && (y >= 0) && (y < image.getImageHeight())) { //coté gauche
			//System.out.println("Coté gauche : " + x + " " + y);
			copyPixelDouble = image.getPixel(image.getImageWidth() - 1, y);
			return copyPixelDouble;
		} else if ((x < 0) && (y == -1)) { //coin haut gauche
			//System.out.println("Coin haut gauche : " + x + " " + y);
			copyPixelDouble = image.getPixel(image.getImageWidth() - 1, image.getImageHeight() - 1);
			return copyPixelDouble;
		} else if ((x == image.getImageWidth()) && (y == -1)) { //coin haut droite
			//System.out.println("Coin haut droite : " + x + " " + y);
			copyPixelDouble = image.getPixel(0, image.getImageHeight() - 1);
			return copyPixelDouble;
		} else if (x == image.getImageWidth() && (y >= 0) && (y < image.getImageHeight())) { //côté droite
			//System.out.println("Coté droite : " + x + " " + y);
			copyPixelDouble = image.getPixel(0, y);
			return copyPixelDouble;
		} else if (x == -1 && (y == image.getImageHeight())) { //coin bas gauche
			//System.out.println("Coin bas gauche : " + x + " " + y);
			copyPixelDouble = image.getPixel(image.getImageWidth() - 1, 0);
			return copyPixelDouble;
		} else if (x == image.getImageWidth() && (y == image.getImageHeight())) {//coin bas droite
			//System.out.println("Coin bas droite : " + x + " " + y);
			copyPixelDouble = image.getPixel(0, 0);
			return copyPixelDouble;
		} else if ((y == -1) && (x >= 0) && (x < image.getImageWidth())) { //côté haut
			//System.out.println("Coté haut : " + x + " " + y);
			copyPixelDouble = image.getPixel(x, image.getImageHeight() - 1);
			return copyPixelDouble;
		} else if ((y == image.getImageHeight()) && (x >= 0) && (x < image.getImageWidth())) { //côté bas
			//System.out.println("Coté bas : " + x + " " + y);
			copyPixelDouble = image.getPixel(x, 0);
			return copyPixelDouble;
		}
		//System.out.println("sortie : " + x + " " + y);
		return copyPixelDouble;
	}
}