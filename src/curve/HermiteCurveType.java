package curve;

import java.awt.Point;
import java.util.List;

import model.ControlPoint;
import model.Matrix;

/******************************************************
 * @CLASS_TITLE:	HERMITE CURVE TYPE
 * 
 * @Description: 	
 * 
 ******************************************************/
public class HermiteCurveType extends CurveType{

	/***************************
	 * List Matrix
	 ***************************/
	private List<?> hermiteMatrix = Matrix.buildMatrix4( 2, -2,  1,  1, 
														-3,  3, -2, -1, 
														 0,  0,  1,  0, 
														 1,  0,  0,  0);
	private List<?> matrix = hermiteMatrix;
	
	
	/******************************************************
	 * @Titre:			HERMITE CURVE TYPE CONSTRUCTOR
	 * 
	 * @Param: 			String name
	 * 
	 ******************************************************/
	public HermiteCurveType(String name) {
		super(name);
	}

	/******************************************************
	 * @Titre:		Get Number of Segments	
	 * 
	 * @Resumer:	See Labo_04.CurveType#getNumberOfSegments(int)	
	 * 				La première curve est représenté par les points 
	 * 				partant de 1 à 4. Ensuite, le point 4 représente 
	 * 				le premier point de la prochaine suite.
	 * 
	 * @Param: 		int numberOfControlPoints
	 * 
	 ******************************************************/
	public int getNbOfSegments(int numberOfCtrlPoints) {
		if (numberOfCtrlPoints >= 4) {
			return (numberOfCtrlPoints - 1) / 3;
		} else {
			return 0;
		}
	}

	/******************************************************
	 * @Titre:		Get Number of Control Points Segment	
	 * 
	 * @Resumer:	See Labo_04.CurveType#getNumberOfSegments(int)	
	 * 
	 * @Param: 		Aucun	
	 * 
	 ******************************************************/
	public int getNbOfCtrlPointsPerSegment() {
		return 4;
	}

	/******************************************************
	 * @Titre:		Get Control Point	
	 * 
	 * @Resumer:	See Labo_4.CurveType#getControlPoint(java.util.List, int, int)	
	 * 
	 * @Param: 		List<?> ctrlPoints, int segmentNumber, int controlPointNumber
	 * @Return:		control point index
	 * 
	 ******************************************************/
	public ControlPoint getCtrlPoint(List<?> ctrlPoints, int segmentNumber, int ctrlPointNumber) {
		return (ControlPoint)ctrlPoints.get(segmentNumber * 3 + ctrlPointNumber);
	}

	/******************************************************
	 * @Titre:		Evaluate Curve at Point	
	 * 
	 * @Resumer:	Labo_04.CurveType#evalCurveAt(java.util.List, double)	
	 * 
	 * @Param: 		List<?> ctrlPoints, double t	
	 * 
	 ******************************************************/
	public Point evalCurveAt(List<?> ctrlPoints, double t) {

		Point //Récupère les 4 points entrés 
		p1 = ((ControlPoint)ctrlPoints.get(0)).getCenter(),
		p2 = ((ControlPoint)ctrlPoints.get(1)).getCenter(),
		p3 = ((ControlPoint)ctrlPoints.get(2)).getCenter(),
		p4 = ((ControlPoint)ctrlPoints.get(3)).getCenter();
		
		ControlPoint 
		r1 = new ControlPoint((p2.x - p1.x), (p2.y - p1.y)),	
		r4 = new ControlPoint((p4.x - p3.x), (p4.y - p3.y));

		List<?> //Init matrice avec un polynome de degré 3 et les points
		tVector = Matrix.buildRowVector4(t*t*t, t*t, t, 1), 
		gVector = Matrix.buildColumnVector4(p1, p4, r1.getCenter(), r4.getCenter());
		
		return Matrix.eval(tVector, matrix, gVector);
	}
}
