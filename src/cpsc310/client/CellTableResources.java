package cpsc310.client;

import com.google.gwt.user.cellview.client.CellTable;

/**
 * For attaching custom styling to CellTable.
 *
 */
public interface CellTableResources extends CellTable.Resources {

	interface TableStyle extends CellTable.Style {
	}
	
	@Override
    @Source({ CellTable.Style.DEFAULT_CSS, "CellTable.css" })
    TableStyle cellTableStyle();
}