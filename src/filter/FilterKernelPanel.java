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
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import controller.TransformersIndex;
import model.KernelModel;
import model.ObserverIF;
import view.KernelPanel;

/**
 * <p>Title: FilterKernelPanel</p>
 * <p>Description: </p>
 * @version $Revision: 1.8 $
 */
public class FilterKernelPanel extends JPanel implements ObserverIF {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private JPanel _setUpPanel;
	/**
	 * 
	 */
	private KernelPanel _kernelPanel;
	/**
	 * 
	 */
	private JLabel _handlingBorderLabel;
	/**
	 * 
	 */ 
	private JComboBox<?> _handlingBorderComboBox;
	/**
	 * 
	 */
	private JLabel _rangeClampLabel;
	/**
	 * 
	 */
	private JComboBox<?> _clampComboBox;
	/**
	 * 
	 */
	private JLabel _filterTypeLabel;
	/**
	 * 
	 */
	private JComboBox<?> _filterTypeComboBox;
	/**
	 * 
	 */
	private TransformersIndex tIndex;


	public FilterKernelPanel(TransformersIndex _tIndex){
		_setUpPanel  = new JPanel();

		this.tIndex = _tIndex;

		_setUpPanel.setLayout(new GridLayout(3, 2));

		_kernelPanel = new KernelPanel(_tIndex);
		_kernelPanel.addObserver(this);

		_handlingBorderLabel	= new JLabel("Handling Border"); 
		_handlingBorderComboBox	= new JComboBox(KernelModel.HANDLING_BORDER_ARRAY);	

		_rangeClampLabel = new JLabel("Range");
		_clampComboBox   = new JComboBox(KernelModel.CLAMP_ARRAY);

		_filterTypeLabel    = new JLabel("Filter Type");
		_filterTypeComboBox = new JComboBox(KernelModel.FILTER_TYPE_ARRAY);

		// Initialize the Handling Border combo box to the default value of the handling border combo box
		_handlingBorderComboBox.setSelectedIndex(0);
		//_model.setHandlingBorderValue((String)_handlingBorderComboBox.getSelectedItem());

		// Initialize the Handling Border combo box to the default value of the handling border combo box
		_clampComboBox.setSelectedIndex(0);	
		//_model.setClampValue((String)_clampComboBox.getSelectedItem());

		_filterTypeComboBox.setSelectedIndex(0);

		_handlingBorderComboBox.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent ae) {
						FilterKernelPanel.this.tIndex.getTheFilter().setBorder((String)_handlingBorderComboBox.getSelectedItem());
					}	
				});

		_clampComboBox.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent ae) {
						FilterKernelPanel.this.tIndex.getTheFilter().setClamp((String)_clampComboBox.getSelectedItem());
					}	
				});

		_filterTypeComboBox.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent ae) {
						setFilter((String)_filterTypeComboBox.getSelectedItem());
					}	
				});

		_handlingBorderLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		_rangeClampLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		_filterTypeLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

		_setUpPanel.add(_handlingBorderLabel);
		_setUpPanel.add(_handlingBorderComboBox);
		_setUpPanel.add(_rangeClampLabel);
		_setUpPanel.add(_clampComboBox);
		_setUpPanel.add(_filterTypeLabel);
		_setUpPanel.add(_filterTypeComboBox);

		//
		setLayout(new BorderLayout());
		add(_setUpPanel, BorderLayout.NORTH);
		add(_kernelPanel, BorderLayout.CENTER);
	}

	/* (non-Javadoc)
	 * @see model.ObserverIF#update()
	 */
	public void update() {
		// Switch to Custom.
		_filterTypeComboBox.setSelectedIndex(0);
	}

	/******************************************************
	 * @Titre:			SET FILTER
	 * 
	 * @Resumer:		Selon le filtre choisi pas
	 * 					l'utilisateur, un switch case va
	 * 					pointer vers la bonne matrice a
	 * 					utiliser.
	 * 
	 * @Param: 			String selectFilter		
	 * 
	 ******************************************************/
	private void setFilter(String selectFilter) {
		System.out.println("Filter: " + selectFilter);
		
		int index = 0;
		for (int i = 0; i < KernelModel.FILTER_TYPE_ARRAY.length; ++i) {
			if (selectFilter.equals(KernelModel.FILTER_TYPE_ARRAY[i])) {
				index = i;	
			}
		}
		
		switch (index) {

		case 1: // Mean Filter
		{
			float meanKernel[][] = {{1, 1, 1},
									{1, 1, 1},
									{1, 1, 1}};
			_kernelPanel.setKernelValues(meanKernel);
		} 
		break;

		case 2: // Gaussian Filter
		{
			float meanKernel[][] = {{1, 2, 1},
									{2, 4, 2},
									{1, 2, 1}};
			_kernelPanel.setKernelValues(meanKernel);
		} 
		break;

		case 3: // Laplacian 4-Neighbour filter
		{
			float meanKernel[][] = {{ 0, -1,  0},
									{-1,  4, -1},
									{ 0, -1,  0}};
			_kernelPanel.setKernelValues(meanKernel);
		} 
		break;

		case 4: // Laplacian 8-Neighbour filter
		{
			float meanKernel[][] = {{-1, -1, -1},
									{-1,  8, -1},
									{-1, -1, -1}};
			_kernelPanel.setKernelValues(meanKernel);
		} 
		break;

		case 5: // Prewitt Horizontale filter
		{
			System.out.println("\n*** Prewit Horizontal filter not configured ***\n");
		} 
		break;

		case 6: // Prewitt Verticale filter
		{
			System.out.println("\n*** Prewit Vertical filter not configured ***\n");
		} 
		break;

		case 7: // Sobel Horizontale filter
		{
			float meanKernel[][] = {{-1, 0, 1},
									{-2, 0, 2},
									{-1, 0, 1}};
			_kernelPanel.setKernelValues(meanKernel);
		} 
		break;

		case 8: // Sobel Verticale filter
		{
			float meanKernel[][] = {{-1, -2, -1},
									{ 0,  0,  0},
									{ 1,  2,  1}};
			_kernelPanel.setKernelValues(meanKernel);
		} 
		break;

		case 9: // Roberts 45 degrees
		{
			System.out.println("\n*** Roberts 45 degrees filter not configured ***\n");
		} 
		break;

		case 10: // Roberts -45 degrees
		{
			System.out.println("\n*** Roberts -45 degrees filter not configured ***\n");
		} 
		break;

		case 0: // Custom
		
		default:
		break;
		}
		// The following is needed because as we were updated, we automatically switched to Custom.
		_filterTypeComboBox.setSelectedIndex(index);
	}
}