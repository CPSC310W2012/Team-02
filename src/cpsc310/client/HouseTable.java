package cpsc310.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;


/**
 * Singleton class which encapsulates the CellTable on main UI.
 */
public class HouseTable {
	
	private static HouseTable houseTable = null;
	private CellTable<HouseData> homesCellTable = null;
	private CellTable.Resources resource = GWT.create(CellTableResources.class);	
	private HouseDataServiceAsync houseDataSvc = GWT.create(HouseDataService.class);
	private AsyncDataProvider<HouseData> dataProvider;
	private MultiSelectionModel<HouseData> selectionModel;
	private int currentStartItem = 0;
	private int pageLength = 0;
	private int databaseLength = 0;
	private List<HouseData> currentHouseList = null;
	private boolean isSearching = false;
	private Column<HouseData, String> ownerColumn;
	private Column<HouseData, String> priceColumn;
	private Column<HouseData, String> isSellingColumn;
	private SelectionCell editSellingCell;
	private List<String> category = new ArrayList<String>(2);
	private String[] searchCriteria = 
		{"Address", "Postal Code", "Current Land Value",
			"Current Improvement Value", "Assessment Year", "Previous Land Value", 
			"Previous Improvement Value", "Year Built", "Big Improvement Year",
			"Price", "Realtor", "For Sale"};

	
	/**
	 * Singleton constructor. Create one CellTable which contains HouseData. 
	 */
	private HouseTable() {
		homesCellTable = createCellTable();
	}
	
	/**
	 * Create only one instance for HouseTable
	 * 
	 * @return singleton HouseTable instance
	 */
	public static HouseTable createHouseTable() {
		if (houseTable == null) {
			houseTable = new HouseTable();
		}
		return houseTable;
	}
	
	/**
	 * Build CellTable<HouseData>, assemble sorting, and populate table with HouseData
	 * fetched from server.
	 * 
	 * @return populated CellTable<HouseData> with sorting ability
	 */
	private CellTable<HouseData> createCellTable() {
		homesCellTable = new CellTable<HouseData>(9, resource, HouseData.KEY_PROVIDER);	  	
		addColumns();
		createSort();
		populateTable();
		return homesCellTable;
	}
	
	/**
	 * Helper to createCellTable(). Adds columns to the table.
	 */
	private void addColumns() {	  	
		NumberFormat yearFormat = NumberFormat.getFormat("0000");
		
	  	// Address column
	  	TextColumn<HouseData> addrColumn = new TextColumn<HouseData>() {
	  		@Override
	  		public String getValue(HouseData house) {
	  			return house.getAddress();
	  		}
	  	};
	  	homesCellTable.addColumn(addrColumn, "Address");		
	  	addrColumn.setSortable(true);
	  	
	  	// Postal code column
	  	TextColumn<HouseData> postalColumn = new TextColumn<HouseData>() {
			@Override
			public String getValue(HouseData house) {
				return house.getPostalCode();
			}
		};
		homesCellTable.addColumn(postalColumn, "Postal Code");		
	  	postalColumn.setSortable(true);
	  		  	
	  	// Current Land Value column
	  	Column<HouseData, Number> currlandValColumn = 
	  			new Column<HouseData, Number>(new NumberCell()) {
	  		@Override
	  		public Number getValue(HouseData house) {
	  			return house.getCurrentLandValue();
	  		}
	  	};
	  	homesCellTable.addColumn(currlandValColumn, "Current Land Value");				
	  	currlandValColumn.setSortable(true);
	  	
	  	// Current Improvement Value column
	  	Column<HouseData, Number> currImprovValColumn = 
	  			new Column<HouseData, Number>(new NumberCell()) {
	  		@Override
	  		public Number getValue(HouseData house) {
	  			return house.getCurrentImprovementValue();
	  		}
	  	};
	  	homesCellTable.addColumn(currImprovValColumn, "Current Improvement Value");				
	  	currImprovValColumn.setSortable(true);  	  	
	  	
	  	
	  	// Assessment Year column
	  	Column<HouseData, Number> assYearColumn = 
	  			new Column<HouseData, Number>(new NumberCell(yearFormat)) {
	  		@Override
	  		public Number getValue(HouseData house) {
	  			return house.getAssessmentYear();
	  		}
	  	};
	  	homesCellTable.addColumn(assYearColumn, "Assessment Year");				
	  	assYearColumn.setSortable(true);
	  	
	  	
	  	// Previous Land Value column
	  	Column<HouseData, Number> prevlandValColumn = 
	  			new Column<HouseData, Number>(new NumberCell()) {
	  		@Override
	  		public Number getValue(HouseData house) {
	  			return house.getPreviousLandValue();
	  		}
	  	};
	  	homesCellTable.addColumn(prevlandValColumn, "Previous Land Value");				
	  	prevlandValColumn.setSortable(true);
	  	
	  	// Previous Improvement Value column
	  	Column<HouseData, Number> prevImprovValColumn = 
	  			new Column<HouseData, Number>(new NumberCell()) {
	  		@Override
	  		public Number getValue(HouseData house) {
	  			return house.getPreviousImprovementValue();
	  		}
	  	};
	  	homesCellTable.addColumn(prevImprovValColumn, "Previous Improvement Value");				
	  	prevImprovValColumn.setSortable(true);  	  	
	  	
	  	
	  	// Built Year column
	  	Column<HouseData, Number> yrBuiltColumn = 
	  			new Column<HouseData, Number>(new NumberCell(yearFormat)) {
	  		@Override
	  		public Number getValue(HouseData house) {
	  			return house.getYearBuilt();
	  		}
	  	};
	  	homesCellTable.addColumn(yrBuiltColumn, "Year Built");				
	  	yrBuiltColumn.setSortable(true);
	  	
	  	// Big Improvement Year column
	  	Column<HouseData, Number> improvYearColumn = 
	  			new Column<HouseData, Number>(new NumberCell(yearFormat)) {
	  		@Override
	  		public Number getValue(HouseData house) {
	  			return house.getBigImprovementYear();
	  		}
	  	};
	  	homesCellTable.addColumn(improvYearColumn, "Big Improvement Year");				
	  	improvYearColumn.setSortable(true);
	  	
	  	
	  	/* User specified column begins.
	  	 * User specified columns are class variables because enableEdit() needs to
	  	 * replace following columns with editable cells.
	  	 */
	  	// Realtor column 
	  	ownerColumn = new TextColumn<HouseData>() {
	  		@Override
	  		public String getValue(HouseData house) {
	  			return house.getOwner();
	  		}
	  	};
	  	homesCellTable.addColumn(ownerColumn, "Realtor");
	  	ownerColumn.setSortable(true);
	  	
	  	// Price column
	  	priceColumn = new TextColumn<HouseData>() {
	  		@Override
	  		public String getValue(HouseData house) {
	  			return Double.toString(house.getPrice());
	  		}
	  	};
	  	homesCellTable.addColumn(priceColumn, "Price");
	  	priceColumn.setSortable(true);	  		  
	  	
	  	// For Sale column
	  	isSellingColumn = new TextColumn<HouseData>() {
	  		@Override
	  		public String getValue(HouseData house) {
	  			if (house.getIsSelling())
	  				return "For Sale";
	  			return "";
	  		}
	  	};
	  	homesCellTable.addColumn(isSellingColumn, "Sale");
	  	isSellingColumn.setSortable(true);
	  	  	
	}
	
	/**
	 * Helper to createTable(). Populate table with data fetched by rpc call to the server.
	 */
	private void populateTable() {
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
					homesCellTable.setRowData(currentStartItem, currentHouseList.subList(currentStartItem, end));
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
		
	/**
	 * Helper to createTable(). Creates ColumnSortEvent.Handler for homesCellTable,
	 * attaches comparators to enable sorting, and attaches the hander to the table.
	 */
	private void createSort() {
		// Create sort handler, associate sort handler to the table		
		homesCellTable.addColumnSortHandler( new ColumnSortEvent.Handler() {
			public void onColumnSort(ColumnSortEvent event) {
				@SuppressWarnings("unchecked")
				Column<HouseData,?> sortedColumn = (Column<HouseData, ?>) event.getColumn();
				int sortedIndex = homesCellTable.getColumnIndex(sortedColumn);
				List<HouseData> newData = new ArrayList<HouseData>(homesCellTable.getVisibleItems());
				
				Comparator<HouseData> c = HouseData.HouseAddrComparator;
				switch(sortedIndex) {
				case 0:
					c = HouseData.HouseAddrComparator;
					break;
				case 1:
					c = HouseData.HousePostalCodeComparator;
					break;
				case 2:
					c = HouseData.HouseCurrentLandValueComparator;
					break;
				case 3:
					c = HouseData.HouseCurrentImprovementValueComparator;
					break;
				case 4:
					c = HouseData.HouseAssYearComparator;
					break;
				case 5:
					c = HouseData.HousePrevLandValueComparator;
					break;				
				case 6:
					c = HouseData.HousePrevImprovementValueComparator;
					break;
				case 7:
					c = HouseData.HouseYearBuiltComparator;
					break;
				case 8:
					c = HouseData.HouseBigImprYearComparator;
					break;
				case 9:
					c = HouseData.HouseOwnerComparator;
					break;
				case 10:
					c = HouseData.HousePriceComparator;
					break;
				case 11:
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
				homesCellTable.setRowData(homesCellTable.getPageStart(), newData);
			}
		});
			
		/* Server side sorting for Sprint 2
		AsyncHandler columnSortHandler = new AsyncHandler(homesCellTable);
		homesCellTable.addColumnSortHandler(columnSortHandler);
		*/		
	}
	
	// Getter begins
	/**
	 * Getter for CellTable.
	 * @return CellTable<HouseData>
	 */
	public CellTable<HouseData> getHouseTable() {
		return this.homesCellTable;
	}
	
	/**
	 * Getter for page length
	 * @return length of a cell table page
	 */
	public int getPageLength() {
		return this.pageLength;
	}
	
	/**
	 * Getter for start item
	 * @return index of current start item in the table
	 */
	public int getCurrentStartItem() {
		return this.currentStartItem;
	}
	
	/**
	 * Attach selection model to homesCellTable
	 * @param model MultiSelectionModel
	 */
	public void enableSelection (MultiSelectionModel<HouseData> model) {
		// Associate the selection model with the table
		homesCellTable.setSelectionModel(model);
		selectionModel = model;
	}
	
	/**
	 * Update table with given list of houses.
	 * @param houses - list of houses to populate the table
	 * @param size - number of houses in the list
	 * @param start - index of the start item in the table
	 * @param updateWhole - true if resetting the table to the initial view
	 */
	public void updateTable(List<HouseData> houses, int size, int start, boolean updateWhole) {
		if (updateWhole) {
			dataProvider.updateRowCount(databaseLength, true);
		}
		else {
			dataProvider.updateRowCount(size, true);
		}
		dataProvider.updateRowData(start, houses);
	}
	
	/**
	 * Refresh current view of table
	 */
	public void refreshTable() {
		AsyncCallback<List<HouseData>> callback = new AsyncCallback<List<HouseData>> () {
			@Override
			public void onFailure (Throwable caught) {
				Window.alert(caught.getMessage());
			}
			@Override
			public void onSuccess (List<HouseData> result) {
				dataProvider.updateRowData(currentStartItem, result);

			}
		};
		houseDataSvc.getHouses(currentStartItem, pageLength, callback);
	}
	
	/**
	 * Notify homesCellTable in HouseTable that search is in effect.
	 * If search is in effect, the cell table will display locally stored
	 * search results in currentHouseList.
	 * @param isSearching - true if search is in effect
	 * @param searchResults - results of search to display in table
	 */
	public void setSearch(boolean isSearching, List<HouseData> searchResults) {
		this.isSearching = isSearching;
		this.currentHouseList = searchResults;
	}
	
	/**
	 * Expand the page size of the table.
	 * @param pageSize - size of the page to which the table will expand
	 */
	public void expandElement(int pageSize) {
		this.homesCellTable.setPageSize(pageSize);
	}	
	
}
