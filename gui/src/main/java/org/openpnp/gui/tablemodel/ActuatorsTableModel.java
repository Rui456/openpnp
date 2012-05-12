/*
 	Copyright (C) 2011 Jason von Nieda <jason@vonnieda.org>
 	
 	This file is part of OpenPnP.
 	
	OpenPnP is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    OpenPnP is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with OpenPnP.  If not, see <http://www.gnu.org/licenses/>.
 	
 	For more information about OpenPnP visit http://openpnp.org
*/

package org.openpnp.gui.tablemodel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.openpnp.ConfigurationListener;
import org.openpnp.model.Configuration;
import org.openpnp.spi.Actuator;
import org.openpnp.spi.Head;

public class ActuatorsTableModel extends AbstractTableModel implements ConfigurationListener {
	
	private String[] columnNames = new String[] { "Id", "Head", "Class" };
	private List<HeadActuator> actuators;

	public ActuatorsTableModel(Configuration configuration) {
		configuration.addListener(this);
	}

	public void configurationLoaded(Configuration configuration) {
		actuators = new ArrayList<HeadActuator>();
		for (Head head : configuration.getMachine().getHeads()) {
			for (Actuator actuator : head.getActuators()) {
				actuators.add(new HeadActuator(head, actuator));
			}
		}
		fireTableDataChanged();
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return (actuators == null) ? 0 : actuators.size();
	}
	
	public Actuator getActuator(int index) {
		return actuators.get(index).actuator;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
	
//	@Override
//	public Class<?> getColumnClass(int columnIndex) {
//		if (columnIndex == 2 || columnIndex == 3 || columnIndex == 4) {
//			return LengthCellValue.class;
//		}
//		return super.getColumnClass(columnIndex);
//	}

//	@Override
//	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//		try {
//			// TODO: Evil hack until we move the settable properties for Camera
//			// into the Camera interface.
//			Actuator actuator = (Actuator) actuators.get(rowIndex);
//			if (columnIndex == 0) {
//				camera.setName((String) aValue);
//			}
//			else if (columnIndex == 1) {
//				camera.setLooking((Looking) aValue);
//			}
//			else if (columnIndex == 2) {
//				Length length = ((LengthCellValue) aValue).getLength();
//				Location location = camera.getLocation();
//				if (location.getUnits() == null) {
//					location.setUnits(length.getUnits());
//				}
//				else {
//					location = LengthUtil.convertLocation(location, length.getUnits());
//				}
//				location.setX(length.getValue());
//				camera.setLocation(location);
//			}
//			else if (columnIndex == 3) {
//				Length length = ((LengthCellValue) aValue).getLength();
//				Location location = camera.getLocation();
//				if (location.getUnits() == null) {
//					location.setUnits(length.getUnits());
//				}
//				else {
//					location = LengthUtil.convertLocation(location, length.getUnits());
//				}
//				location.setY(length.getValue());
//				camera.setLocation(location);
//			}
//			else if (columnIndex == 4) {
//				Length length = ((LengthCellValue) aValue).getLength();
//				Location location = camera.getLocation();
//				if (location.getUnits() == null) {
//					location.setUnits(length.getUnits());
//				}
//				else {
//					location = LengthUtil.convertLocation(location, length.getUnits());
//				}
//				location.setZ(length.getValue());
//				camera.setLocation(location);
//			}
//			else if (columnIndex == 5) {
//				camera.getLocation().setRotation(Double.parseDouble(aValue.toString()));
//			}
//			configuration.setDirty(true);
//		}
//		catch (Exception e) {
//			// TODO: dialog, bad input
//		}
//	}
	
	public Object getValueAt(int row, int col) {
		HeadActuator actuator = actuators.get(row);
		switch (col) {
		case 0:
			return actuator.actuator.getId();
		case 1:
			return actuator.head != null ? actuator.head.getId() : "";
		case 2:
			return actuator.actuator.getClass().getSimpleName();
		default:
			return null;
		}
	}
	
	private class HeadActuator {
		public Head head;
		public Actuator actuator;
		
		public HeadActuator(Head head, Actuator actuator) {
			this.head = head;
			this.actuator = actuator;
		}
	}
}