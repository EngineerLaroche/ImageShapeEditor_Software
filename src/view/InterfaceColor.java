package view;

/**
 * 
 * <p>Title: FillPanel</p>
 * <p>Description: Utilis�e pour indiquer la position 
 * 					des couleurs dans les Arrays.
 * 					L'interface sert surtour � �viter la
 * 					duplication de constantes dans les
 * 					diff�rentes classes.</p>
 * @version $Revision: 1.3 $
 */
public interface InterfaceColor {

	public static final int
	RED = 0,
	GREEN = 1,
	BLUE = 2,
	
	CYAN = 0,
	MAGENTA = 1,
	YELLOW = 2,
	BLACK = 3,
	
	HUE = 0,
	SATURATION = 1,
	VALUE = 2,
	
	LUMA = 0,
	CB = 1,
	CR = 2,
	
	ALPHA = 255,
	SPECTRUM = 360,
	MAX = 100;
	
	public static final String
	RGB = "RGB",
	CMYK = "CMYK",
	HSV = "HSV",
	YCBCR = "YCbCr",
	YCC = "YCC";
	
}
