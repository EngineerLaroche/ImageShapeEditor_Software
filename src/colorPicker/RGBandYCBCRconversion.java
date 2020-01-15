package colorPicker;

import view.InterfaceColor;

/******************************************************
 * @CLASS_TITLE:	RGB AND YCbCr CONVERTER
 * 
 * @Description: 	Permet de convertir les valeurs du RGB
 * 					en des valeurs YCbCr et vice versa.
 * 
 ******************************************************/
public class RGBandYCBCRconversion implements InterfaceColor{

	/******************************************************
	 * @Titre:			RGB AND YCbCr CONVERTER CONSTRUCTER	
	 ******************************************************/
	public RGBandYCBCRconversion(){
		
	}
	
	/******************************************************
	 * @Titre:			CONVERT YCBCR TO RGB
	 * 
	 * @Resumer:		Conversion de YCbCr vers RGB.
	 * 
	 * @Param: 			y, cb, cr
	 * @Return:			int[] RGB
	 * 
	 * @Source:			Seulement pris les chiffres.
	 * 					http://what-when-how.com/introduction-to-video-and-image-processing/conversion-between-rgb-and-yuvycbcr-introduction-to-video-and-image-processing/
	 * 
	 ******************************************************/
	public int[] convertYCBCRtoRGB(float _y, float _cb, float _cr){

		//On s'assure que les valeurs restent entre [0-255]
		float r = Math.max(0, Math.min(_y + 1.403f * (_cr - 128.0f), ALPHA));
		float g = Math.max(0, Math.min(_y - 0.344f * (_cb - 128.0f) - 0.714f * (_cr - 128.0f), ALPHA));
		float b = Math.max(0, Math.min(_y + 1.773f * (_cb - 128.0f), ALPHA));
		
		int[] arrayRGB  = new int[RGB.length()];
		arrayRGB[RED]   = (int) r;
		arrayRGB[GREEN] = (int) g;
		arrayRGB[BLUE]  = (int) b;

		return arrayRGB;
	}
	
	/******************************************************
	 * @Titre:			CONVERT RGB TO YCBCR
	 * 
	 * @Resumer:		Conversion RGB vers YCbCr
	 * 
	 * @Param: 			red, green, blue
	 * @Return:			int[] YCbCr
	 * 
	 * @Source:			Seulement pris les chiffres.
	 * 					http://what-when-how.com/introduction-to-video-and-image-processing/conversion-between-rgb-and-yuvycbcr-introduction-to-video-and-image-processing/
	 * 
	 ******************************************************/
	public int[] convertRGBtoYCBCR(float _red, float _green, float _blue){

		//On s'assure que les valeurs restent entre [0-255]
		float y = Math.max(0, Math.min(0.299f    * _red + 0.587f  * _green + 0.114f  * _blue, ALPHA));
		float cb = Math.max(0, Math.min(-0.1687f * _red - 0.3313f * _green + 0.5f    * _blue + 128.0f, ALPHA));
		float cr = Math.max(0, Math.min(0.5f     * _red - 0.4187f * _green - 0.0813f * _blue + 128.0f, ALPHA));
		
		int[] arrayYCBCR = new int[YCC.length()];
		arrayYCBCR[LUMA] = (int) y;
		arrayYCBCR[CB]   = (int) cb;
		arrayYCBCR[CR]   = (int) cr;
		
		return arrayYCBCR;
	}
}
