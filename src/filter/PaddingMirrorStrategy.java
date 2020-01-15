package filter;

import controller.PaddingStrategy;
import model.ImageDouble;
import model.ImageX;
import model.Pixel;
import model.PixelDouble;

public class PaddingMirrorStrategy extends PaddingStrategy{

	private Pixel mirrorPixel = null;
	private PixelDouble mirrorPixelDouble = null;
	
	public Pixel pixelAt(ImageX image, int x, int y) {
		
		mirrorPixel = new Pixel(0,0,0,0);

		if ((x >= 0) && (x < image.getImageWidth()) &&
				(y >= 0) && (y < image.getImageHeight())) {
			mirrorPixel = image.getPixel(x, y);
			return mirrorPixel;
		} else if ((x < 0) && (y >= 0) && (y < image.getImageHeight())) { //coté gauche
			//System.out.println("Coté gauche : " + x + " " + y);
			mirrorPixel = image.getPixel(0, y);
			return mirrorPixel;
		} else if ((x < 0) && (y == -1)) { //coin haut gauche
			//System.out.println("Coin haut gauche : " + x + " " + y);
			mirrorPixel = image.getPixel(0, 0);
			return mirrorPixel;
		} else if ((x == image.getImageWidth()) && (y == -1)) { //coin haut droite
			//System.out.println("Coin haut droite : " + x + " " + y);
			mirrorPixel = image.getPixel(image.getImageWidth() - 1, 0);
			return mirrorPixel;
		} else if (x == image.getImageWidth() && (y >= 0) && (y < image.getImageHeight())) { //côté droite
			//System.out.println("Coté droite : " + x + " " + y);
			mirrorPixel = image.getPixel(image.getImageWidth() - 1, y);
			return mirrorPixel;
		} else if (x == -1 && (y == image.getImageHeight())) { //coin bas gauche
			//System.out.println("Coin bas gauche : " + x + " " + y);
			mirrorPixel = image.getPixel(0, image.getImageHeight() - 1);
			return mirrorPixel;
		} else if (x == image.getImageWidth() && (y == image.getImageHeight())) {//coin bas droite
			//System.out.println("Coin bas droite : " + x + " " + y);
			mirrorPixel = image.getPixel(image.getImageWidth() - 1, image.getImageHeight() - 1);
			return mirrorPixel;
		} else if ((y == -1) && (x >= 0) && (x < image.getImageWidth())) { //côté haut
			//System.out.println("Coté haut : " + x + " " + y);
			mirrorPixel = image.getPixel(x, 0);
			return mirrorPixel;
		} else if ((y == image.getImageHeight()) && (x >= 0) && (x < image.getImageWidth())) { //côté bas
			//System.out.println("Coté bas : " + x + " " + y);
			mirrorPixel = image.getPixel(x, image.getImageHeight() - 1);
			return mirrorPixel;
		}
		//System.out.println("sortie : " + x + " " + y);
		return mirrorPixel;
	}

	public PixelDouble pixelAt(ImageDouble image, int x, int y) {
		
		mirrorPixelDouble = new PixelDouble(0,0,0,0);
		
		if ((x >= 0) && (x < image.getImageWidth()) &&
				(y >= 0) && (y < image.getImageHeight())) {
			mirrorPixelDouble = image.getPixel(x, y);
			return mirrorPixelDouble;
		} else if ((x < 0) && (y >= 0) && (y < image.getImageHeight())) { //coté gauche
			//System.out.println("Coté gauche : " + x + " " + y);
			mirrorPixelDouble = image.getPixel(0, y);
			return mirrorPixelDouble;
		} else if ((x < 0) && (y == -1)) { //coin haut gauche
			//System.out.println("Coin haut gauche : " + x + " " + y);
			mirrorPixelDouble = image.getPixel(0, 0);
			return mirrorPixelDouble;
		} else if ((x == image.getImageWidth()) && (y == -1)) { //coin haut droite
			//System.out.println("Coin haut droite : " + x + " " + y);
			mirrorPixelDouble = image.getPixel(image.getImageWidth() - 1, 0);
			return mirrorPixelDouble;
		} else if (x == image.getImageWidth() && (y >= 0) && (y < image.getImageHeight())) { //côté droite
			//System.out.println("Coté droite : " + x + " " + y);
			mirrorPixelDouble = image.getPixel(image.getImageWidth() - 1, y);
			return mirrorPixelDouble;
		} else if (x == -1 && (y == image.getImageHeight())) { //coin bas gauche
			//System.out.println("Coin bas gauche : " + x + " " + y);
			mirrorPixelDouble = image.getPixel(0, image.getImageHeight() - 1);
			return mirrorPixelDouble;
		} else if (x == image.getImageWidth() && (y == image.getImageHeight())) {//coin bas droite
			//System.out.println("Coin bas droite : " + x + " " + y);
			mirrorPixelDouble = image.getPixel(image.getImageWidth() - 1, image.getImageHeight() - 1);
			return mirrorPixelDouble;
		} else if ((y == -1) && (x >= 0) && (x < image.getImageWidth())) { //côté haut
			//System.out.println("Coté haut : " + x + " " + y);
			mirrorPixelDouble = image.getPixel(x, 0);
			return mirrorPixelDouble;
		} else if ((y == image.getImageHeight()) && (x >= 0) && (x < image.getImageWidth())) { //côté bas
			//System.out.println("Coté bas : " + x + " " + y);
			mirrorPixelDouble = image.getPixel(x, image.getImageHeight() - 1);
			return mirrorPixelDouble;
		}
		//System.out.println("sortie : " + x + " " + y);
		return mirrorPixelDouble;
	}
}
