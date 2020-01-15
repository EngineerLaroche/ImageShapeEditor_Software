package filter;

import controller.ImageConversionStrategy;
import model.ImageDouble;
import model.ImageX;
import model.Pixel;
import model.PixelDouble;

public class ImageNormalize_0_255 extends ImageConversionStrategy{


	/******************************************************
	 * @Titre:			Convert
	 * 
	 * @Resumer:		Converts an ImageDouble to an ImageX 
	 * 					using a Normalizing strategy (0-255).
	 * 
	 * @Param: 			ImageDouble
	 * 
	 ******************************************************/
	public ImageX convert(ImageDouble image) {

		double rgb = 0.0;
		int imageWidth = image.getImageWidth();
		int imageHeight = image.getImageHeight();
		ImageX newImage = new ImageX(0, 0, imageWidth, imageHeight);
		PixelDouble curPixelDouble = null;
		newImage.beginPixelUpdate();
		
		for (int x = 0; x < imageWidth; x++) {
			for (int y = 0; y < imageHeight; y++) {
				
				curPixelDouble = image.getPixel(x,y);
				rgb = curPixelDouble.getRed() + curPixelDouble.getGreen() + curPixelDouble.getBlue();
				newImage.setPixel(x, y, new Pixel((int)(norm0To255(curPixelDouble.getRed(),  rgb)),
												  (int)(norm0To255(curPixelDouble.getGreen(),rgb)),
												  (int)(norm0To255(curPixelDouble.getBlue(), rgb)),
												  (int)(curPixelDouble.getAlpha())));
			}
		}
		newImage.endPixelUpdate();
		return newImage;
	}

	/******************************************************
	 * @Titre:			Normalizing 0 to 255
	 * 
	 * @Resumer:		Pour chaque valeur (R,G,B) reçu en
	 * 					paramètre, on multiplie avec alpha
	 * 					et on normalise en divisant par la
	 * 					somme des valeurs RGB.
	 * 
	 * @Param: 			Color value, RGB sum
	 * 
	 * @Source:			http://www.aishack.in/tutorials/normalized-rgb/
	 * 
	 ******************************************************/
	private double norm0To255(double value, double rgb) {	
		return  (255.0 * value / rgb);
	}
}
