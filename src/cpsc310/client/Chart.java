package cpsc310.client;

import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;

/**
 * Sample chart!!
 *
 */
public class Chart {
	HouseTable table = HouseTable.createHouseTable();
	
	
	public Chart() {
		PieChart pie = new PieChart(createTable(), createOptions());
	}
	
	private Options createOptions() {
	    Options options = Options.create();
	    options.setWidth(400);
	    options.setHeight(240);
	    options.setTitle("My Daily Activities");
	    return options;
	  }
	
	private AbstractDataTable createTable() {
	    DataTable data = DataTable.create();
	    data.addColumn(ColumnType.STRING, "Task");
	    data.addColumn(ColumnType.NUMBER, "Hours per Day");
	    data.addRows(2);
	    data.setValue(0, 0, "Work");
	    data.setValue(0, 1, 14);
	    data.setValue(1, 0, "Sleep");
	    data.setValue(1, 1, 10);
	    return data;
	  }	
}
