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
	 * Enable edit by replacing current User Specified columns
	 * with editable cells and category cells.
	 */
	public void enableEdit() {
		selectionModel.getSelectedSet();
		
		this.homesCellTable.removeColumn(ownerColumn);
		final EditTextCell editOwnerCell = new EditTextCell();
		ownerColumn = 
	  			new Column<HouseData, String>(editOwnerCell) {
	  		@Override
	  		public String getValue(HouseData house) {
	  			return house.getOwner();
	  		}
	  	};
	  	ownerColumn.setFieldUpdater(new FieldUpdater<HouseData, String> () {
	  		 public void update(int index, HouseData house, String owner) {
	  			 int switchValue = 0;
	  			 if (validateInput(owner, switchValue) == 0) {
	  				editOwnerCell.clearViewData(HouseData.KEY_PROVIDER.getKey(house));
	  				homesCellTable.redraw();
	  				Window.alert("Only alphabet characters are allowed for realtor name");
	  				return;
	  			 }
	  			 editHouse(owner, "", "", helpMultiEdit(house), switchValue);
	  		 }
	  	});
	  	this.homesCellTable.addColumn(ownerColumn, "Realtor");
	  	ownerColumn.setSortable(true);
	  	
	  	this.homesCellTable.removeColumn(priceColumn);
	  	final EditTextCell editPriceCell = new EditTextCell();
	  	priceColumn = 
	  			new Column<HouseData, String>(editPriceCell) {
	  		@Override
	  		public String getValue(HouseData house) {
	  			return Double.toString(house.getPrice());
	  		}
	  	};
	  	priceColumn.setFieldUpdater(new FieldUpdater<HouseData, String> () {
	  		 public void update(int index, HouseData house, String price) {
	  			 int switchValue = 1;
	  			 if (validateInput(price, switchValue) == 0) {
	  				editPriceCell.clearViewData(HouseData.KEY_PROVIDER.getKey(house));
	  				homesCellTable.redraw();
	  				Window.alert("Only non-decimal numeric value is allowed for price.");
	  				return;
	  			 }
	  			 editHouse("", price, "", helpMultiEdit(house), switchValue);
	  		 }
	  	});
	  	this.homesCellTable.addColumn(priceColumn, "Price");
	  	priceColumn.setSortable(true);	  		  
	  	
	  	this.homesCellTable.removeColumn(isSellingColumn);
	  	category.add("Yes");
	  	category.add("No");
	  	editSellingCell = new SelectionCell(category);
	  	isSellingColumn = new Column<HouseData, String> (editSellingCell) {
	  		@Override
	  		public String getValue(HouseData house) {
	  			if (house.getIsSelling())
	  				return category.get(0);
	  			return category.get(1);
	  		}
	  	};
	  	isSellingColumn.setFieldUpdater(new FieldUpdater<HouseData, String> () {
	  		 public void update(int index, HouseData house, String isSelling) {
	  			 int switchValue = 2;
	  			 editHouse("", "", isSelling, helpMultiEdit(house), switchValue);
	  		 }
	  	});
	  	this.homesCellTable.addColumn(isSellingColumn, "For Sale");
	}
	
	/**
	 * Update table. If updateWhole is true, just repopulate the table with the initial data
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
	
	/**
	 * Helper to Edit function to enable multiedit.
	 * @param house - house object in which the edit is currently invoked 
	 * @return set of house data specified by selection to edit 
	 */
	private Set<HouseData> helpMultiEdit(HouseData house) {
		Set<HouseData> set;
		if (selectionModel.getSelectedSet().size() > 1) {
			set = selectionModel.getSelectedSet();
		}
		else {
			set = new HashSet<HouseData>(1);
			set.add(house);
		}
		return set;
	}
	
	/**
	 * Validate edit input
	 * @param input - user input into edit cell
	 * @param switchValue - 0 for ownerInput, 1 for priceInput
	 * @return int value 1 if input is valid; 0 if input is invalid; 
	 * -1 if input does not fall into neither cases.
	 */
	private int validateInput (String input, int switchValue) {
		int check = -1;
		switch (switchValue) {
		case 0:
			if (!input.matches("[A-Za-z\\s]*")) {
				check = 0;
			}
			else {
				check = 1;
			}
			return check;
		case 1:
			// Price input must be numerical
			if (!input.matches("\\d*")) {
				check = 0;
			}
			else {
				check = 1;
			}
			return check;
		default:
			return check;
		}
	}
	
	/**
	 * Asynchronous call to update HouseDataPoint in the server database.
	 * @param owner - name of the realtor
	 * @param priceInput - price of the house
	 * @param isSellingInput - for-sale indicator
	 * @param houses - set of houses to update
	 * @param switchValue - 0 for updating owner; 1 for updating price; 2 for updating isSelling
	 */
	private void editHouse(String owner, String priceInput, String isSellingInput, 
			Set<HouseData> houses, int switchValue) {
		
		// Assemble edit request
		double price;
		if (priceInput.isEmpty())	
			price = 0;
		else	
			price = Double.parseDouble(priceInput);
		
		boolean isSelling;
		if (isSellingInput.equals("Yes")) 
			isSelling = true;
		else
			isSelling = false;		
		
		// Initialize the service proxy
		if (houseDataSvc == null) {
			houseDataSvc = GWT.create(HouseDataService.class);
		}
		
		// Set up the callback object
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
			public void onSuccess(Void result) {
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
				houseDataSvc.getHouses(0, pageLength, callback);
			}
		};
				
		// make the call to the house data service
		// TODO need to update to changing only one house using method
		//updateHouse(String Owner, int price, boolean isSelling, HouseData house, double longitude, double latitude)
//		houseDataSvc.updateHouses(owner, price, isSelling, houses, switchValue, callback);
	}
}
