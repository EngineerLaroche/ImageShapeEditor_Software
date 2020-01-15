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

package controller;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;
import java.awt.geom.NoninvertibleTransformException;
import java.util.List;
import java.util.Stack;

import controller.AbstractTransformer;
import controller.Selector;
import model.*;
import view.InterfaceColor;

/**
 * <p>Title: ImageLineFiller</p>
 * <p>Description: Image transformer that inverts the row color</p>
 * @version $Revision: 1.12 $
 */
public class ImageFiller extends AbstractTransformer implements InterfaceColor{

	/***************************
	 * Classes instanciées
	 ***************************/
	private ImageX currentImage = null;
	private Pixel verifyColor 	= null;
	private Pixel fillColor 	= new Pixel(0xFF00FFFF);
	private Pixel borderColor 	= new Pixel(0xFFFFFF00);

	/***************************
	 * Prémitive
	 ***************************/
	private boolean floodFill = true;

	/***************************
	 * Variables
	 ***************************/
	private int hueThreshold 		= 1;
	private int saturationThreshold = 2;
	private int valueThreshold 		= 3;


	/**
	 * Creates an ImageLineFiller with default parameters.
	 * Default pixel change color is black.
	 */
	public ImageFiller() { }

	/* (non-Javadoc)
	 * @see controller.AbstractTransformer#getID()
	 */
	public int getID() { return ID_FLOODER; } 

	protected boolean mouseClicked(MouseEvent e){

		List intersectedObjects = Selector.getDocumentObjectsAtLocation(e.getPoint());
		if (!intersectedObjects.isEmpty()) {
			Shape shape = (Shape)intersectedObjects.get(0);
			if (shape instanceof ImageX) {
				currentImage = (ImageX)shape;

				Point pt = e.getPoint();
				Point ptTransformed = new Point();
				
				try {
					shape.inverseTransformPoint(pt, ptTransformed);
				} catch (NoninvertibleTransformException e1) {
					e1.printStackTrace();
					return false;
				}
				ptTransformed.translate(-currentImage.getPosition().x, -currentImage.getPosition().y);
				
				if (0 <= ptTransformed.x && ptTransformed.x < currentImage.getImageWidth() &&
						0 <= ptTransformed.y && ptTransformed.y < currentImage.getImageHeight()) {

					currentImage.beginPixelUpdate();
					verifyColor = currentImage.getPixel(ptTransformed.x, ptTransformed.y);

					if (isFloodFill()){
						floodFillStack(ptTransformed);
					}else{
						boundaryFillStack(ptTransformed);
					}
					currentImage.endPixelUpdate();											 	
					return true;
				}
			}
		}
		return false;
	}


	/******************************************************
	 * @Titre:			FLOOD FILL STACK
	 * 
	 * @Param: 			Point
	 * 
	 ******************************************************/
	private void floodFillStack(Point ptClicked) {

		// Pile qui garde en mémoire les Points de type 4-voisins
		Stack<Point> stack = new Stack<Point>();
		stack.push(ptClicked);

		while (!stack.empty()) {
			Point current = ptClicked;
			current = (Point)stack.pop();

			// Vérifie si le point est plus grand que zéro et plus petit que la taille de l'image			
			if(current.x >= 0 && current.x < currentImage.getImageWidth() &&
					current.y >= 0 && current.y < currentImage.getImageHeight()){

				// On s'assure de peindre les pixels qui n'ont pas la couleur sélectionnée.
				if(!currentImage.getPixel(current.x, current.y).equals(fillColor) &&
						currentImage.getPixel(current.x, current.y).equals(verifyColor)){

					currentImage.setPixel(current.x, current.y, fillColor);
					Point nextLeft 	= new Point (current.x-1, current.y);
					Point nextRight = new Point (current.x+1, current.y);
					Point nextUp 	= new Point (current.x, current.y-1);
					Point nextDown 	= new Point (current.x, current.y+1);

					// Vérifie si le pixel est à l'intérieur ou à l'extérieur de la région.
					if(hsvThreshold()){
						stack.push(nextLeft);
						stack.push(nextRight);
						stack.push(nextUp);
						stack.push(nextDown);
					}
				}
			}
		}
	}

	/******************************************************
	 * @Titre:			BOUNDARY FILL STACK
	 * 
	 * @Param: 			Point
	 * 
	 ******************************************************/
	private void boundaryFillStack(Point ptClicked) {

		// Pile qui garde en mémoire les Points de type 4-voisins
		Stack<Point> stack = new Stack<Point>();
		stack.push(ptClicked);

		while (!stack.empty()) {	
			Point current = (Point)stack.pop();

			// Vérifie si le point est plus grand que zéro et plus petit que la taille de l'image			
			if(current.x >= 0 && current.x < currentImage.getImageWidth() &&
					current.y >= 0 && current.y < currentImage.getImageHeight()){

				// On s'assure de peindre les pixels qui n'ont pas la couleur sélectionnée.
				if(!currentImage.getPixel(current.x, current.y).equals(borderColor) &&
						!currentImage.getPixel(current.x, current.y).equals(fillColor)){

					currentImage.setPixel(current.x, current.y, fillColor);
					Point nextLeft 	= new Point (current.x-1, current.y);	
					Point nextRight = new Point (current.x+1, current.y);
					Point nextUp 	= new Point (current.x, current.y-1);
					Point nextDown 	= new Point (current.x, current.y+1);

					// Vérifie si le pixel est à l'intérieur ou à l'extérieur de la région.
					if(hsvThreshold()){
						stack.push(nextLeft);
						stack.push(nextRight);
						stack.push(nextUp);
						stack.push(nextDown);
					}
				}
			}
		}
	}

	/******************************************************
	 * @Titre:			FLOOD FILL RECURSIVE
	 * 
	 * @Param: 			Point
	 * 
	 ******************************************************/
	private void floodFillRecursive(Point ptClicked) {

		// Vérifie si le point est plus grand que zéro et plus petit que la taille de l'image			
		if(ptClicked.x >= 0 && ptClicked.x < currentImage.getImageWidth() &&
				ptClicked.y >= 0 && ptClicked.y < currentImage.getImageHeight()){

			// On s'assure de peindre les pixels qui n'ont pas la couleur sélectionnée.
			if(!currentImage.getPixel(ptClicked.x, ptClicked.y).equals(fillColor) &&
					currentImage.getPixel(ptClicked.x, ptClicked.y).equals(verifyColor)) {

				// Vérifie si le pixel est à l'intérieur ou à l'extérieur de la région.
				if(hsvThreshold()){
					currentImage.setPixel(ptClicked.x, ptClicked.y, fillColor);
					floodFillRecursive(new Point(ptClicked.x+1, ptClicked.y));
					floodFillRecursive(new Point(ptClicked.x-1, ptClicked.y));
					floodFillRecursive(new Point(ptClicked.x, ptClicked.y+1));
					floodFillRecursive(new Point(ptClicked.x, ptClicked.y-1));
				}
			}
		}
	}

	/******************************************************
	 * @Titre:			BOUNDARY FILL RECURSIVE
	 * 
	 * @Param: 			Point
	 * 
	 ******************************************************/
	private void boundaryFillRecursive(Point ptClicked) {

		// Vérifie si le point est plus grand que zéro et plus petit que la taille de l'image			
		if(ptClicked.x >= 0 && ptClicked.x < currentImage.getImageWidth() &&
				ptClicked.y >= 0 && ptClicked.y < currentImage.getImageHeight()){

			// On s'assure de peindre les pixels qui n'ont pas la couleur sélectionnée.
			if(!currentImage.getPixel(ptClicked.x, ptClicked.y).equals(borderColor) &&
					!currentImage.getPixel(ptClicked.x, ptClicked.y).equals(fillColor)){

				// Vérifie si le pixel est à l'intérieur ou à l'extérieur de la région.
				if(hsvThreshold()){
					currentImage.setPixel(ptClicked.x, ptClicked.y, fillColor);
					boundaryFillRecursive(new Point(ptClicked.x+1, ptClicked.y));
					boundaryFillRecursive(new Point(ptClicked.x-1, ptClicked.y));
					boundaryFillRecursive(new Point(ptClicked.x, ptClicked.y+1));
					boundaryFillRecursive(new Point(ptClicked.x, ptClicked.y-1));
				}
			}
		}
	}

	/******************************************************
	 * @Titre:			HSV THRESHOLD
	 * 
	 * @Param: 			Point
	 * 
	 ******************************************************/
	private boolean hsvThreshold(){

		//Pixel verifyColor = currentImage.getPixel(ptClicked.x, ptClicked.y);
		
		int red 				= verifyColor.getRed();
		int green 				= verifyColor.getGreen();
		int blue 				= verifyColor.getBlue();

		float verifyHue 		= Color.RGBtoHSB(red, green, blue, null)[HUE] * ALPHA;
		float verifySaturation 	= Color.RGBtoHSB(red, green, blue, null)[SATURATION] * ALPHA;
		float verifyValue 		= Color.RGBtoHSB(red, green, blue, null)[VALUE] * ALPHA;

		int _red 				= borderColor.getRed();
		int _green 				= borderColor.getGreen();
		int _blue 				= borderColor.getBlue();
		
		float borderHue 		= Color.RGBtoHSB(_red, _green, _blue, null)[HUE] * ALPHA;
		float borderSaturation 	= Color.RGBtoHSB(_red, _green, _blue, null)[SATURATION] * ALPHA;
		float borderValue 		= Color.RGBtoHSB(_red, _green, _blue, null)[VALUE] * ALPHA;

		if(inRange((borderHue - hueThreshold), (borderHue + hueThreshold), verifyHue) &&
				inRange((borderSaturation - saturationThreshold), (borderSaturation + saturationThreshold), verifySaturation) &&
						inRange((borderValue - valueThreshold), (borderValue + valueThreshold), verifyValue)){
			
			System.out.println("FALSE");			
			return false;   			
		}	
		System.out.println("TRUE");
		return true;
	}
	
	/******************************************************
	 * @Titre:			IN RANGE
	 * 
	 * @Param: 			low, high, val	
	 * 
	 * @URL:			https://www.geeksforgeeks.org/how-to-check-whether-a-number-is-in-the-rangea-b-using-one-comparison/
	 * 
	 ******************************************************/
	private boolean inRange(float low, float high, float val){ 
		return ((val - high) * (val - low) <= 0); 
	} 


	/**
	 * @return
	 */
	public Pixel getBorderColor() {
		return borderColor;
	}

	/**
	 * @return
	 */
	public Pixel getFillColor() {
		return fillColor;
	}

	/**
	 * @param pixel
	 */
	public void setBorderColor(Pixel pixel) {
		borderColor = pixel;
		System.out.println("new border color");
	}

	/**
	 * @param pixel
	 */
	public void setFillColor(Pixel pixel) {
		fillColor = pixel;
		System.out.println("new fill color");
	}
	/**
	 * @return true if the filling algorithm is set to Flood Fill, false if it is set to Boundary Fill.
	 */
	public boolean isFloodFill() {
		return floodFill;
	}

	/******************************************************
	 * @Titre:		SET FLOOD FILL
	 * 
	 * @Resumer:	If boolean is set to true, it will enable 
	 * 				Flood Fill. If boolean is set to false, it
	 * 				will enable Boundary Fill.	
	 * 
	 * @Param: 		boolean	
	 * 
	 ******************************************************/
	public void setFloodFill(boolean b) {
		floodFill = b;
		if (floodFill) {
			System.out.println("now doing Flood Fill");
		} else {
			System.out.println("now doing Boundary Fill");
		}
	}

	/**
	 * @return
	 */
	public int getHueThreshold() {
		return hueThreshold;
	}

	/**
	 * @return
	 */
	public int getSaturationThreshold() {
		return saturationThreshold;
	}

	/**
	 * @return
	 */
	public int getValueThreshold() {
		return valueThreshold;
	}

	/**
	 * @param i
	 */
	public void setHueThreshold(int i) {
		hueThreshold = i;
		System.out.println("new Hue Threshold " + i);
	}

	/**
	 * @param i
	 */
	public void setSaturationThreshold(int i) {
		saturationThreshold = i;
		System.out.println("new Saturation Threshold " + i);
	}

	/**
	 * @param i
	 */
	public void setValueThreshold(int i) {
		valueThreshold = i;
		System.out.println("new Value Threshold " + i);
	}

}
