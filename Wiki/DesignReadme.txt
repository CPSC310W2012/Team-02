Team Name: XD (Extreme Developers)

Note: see wiki for documented diagrams

Team Members:
Justin Chu	17685074	d2t6
Bessie (Bik San) Chan	15072077	w1x6
Jae Yun Lee	35901099	e4y7
Richard Mar	68573062	y0c7

Class Diagram Explanations
Class Descriptions:
ServerObserver: Checks the Data Vancouver server for updates and tells the iVanSystem if the DataBase needs to be updated.
iVanSystem: Mediates communication between the database, displayers and UI.
Displayer: Takes housedatapoints and presents them for display.
DataBase: Holds the information retrieved from the Data Vancouver server.
HouseDataPoint: Contains the information for a particular house.
User: Accounts for Realtors.
FileParser: Parse for files.

Sequence Diagrams
SequenceDiagram_DataUpdate_v1-0.jpg
Server interaction: “Update homes information from Data Vancouver”

SequenceDiagram_MapRetrival_v1-0.jpg
- GUI and Database interaction: “Display homes in a specific price range on the map”

Class Diagrams

UML_Draft1.jpg
- first draft of our class diagram

UML_Draft2.jpg
- second draft of our class diagram

UML_Final_v1-0.jpg
- final copy of our class diagram as of 16/2/2012

GUIMockup1.jpg, GUIMockup2.jpg
- GUI mockup, contains diagrams for the login page, search criteria box upon login and results table

A5 - GUI Mockup

GWT HorizontalPanel
GWT VerticalPanel
GWT FlexTable
GWT TextBox
GWT PasswordBox
GWT Label
GWT Button
GWT Maps

Organizational Containers - HorizontalPanel, VerticalPanel
Login Button - Button
Search box - HorizontalPanel, TextBox, Label, Button
Data display - FlexTable
Map - GWT Map
Login Panel - HorizontalPanel, TextBox, PasswordBox, Button, Label
Edit Homes Button - Button
Edit Home Panel - HorizontalPanel, TextBox, PasswordBox, Button, Label

Logo images, button/table/page/font style: html/css