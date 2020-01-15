package filter;

import controller.PaddingStrategy;
import model.ImageDouble;
import model.ImageX;
import model.Pixel;
import model.PixelDouble;

public class PaddingCircularStrategy extends PaddingStrategy {
	
	private Pixel circularPixel = null;
	private PixelDouble circularPixelDouble = null;
	
	@Override
	public Pixel pixelAt(ImageX image, int x, int y) {
		
		circularPixel = new Pixel(0,0,0,0);
		
		if ((x >= 0) && (x < image.getImageWidth()) &&
				(y >= 0) && (y < image.getImageHeight())) {
			circularPixel = image.getPixel(x, y);
			return circularPixel;
		} else if ((x < 0) && (y >= 0) && (y < image.getImageHeight())) { //cot� gauche
			//System.out.println("Cot� gauche : " + x + " " + y);
			circularPixel = image.getPixel(image.getImageWidth() - 1, y);
			return circularPixel;
		} else if ((x < 0) && (y == -1)) { //coin haut gauche
			//System.out.println("Coin haut gauche : " + x + " " + y);
			circularPixel = image.getPixel(image.getImageWidth() - 1, image.getImageHeight() - 1);
			return circularPixel;
		} else if ((x == image.getImageWidth()) && (y == -1)) { //coin haut droite
			//System.out.println("Coin haut droite : " + x + " " + y);
			circularPixel = image.getPixel(0, image.getImageHeight() - 1);
			return circularPixel;
		} else if (x == image.getImageWidth() && (y >= 0) && (y < image.getImageHeight())) { //c�t� droite
			//System.out.println("Cot� droite : " + x + " " + y);
			circularPixel = image.getPixel(0, y);
			return circularPixel;
		} else if (x == -1 && (y == image.getImageHeight())) { //coin bas gauche
			//System.out.println("Coin bas gauche : " + x + " " + y);
			circularPixel = image.getPixel(image.getImageWidth() - 1, 0);
			return circularPixel;
		} else if (x == image.getImageWidth() && (y == image.getImageHeight())) {//coin bas droite
			//System.out.println("Coin bas droite : " + x + " " + y);
			circularPixel = image.getPixel(0, 0);
			return circularPixel;
		} else if ((y == -1) && (x >= 0) && (x < image.getImageWidth())) { //c�t� haut
			//System.out.println("Cot� haut : " + x + " " + y);
			circularPixel = image.getPixel(x, image.getImageHeight() - 1);
			return circularPixel;
		} else if ((y == image.getImageHeight()) && (x >= 0) && (x < image.getImageWidth())) { //c�t� bas
			//System.out.println("Cot� bas : " + x + " " + y);
			circularPixel = image.getPixel(x, 0);
			return circularPixel;
		}
		//System.out.println("sortie : " + x + " " + y);
		return circularPixel;
	}

	@Override
	public PixelDouble pixelAt(ImageDouble image, int x, int y) {
		
		circularPixelDouble = new PixelDouble(0,0,0,0);
		
		if ((x >= 0) && (x < image.getImageWidth()) &&
				(y >= 0) && (y < image.getImageHeight())) {
			circularPixelDouble = image.getPixel(x, y);
			return circularPixelDouble;
		} else if ((x < 0) && (y >= 0) && (y < image.getImageHeight())) { //cot� gauche
			//System.out.println("Cot� gauche : " + x + " " + y);
			circularPixelDouble = image.getPixel(image.getImageWidth() - 1, y);
			return circularPixelDouble;
		} else if ((x < 0) && (y == -1)) { //coin haut gauche
			//System.out.println("Coin haut gauche : " + x + " " + y);
			circularPixelDouble = image.getPixel(image.getImageWidth() - 1, image.getImageHeight() - 1);
			return circularPixelDouble;
		} else if ((x == image.getImageWidth()) && (y == -1)) { //coin haut droite
			//System.out.println("Coin haut droite : " + x + " " + y);
			circularPixelDouble = image.getPixel(0, image.getImageHeight() - 1);
			return circularPixelDouble;
		} else if (x == image.getImageWidth() && (y >= 0) && (y < image.getImageHeight())) { //c�t� droite
			//System.out.println("Cot� droite : " + x + " " + y);
			circularPixelDouble = image.getPixel(0, y);
			return circularPixelDouble;
		} else if (x == -1 && (y == image.getImageHeight())) { //coin bas gauche
			//System.out.println("Coin bas gauche : " + x + " " + y);
			circularPixelDouble = image.getPixel(image.getImageWidth() - 1, 0);
			return circularPixelDouble;
		} else if (x == image.getImageWidth() && (y == image.getImageHeight())) {//coin bas droite
			//System.out.println("Coin bas droite : " + x + " " + y);
			circularPixelDouble = image.getPixel(0, 0);
			return circularPixelDouble;
		} else if ((y == -1) && (x >= 0) && (x < image.getImageWidth())) { //c�t� haut
			//System.out.println("Cot� haut : " + x + " " + y);
			circularPixelDouble = image.getPixel(x, image.getImageHeight() - 1);
			return circularPixelDouble;
		} else if ((y == image.getImageHeight()) && (x >= 0) && (x < image.getImageWidth())) { //c�t� bas
			//System.out.println("Cot� bas : " + x + " " + y);
			circularPixelDouble = image.getPixel(x, 0);
			return circularPixelDouble;
		}
		//System.out.println("sortie : " + x + " " + y);
		return circularPixelDouble;
	}  
}