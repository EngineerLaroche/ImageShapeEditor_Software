package curve;

import java.awt.Point;
import java.util.List;

import model.ControlPoint;
import model.Matrix;

/******************************************************
 * @CLASS_TITLE:	B_SPLINE CURVE TYPE
 * 
 * @Description: 	
 * 
 ******************************************************/
public class BSplineCurveType extends CurveType{

	/***************************
	 * List Matrix
	 ***************************/
	private List<?> bsplineMatrix = Matrix.buildMatrix4( -1,  3, -3,  1, 
														  3, -6,  3,  0, 
														 -3,  0,  3,  0, 
														  1,  4,  1,  0);

	private List<?> matrix = bsplineMatrix;
	
	/******************************************************
	 * @Titre:			B_SPLINE CURVE TYPE CONSTRUCTOR
	 * 
	 * @Param: 			String name
	 * 
	 ******************************************************/
	public BSplineCurveType(String name) {
		super(name);
	}

	/******************************************************
	 * @Titre:		Get Number of Segments	
	 * 
	 * @Resumer:	See Labo_04.CurveType#getNumberOfSegments(int).
	 * 				La première curve est représenté par les points 
	 * 				partant de 1 à 4. Ensuite, le point 4 représente 
	 * 				le premier point de la prochaine suite.
	 * 
	 * @Param: 		nombre de point de controle
	 * @Return:		nombre de suite de points de controles
	 * 
	 ******************************************************/
	public int getNbOfSegments(int numberOfCtrlPoints) {

		if (numberOfCtrlPoints < 4)  return 0;
		if (numberOfCtrlPoints == 4) return 1;
		return (numberOfCtrlPoints - 3);
	}

	/******************************************************
	 * @Titre:		Get Number of Control Points Segment	
	 * 
	 * @Resumer:	See model.CurveType#getNumberOfControlPointsPerSegment()
	 * 
	 * @Param: 		N/A
	 * @Return:		4	
	 * 
	 ******************************************************/
	public int getNbOfCtrlPointsPerSegment() {
		return 4;
	}

	/******************************************************
	 * @Titre:		Get Control Point	
	 * 
	 * @Resumer:	See Labo_04.CurveType#getControlPoint(java.util.List, int, int)	
	 * 
	 * @Param: 		List<?> ctrlPoints, int segmentNumber, int ctrlPointNumber
	 * @Return:		control point index
	 * 
	 ******************************************************/
	public ControlPoint getCtrlPoint(List<?> controlPoints, int segmentNumber, int controlPointNumber) {		
		int controlPointIndex = segmentNumber + controlPointNumber;
		return (ControlPoint)controlPoints.get(controlPointIndex);

	}

	/******************************************************
	 * @Titre:		Evaluate Curve at Point	
	 * 
	 * @Resumer:	See Labo_04.CurveType#evalCurveAt(java.util.List, double)	
	 * 
	 * @Param: 		List<?> ctrlPoints, double t
	 * @Return:		Point	
	 * 
	 ******************************************************/
	public Point evalCurveAt(List<?> ctrlPoints, double t) {
		
		Point //Récupère les 4 points entrés 
		p1 = ((ControlPoint)ctrlPoints.get(0)).getCenter(),
		p2 = ((ControlPoint)ctrlPoints.get(1)).getCenter(),
		p3 = ((ControlPoint)ctrlPoints.get(2)).getCenter(),
		p4 = ((ControlPoint)ctrlPoints.get(3)).getCenter(),
		p  = null;
		
		List<?> //Init matrice avec un polynome de degré 3 et les points
		tVector = Matrix.buildRowVector4(t*t*t, t*t, t, 1), 
		gVector = Matrix.buildColumnVector4(p1, p2, p3, p4);
		
		//Multiplication des matrices et division de X et Y par 6
		p = Matrix.eval(tVector, matrix, gVector); 
		p.setLocation((p.x / 6.0), (p.y / 6.0));
		
		return p;
	}
}
