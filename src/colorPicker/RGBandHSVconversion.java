package colorPicker;

import view.InterfaceColor;

/******************************************************
 * @CLASS_TITLE:	RGB AND HSV CONVERTER
 * 
 * @Description: 	Permet de convertir les valeurs du RGB
 * 					en des valeurs HSV et vice versa.
 * 
 ******************************************************/
public class RGBandHSVconversion implements InterfaceColor{


	/******************************************************
	 * @Titre:			RGB AND CMYK CONVERTER CONSTRUCTER	
	 ******************************************************/
	public RGBandHSVconversion(){

	}

	/******************************************************
	 * @Titre:			CONVERT HSV TO RGB
	 * 
	 * @Resumer:		Conversion HSV vers RGB.
	 * 
	 * @Param: 			hue, saturation, value
	 * @Return:			int[] RGB
	 * 
	 * @Source:			Totalité du code. Certains ajustements apportés.
	 * 					http://www.easyrgb.com/en/math.php
	 * 
	 ******************************************************/
	public int[] convertHSVtoRGB(float _hue, float _saturation, float _value){

		float 
		red = 0.0f,
		green = 0.0f,
		blue = 0.0f,

		var_h = 0.0f,
		var_i = 0.0f,
		var_1 = 0.0f,
		var_2 = 0.0f,
		var_3 = 0.0f,

		hue 		= _hue / SPECTRUM,
		saturation 	= _saturation / MAX,
		value 		= _value / MAX,
		var_rgb 	= value * ALPHA;
		
		int[] rgbArray  = new int[RGB.length()];

		if (saturation != 0.0f){
			var_h = hue * 6.0f;
			
			if (var_h == 6.0f){ 
				var_h = 0.0f; 
			}
			var_i = (int) var_h;          
			var_1 = value * (1.0f - saturation);
			var_2 = value * (1.0f - saturation * (var_h - var_i));
			var_3 = value * (1.0f - saturation * (1.0f - ( var_h - var_i)));

			if      (var_i == 0.0f) { red = value ; green = var_3 ; blue = var_1; }
			else if (var_i == 1.0f) { red = var_2 ; green = value ; blue = var_1; }
			else if (var_i == 2.0f) { red = var_1 ; green = value ; blue = var_3; }
			else if (var_i == 3.0f) { red = var_1 ; green = var_2 ; blue = value; }
			else if (var_i == 4.0f) { red = var_3 ; green = var_1 ; blue = value; }
			else                   	{ red = value ; green = var_1 ; blue = var_2; }
		}
		else{	
			rgbArray[RED]   = (int)var_rgb;
			rgbArray[GREEN] = (int)var_rgb;
			rgbArray[BLUE]  = (int)var_rgb;
			return rgbArray;
		}
		
		rgbArray[RED] 	= (int) (red * ALPHA);
		rgbArray[GREEN] = (int) (green * ALPHA);
		rgbArray[BLUE] 	= (int) (blue * ALPHA);

		return rgbArray;
	}

	/******************************************************
	 * @Titre:			CONVERT RGB TO HSV
	 * 
	 * @Resumer:		Conversion RGB vers HSV.
	 * 
	 * @Param: 			red, green, blue
	 * @Return:			int[] HSV
	 * 
	 * @Source:			Totalité du code. Certains ajustements apportés.
	 * 					http://www.easyrgb.com/en/math.php
	 * 
	 ******************************************************/
	public int[] convertRGBtoHSV(float _red, float _green, float _blue){
		
		float
		red 	= _red / ALPHA,
		green 	= _green / ALPHA,
		blue 	= _blue / ALPHA,
		
		var_r 	= 0.0f,
		var_g 	= 0.0f,
		var_b 	= 0.0f,
		
		min 	= Math.min(Math.min(red, green), blue),
		max 	= Math.max(Math.max(red, green), blue),
		delta 	= max - min,
		
		hue			= 0.0f,
		saturation	= 0.0f,
		value		= max,

		div_g = (1.0f / 3.0f),
		div_b = (2.0f / 3.0f);
		

		if ( delta == 0.0f ){
			hue = saturation = 0.0f;
			value = max;
		}
		else{
			saturation = delta / max;
			var_r = (((max - red) 	/ 6.0f) + (delta / 2.0f)) / delta;
			var_g = (((max - green) / 6.0f) + (delta / 2.0f)) / delta;
			var_b = (((max - blue) 	/ 6.0f) + (delta / 2.0f)) / delta;

			if      ( red == max ) 	 hue = var_b - var_g;
			else if ( green == max ) hue = div_g + var_r - var_b;
			else if ( blue == max )  hue = div_b + var_g - var_r;

			if (hue < 0.0f) hue += 1.0f;
			if (hue > 1.0f) hue -= 1.0f;
		}
		
		int[] arrayHSV 		  = new int[HSV.length()];
		arrayHSV[HUE] 		  = (int) (hue * SPECTRUM);
		arrayHSV[SATURATION]  = (int) (saturation * MAX);
		arrayHSV[VALUE] 	  = (int) (value * MAX);
	
		return arrayHSV;
	}
}
