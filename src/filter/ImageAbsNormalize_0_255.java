package filter;

import controller.ImageConversionStrategy;
import model.ImageDouble;
import model.ImageX;
import model.Pixel;
import model.PixelDouble;

public class ImageAbsNormalize_0_255 extends ImageConversionStrategy{
	

	/******************************************************
	 * @Titre:			Convert
	 * 
	 * @Resumer:		Converts an ImageDouble to an ImageX 
	 * 					using a ABS Normalizing strategy (0-255).
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
				
				newImage.setPixel(x, y, new Pixel((int)(absNorm0To255(curPixelDouble.getRed(), 	rgb)),
												  (int)(absNorm0To255(curPixelDouble.getGreen(),rgb)),
												  (int)(absNorm0To255(curPixelDouble.getBlue(), rgb)),
												  (int)(curPixelDouble.getAlpha())));
			}
		}
		newImage.endPixelUpdate();
		return newImage;
	}

	/******************************************************
	 * @Titre:			Absolute Normalizing 0 to 255
	 * 
	 * @Resumer:		Pour chaque valeur (R,G,B) reçu en
	 * 					paramètre, on s'assure de garder les
	 * 					valeurs R,G,B en valeur absolue, 
	 * 					on les multiplient avec alpha et
	 * 					on normalise en divisant par la
	 * 					somme des valeurs RGB.
	 * 
	 * @Param: 			Color value, RGB sum
	 * 
	 * @Source:			http://www.aishack.in/tutorials/normalized-rgb/
	 * 
	 ******************************************************/
	private double absNorm0To255(double value, double rgb) {
//		if 		(value < 0)		{value = (double) Math.abs(value);}
//		else if (value > 255)	{value = 255.0;}
		return  (255.0 * (double) Math.abs(value) / rgb);
	}
}
