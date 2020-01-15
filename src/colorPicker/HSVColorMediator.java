package colorPicker;

import java.awt.image.BufferedImage;

import model.ObserverIF;
import model.Pixel;
import view.ColorSlider;
import view.InterfaceColor;
import view.SliderObserver;

/******************************************************
 * @CLASS_TITLE:	HSVColorMediator
 * 
 * @Description: 	C'est ici que le calcul des coefficients
 * 					H,S et V est effectué. L'appel de la
 * 					classe Conversion et Pixel est utilisée
 * 					dans le but de mettre à jour l'affichage
 * 					des couleurs slont les manipulations de
 * 					l'utilisateur.
 * 
 ******************************************************/
public class HSVColorMediator extends Object implements SliderObserver, ObserverIF, InterfaceColor{


	/***************************
	 * Classes instanciees
	 ***************************/
	private Pixel pixel = 						null;
	private ColorDialogResult result = 			null;
	private RGBandHSVconversion conversion = 	null;

	/***************************
	 * BufferedImage HSV
	 ***************************/
	private BufferedImage 
	hueImage = 			null,
	saturationImage = 	null,
	valueImage = 		null;

	/***************************
	 * ColorSlider HSV
	 ***************************/
	private ColorSlider 
	hueCS = 		null,
	saturationCS = 	null,
	valueCS = 		null;

	/***************************
	 * Integer HSV
	 ***************************/
	private int
	imageWidths = 	0, 
	imageHeights = 	0,
	approximation = 0,

	hue = 			0,
	saturation = 	0,
	value = 		0,

	red = 			0,
	blue = 			0,
	green = 		0;	


	/******************************************************
	 * @Titre:			HSV COLOR MEDIATOR CONSTRUCTER
	 * 
	 * @Resumer:		Le constructeur initialise les
	 * 					variables du RGB et déclanche le
	 * 					démarrage de calcul des couleurs.
	 * 
	 * @Param: 			ColorDialogResult, imageWidths et imageHeights
	 * @Complexity: 	O(1)
	 * 
	 ******************************************************/
	public HSVColorMediator(ColorDialogResult _result, int _imageWidths, int _imageHeights){

		_result.addObserver(this);

		this.result = 		_result;
		this.imageWidths = 	_imageWidths;
		this.imageHeights = _imageHeights;

		this.red = 			_result.getPixel().getRed();
		this.green = 		_result.getPixel().getGreen();
		this.blue = 		_result.getPixel().getBlue();

		conversion = 		new RGBandHSVconversion();	
		hueImage = 			new BufferedImage(_imageWidths, _imageHeights, BufferedImage.TYPE_INT_ARGB);
		saturationImage = 	new BufferedImage(_imageWidths, _imageHeights, BufferedImage.TYPE_INT_ARGB);
		valueImage = 		new BufferedImage(_imageWidths, _imageHeights, BufferedImage.TYPE_INT_ARGB);
		
		//Conversion vers HSV et prepare H, S et V 
		setHSV(red, green, blue);
		
		computeHueImage(hue, saturation, value);
		computeSaturationImage(hue, saturation, value);
		computeValueImage(hue, saturation, value);
	}

	/*******************************************************
	 * @Titre:	UPDATE
	 * 
	 * @Param:	ColorSlider, int
	 ******************************************************/
	public void update(ColorSlider cs, int v) {

		boolean 
		updateHue = 		false,
		updateSaturation = 	false,
		updateValue = 		false;

		if (cs == hueCS && getGradation(SPECTRUM, ALPHA, v) != hue) {
			this.hue = approximation;
			updateSaturation = updateValue = true;
		}
		if (cs == saturationCS && getGradation(MAX, ALPHA, v) != saturation) {
			this.saturation = approximation;
			updateHue = updateValue = true;
		}
		if (cs == valueCS && getGradation(MAX, ALPHA, v) != value) {
			this.value = approximation;
			updateHue = updateSaturation = true;
		}
		if (updateHue) {
			computeHueImage(hue, saturation, value);
		}
		if (updateSaturation) {
			computeSaturationImage(hue, saturation, value);
		}
		if (updateValue) {
			computeValueImage(hue, saturation, value);
		}

		//Va convertir HSV à RGB et mettre a jour le pixel
		updatePixel(hue, saturation, value);
		result.setPixel(pixel);
	}

	/******************************************************
	 * @Titre:			COMPUTE HUE IMAGE
	 * 
	 * @Resumer:		Mise à jour du pixel pour ensuite
	 * 					calculer la valeur à afficher selon
	 * 					le déplacement du slider. La valeur
	 * 					traitée ici est le hue.
	 * 
	 * @Param: 			hue, saturation and value	
	 * 
	 ******************************************************/
	private void computeHueImage(int _hue, int _saturation, int _value) {
		
		for (int i = 0; i < imageWidths; ++i) {

			//Calcul gradation hue et mise a jour du pixel
			updatePixel(getGradation(i, imageWidths, SPECTRUM), _saturation, _value);

			for (int j = 0; j < imageHeights; ++j) {
				hueImage.setRGB(i, j, pixel.getARGB());
			}
		}
		if (hueCS != null) {
			hueCS.update(hueImage);
		}
	}

	/******************************************************
	 * @Titre:			COMPUTE SATURATION IMAGE
	 * 
	 * @Resumer:		Mise à jour du pixel pour ensuite
	 * 					calculer la valeur à afficher selon
	 * 					le déplacement du slider. La valeur
	 * 					traitée ici est la saturation.
	 * 
	 * @Param: 			hue, saturation and value	
	 * 
	 ******************************************************/
	private void computeSaturationImage(int _hue, int _saturation, int _value) {
		
		for (int i = 0; i < imageWidths; ++i) {

			//Calcul gradation saturation et mise a jour du pixel
			updatePixel(_hue, getGradation(i, imageWidths, MAX), _value);

			for (int j = 0; j < imageHeights; ++j) {
				saturationImage.setRGB(i, j, pixel.getARGB());
			}
		}
		if (saturationCS != null) {
			saturationCS.update(saturationImage);
		}
	}

	/******************************************************
	 * @Titre:			COMPUTE VALUE IMAGE
	 * 
	 * @Resumer:		Mise à jour du pixel pour ensuite
	 * 					calculer la valeur à afficher selon
	 * 					le déplacement du slider. La valeur
	 * 					traitée ici est luminosité.
	 * 
	 * @Param: 			hue, saturation and value
	 * 
	 ******************************************************/
	private void computeValueImage(int _hue, int _saturation, int _value) {
		
		for (int i = 0; i < imageWidths; ++i) {

			//Calcul gradation value et mise a jour du pixel
			updatePixel(_hue, _saturation, getGradation(i, imageWidths, MAX));

			for (int j = 0; j < imageHeights; ++j) {
				valueImage.setRGB(i, j, pixel.getARGB());
			}
		}
		if (valueCS != null) {
			valueCS.update(valueImage);
		}
	}

	/******************************************************
	 * @Titre:			UPDATE PIXEL
	 * 
	 * @Resumer:		Appel de la fonction de conversion
	 * 					du HSV vers RGB pour ensuite mettre
	 * 					à jour le pixel. Cette méthode permet
	 * 					de réduire la duplication de code.
	 * 
	 * @Param: 			cyan, magenta, yellow and black
	 * 
	 ******************************************************/
	private void updatePixel(float _hue, float _saturation, float _value){
		
		//Conversion HSV a RGB
		int[] arrayRGB = 	conversion.convertHSVtoRGB(_hue, _saturation, _value);
		this.red = 			arrayRGB[RED];
		this.green = 		arrayRGB[GREEN];
		this.blue = 		arrayRGB[BLUE];

		//Mise a jour du pixel
		pixel = new Pixel(arrayRGB[RED], arrayRGB[GREEN], arrayRGB[BLUE], ALPHA);
	}
	
	/******************************************************
	 * @Titre:			GET GRADATION
	 * 
	 * @Resumer:		Le calcul effectué permet d'afficher
	 * 					le changement de couleur selon la position
	 * 					du slider. Supporte l'affichage des couleurs
	 * 					en dégradé et en fonction de la taille de la boite.
	 * 					La méyhode permet aussi de travailler avec le
	 * 					spectre de couleur basée sur 360 et les deux autres
	 * 					valeurs basée sur 100. 
	 * 
	 * @Param: 			v1, v2, v3
	 * @Return:			int
	 * 
	 ******************************************************/
	private int getGradation(int v1, int v2, int v3){
		return approximation = (int) (((double) v1 / (double) v2) * (double) v3);
	}
	
	/******************************************************
	 * @Titre:			SET HSV
	 * 
	 * @Resumer:		Appel de la fonction de conversion
	 * 					du RGB vers CMYK pour ensuite
	 * 					initialiser les valeurs h,s et v qui sont 
	 * 					utilisées un peu partout dans la classe. 
	 * 					Cette méthode permet aussi de réduire 
	 *					la duplication de code.
	 * 
	 * @Param: 			red, green, blue		
	 * 
	 ******************************************************/
	private void setHSV(int _red, int _green, int _blue){

		//Conversion RGB a HSV
		int[] arrayHSV = 	conversion.convertRGBtoHSV(_red, _green, _blue);
		this.hue = 			arrayHSV[HUE];
		this.saturation = 	arrayHSV[SATURATION];
		this.value = 		arrayHSV[VALUE];
	}

	/******************************************************
	 * @Titre:	SET COLOR SLIDER HUE
	 * @Param:	ColorSlider 
	 ******************************************************/
	public void setHueCS(ColorSlider _colorSlider){
		this.hueCS = _colorSlider;
		_colorSlider.addObserver(this);
	}
	/******************************************************
	 * @Titre:	SET COLOR SLIDER SATURATION
	 * @Param:	ColorSlider 
	 ******************************************************/
	public void setSaturationCS(ColorSlider _colorSlider){
		this.saturationCS = _colorSlider;
		_colorSlider.addObserver(this);
	}
	/******************************************************
	 * @Titre:	SET COLOR SLIDER YVALUE
	 * @Param:	ColorSlider 
	 ******************************************************/
	public void setValueCs(ColorSlider _colorSlider){
		this.valueCS = _colorSlider;
		_colorSlider.addObserver(this);
	}


	/********************************************************
	 * @Titre:	GET HUE
	 * @Return:	int hue
	 ******************************************************/
	public int getHue(){
		return hue;
	}
	/******************************************************
	 * @Titre:	GET SATURATION
	 * @Return:	int saturation
	 ******************************************************/
	public int getSaturation(){
		return saturation;
	}
	/******************************************************
	 * @Titre:	GET VALUE
	 * @Return:	int value
	 ******************************************************/
	public int getValue(){
		return value;
	}


	/*******************************************************
	 * @Titre:	GET HUE IMAGE
	 * @Return:	BufferedImage hueImage
	 ******************************************************/
	public BufferedImage getHueImage(){
		return hueImage;
	}
	/******************************************************
	 * @Titre:	GET SATURATION IMAGE
	 * @Return:	BufferedImage saturationImage
	 ******************************************************/
	public BufferedImage getSaturationImage(){
		return saturationImage;
	}
	/******************************************************
	 * @Titre:	GET VALUE IMAGE
	 * @Return:	BufferedImage valueImage
	 ******************************************************/
	public BufferedImage getValueImage(){
		return valueImage;
	}

	/*******************************************************
	 * @Titre:	UPDATE
	 ******************************************************/
	public void update() {
		
		if(pixel.getARGB() == result.getPixel().getARGB()) return;

		this.red = 		result.getPixel().getRed();
		this.green = 	result.getPixel().getGreen();
		this.blue = 	result.getPixel().getBlue();

		//Conversion vers HSV et prepare H, S et V 
		setHSV(red, green, blue);

		hueCS.setValue(getGradation(hue, SPECTRUM, ALPHA));
		saturationCS.setValue(getGradation(saturation, MAX, ALPHA));
		valueCS.setValue(getGradation(value, MAX, ALPHA));

		computeHueImage(hue, saturation, value);
		computeSaturationImage(hue, saturation, value);
		computeValueImage(hue, saturation, value);
	}
}
