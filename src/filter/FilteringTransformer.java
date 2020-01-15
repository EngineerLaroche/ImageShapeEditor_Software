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

import java.awt.event.MouseEvent;
import java.util.List;

import controller.AbstractTransformer;
import controller.Coordinates;
import controller.PaddingZeroStrategy;
import controller.Selector;
import model.ImageDouble;
import model.ImageX;
import model.KernelModel;
import model.Shape;

/**
 * 
 * <p>Title: FilteringTransformer</p>
 * <p>Description: ... (AbstractTransformer)</p>
 * @version $Revision: 1.6 $
 */
public class FilteringTransformer extends AbstractTransformer{

	private String _border;

	/***************************
	 * Classe Instanciee
	 ***************************/
	MainFilter filter = new MainFilter(new PaddingZeroStrategy(), new ImageClampStrategy());


	/******************************************************
	 * @Titre:			Update Kernel
	 * 
	 * @Resumer:		Envoit les parametres qui permet de 
	 * 					mettre a jour la matrice du filtre
	 * 					selon les valeurs entrées de 
	 * 					l'utilisateur. (custom matrix)
	 * 
	 * @Param: 			Coordinates et float (value)		
	 * 
	 ******************************************************/
	public void updateKernel(Coordinates _coordinates, float _value) {
		filter.updateFilterMatrix(_coordinates, _value);
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	protected boolean mouseClicked(MouseEvent e){
		List<?> intersectedObjects = Selector.getDocumentObjectsAtLocation(e.getPoint());
		if (!intersectedObjects.isEmpty()) {			
			Shape shape = (Shape)intersectedObjects.get(0);			
			if (shape instanceof ImageX) {				
				ImageX currentImage = (ImageX)shape;
				ImageDouble filteredImage = filter.filterToImageDouble(currentImage);
				ImageX filteredDisplayableImage = filter.getImageConversionStrategy().convert(filteredImage);
				currentImage.beginPixelUpdate();

				if (this.get_border() == "None") {
					for (int i = 1; i < currentImage.getImageWidth() - 1; ++i) {
						for (int j = 1; j < currentImage.getImageHeight() - 1; ++j) {
							currentImage.setPixel(i, j, filteredDisplayableImage.getPixelInt(i, j));		
						}
					}
				} else {
					for (int i = 0; i < currentImage.getImageWidth(); ++i) {
						for (int j = 0; j < currentImage.getImageHeight(); ++j) {
							currentImage.setPixel(i, j, filteredDisplayableImage.getPixelInt(i, j));		
						}
					}
				}
				
				currentImage.endPixelUpdate();
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see controller.AbstractTransformer#getID()
	 */
	public int getID() { return ID_FILTER; }

	/******************************************************
	 * @Titre:			SET BORDER
	 * 
	 * @Resumer:		Selon le type de bordure sélectionnée
	 * 					par l'utilisateur, on pointe vers la 
	 * 					bonne classe de détection de bordure.
	 * 
	 * @Param: 			String border		
	 * 
	 ******************************************************/
	public void setBorder(String border) {
		_border = border;
		System.out.println("Border: " + border);	

		int index = 0;
		for (int i = 0; i < KernelModel.HANDLING_BORDER_ARRAY.length; ++i) {
			if (border.equals(KernelModel.HANDLING_BORDER_ARRAY[i])) {
				index = i;	
			}
		}

		switch (index){

		case 0: // Zero Border
			filter.setPaddingStrategy(new PaddingZeroStrategy());
			break;

		case 1: // None Border
			System.out.println("\n*** None Border not configured ***\n");
			break;

		case 2: // Copy Border
			filter.setPaddingStrategy(new PaddingCopyStrategy());
			break;

		case 3: // Mirror Border
			filter.setPaddingStrategy(new PaddingMirrorStrategy());
			break;

		case 4: // Circular Border
			filter.setPaddingStrategy(new PaddingCircularStrategy());
			break;

		default:
			break;
		}
	}
	
	public String get_border() {
		return _border;
	}

	/******************************************************
	 * @Titre:			SET CLAMP
	 * 
	 * @Resumer:		Selon le type d'intervalle choisi
	 * 					par l'utilisateur, on pointe vers la 
	 * 					bonne classe de détection d'intervalle.
	 * 
	 * @Param: 			String border		
	 * 
	 ******************************************************/
	public void setClamp(String intervalle) {
		System.out.println("Intervalle: " + intervalle);

		int index = 0;
		for (int i = 0; i < KernelModel.CLAMP_ARRAY.length; ++i) {
			if (intervalle.equals(KernelModel.CLAMP_ARRAY[i])) {
				index = i;	
			}
		}

		switch (index){

		case 0: // Clamp 0...255
			filter.setImageConversionStrategy(new ImageClampStrategy());
			break;

		case 1: 
			System.out.println("\n*** Absolute Normalizing to 255 Strategy not configured ***\n");
			break;

		case 2: // Absolute Normalizing 0 to 255
			filter.setImageConversionStrategy(new ImageAbsNormalize_0_255());
			break;

		case 3: // Normalizing 0 to 255
			filter.setImageConversionStrategy(new ImageNormalize_0_255());
			break;

		default:
			break;
		}
	}
}
