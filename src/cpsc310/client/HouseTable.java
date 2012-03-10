package cpsc310.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionModel;

/**
 * 
 * All the table drawing methods in Team_02.java will be refactored 
 * into this class for a clean up.
 *
 */
public class HouseTable {
	
	private static HouseTable houseTable = null;
	private CellTable<HouseData> homesCellTable = null;
	private CellTable.Resources resource = GWT.create(CellTableResources.class);	
	private HouseDataServiceAsync houseDataSvc = GWT.create(HouseDataService.class);
	private AsyncDataProvider<HouseData> dataProvider;
	private SelectionModel<HouseData> selectionModel;
	private int currentStartItem = 0;
	private int pageLength = 0;
	private int databaseLength = 0;
	private List<HouseData> currentHouseList = null;
	private boolean isSearching = false;	

	
	private HouseTable() {
		homesCellTable = createCellTable();
	}
	
	public static HouseTable createHouseTable() {
		if (houseTable == null) {
			houseTable = new HouseTable();
		}
		return houseTable;
	}
	
	/**
	 * Creates homesCellTable.
	 * Creates cell columns, adds those columns to the table,
	 * populates the table using dataProvider, creates sort handler,
	 * sets sort comparators, then sets the column width of the table. 
	 * Note: sorting and populating will be replaced by server-side methods in Sprint 2.
	 */
	private CellTable<HouseData> createCellTable() {
		homesCellTable = new CellTable<HouseData>(15, resource, HouseData.KEY_PROVIDER);	  	

		addColumns(homesCellTable);
		createSort(homesCellTable);
		populateTable(homesCellTable);
		return homesCellTable;
	}
	
	private void addColumns(CellTable<HouseData> cellTable) {
		
		// Create cell columns
	  	TextColumn<HouseData> pidColumn = 
	  			new TextColumn<HouseData>() {
	  		@Override
	  		public String getValue(HouseData house) {
	  			return house.getPID();
	  		}
	  	};
	  	cellTable.addColumn(pidColumn, "PID");
	  	pidColumn.setSortable(true);
	  	
	  	TextColumn<HouseData> addrColumn = 
	  			new TextColumn<HouseData>() {
	  		@Override
	  		public String getValue(HouseData house) {
	  			return house.getAddress();
	  		}
	  	};
	  	cellTable.addColumn(addrColumn, "Address");		
	  	addrColumn.setSortable(true);
	  	
	  	TextColumn<HouseData> postalColumn = 
	  			new TextColumn<HouseData>() {
			@Override
			public String getValue(HouseData house) {
				return house.getPostalCode();
			}
		};
		cellTable.addColumn(postalColumn, "Postal Code");		
	  	postalColumn.setSortable(true);
	  	
	  	Column<HouseData, Number> coordColumn = 
	  			new Column<HouseData, Number>(new NumberCell()) {
	  		@Override
	  		public Number getValue(HouseData house) {
	  			return house.getCoordinate();
	  		}
	  	};
	  	cellTable.addColumn(coordColumn, "Land Coordinate");
	  	coordColumn.setSortable(true);
	  	
	  	Column<HouseData, Number> landValColumn = 
	  			new Column<HouseData, Number>(new NumberCell()) {
	  		@Override
	  		public Number getValue(HouseData house) {
	  			return house.getLandValue();
	  		}
	  	};
	  	cellTable.addColumn(landValColumn, "Current Value");				
	  	landValColumn.setSortable(true);	  	
	  	
	  	TextColumn<HouseData> ownerColumn = 
	  			new TextColumn<HouseData>() {
	  		@Override
	  		public String getValue(HouseData house) {
	  			return house.getOwner();
	  		}
	  	};
	  	cellTable.addColumn(ownerColumn, "Realtor");
	  	ownerColumn.setSortable(true);
	  	
	  	Column<HouseData, Number> priceColumn = 
	  			new Column<HouseData, Number>(new NumberCell()) {
	  		@Override
	  		public Number getValue(HouseData house) {
	  			return house.getPrice();
	  		}
	  	};
	  	cellTable.addColumn(priceColumn, "Price");
	  	priceColumn.setSortable(true);	  		  
	  	
	  	TextColumn<HouseData> isSellingColumn = 
	  			new TextColumn<HouseData>() {
	  		@Override
	  		public String getValue(HouseData house) {
	  			if (house.getIsSelling())
	  				return "For Sale";
	  			return "";
	  		}
	  	};
	  	cellTable.addColumn(isSellingColumn, "Sale");
	  	isSellingColumn.setSortable(true);
	  	
	  	// Create checkBox column		
 		final Column<HouseData, Boolean> selectRowColumn = 
 				new Column<HouseData, Boolean>(new CheckboxCell(true, false)) {
 			@Override
 			public Boolean getValue(HouseData house) {
 				return selectionModel.isSelected(house);
 			}
 		};
 		
 		// Add checkBox column to the table
 		homesCellTable.setColumnWidth(selectRowColumn, 10.0, Unit.PX);
 		homesCellTable.addColumn(selectRowColumn, "Select");	  	
	  	
	  	// Set Column width of longer columns.
 		cellTable.setColumnWidth(addrColumn, 120.0, Unit.PX);
 		cellTable.setColumnWidth(ownerColumn,100.0, Unit.PX);	  	
	}
		
	private void populateTable(final CellTable<HouseData> cellTable) {
		// Initialize the service proxy
		if (houseDataSvc == null) {
			houseDataSvc = GWT.create(HouseDataService.class);
		}		
		
		// Grab database length
		AsyncCallback<Integer> callback = new AsyncCallback<Integer> () {
			@Override
			public void onFailure (Throwable caught) {
				Window.alert(caught.getMessage());	
			}
			@Override
			public void onSuccess (Integer result) {
				dataProvider.updateRowCount(result, true);
				databaseLength = result;
			}
		};
		houseDataSvc.getHouseDatabaseLength(callback);		
		
		// Data provider to populate Table
		dataProvider = new AsyncDataProvider<HouseData>() {
			@Override
			protected void onRangeChanged(HasData<HouseData> display) {
				currentStartItem = display.getVisibleRange().getStart();
				int range = display.getVisibleRange().getLength();
				pageLength = range;
				
				if (isSearching) {
					int end = currentHouseList.size();
					cellTable.setRowData(currentStartItem, currentHouseList.subList(currentStartItem, end));
					return;
				}
				
				AsyncCallback<List<HouseData>> callback = new AsyncCallback<List<HouseData>> () {
					@Override
					public void onFailure (Throwable caught) {
						Window.alert(caught.getMessage());	
					}
					@Override
					public void onSuccess (List<HouseData> result) {
						updateRowData(currentStartItem, result);
					}
				};
				houseDataSvc.getHouses(currentStartItem, range, callback);
			}
		};
		dataProvider.addDataDisplay(homesCellTable);		
		
	}
	
	private void createSort(final CellTable<HouseData> cellTable) {
		// Create sort handler, associate sort handler to the table		
		cellTable.addColumnSortHandler( new ColumnSortEvent.Handler() {
			public void onColumnSort(ColumnSortEvent event) {
				@SuppressWarnings("unchecked")
				Column<HouseData,?> sortedColumn = (Column<HouseData, ?>) event.getColumn();
				int sortedIndex = cellTable.getColumnIndex(sortedColumn);
				List<HouseData> newData = new ArrayList<HouseData>(cellTable.getVisibleItems());
				
				Comparator<HouseData> c = HouseData.HousePidComparator;
				switch(sortedIndex) {
				case 0:
					c = HouseData.HousePidComparator;
					break;
				case 1:
					c = HouseData.HouseAddrComparator;
					break;
				case 2:
					c = HouseData.HousePostalCodeComparator;
					break;
				case 3:
					c = HouseData.HouseCoordinateComparator;
					break;
				case 4:
					c = HouseData.HouseLandValueComparator;
					break;
				case 5:
					c = HouseData.HouseOwnerComparator;
					break;
				case 6:
					c = HouseData.HousePriceComparator;
					break;
				case 7:
					c = HouseData.HouseIsSellingComparator;
					break;
				default:
					break;
				}
				if (event.isSortAscending()) {
					Collections.sort(newData, c);
				}
				else {
					Collections.sort(newData, Collections.reverseOrder(c));
				}
				cellTable.setRowData(cellTable.getPageStart(), newData);
			}
		});
			
		/* Server side sorting for Sprint 2
		AsyncHandler columnSortHandler = new AsyncHandler(homesCellTable);
		homesCellTable.addColumnSortHandler(columnSortHandler);
		*/		
	}
	
	public CellTable<HouseData> getHouseTable() {
		return this.homesCellTable;
	}
	
	public int getPageLength() {
		return this.pageLength;
	}
	
	public int getCurrentStartItem() {
		return this.currentStartItem;
	}
	
	public void enableSelection (SelectionModel<HouseData> model) {
		// Associate the selection model with the table
		homesCellTable.setSelectionModel(model, 
				DefaultSelectionEventManager.<HouseData> createCheckboxManager());
		selectionModel = model;
	}
	
	public void updateTable(List<HouseData> houses, int size, int start, boolean updateWhole) {
		if (updateWhole) {
			dataProvider.updateRowCount(databaseLength, true);
		}
		else {
			dataProvider.updateRowCount(size, true);
		}
		dataProvider.updateRowData(start, houses);
	}
	
	public void setSearch(boolean isSearching, List<HouseData> searchResults) {
		this.isSearching = isSearching;
		this.currentHouseList = searchResults;
	}
	
}
