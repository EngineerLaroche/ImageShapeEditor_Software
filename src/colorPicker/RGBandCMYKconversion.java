package colorPicker;

import view.InterfaceColor;

/******************************************************
 * @CLASS_TITLE:	RGB AND CMYK CONVERTER
 * 
 * @Description: 	Permet de convertir les valeurs du RGB
 * 					en des valeurs CMYK et vice versa.
 * 
 ******************************************************/
public class RGBandCMYKconversion implements InterfaceColor{


	/******************************************************
	 * @Titre:			RGB AND CMYK CONVERSION CONSTRUCTER
	 ******************************************************/
	public void RGBandCYMKconversion(){
		
	}

	/******************************************************
	 * @Titre:			CONVERT RGB TO CMYK
	 * 
	 * @Resumer:		Conversion RGB vers CMYK.
	 * 
	 * @Param: 			red, green, blue
	 * @Return:			int[] CMYK
	 * 
	 * @Source:			Inspiration sur la Conversion et l'utilisation du float
	 * 					https://imagej.nih.gov/ij/plugins/cmyk/RGB_to_CMYK.java	
	 * 
	 ******************************************************/
	public int[] convertRGBtoCMYK(float red, float green, float blue){
		
		float c = 1.0f - red / ALPHA;
		float m = 1.0f - green / ALPHA;
		float y = 1.0f - blue / ALPHA;
		float k = Math.min(c, Math.min(m, y));

		if (k >= 1.0f)
			c = m = y = 0.0f;
		else {
			float s = 1.0f - k;
			c = (c - k) / s;
			m = (m - k) / s;
			y = (y - k) / s;
		}

		int[] arrayCMYK = 	 new int[CMYK.length()];
		arrayCMYK[CYAN] = 	 (int)(c * ALPHA);
		arrayCMYK[MAGENTA] = (int)(m * ALPHA);
		arrayCMYK[YELLOW] =  (int)(y * ALPHA);
		arrayCMYK[BLACK] = 	 (int)(k * ALPHA);

		return arrayCMYK;
	}

	/******************************************************
	 * @Titre:			CONVERT CMYK TO RGB
	 * 
	 * @Resumer:		Conversion CMYK vers RGB.
	 * 
	 * @Param: 			cyan, magenta, yellow, black
	 * @return:			int[] RGB
	 * 
	 * @Source:			Inspiration sur la Conversion
	 * 					https://www.openprocessing.org/sketch/46231#
	 * 
	 ******************************************************/
	public int[] convertCMYKtoRGB(float cyan, float magenta, float yellow, float black){

		float c = cyan / ALPHA;
		float m = magenta / ALPHA;
		float y = yellow / ALPHA;
		float k = black / ALPHA;

		float s = 1.0f - k;
		c = k + c * s;
		m = k + m * s;
		y = k + y * s;

		int[] arrayRGB = 	new int[RGB.length()];
		arrayRGB[RED] = 	(int)((1.0f - c) * ALPHA);
		arrayRGB[GREEN] = 	(int)((1.0f - m) * ALPHA);
		arrayRGB[BLUE] = 	(int)((1.0f - y) * ALPHA);

		return arrayRGB;
	}
}
