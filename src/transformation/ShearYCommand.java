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
package transformation;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.Iterator;
import java.util.List;

import controller.AnchoredTransformationCommand;
import controller.MementoTracker;
import model.Shape;

/**
 * <p>Title: ShearYCommand</p>
 * <p>Description: </p>
 * @version $Revision: 1.4 $
 */
public class ShearYCommand extends AnchoredTransformationCommand {

	/**
	 * @param angleDegrees The angle to which the horizontal lines will be transformed.
	 * @param anchor one of the predefined positions for the anchor point
	 */
	public ShearYCommand(double angleDegrees, int anchor, List aObjects) {
		super(anchor);
		this.angleDegrees = angleDegrees;
		objects = aObjects;
	}
	
	/* (non-Javadoc)
	 * @see controller.Command#execute()
	 */
	public void execute() {
		System.out.println("command: shearing on y-axis to " + angleDegrees +
				           " degrees anchored on " + getAnchor());
				           		
		//Récuprération du point d'ancrage
		Point point = this.getAnchorPoint(objects);
		Iterator iter = objects.iterator();
		Shape shape;
		while(iter.hasNext()){
			shape = (Shape)iter.next();
			mt.addMememto(shape);
			AffineTransform t = shape.getAffineTransform();
			
			//Translation au point relatif (ancrage)
			t.translate(point.getX(), point.getY());
			//ShearY
			t.shear(0,Math.tan(-1 * angleDegrees * Math.PI/180));
			//Translation du point relatif choisi à son point initial
			t.translate(-point.getX(), -point.getY());
			
			shape.setAffineTransform(t);
		}
	}

	/* (non-Javadoc)
	 * @see controller.Command#undo()
	 */
	public void undo() {
		mt.setBackMementos();
	}

	private MementoTracker mt = new MementoTracker();
	private List objects;
	private double angleDegrees;

}