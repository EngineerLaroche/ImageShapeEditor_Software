package colorPicker;

import java.awt.image.BufferedImage;

import model.ObserverIF;
import model.Pixel;
import view.ColorSlider;
import view.InterfaceColor;
import view.SliderObserver;

/******************************************************
 * @CLASS_TITLE:	YCBCRColorMediator
 * 
 * @Description: 	C'est ici que le calcul des coefficients
 * 					Y, Cb et Cr est effectué. L'appel de la
 * 					classe Conversion et Pixel est utilisée
 * 					dans le but de mettre à jour l'affichage
 * 					des couleurs slont les manipulations de
 * 					l'utilisateur.
 * 
 ******************************************************/
public class YCBCRColorMediator extends Object implements SliderObserver, ObserverIF, InterfaceColor{

	/***************************
	 * Classes instanciees
	 ***************************/
	private Pixel pixel = null;
	private ColorDialogResult result = null;
	private RGBandYCBCRconversion conversion = null;

	/***************************
	 * BufferedImage Y Cb Cr
	 ***************************/
	private BufferedImage 
	yImage = null,
	cbImage = null,
	crImage = null;

	/***************************
	 * ColorSlider Y Cb Cr
	 ***************************/
	private ColorSlider 
	yCS = null,
	cbCS = null,
	crCS = null;

	/***************************
	 * Integer Y Cb Cr
	 ***************************/
	private int
	imageWidths = 0, 
	imageHeights = 0,

	y = 0,
	cb = 0,
	cr = 0,

	red = 0,
	blue = 0,
	green = 0;	

	/******************************************************
	 * @Titre:			YCbCr COLOR MEDIATOR CONSTRUCTER
	 * 
	 * @Resumer:		Le constructeur initialise les
	 * 					variables du RGB et déclanche le
	 * 					démarrage de calcul des couleurs.
	 * 
	 * @Param: 			ColorDialogResult, imageWidths et imageHeights
	 * 
	 ******************************************************/
	public YCBCRColorMediator(ColorDialogResult _result, int _imageWidths, int _imageHeights){

		_result.addObserver(this);

		this.result = 		_result;
		this.imageWidths = 	_imageWidths;
		this.imageHeights = _imageHeights;

		this.red = 			_result.getPixel().getRed();
		this.green = 		_result.getPixel().getGreen();
		this.blue = 		_result.getPixel().getBlue();
		
		conversion =		new RGBandYCBCRconversion();
		yImage = 			new BufferedImage(_imageWidths, _imageHeights, BufferedImage.TYPE_INT_ARGB);
		cbImage = 			new BufferedImage(_imageWidths, _imageHeights, BufferedImage.TYPE_INT_ARGB);
		crImage = 			new BufferedImage(_imageWidths, _imageHeights, BufferedImage.TYPE_INT_ARGB);
		
		//Conversion vers YCbCr et prepare Y, Cb et Cr 
		setYCBCR(y, cb, cr);
				
		computeYImage(y, cb, cr);
		computeCbImage(y, cb, cr);
		computeCrImage(y, cb, cr);
	}
	
	/*******************************************************
	 * @Titre:	UPDATE
	 * 
	 * @Param:	ColorSlider, int
	 ******************************************************/
	public void update(ColorSlider cs, int v) {

		boolean 
		updateY = 	false,
		updateCB = 	false,
		updateCR = 	false;

		if (cs == yCS && v != y) {
			this.y = v;
			updateCB = updateCR = true;
		}
		if (cs == cbCS && v != cb) {
			this.cb = v;
			updateY = updateCR = true;
		}
		if (cs == crCS && v != cr) {
			this.cr = v;
			updateY = updateCB = true;
		}
		if (updateY) {
			computeYImage(y, cb, cr);
		}
		if (updateCB) {
			computeCbImage(y, cb, cr);
		}
		if (updateCR) {
			computeCrImage(y, cb, cr);
		}

		//Va convertir HSV à RGB et mettre a jour le pixel
		updatePixel(y, cb, cr);
		result.setPixel(pixel);
	}
	
	/******************************************************
	 * @Titre:			COMPUTE Y IMAGE
	 * 
	 * @Resumer:		Mise à jour du pixel pour ensuite
	 * 					calculer la couleur à afficher selon
	 * 					le déplacement du slider. La couleur
	 * 					traité ici est le Y (luminance). 
	 * 
	 * @Param: 			y, cb, cr	
	 * 
	 ******************************************************/
	private void computeYImage(int _y, int _cb, int _cr) {
		
		for (int i = 0; i < imageWidths; ++i) {

			//Calcul gradation hue et mise a jour du pixel
			updatePixel(getGradation(i, imageWidths, ALPHA), _cb, _cr);

			for (int j = 0; j < imageHeights; ++j) {
				yImage.setRGB(i, j, pixel.getARGB());
			}
		}
		if (yCS != null) {
			yCS.update(yImage);
		}
	}

	/******************************************************
	 * @Titre:			COMPUTE Cb IMAGE
	 * 
	 * @Resumer:		Mise à jour du pixel pour ensuite
	 * 					calculer la couleur à afficher selon
	 * 					le déplacement du slider. La couleur
	 * 					traité ici est le Cb (chrominance). 
	 * 
	 * @Param: 			y, cb, cr
	 * 
	 ******************************************************/
	private void computeCbImage(int _y, int _cb, int _cr) {
		
		for (int i = 0; i < imageWidths; ++i) {

			//Calcul gradation hue et mise a jour du pixel
			updatePixel(_y, getGradation(i, imageWidths, ALPHA), _cr);

			for (int j = 0; j < imageHeights; ++j) {
				cbImage.setRGB(i, j, pixel.getARGB());
			}
		}
		if (cbCS != null) {
			cbCS.update(cbImage);
		}
	}

	/******************************************************
	 * @Titre:			COMPUTE Cr IMAGE
	 * 
	 * @Resumer:		Mise à jour du pixel pour ensuite
	 * 					calculer la couleur à afficher selon
	 * 					le déplacement du slider. La couleur
	 * 					traité ici est le Cr (chrominance).
	 * 
	 * @Param: 			y, cb, cr
	 * 
	 ******************************************************/
	private void computeCrImage(int _y, int _cb, int _cr) {
				
		for (int i = 0; i < imageWidths; ++i) {

			//Calcul gradation hue et mise a jour du pixel
			updatePixel(_y, _cb, getGradation(i, imageWidths, ALPHA));

			for (int j = 0; j < imageHeights; ++j) {
				crImage.setRGB(i, j, pixel.getARGB());
			}
		}
		if (crCS != null) {
			crCS.update(crImage);
		}
	}
	
	/******************************************************
	 * @Titre:			UPDATE PIXEL
	 * 
	 * @Resumer:		Appel de la fonction de conversion
	 * 					du YCbCr vers RGB pour ensuite mettre
	 * 					à jour le pixel. Cette méthode permet
	 * 					de réduire la duplication de code.
	 * 
	 * @Param: 			Y, Cb, Cr
	 * @Complexity: 	
	 * 
	 ******************************************************/
	private void updatePixel(float _y, float _cb, float _cr){

		//Conversion YCbCr a RGB
		int[] arrayRGB = conversion.convertYCBCRtoRGB(_y, _cb, _cr);
		this.red = 		 arrayRGB[RED];
		this.green = 	 arrayRGB[GREEN];
		this.blue = 	 arrayRGB[BLUE];

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
	 * 
	 * @Param: 			v1, v2, v3
	 * @Return:			int
	 * 
	 ******************************************************/
	private int getGradation(int v1, int v2, int v3){
		return (int) (((double) v1 / (double) v2) * (double) v3);
	}
	
	/******************************************************
	 * @Titre:			SET YCbCr
	 * 
	 * @Resumer:		Appel de la fonction de conversion
	 * 					du RGB vers YCbCr pour ensuite
	 * 					initialiser les valeurs Y,Cb et Cr qui sont 
	 * 					utilisées un peu partout dans la classe. 
	 * 					Cette méthode permet aussi de réduire 
	 *					la duplication de code.
	 * 
	 * @Param: 			red, green, blue	
	 * 
	 ******************************************************/
	private void setYCBCR(int _red, int _green, int _blue){

		//Conversion RGB a YCbCr
		int[] arrayHSV = 	conversion.convertRGBtoYCBCR(_red, _green, _blue);
		this.y = 			arrayHSV[HUE];
		this.cb = 			arrayHSV[SATURATION];
		this.cr = 			arrayHSV[VALUE];
	}

	/******************************************************
	 * @Titre:	SET COLOR SLIDER Y
	 * @Param:	ColorSlider 
	 ******************************************************/
	public void setYCS(ColorSlider _colorSlider){
		this.yCS = _colorSlider;
		_colorSlider.addObserver(this);
	}
	/******************************************************
	 * @Titre:	SET COLOR SLIDER Cb
	 * @Param:	ColorSlider 
	 ******************************************************/
	public void setCbCS(ColorSlider _colorSlider){
		this.cbCS = _colorSlider;
		_colorSlider.addObserver(this);
	}
	/******************************************************
	 * @Titre:	SET COLOR SLIDER Cr
	 * @Param:	ColorSlider 
	 ******************************************************/
	public void setCrCs(ColorSlider _colorSlider){
		this.crCS = _colorSlider;
		_colorSlider.addObserver(this);
	}

	/********************************************************
	 * @Titre:	GET Y
	 * @Return:	int Y
	 ******************************************************/
	public int getY(){
		return y;
	}
	/******************************************************
	 * @Titre:	GET Cb
	 * @Return:	int Cb
	 ******************************************************/
	public int getCb(){
		return cb;
	}
	/******************************************************
	 * @Titre:	GET Cr
	 * @Return:	int Cr
	 ******************************************************/
	public int getCr(){
		return cr;
	}

	/*******************************************************
	 * @Titre:	GET Y IMAGE
	 * @Return:	BufferedImage yImage
	 ******************************************************/
	public BufferedImage getYImage(){
		return yImage;
	}
	/******************************************************
	 * @Titre:	GET CB IMAGE
	 * @Return:	BufferedImage cbImage
	 ******************************************************/
	public BufferedImage getCbImage(){
		return cbImage;
	}
	/******************************************************
	 * @Titre:	GET CR IMAGE
	 * @Return:	BufferedImage crImage
	 ******************************************************/
	public BufferedImage getCrImage(){
		return crImage;
	}

	/*******************************************************
	 * @Titre:	UPDATE
	 ******************************************************/
	public void update() {

		if(pixel.getARGB() == result.getPixel().getARGB()) return;

		this.red = 		result.getPixel().getRed();
		this.green = 	result.getPixel().getGreen();
		this.blue = 	result.getPixel().getBlue();

		//Conversion vers YCbCr et prepare Y, Cb et Cr 
		setYCBCR(red, green, blue);

		yCS.setValue(y);
		cbCS.setValue(cb);
		crCS.setValue(cr);

		computeYImage(y, cb, cr);
		computeCbImage(y, cb, cr);
		computeCrImage(y, cb, cr);
	}
}
