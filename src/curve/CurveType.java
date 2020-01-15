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

import java.awt.Point;
import java.util.List;

import model.ControlPoint;

/**
 * <p>Title: CurveType</p>
 * <p>Description: </p>
 * @version $Revision: 1.3 $
 */
public abstract class CurveType {
	public CurveType(String name) {
		this.name = name;
	}
	public abstract int getNbOfSegments(int numberOfControlPoints);
	public abstract int getNbOfCtrlPointsPerSegment();
	public abstract ControlPoint getCtrlPoint(List<?> controlPoints, int segmentNumber, int controlPointNumber);
	public double getMinT(int segmentNumber) {return 0;};
	public double getMaxT(int segmentNumber) {return 1;};
	public abstract Point evalCurveAt(List<?> controlPoints, double t);
	public String getName() {
		return name;
	}
	private String name;
}
