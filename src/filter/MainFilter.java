/*
   This file is part of j2dcg.
   j2dcg is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.
   j2dcg is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   You should have received a copy of the GNU General Public License
   along with j2dcg; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package filter;

import controller.Coordinates;
import controller.Filter;
import controller.ImageConversionStrategy;
import controller.PaddingStrategy;
import model.*;

/**
 * <p>Title: MeanFilter3x3</p>
 * <p>Description: A mean filter implementation.</p>
 * @version $Revision: 1.11 $
 */
public class MainFilter extends Filter {	
	
	/***************************
	 * Matrice Filtre
	 ***************************/
	private double filterMatrix[][] = null;
	
	/**
	 * Default constructor.
	 * @param paddingStrategy PaddingStrategy used 
	 * @param conversionStrategy ImageConversionStrategy used
	 */
	public MainFilter(PaddingStrategy paddingStrategy, ImageConversionStrategy conversionStrategy) {	
		super(paddingStrategy, conversionStrategy);		
		filterMatrix = new double[3][3];
	}

	/**
	 * Filters an ImageX and returns a ImageDouble.
	 */
	public ImageDouble filterToImageDouble(ImageX image) {
		return filter(conversionStrategy.convert(image));
	}
	
	/**
	 * Filters an ImageDouble and returns a ImageDouble.
	 */	
	public ImageDouble filterToImageDouble(ImageDouble image) {
		return filter(image);
	}
	
	/**
	 * Filters an ImageX and returns an ImageX.
	 */	
	public ImageX filterToImageX(ImageX image) {
		ImageDouble filtered = filter(conversionStrategy.convert(image)); 
		return conversionStrategy.convert(filtered);
	}
	
	/**
	 * Filters an ImageDouble and returns a ImageX.
	 */	
	public ImageX filterToImageX(ImageDouble image) {
		ImageDouble filtered = filter(image); 
		return conversionStrategy.convert(filtered);		
	}
	
	/*
	 * Filter Implementation 
	 */
	private ImageDouble filter(ImageDouble image) {
		
		int imageWidth = image.getImageWidth();
		int imageHeight = image.getImageHeight();
		ImageDouble newImage = new ImageDouble(imageWidth, imageHeight);
		PixelDouble newPixel = null;
		double result = 0; 
	
		for (int x = 0; x < imageWidth; x++) {
			for (int y = 0; y < imageHeight; y++) {
				newPixel = new PixelDouble();
			
				//*******************************
				// RED
				for (int i = 0; i <= 2; i++) {
					for (int j = 0; j <= 2; j++) {
						result += filterMatrix[i][j] * getPaddingStrategy().pixelAt(image, 
																				    x+(i-1), 
																				    y+(j-1)).getRed();
					}
				}
				
				newPixel.setRed(getK(filterMatrix) * result);
				result = 0;
						
				//*******************************
				// Green
				for (int i = 0; i <= 2; i++) {
					for (int j = 0; j <= 2; j++) {
						result += filterMatrix[i][j] * getPaddingStrategy().pixelAt(image, 
																					x+(i-1), 
																					y+(j-1)).getGreen();
					}
				}
				
				newPixel.setGreen(getK(filterMatrix) * result);
				result = 0;
							  
				//*******************************
				// Blue
				for (int i = 0; i <= 2; i++) {
					for (int j = 0; j <= 2; j++) {
						result += filterMatrix[i][j] * getPaddingStrategy().pixelAt(image,
																					x+(i-1), 
																					y+(j-1)).getBlue();
					}
				}
				
				newPixel.setBlue(getK(filterMatrix) * result);
				result = 0;
							
				//*******************************
				// Alpha - Untouched in this filter
				newPixel.setAlpha(getPaddingStrategy().pixelAt(image, x,y).getAlpha());
							 
				//*******************************
				// Done
				newImage.setPixel(x, y, newPixel);
			}
		}
		
		return newImage;
	}
	
	/******************************************************
	 * @Titre:			UPDATE FILTER MATRIX
	 * 
	 * @Resumer:		Met a jour la matrice du filtre selon
	 * 					les valeurs entrées de l'utilisateur.
	 * 
	 * @Param: 			Coordinates et float (value)	
	 * 
	 ******************************************************/
	public void updateFilterMatrix(Coordinates _coordinates, float _value){
		System.out.println("[" + (_coordinates.getColumn() - 1) + "]"
						 + "[" + (_coordinates.getRow() - 1) 	+ "] = " + _value);

		filterMatrix[_coordinates.getColumn() - 1][_coordinates.getRow() - 1] = _value;
	}
	
	/******************************************************
	 * @Titre:			GET K
	 * 
	 * @Resumer:		Calcul la somme des valeurs de la
	 * 					matrice du filtre et applique ou pas
	 * 					la multiplication 1/k aux couleurs.
	 * 					La somme des valeurs de la matrice
	 * 					du filtre permet de catégoriser le
	 * 					type de filtre.
	 * 
	 * @Parametre:		filterMatrix 
	 * @return:			1 ou 1/k
	 * 
	 ******************************************************/
	private double getK(double[][] filterMatrix) {
		
		double k = 0.0;
		for (int i = 0; i < filterMatrix.length; i++) {
			for (int j = 0; j < filterMatrix[0].length; j++) {
				k += filterMatrix[i][j];
			}
		}
		if(k == 0.0) { return 1;}
		else	   	 { return (1.0 / k);}
	}		
}
