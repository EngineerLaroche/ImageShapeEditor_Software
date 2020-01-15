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
package curve;

import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JOptionPane;

import controller.AbstractTransformer;
import model.ControlPoint;
import model.Curve;
import model.CurvesModel;
import model.DocObserver;
import model.Document;
import model.Shape;
import view.Application;
import view.CurvesPanel;

/**
 * <p>Title: Curves</p>
 * <p>Description: (AbstractTransformer)</p>
 * @version $Revision: 1.9 $
 */
public class Curves extends AbstractTransformer implements DocObserver {
	
	private boolean firstPoint = false;
	private Curve curve;
	private CurvesPanel cp;
	
	/**
	 * Default constructor
	 */
	public Curves() {
		Application.getInstance().getActiveDocument().addObserver(this);
	}	

	/* (non-Javadoc)
	 * @see controller.AbstractTransformer#getID()
	 */
	public int getID() { return ID_CURVES; }
	
	public void activate() {
		firstPoint = true;
		Document doc = Application.getInstance().getActiveDocument();
		List selectedObjects = doc.getSelectedObjects();
		boolean selectionIsACurve = false; 
		if (selectedObjects.size() > 0){
			Shape s = (Shape)selectedObjects.get(0);
			if (s instanceof Curve){
				curve = (Curve)s;
				firstPoint = false;
				cp.setCurveType(curve.getCurveType());
				cp.setNumberOfSections(curve.getNumberOfSections());
			}
			else if (s instanceof ControlPoint){
				curve = (Curve)s.getContainer();
				firstPoint = false;
			}
		}
		
		if (firstPoint) {
			// First point means that we will have the first point of a new curve.
			// That new curve has to be constructed.
			curve = new Curve(100,100);
			setCurveType(cp.getCurveType());
			setNumberOfSections(cp.getNumberOfSections());
		}
	}
    
	/**
	 * 
	 */
	protected boolean mouseReleased(MouseEvent e){
		int mouseX = e.getX();
		int mouseY = e.getY();

		if (firstPoint) {
			firstPoint = false;
			Document doc = Application.getInstance().getActiveDocument();
			doc.addObject(curve);
		}
		ControlPoint cp = new ControlPoint(mouseX, mouseY);
		curve.addPoint(cp);
	
		return true;
	}

	/******************************************************
	 * @Titre:		Set Curve Type
	 * 
	 * @Resumer:	Selon le type de courbe choisi par 
	 * 				l'utilisateur, on pointe vers la bonne
	 * 				entrée et on initialise une nouvelle
	 * 				classe qui contient le bon algorithme.	
	 * 
	 * @Param: 		String (type de curve choisi)
	 * 
	 ******************************************************/
	public void setCurveType(String curveSelected) {
		
		if (curveSelected == CurvesModel.BEZIER) {
			curve.setCurveType(new BezierCurveType(CurvesModel.BEZIER));
		} else if (curveSelected == CurvesModel.LINEAR) {
			curve.setCurveType(new PolylineCurveType(CurvesModel.LINEAR));
		} else if (curveSelected == CurvesModel.HERMITE) {
			curve.setCurveType(new HermiteCurveType(CurvesModel.HERMITE));
		}else if (curveSelected == CurvesModel.BSPLINE) {
			curve.setCurveType(new BSplineCurveType(CurvesModel.BSPLINE));
		}
	}
	
	/******************************************************
	 * @Titre:		Align Control Point
	 * 
	 * @Resumer:	Lorsque le point de contrôle qui est à 
	 * 				la jonction de deux segments est sélectionné 
	 * 				et que le bouton "Aligned" est activé, alors 
	 * 				les points de contrôle pour cette jonction 
	 * 				sont déplacés de façon que les deux 
	 * 				tangentes soient alignées (continuité G1). 	
	 * 
	 * @Param: 		N/A
	 * @throws 		Exception 
	 * 
	 ******************************************************/
	public void alignControlPoint() throws Exception {
		
		if (curve != null) {
			Document doc = Application.getInstance().getActiveDocument();
			List selectedObjects = doc.getSelectedObjects(); 
			
			if (selectedObjects.size() > 0){
				Shape s = (Shape)selectedObjects.get(0);
			
				if (curve.getShapes().contains(s)){
					
					int ctrlPointIndex = curve.getShapes().indexOf(s);
					System.out.println("Try to apply G1 continuity on control point [" + ctrlPointIndex + "]");
				
					Shape s1 = null, s2 = null;
					
					//Verifie qu'on ne selectionne pas le premier ou le dernier point
					if(ctrlPointIndex > 0 && ctrlPointIndex < (curve.getShapes().size() - 1)){
						s1 = (Shape)curve.getShapes().get(ctrlPointIndex - 1);
						s2 = (Shape)curve.getShapes().get(ctrlPointIndex + 1); 
					}
					else{ 
						JOptionPane d = new JOptionPane();
						d.showMessageDialog(null, "Veuillex choisir un point different du premier ou du dernier", "Modification", JOptionPane.PLAIN_MESSAGE);
						return;	
						//throw new Exception("\n*** Veuillex choisir un point different du premier ou du dernier ***\n");}
					}
						
					double //Calcul des positions x et y qui sont mis au carré pour le calcul de la distance
					x1 = Math.pow((s.getCenter().getX()  - s1.getCenter().getX()), 2),
					y1 = Math.pow((s.getCenter().getY()  - s1.getCenter().getY()), 2),
					x2 = Math.pow((s2.getCenter().getX() - s.getCenter().getX()),  2),		
					y2 = Math.pow((s2.getCenter().getY() - s.getCenter().getY()),  2);
					
					double //Calcul de la distance d1 et d2 --> racine(x^2 + y^2)
					d1 = Math.sqrt(x1 + y1),
					d2 = Math.sqrt(x2 + y2);
					
					//s2.setCenter((-(s2.getCenter().getX() - s.getCenter().getX()) / d1) * d2) + s.getCenter().getX()), centerY);
					s2.setCenter(((-(s1.getCenter().getX()-s.getCenter().getX()) / d1) * d2) + (s.getCenter().getX()),
					((-(s1.getCenter().getY()-s.getCenter().getY()) / d1) * d2) + s.getCenter().getY());
					
//					double //Calcul de la nouvelle distance et de la nouvelle position X et Y
//					d = (d2 / d1),
//					x = (s.getCenter().getX() + (Math.sqrt(x1) * d)),
//					y = (s.getCenter().getY() + (Math.sqrt(y1) * d));
//					
//					s2.setCenter(x, y);
					
					System.out.println("\nS  X: " + s.getCenter().getX() + "   Y: " + s.getCenter().getY());
					System.out.println("S1  X: " + s1.getCenter().getX() + "   Y: " + s1.getCenter().getY());
					System.out.println("S2  X: " + s2.getCenter().getX() + "   Y: " + s2.getCenter().getY());
					
					s2.notifyObservers();
				}
			}		
		}
	}
	
	/******************************************************
	 * @Titre:		Symetric Control Point
	 * 
	 * @Resumer:	Lorsque le point de contrôle qui est à 
	 * 				la jonction de deux segments est sélectionné 
	 * 				et que le bouton "Symetric" est activé, alors 
	 * 				les points de contrôle pour cette jonction 
	 * 				sont déplacés de façon que les deux tangentes 
	 * 				soient égales (continuité C1).	
	 * 
	 * @Param: 		N/A
	 * 
	 ******************************************************/
	public void symetricControlPoint() throws Exception {
		
		if (curve != null) {
			Document doc = Application.getInstance().getActiveDocument();
			List selectedObjects = doc.getSelectedObjects(); 
			
			if (selectedObjects.size() > 0){
				Shape s = (Shape)selectedObjects.get(0);
				
				if (curve.getShapes().contains(s)){
					
					int ctrlPointIndex = curve.getShapes().indexOf(s);
					System.out.println("Try to apply C1 continuity on control point [" + ctrlPointIndex + "]");
					
					Shape s1 = null, s2 = null;
					
					//Verifie qu'on ne selectionne pas le premier ou le dernier point
					if(ctrlPointIndex > 0 && ctrlPointIndex < (curve.getShapes().size() - 1)){
						s1 = (Shape)curve.getShapes().get(ctrlPointIndex - 1);
						s2 = (Shape)curve.getShapes().get(ctrlPointIndex + 1); 
					}
					else{ 
						JOptionPane d = new JOptionPane();
						d.showMessageDialog(null, "Veuillex choisir un point different du premier ou du dernier", "Modification", JOptionPane.PLAIN_MESSAGE);
						return;	
						//throw new Exception("\n*** Veuillex choisir un point different du premier ou du dernier ***\n");
						} 
					
					double //Calcul du delta pour X et pour Y
					x1 = (s.getCenter().getX() - s1.getCenter().getX()),
					y1 = (s.getCenter().getY() - s1.getCenter().getY());
					
					double //Calcul la nouvelle position X et Y
					x = (s.getCenter().x + x1),
					y = (s.getCenter().y + y1);
					
					s2.setCenter(x, y);
					s2.notifyObservers();
				}
			}	
		}
	}

	public void setNumberOfSections(int n) {
		curve.setNumberOfSections(n);
	}
	
	public int getNumberOfSections() {
		if (curve != null)
			return curve.getNumberOfSections();
		else
			return Curve.DEFAULT_NUMBER_OF_SECTIONS;
	}
	
	public void setCurvesPanel(CurvesPanel cp) {
		this.cp = cp;
	}
	
	/* (non-Javadoc)
	 * @see model.DocObserver#docChanged()
	 */
	public void docChanged() {
	}

	/* (non-Javadoc)
	 * @see model.DocObserver#docSelectionChanged()
	 */
	public void docSelectionChanged() {
		activate();
	}
}
