package colorPicker;

import java.awt.image.BufferedImage;

import model.ObserverIF;
import model.Pixel;
import view.ColorSlider;
import view.InterfaceColor;
import view.SliderObserver;

/******************************************************
 * @CLASS_TITLE:	CMYK COLOR MEDIATOR
 * 
 * @Description: 	C'est ici que le calcul des coefficients
 * 					C,M,Y et K est effectué. L'appel de la
 * 					classe Conversion et Pixel est utilisée
 * 					dans le but de mettre à jour l'affichage
 * 					des couleurs slont les manipulations de
 * 					l'utilisateur.
 * 
 ******************************************************/
public class CMYKColorMediator extends Object implements SliderObserver, ObserverIF, InterfaceColor{

	/***************************
	 * Classes instanciees
	 ***************************/
	private Pixel pixel = 						null;
	private ColorDialogResult result = 			null;
	private RGBandCMYKconversion conversion = 	null;

	/***************************
	 * BufferedImage CMYK
	 ***************************/
	private BufferedImage 
	cyanImage = 	null,
	magentaImage = 	null,
	yellowImage = 	null,
	blackImage = 	null;

	/***************************
	 * ColorSlider CMYK
	 ***************************/
	private ColorSlider 
	cyanCS = 		null,
	magentaCS = 	null,
	yellowCS = 		null,
	blackCS = 		null;

	/***************************
	 * Variables
	 ***************************/
	private int
	imageWidths = 	0, 
	imageHeights = 	0,

	red = 			0,
	green = 		0,
	blue = 			0,

	cyan = 			0,
	magenta = 		0,
	yellow = 		0,
	black = 		0;


	/******************************************************
	 * @Titre:			CMYK COLOR MEDIATOR CONSTRUCTOR
	 * 
	 * @Resumer:		Le constructeur initialise les
	 * 					variables du RGB et déclanche le
	 * 					démarrage de calcul des couleurs.
	 * 
	 * @Param: 			ColorDialogResult, imageWidths et imageHeights
	 * @Complexity: 	O(1)
	 * 
	 ******************************************************/
	public CMYKColorMediator(ColorDialogResult _result, int _imageWidths, int _imageHeights){

		_result.addObserver(this);

		this.result = 		_result;
		this.imageWidths = 	_imageWidths;
		this.imageHeights = _imageHeights;

		this.red = 			_result.getPixel().getRed();
		this.green = 		_result.getPixel().getGreen();
		this.blue = 		_result.getPixel().getBlue();

		conversion = 		new RGBandCMYKconversion();	
		cyanImage = 		new BufferedImage(_imageWidths, _imageHeights, BufferedImage.TYPE_INT_ARGB);
		magentaImage = 		new BufferedImage(_imageWidths, _imageHeights, BufferedImage.TYPE_INT_ARGB);
		yellowImage = 		new BufferedImage(_imageWidths, _imageHeights, BufferedImage.TYPE_INT_ARGB);
		blackImage = 		new BufferedImage(_imageWidths, _imageHeights, BufferedImage.TYPE_INT_ARGB);
		
		//Conversion vers CMYK et prepare cyan, magenta, yellow et black
		setCMYK(red, green, blue);
				
		computeCyanImage(cyan, magenta, yellow, black);
		computeMagentaImage(cyan, magenta, yellow, black);
		computeYellowImage(cyan, magenta, yellow, black);
		computeBlackImage(cyan, magenta, yellow, black);
	}	

	/*******************************************************
	 * @Titre:	UPDATE
	 * 
	 * @Param:	ColorSlider, int
	 ******************************************************/
	public void update(ColorSlider _colorSlider, int v) {

		boolean 
		updateCyan = 	false,
		updateMagenta = false,
		updateYellow = 	false,
		updateBlack = 	false;

		if (_colorSlider == cyanCS && v != cyan) {
			this.cyan = v;
			updateYellow = updateMagenta = updateBlack = true;
		}
		if (_colorSlider == magentaCS && v != magenta) {       
			this.magenta = v;
			updateCyan = updateYellow = updateBlack = true;
		}
		if (_colorSlider == yellowCS && v != yellow) {         
			this.yellow = v;
			updateCyan = updateMagenta = updateBlack = true;
		}
		if (_colorSlider == blackCS && v!= black){         
			this.black = v;
			updateCyan = updateMagenta = updateYellow = true;
		}
		if (updateCyan) {
			computeCyanImage(cyan, magenta, yellow, black);
		}
		if (updateMagenta) {
			computeMagentaImage(cyan, magenta, yellow, black);
		}
		if (updateYellow) {
			computeYellowImage(cyan, magenta, yellow, black);
		}
		if (updateBlack) {
			computeBlackImage(cyan, magenta, yellow, black);
		}

		//Will convert CMYK to RGB and update the pixel
		updatePixel(cyan, magenta, yellow, black);
		result.setPixel(pixel);
	}

	/******************************************************
	 * @Titre:			COMPUTE CYAN IMAGE
	 * 
	 * @Resumer:		Mise à jour du pixel pour ensuite
	 * 					calculer la couleur à afficher selon
	 * 					le déplacement du slider. La couleur
	 * 					traité ici est le cyan. 
	 * 
	 * @Param: 			cyan, magenta, yellow and black
	 * 
	 ******************************************************/
	private void computeCyanImage(int _cyan, int _magenta, int _yellow, int _black) {

		//Will convert CMYK to RGB and update the pixel
		updatePixel(_cyan, _magenta, _yellow, _black);
		
		for (int i = 0; i < imageWidths; ++i) {
			
			//Calcul gradation cyan
			pixel.setRed(getGradation(_black, i));

			for (int j = 0; j < imageHeights; ++j) {
				cyanImage.setRGB(i, j, pixel.getARGB());
			}
		}
		if (cyanCS != null) {
			cyanCS.update(cyanImage);
		}
	}

	/******************************************************
	 * @Titre:			COMPUTE MAGENTA IMAGE
	 * 
	 * @Resumer:		Mise à jour du pixel pour ensuite
	 * 					calculer la couleur à afficher selon
	 * 					le déplacement du slider. La couleur
	 * 					traité ici est le magenta.
	 * 
	 * @Param: 			cyan, magenta, yellow and black	
	 * 
	 ******************************************************/
	private void computeMagentaImage(float _cyan, float _magenta, float _yellow, float _black) {

		//Will convert CMYK to RGB and update the pixel
		updatePixel(_cyan, _magenta, _yellow, _black);

		for (int i = 0; i < imageWidths; ++i) {
			
			//Calcul gradation magenta
			pixel.setGreen(getGradation(_black, i));

			for (int j = 0; j < imageHeights; ++j) {
				magentaImage.setRGB(i, j, pixel.getARGB());
			}
		}
		if (magentaCS != null) {
			magentaCS.update(magentaImage);
		}
	}

	/******************************************************
	 * @Titre:			COMPUTE YELLOW IMAGE
	 * 
	 * @Resumer:		Mise à jour du pixel pour ensuite
	 * 					calculer la couleur à afficher selon
	 * 					le déplacement du slider. La couleur
	 * 					traité ici est le jaune.
	 * 
	 * @Param: 			cyan, magenta, yellow and black	
	 * 
	 ******************************************************/
	private void computeYellowImage(float _cyan, float _magenta, float _yellow, float _black) {

		//Will convert CMYK to RGB and update the pixel
		updatePixel(_cyan, _magenta, _yellow, _black);

		for (int i = 0; i < imageWidths; ++i) {
			
			//Calcul gradation yellow
			pixel.setBlue(getGradation(_black, i));

			for (int j = 0; j < imageHeights; ++j) {
				yellowImage.setRGB(i, j, pixel.getARGB());
			}
		}
		if (yellowCS != null) {
			yellowCS.update(yellowImage);
		}
	}

	/******************************************************
	 * @Titre:			COMPUTE BLACK IMAGE
	 * 
	 * @Resumer:		Mise à jour du pixel pour ensuite
	 * 					calculer la couleur à afficher selon
	 * 					le déplacement du slider. La couleur
	 * 					traité ici est le noir. Le calcul de
	 * 					la gradation est légèrement différent
	 * 					afin d'afficher dès le départ le dégradé
	 * 					partant du noir vers le blanc.
	 * 
	 * @Param: 			cyan, magenta, yellow and black
	 * @Complexity: 	
	 * 
	 ******************************************************/
	private void computeBlackImage(float _cyan, float _magenta, float _yellow, float _black) {

		//Will convert CMYK to RGB and update the pixel
		updatePixel(_cyan, _magenta, _yellow, _black);

		for (int i = 0; i < imageWidths; ++i) {

			//Calcul gradation black
			int gradation = (int)(ALPHA - ((double) i / (double) imageWidths * ALPHA));

			pixel.setRed(gradation);
			pixel.setGreen(gradation);
			pixel.setBlue(gradation);

			for (int j = 0; j < imageHeights; ++j) {
				blackImage.setRGB(i, j, pixel.getARGB());
			}
		}
		if (blackCS != null) {
			blackCS.update(blackImage);
		}
	}
	
	/******************************************************
	 * @Titre:			UPDATE PIXEL
	 * 
	 * @Resumer:		Appel de la fonction de conversion
	 * 					du CMYK vers RGB pour ensuite mettre
	 * 					à jour le pixel. Cette méthode permet
	 * 					de réduire la duplication de code.
	 * 
	 * @Param: 			cyan, magenta, yellow and black
	 * 
	 ******************************************************/
	private void updatePixel(float _cyan, float _magenta, float _yellow, float _black){
		
		int[] arrayRGB = conversion.convertCMYKtoRGB(_cyan, _magenta, _yellow, _black);
		pixel = new Pixel(arrayRGB[RED], arrayRGB[GREEN], arrayRGB[BLUE], ALPHA);
	}
	
	/******************************************************
	 * @Titre:			GET GRADATION COULEUR
	 * 
	 * @Resumer:		Le calcul effectué permet d'afficher
	 * 					le changement de couleur selon la position
	 * 					du slider. Supporte l'affichage des couleurs
	 * 					en dégradé et en fonction de la taille de la boite. 
	 * 
	 * @Param: 			black, i	
	 * 
	 ******************************************************/
	private int getGradation(float _black, int i){
		return (int) ((double) ((_black - ALPHA) * (i - imageWidths)) / (double) imageWidths);
	}
	
	/******************************************************
	 * @Titre:			SET CMYK
	 * 
	 * @Resumer:		Appel de la fonction de conversion
	 * 					du RGB vers CMYK pour ensuite
	 * 					initialiser les valeurs c,m,y et k qui sont 
	 * 					utilisées un peu partout dans la classe. 
	 * 					Cette méthode permet aussi de réduire 
	 *					la duplication de code.
	 * 
	 * @Param: 			red, green, blue		
	 * 
	 ******************************************************/
	private void setCMYK(int _red, int _green, int _blue){

		int arrayCMYK[] = 	conversion.convertRGBtoCMYK(_red, _green, _blue);
		this.cyan = 		arrayCMYK[CYAN];
		this.magenta = 		arrayCMYK[MAGENTA];
		this.yellow = 		arrayCMYK[YELLOW];
		this.black = 		arrayCMYK[BLACK];
	}

	/******************************************************
	 * @Titre:	SET COLOR SLIDER CYAN
	 * @Param:	ColorSlider 
	 ******************************************************/
	public void setCyanCS(ColorSlider _colorSlider){
		this.cyanCS = _colorSlider;
		_colorSlider.addObserver(this);
	}
	/******************************************************
	 * @Titre:	SET COLOR SLIDER MAGENTA
	 * @Param:	ColorSlider 
	 ******************************************************/
	public void setMagentaCS(ColorSlider _colorSlider){
		this.magentaCS = _colorSlider;
		_colorSlider.addObserver(this);
	}
	/******************************************************
	 * @Titre:	SET COLOR SLIDER YELLOW
	 * @Param:	ColorSlider 
	 ******************************************************/
	public void setYellowCs(ColorSlider _colorSlider){
		this.yellowCS = _colorSlider;
		_colorSlider.addObserver(this);
	}
	/******************************************************
	 * @Titre:	SET COLOR SLIDER BLACK
	 * @Param:	ColorSlider 
	 ******************************************************/
	public void setBlackCs(ColorSlider _colorSlider){
		this.blackCS = _colorSlider;
		_colorSlider.addObserver(this);
	}


	/********************************************************
	 * @Titre:	GET CYAN
	 * @Return:	int Cyan
	 ******************************************************/
	public int getCyan(){
		return cyan;
	}
	/******************************************************
	 * @Titre:	GET MAGENTA
	 * @Return:	int Magenta
	 ******************************************************/
	public int getMagenta(){
		return magenta;
	}
	/******************************************************
	 * @Titre:	GET YELLOW
	 * @Return:	int Yellow
	 ******************************************************/
	public int getYellow(){
		return yellow;
	}
	/******************************************************
	 * @Titre:	GET BLACK
	 * @Return:	int Black
	 ******************************************************/
	public int getBlack(){
		return black;
	}


	/*******************************************************
	 * @Titre:	GET CYAN IMAGE
	 * @Return:	BufferedImage cyanImage
	 ******************************************************/
	public BufferedImage getCyanImage(){
		return cyanImage;
	}
	/******************************************************
	 * @Titre:	GET MAGENTA IMAGE
	 * @Return:	BufferedImage magentaImage
	 ******************************************************/
	public BufferedImage getMagentaImage(){
		return magentaImage;
	}
	/******************************************************
	 * @Titre:	GET YELLOW IMAGE
	 * @Return:	BufferedImage yellowImage
	 ******************************************************/
	public BufferedImage getYellowImage(){
		return yellowImage;
	}
	/******************************************************
	 * @Titre:	GET BLACK IMAGE
	 * @Return:	BufferedImage blackImage
	 ******************************************************/
	public BufferedImage getBlackImage(){
		return blackImage;
	}


	/*******************************************************
	 * @Titre:	UPDATE
	 ******************************************************/
	public void update() {

		//Will convert CMYK to RGB and update the pixel
		updatePixel(cyan, magenta, yellow, black);

		if (pixel.getARGB() == result.getPixel().getARGB()) return;

		this.red = result.getPixel().getRed();
		this.green = result.getPixel().getGreen();
		this.blue = result.getPixel().getBlue();

		//Conversion vers CMYK et prepare cyan, magenta, yellow et black
		setCMYK(red, green, blue);

		cyanCS.setValue(cyan);
		magentaCS.setValue(magenta);
		yellowCS.setValue(yellow);
		blackCS.setValue(black);

		computeCyanImage(cyan, magenta, yellow, black);
		computeMagentaImage(cyan, magenta, yellow, black);
		computeYellowImage(cyan, magenta, yellow, black);
		computeBlackImage(cyan, magenta, yellow, black);
	}
}