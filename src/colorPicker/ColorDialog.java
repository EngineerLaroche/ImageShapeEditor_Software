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

package colorPicker;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import model.Pixel;
import view.ColorSlider;
import view.InterfaceColor;
import view.RGBColorMediator;


/******************************************************
 * @CLASS_TITLE:	COLOR DIALOG
 * 
 * @Description: 	...
 * 
 ******************************************************/
public class ColorDialog extends JDialog implements InterfaceColor{


	/***************************
	 * Classes instanciees
	 ***************************/
	private ColorDialogResult result = null;

	/***************************
	 * Listeners
	 ***************************/
	private ActionListener okActionListener;

	/***************************
	 * Variables
	 ***************************/
	private int imageHeights = 30;

	/***************************
	 * Constantes
	 ***************************/
	private static final long serialVersionUID = 1L;

	private static final String
	R = "R:",
	G = "G:",
	B = "B:",

	C = "C:",
	M = "M:",
	Y = "Y:",
	K = "K:",

	H = "H:",
	S = "S:",
	V = "V:",

	Luma = "Y:",
	Cb = "Cb:",
	Cr = "Cr:",

	OK = "OK",
	CANCEL = "Cancel";


	/******************************************************
	 * @Titre:			COLOR MEDIATOR CONSTRUCTOR
	 * 
	 * @Resumer:		Creation du panneau principal pour
	 * 					afficher les cases de couleurs.
	 * 					On y retrouve l'onglet RGB, CMYK,
	 * 					HSV et YCbCr.
	 * 
	 * @Param: 			Frame, ColorDialogResult et imageWidths
	 * 
	 ******************************************************/
	ColorDialog(Frame owner, ColorDialogResult result, int imageWidths) {

		super(owner, true);
		this.result = result;

		JPanel rgbPanel = createRGBPanel(result, imageWidths);
		JPanel cmykPanel = createCMYKPanel(result, imageWidths);
		JPanel hsvPanel = createHSVPanel(result, imageWidths);
		JPanel ycbcrPanel = createYCBCRPanel(result, imageWidths);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab(RGB, rgbPanel);
		tabbedPane.addTab(CMYK, cmykPanel);
		tabbedPane.addTab(HSV, hsvPanel);
		tabbedPane.addTab(YCBCR, ycbcrPanel);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

		AbstractAction okAction = new AbstractAction(OK) {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				ColorDialog.this.result.setAccepted(true);
				dispose();
			}
		};

		JButton okButton = new JButton(okAction);
		buttonsPanel.add(okButton);

		AbstractAction cancelAction = new AbstractAction(CANCEL) {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				ColorDialog.this.dispose();
			}
		};

		buttonsPanel.add(new JButton(cancelAction));

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(tabbedPane);
		mainPanel.add(buttonsPanel);

		getContentPane().add(mainPanel, BorderLayout.CENTER);
	}

	/******************************************************
	 * @Titre:			CREATE RGB PANEL
	 * 
	 * @Resumer:		Creation du rectangle de couleur RGB.
	 * 					Va chercher les couleurs RGB en temps
	 * 					réel pour les afficher dans le rectangle.
	 * 
	 * @Param: 			ColorDialogResult et imageWidths
	 * @Return:			JPanel
	 * 
	 ******************************************************/
	private JPanel createRGBPanel(ColorDialogResult result, int imageWidths) {	

		ColorSlider csArrayRGB[] = new ColorSlider[RGB.length()];
		RGBColorMediator rgbMediator = new RGBColorMediator(result, imageWidths, imageHeights);

		csArrayRGB[RED] = new ColorSlider(R, result.getPixel().getRed(), rgbMediator.getRedImage());
		csArrayRGB[GREEN] = new ColorSlider(G, result.getPixel().getGreen(), rgbMediator.getGreenImage());
		csArrayRGB[BLUE] = new ColorSlider(B, result.getPixel().getBlue(), rgbMediator.getBlueImage());

		rgbMediator.setRedCS(csArrayRGB[RED]);
		rgbMediator.setGreenCS(csArrayRGB[GREEN]);
		rgbMediator.setBlueCS(csArrayRGB[BLUE]);

		return newPanel(csArrayRGB);
	}

	/******************************************************
	 * @Titre:			CREATE CMYK PANEL
	 * 
	 * @Resumer:		Creation du rectangle de couleur CMYK.
	 * 					Va chercher les couleurs CMYK en temps
	 * 					réel pour les afficher dans le rectangle.
	 * 
	 * @Param: 			ColorDialogResult et imageWidths
	 * @Return:			JPanel
	 * 
	 ******************************************************/
	private JPanel createCMYKPanel(ColorDialogResult result, int imageWidths) {	

		ColorSlider csArrayCMYK[] = new ColorSlider[CMYK.length()];
		CMYKColorMediator cmykMediator = new CMYKColorMediator(result, imageWidths, imageHeights);

		csArrayCMYK[CYAN] = new ColorSlider(C, cmykMediator.getCyan(), cmykMediator.getCyanImage());
		csArrayCMYK[MAGENTA] = new ColorSlider(M, cmykMediator.getMagenta(), cmykMediator.getMagentaImage());
		csArrayCMYK[YELLOW] = new ColorSlider(Y, cmykMediator.getYellow(), cmykMediator.getYellowImage());
		csArrayCMYK[BLACK] = new ColorSlider(K, cmykMediator.getBlack(), cmykMediator.getBlackImage());

		cmykMediator.setCyanCS(csArrayCMYK[CYAN]);
		cmykMediator.setMagentaCS(csArrayCMYK[MAGENTA]);
		cmykMediator.setYellowCs(csArrayCMYK[YELLOW]);
		cmykMediator.setBlackCs(csArrayCMYK[BLACK]);

		return newPanel(csArrayCMYK);
	}

	/******************************************************
	 * @Titre:			CREATE HSV PANEL
	 * 
	 * @Resumer:		Creation du rectangle de couleur HSV.
	 * 					Va chercher les couleurs HSV en temps
	 * 					réel pour les afficher dans le rectangle.
	 * 
	 * @Param: 			ColorDialogResult et imageWidths
	 * @Return:			JPanel
	 * 
	 ******************************************************/
	private JPanel createHSVPanel(ColorDialogResult result, int imageWidths) {	

		ColorSlider csArrayHSV[] = new ColorSlider[HSV.length()];
		HSVColorMediator hsvMediator = new HSVColorMediator(result, imageWidths, imageHeights);

		csArrayHSV[HUE] = new ColorSlider(H, hsvMediator.getHue(), hsvMediator.getHueImage());
		csArrayHSV[SATURATION] = new ColorSlider(S, hsvMediator.getSaturation(), hsvMediator.getSaturationImage());
		csArrayHSV[VALUE] = new ColorSlider(V, hsvMediator.getValue(), hsvMediator.getValueImage());

		hsvMediator.setHueCS(csArrayHSV[HUE]);
		hsvMediator.setSaturationCS(csArrayHSV[SATURATION]);
		hsvMediator.setValueCs(csArrayHSV[VALUE]);

		return newPanel(csArrayHSV);
	}

	/******************************************************
	 * @Titre:			CREATE YCbCr PANEL
	 * 
	 * @Resumer:		Creation du rectangle de couleur YCbCr.
	 * 					Va chercher les couleurs YCbCr en temps
	 * 					réel pour les afficher dans le rectangle.
	 * 
	 * @Param: 			ColorDialogResult et imageWidths
	 * @Return:			JPanel
	 * 
	 ******************************************************/
	private JPanel createYCBCRPanel(ColorDialogResult result, int imageWidths) {	

		ColorSlider csArrayYCBCR[] = new ColorSlider[YCC.length()];
		YCBCRColorMediator ycbcrMediator = new YCBCRColorMediator(result, imageWidths, imageHeights);

		csArrayYCBCR[LUMA] = new ColorSlider(Luma, ycbcrMediator.getY(), ycbcrMediator.getYImage());
		csArrayYCBCR[CB] = new ColorSlider(Cb, ycbcrMediator.getCb(), ycbcrMediator.getCbImage());
		csArrayYCBCR[CR] = new ColorSlider(Cr, ycbcrMediator.getCr(), ycbcrMediator.getCrImage());

		ycbcrMediator.setYCS(csArrayYCBCR[LUMA]);
		ycbcrMediator.setCbCS(csArrayYCBCR[CB]);
		ycbcrMediator.setCrCs(csArrayYCBCR[CR]);

		return newPanel(csArrayYCBCR);
	}

	/******************************************************
	 * @Titre:			CREATE PANEL
	 * 
	 * @Resumer:		Créer et retourne un simple panneau
	 * 					composé d'un layout sur l'axe Y.
	 * 					Cette méthode permet de minimiser la 
	 * 					duplication de code pour cette classe.
	 * 
	 * @Param: 			ColorSlider[]
	 * @Return:			JPanel
	 * 
	 ******************************************************/
	private JPanel newPanel(ColorSlider csArray[]){

		//Construction du panneau qui affiche les couleurs
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		//Ajout des couleurs sur le panneau
		for(int i = 0; i < csArray.length; i++){
			panel.add(csArray[i]);
		}

		return panel;
	}

	/******************************************************
	 * @Titre:			GET COLOR
	 * 
	 * @Param: 			Frame, Pixel et imageWidths
	 * 
	 ******************************************************/
	static public Pixel getColor(Frame owner, Pixel color, int imageWidths) {

		ColorDialogResult result = new ColorDialogResult(color);
		ColorDialog colorDialog = new ColorDialog(owner, result, imageWidths);
		colorDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		colorDialog.pack();
		colorDialog.setVisible(true);
		
		if (result.isAccepted()) {
			return result.getPixel();
		} else {
			return null;
		}
	}
}

