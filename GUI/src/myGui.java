import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class myGui extends Thread {

	protected Shell shlOopApplication;
	private Text FolderPath;
	private Text CsvFilePath;
	private Text Start;
	private Text End;
	private Text ID_name;
	private Text LatMin;
	private Text LatMax;
	private Text LonMin;
	private Text LonMax;
	private Text AltMin;
	private Text AltMax;
	private Text MacAlgo1;
	private Text Algo2Mac1;
	private Text Algo2Mac2;
	private Text Algo2Mac3;
	private Text Algo2Sig1;
	private Text Algo2Sig2;
	private Text Algo2Sig3;
	
	public List<ArrayList<String>> wiggledata;
	public List<ArrayList<String>> Data;
	public List<ArrayList<String>> DataBackUp;
	
	public myGui()
	{
		this.wiggledata=new ArrayList<ArrayList<String>>();
		this.Data=new ArrayList<ArrayList<String>>();
		this.DataBackUp=new ArrayList<ArrayList<String>>();
	}
	
	static myGui Glist=new myGui();
	
	static boolean timebox=false;
	static boolean locationbox=false;
	static boolean idbox=false;
	static boolean and=false;
	static boolean or=false;
	static boolean none=false;
	static boolean timenotbox=false;
	static boolean locationnotbox=false;
	static boolean idnotbox=false;
	static String outputpath = "/Users/amihaitorgeman/Desktop/NewMatala/Output/outputfile.csv";
	
	static boolean TerminateThread=false;
	static Thread thread1;
	static Thread thread2;
	private Text IP;
	private Text PORT;
	private Text UserName;
	private Text PASS;
	private Text DataBase;
	private Text TableName;
	

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			myGui window = new myGui();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlOopApplication.open();
		shlOopApplication.layout();
		while (!shlOopApplication.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();		
				TerminateThread=true;
			}
		}
		if(TerminateThread=true)
		{
			thread1.interrupt();
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlOopApplication = new Shell();
		shlOopApplication.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
		shlOopApplication.setSize(1000, 1000);
		shlOopApplication.setText("OOP application");
		
		FolderPath = new Text(shlOopApplication, SWT.BORDER);
		FolderPath.setBounds(229, 55, 129, 19);
		
		Label lblGui = new Label(shlOopApplication, SWT.NONE);
		lblGui.setBounds(238, 25, 59, 14);
		lblGui.setText("GUI OOP");
		
		Label lblFolder = new Label(shlOopApplication, SWT.NONE);
		lblFolder.setBounds(179, 58, 59, 14);
		lblFolder.setText("Folder:");
		
		Button btnAdd = new Button(shlOopApplication, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String folderpath=FolderPath.getText();
				
				thread1=new Thread(new Watcher(folderpath));
			
				thread1.start();
				
				InputCsv readfolder=new InputCsv(folderpath);
				try {
					Glist.wiggledata=readfolder.importfiles();
					HashMap<position, List<Scan>> map=new HashMap();
					map=readfolder.makemap(Glist.wiggledata);
					readfolder.makefinaltable(map);//file name will be first
					MakeKML importdata=new MakeKML(outputpath);
					Glist.Data=importdata.inputthefile();
					Glist.DataBackUp=Glist.Data;
					File del=new File(outputpath);
					del.delete();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnAdd.setBounds(229, 80, 94, 28);
		btnAdd.setText("ADD FOLDER");
		
		Label lblCsvFile = new Label(shlOopApplication, SWT.NONE);
		lblCsvFile.setBounds(179, 127, 59, 14);
		lblCsvFile.setText("CSV File:");
		
		CsvFilePath = new Text(shlOopApplication, SWT.BORDER);
		CsvFilePath.setBounds(233, 122, 129, 19);
		
		Button btnAddCsv = new Button(shlOopApplication, SWT.NONE);
		btnAddCsv.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String filepath=CsvFilePath.getText();
				MakeKML importfile=new MakeKML(outputpath);
				Glist.DataBackUp=importfile.inputthefile(filepath);
				for(int i=0; i<Glist.DataBackUp.size(); i++)
				{
					if(!(Glist.Data.contains(Glist.DataBackUp.get(i))))
					{
						Glist.Data.add(Glist.DataBackUp.get(i));
					}
				}
				Glist.DataBackUp=Glist.Data;
			}
		});
		btnAddCsv.setBounds(229, 147, 94, 28);
		btnAddCsv.setText("ADD CSV");
		
		Button btnDeleteData = new Button(shlOopApplication, SWT.NONE);
		btnDeleteData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Glist.Data.clear();
			}
		});
		btnDeleteData.setBounds(238, 190, 105, 28);
		btnDeleteData.setText("DELETE DATA");
		
		Label Entries = new Label(shlOopApplication, SWT.NONE);
		Entries.setBounds(20, 80, 95, 14);
		Entries.setText("Entries:");
		
		Label Wifis = new Label(shlOopApplication, SWT.NONE);
		Wifis.setBounds(20, 114, 95, 14);
		Wifis.setText("WIFI's:");
		
		Button btnDisplayInfo = new Button(shlOopApplication, SWT.NONE);
		btnDisplayInfo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Entries.setText("Entries: "+ Glist.Data.size());
				MakeKML count=new MakeKML(outputpath);
				Wifis.setText("WIFI's: "+ count.wificount(Glist.Data));
			}
		});
		btnDisplayInfo.setBounds(10, 46, 105, 28);
		btnDisplayInfo.setText("DISPLAY INFO");
		
		Label lblNewLabel = new Label(shlOopApplication, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 11, SWT.BOLD));
		lblNewLabel.setBounds(264, 238, 59, 14);
		lblNewLabel.setText("Fillters");
		
		Button btnFillterByTime = new Button(shlOopApplication, SWT.CHECK);
		btnFillterByTime.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(timebox==false) {
					timebox=true;
				}
				else
				{
					timebox=false;
				}
			}
		});
		btnFillterByTime.setBounds(47, 266, 94, 18);
		btnFillterByTime.setText("Filter by time");
		
		Button btnCheckButton = new Button(shlOopApplication, SWT.CHECK);
		btnCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(idbox==false)
				{
					idbox=true;
				}
				else
				{
					idbox=false;
				}
			}
		});
		btnCheckButton.setBounds(229, 269, 94, 18);
		btnCheckButton.setText("Fillter by ID");
		
		Button btnFillterByPosition = new Button(shlOopApplication, SWT.CHECK);
		btnFillterByPosition.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(locationbox==false)
				{
					locationbox=true;
				}
				else
				{
					locationbox=false;
				}
			}
		});
		btnFillterByPosition.setBounds(418, 272, 113, 18);
		btnFillterByPosition.setText("Fillter by position");
		
		Button btnNo = new Button(shlOopApplication, SWT.CHECK);
		btnNo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(timenotbox==false)
				{
					timenotbox=true;
				}
				else
				{
					timenotbox=false;
				}
			}
		});
		btnNo.setBounds(47, 299, 94, 18);
		btnNo.setText("NO");
		
		Button btnNo_1 = new Button(shlOopApplication, SWT.CHECK);
		btnNo_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(idnotbox==false)
				{
					idnotbox=true;
				}
				else
				{
					idnotbox=false;
				}
			}
		});
		btnNo_1.setBounds(229, 299, 94, 18);
		btnNo_1.setText("NO");
		
		Button btnNo_2 = new Button(shlOopApplication, SWT.CHECK);
		btnNo_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(locationnotbox==false)
				{
					locationnotbox=true;
				}
				else
				{
					locationnotbox=false;
				}
			}
		});
		btnNo_2.setBounds(418, 302, 94, 18);
		btnNo_2.setText("NO");
		
		Label lblNewLabel_1 = new Label(shlOopApplication, SWT.NONE);
		lblNewLabel_1.setBounds(33, 324, 59, 14);
		lblNewLabel_1.setText("Start:");
		
		Start = new Text(shlOopApplication, SWT.BORDER);
		Start.setBounds(97, 323, 64, 19);
		
		End = new Text(shlOopApplication, SWT.BORDER);
		End.setBounds(97, 355, 64, 19);
		
		Label lblEnd = new Label(shlOopApplication, SWT.NONE);
		lblEnd.setBounds(33, 358, 59, 14);
		lblEnd.setText("End:");
		
		Label lblId = new Label(shlOopApplication, SWT.NONE);
		lblId.setBounds(229, 324, 33, 14);
		lblId.setText("ID:");
		
		ID_name = new Text(shlOopApplication, SWT.BORDER);
		ID_name.setBounds(263, 323, 77, 19);
		
		LatMin = new Text(shlOopApplication, SWT.BORDER);
		LatMin.setBounds(457, 339, 64, 19);
		
		Label lblLatMin = new Label(shlOopApplication, SWT.NONE);
		lblLatMin.setBounds(392, 342, 59, 14);
		lblLatMin.setText("LAT min");
		
		Label label = new Label(shlOopApplication, SWT.NONE);
		label.setBounds(377, 377, 18, -3);
		label.setText("New Label");
		
		Label lblLatMax = new Label(shlOopApplication, SWT.NONE);
		lblLatMax.setBounds(392, 378, 59, 14);
		lblLatMax.setText("LAT max");
		
		Label lblLongMin = new Label(shlOopApplication, SWT.NONE);
		lblLongMin.setBounds(392, 422, 59, 14);
		lblLongMin.setText("LON min");
		
		Label lblL = new Label(shlOopApplication, SWT.NONE);
		lblL.setBounds(392, 451, 59, 14);
		lblL.setText("LON max");
		
		Label lblAltMin = new Label(shlOopApplication, SWT.NONE);
		lblAltMin.setBounds(392, 483, 59, 14);
		lblAltMin.setText("ALT min");
		
		Label lblAltMax = new Label(shlOopApplication, SWT.NONE);
		lblAltMax.setBounds(392, 517, 59, 14);
		lblAltMax.setText("ALT max");
		
		LatMax = new Text(shlOopApplication, SWT.BORDER);
		LatMax.setBounds(457, 377, 64, 19);
		
		LonMin = new Text(shlOopApplication, SWT.BORDER);
		LonMin.setBounds(457, 422, 64, 19);
		
		LonMax = new Text(shlOopApplication, SWT.BORDER);
		LonMax.setBounds(457, 451, 64, 19);
		
		AltMin = new Text(shlOopApplication, SWT.BORDER);
		AltMin.setBounds(457, 478, 64, 19);
		
		AltMax = new Text(shlOopApplication, SWT.BORDER);
		AltMax.setBounds(457, 512, 64, 19);
		
		Button button = new Button(shlOopApplication, SWT.RADIO);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				and=true;
				none=false;
				or=false;
			}
		});
		button.setBounds(117, 447, 90, 18);
		button.setText("&&");
		
		Button button_1 = new Button(shlOopApplication, SWT.RADIO);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				or=true;
				and=false;
				none=false;
			}
		});
		button_1.setBounds(117, 479, 90, 18);
		button_1.setText("||");
		
		Button btnWithout = new Button(shlOopApplication, SWT.RADIO);
		btnWithout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				none=true;
				and=false;
				or=false;
			}
		});
		btnWithout.setBounds(117, 513, 90, 18);
		btnWithout.setText("Without");
		
		Label CurrentFilter = new Label(shlOopApplication, SWT.NONE);
		CurrentFilter.setBounds(20, 569, 512, 19);
		CurrentFilter.setText("Current Filter:");
		
		Button btnActiveFilters = new Button(shlOopApplication, SWT.NONE);
		btnActiveFilters.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(none==true)
				{
					Filters filt=new Filters(Glist.Data);
					if(timebox==true)
					{
						String from=Start.getText();
						String to=End.getText();
						filt.FilterTime(from, to, timenotbox);
						Glist.Data=filt.Data;
						if(timenotbox==false)
						{
							CurrentFilter.setText("Filtered by: {"+from+"<Time<"+to+"}");
						}
						else
						{
							CurrentFilter.setText("Filtered by: {!("+from+"<Time<"+to+")}");	
						}
					}
					if(locationbox==true)
					{
						double latmin=Double.parseDouble(LatMin.getText());
						double latmax=Double.parseDouble(LatMax.getText());
						double lonmin=Double.parseDouble(LonMin.getText());
						double lonmax=Double.parseDouble(LonMax.getText());
						double altmin=Double.parseDouble(AltMin.getText());
						double altmax=Double.parseDouble(AltMax.getText());
						filt.FilterLoc(latmin, latmax, lonmin, lonmax, altmin, altmax, locationnotbox);
						Glist.Data=filt.Data;
						if(locationnotbox==false)
						{
							CurrentFilter.setText("Filtered by: {"+latmin+"<Latitude<"+latmax+" ,"+lonmin+"<Longitude<"+lonmax+" ,"+altmin+"<Altitude<"+altmax+"}");
						}
						else
						{
							CurrentFilter.setText("Filtered by: {!("+latmin+"<Latitude<"+latmax+" ,"+lonmin+"<Longitude<"+lonmax+" ,"+altmin+"<Altitude<"+altmax+")}");
						}
					}
					if(idbox==true)
					{
						String id=ID_name.getText();
						filt.FilterID(id, idnotbox);
						Glist.Data=filt.Data;
						if(idnotbox==false)
						{
							CurrentFilter.setText("Filtered by: {Device name="+id+"}");
						}
						else
						{
							CurrentFilter.setText("Filtered by: {!(Device name="+id+")}");
						}
					}
				}
				if(and==true)
				{
					Filters filt=new Filters(Glist.Data);
					List<ArrayList<String>> temp=new ArrayList<ArrayList<String>>();
					if(timebox==true && locationbox==true)
					{
						String from=Start.getText();
						String to=End.getText();
						filt.FilterTime(from, to, timenotbox);
						temp=filt.Data;
						Filters nextfilt=new Filters(temp);
						double latmin=Double.parseDouble(LatMin.getText());
						double latmax=Double.parseDouble(LatMax.getText());
						double lonmin=Double.parseDouble(LonMin.getText());
						double lonmax=Double.parseDouble(LonMax.getText());
						double altmin=Double.parseDouble(AltMin.getText());
						double altmax=Double.parseDouble(AltMax.getText());
						nextfilt.FilterLoc(latmin, latmax, lonmin, lonmax, altmin, altmax, locationnotbox);
						Glist.Data=nextfilt.Data;
						if(timenotbox==false && locationnotbox==false)//time & location
						{
							CurrentFilter.setText("Filtered by: {"+from+"<Time<"+to+"} && {"+latmin+"<Latitude<"+latmax+" ,"+lonmin+"<Longitude<"+lonmax+" ,"+altmin+"<Altitude<"+altmax+"}");	
						}
						else if(timenotbox==true && locationnotbox==false)//not time & location
						{
							CurrentFilter.setText("Filtered by: {!("+from+"<Time<"+to+")} && {"+latmin+"<Latitude<"+latmax+" ,"+lonmin+"<Longitude<"+lonmax+" ,"+altmin+"<Altitude<"+altmax+"}");	
						}
						else if(timenotbox==false && locationnotbox==true)//time & not location
						{
							CurrentFilter.setText("Filtered by: {"+from+"<Time<"+to+"} && {!("+latmin+"<Latitude<"+latmax+" ,"+lonmin+"<Longitude<"+lonmax+" ,"+altmin+"<Altitude<"+altmax+")}");	
						}
						else // not time & not location
						{
							CurrentFilter.setText("Filtered by: {!("+from+"<Time<"+to+")} && {!("+latmin+"<Latitude<"+latmax+" ,"+lonmin+"<Longitude<"+lonmax+" ,"+altmin+"<Altitude<"+altmax+")}"); 	
						}
					}
					if(timebox==true && idbox==true)
					{
						String from=Start.getText();
						String to=End.getText();
						filt.FilterTime(from, to, timenotbox);
						temp=filt.Data;
						Filters nextfilt=new Filters(temp);
						String id=ID_name.getText();
						nextfilt.FilterID(id, idnotbox);
						Glist.Data=nextfilt.Data;
						if(timenotbox==false && idnotbox==false)// time & ID
						{
							CurrentFilter.setText("Filtered by: {"+from+"<Time<"+to+"} && {Device name="+id+"}");	
						}
						else if(timenotbox==true && idnotbox==false)//not time & ID
						{
							CurrentFilter.setText("Filtered by: {!("+from+"<Time<"+to+")} && {Device name="+id+"}");  		
						}
						else if(timenotbox==false && idnotbox==true)//time & not ID
						{
							CurrentFilter.setText("Filtered by: {"+from+"<Time<"+to+"} && {!(Device name="+id+")}"); 
						}
						else//not time & not ID
						{
							CurrentFilter.setText("Filtered by: {!("+from+"<Time<"+to+")} && {!(Device name="+id+")}"); 	
						}
					}
					if(locationbox==true && idbox==true)
					{
						String id=ID_name.getText();
						filt.FilterID(id, idnotbox);
						temp=filt.Data;
						Filters nextfilt=new Filters(temp);
						double latmin=Double.parseDouble(LatMin.getText());
						double latmax=Double.parseDouble(LatMax.getText());
						double lonmin=Double.parseDouble(LonMin.getText());
						double lonmax=Double.parseDouble(LonMax.getText());
						double altmin=Double.parseDouble(AltMin.getText());
						double altmax=Double.parseDouble(AltMax.getText());
						nextfilt.FilterLoc(latmin, latmax, lonmin, lonmax, altmin, altmax, locationnotbox);
						Glist.Data=nextfilt.Data;
						if(idnotbox==false && locationnotbox==false)//ID & location
						{
							CurrentFilter.setText("Filtered by: {Device name="+id+"} && {"+latmin+"<Latitude<"+latmax+" ,"+lonmin+"<Longitude<"+lonmax+" ,"+altmin+"<Altitude<"+altmax+"}");	 
						}
						else if(idnotbox==true && locationnotbox==false)// not id && location
						{
							CurrentFilter.setText("Filtered by: {!(Device name="+id+")} && {"+latmin+"<Latitude<"+latmax+" ,"+lonmin+"<Longitude<"+lonmax+" ,"+altmin+"<Altitude<"+altmax+"}");
						}
						else if(idnotbox==false && locationnotbox==true)//id && not location
						{
							CurrentFilter.setText("Filtered by: {Device name="+id+"} && {!("+latmin+"<Latitude<"+latmax+" ,"+lonmin+"<Longitude<"+lonmax+" ,"+altmin+"<Altitude<"+altmax+")}");  
						}
						else//not id && not location
						{
							CurrentFilter.setText("Filtered by: {!(Device name="+id+")} && {!("+latmin+"<Latitude<"+latmax+" ,"+lonmin+"<Longitude<"+lonmax+" ,"+altmin+"<Altitude<"+altmax+")}");
						}
					}
				}
				if(or==true)
				{
					List<ArrayList<String>> Data1=new ArrayList<ArrayList<String>>();
					List<ArrayList<String>> Data2=new ArrayList<ArrayList<String>>();
					Filters filt1=new Filters(Glist.Data);
					Filters filt2=new Filters(Glist.Data);
					if(timebox==true && locationbox==true)
					{
						String from=Start.getText();
						String to=End.getText();
						filt1.FilterTime(from, to, timenotbox);
						Data1=filt1.Data;
						double latmin=Double.parseDouble(LatMin.getText());
						double latmax=Double.parseDouble(LatMax.getText());
						double lonmin=Double.parseDouble(LonMin.getText());
						double lonmax=Double.parseDouble(LonMax.getText());
						double altmin=Double.parseDouble(AltMin.getText());
						double altmax=Double.parseDouble(AltMax.getText());
						filt2.FilterLoc(latmin, latmax, lonmin, lonmax, altmin, altmax, locationnotbox);
						Data2=filt2.Data;
						Glist.Data=Filters.MergeData(Data1, Data2);
						if(timenotbox==false && locationnotbox==false)//time & location
						{
							CurrentFilter.setText("Filtered by: {"+from+"<Time<"+to+"} || {"+latmin+"<Latitude<"+latmax+" ,"+lonmin+"<Longitude<"+lonmax+" ,"+altmin+"<Altitude<"+altmax+"}");	
						}
						else if(timenotbox==true && locationnotbox==false)//not time & location
						{
							CurrentFilter.setText("Filtered by: {!("+from+"<Time<"+to+")} || {"+latmin+"<Latitude<"+latmax+" ,"+lonmin+"<Longitude<"+lonmax+" ,"+altmin+"<Altitude<"+altmax+"}");	
						}
						else if(timenotbox==false && locationnotbox==true)//time & not location
						{
							CurrentFilter.setText("Filtered by: {"+from+"<Time<"+to+"} || {!("+latmin+"<Latitude<"+latmax+" ,"+lonmin+"<Longitude<"+lonmax+" ,"+altmin+"<Altitude<"+altmax+")}");	
						}
						else // not time & not location
						{
							CurrentFilter.setText("Filtered by: {!("+from+"<Time<"+to+")} || {!("+latmin+"<Latitude<"+latmax+" ,"+lonmin+"<Longitude<"+lonmax+" ,"+altmin+"<Altitude<"+altmax+")}"); 	
						}

					}
					if(timebox==true && idbox==true)
					{
						String from=Start.getText();
						String to=End.getText();
						filt1.FilterTime(from, to, timenotbox);
						Data1=filt1.Data;
						String id=ID_name.getText();
						filt2.FilterID(id, idnotbox);
						Data2=filt2.Data;
						Glist.Data=Filters.MergeData(Data1, Data2);
						if(timenotbox==false && idnotbox==false)// time & ID
						{
							CurrentFilter.setText("Filtered by: {"+from+"<Time<"+to+"} || {Device name="+id+"}");	
						}
						else if(timenotbox==true && idnotbox==false)//not time & ID
						{
							CurrentFilter.setText("Filtered by: {!("+from+"<Time<"+to+")} || {Device name="+id+"}");  		
						}
						else if(timenotbox==false && idnotbox==true)//time & not ID
						{
							CurrentFilter.setText("Filtered by: {"+from+"<Time<"+to+"} || {!(Device name="+id+")}"); 
						}
						else//not time & not ID
						{
							CurrentFilter.setText("Filtered by: {!("+from+"<Time<"+to+")} || {!(Device name="+id+")}"); 	
						}
					}
					if(locationbox==true && idbox==true)
					{
						String id=ID_name.getText();
						filt1.FilterID(id, idnotbox);
						Data1=filt1.Data;
						double latmin=Double.parseDouble(LatMin.getText());
						double latmax=Double.parseDouble(LatMax.getText());
						double lonmin=Double.parseDouble(LonMin.getText());
						double lonmax=Double.parseDouble(LonMax.getText());
						double altmin=Double.parseDouble(AltMin.getText());
						double altmax=Double.parseDouble(AltMax.getText());
						filt2.FilterLoc(latmin, latmax, lonmin, lonmax, altmin, altmax, locationnotbox);
						Data2=filt2.Data;
						Glist.Data=Filters.MergeData(Data1, Data2);
						if(idnotbox==false && locationnotbox==false)//ID & location
						{
							CurrentFilter.setText("Filtered by: {Device name="+id+"} || {"+latmin+"<Latitude<"+latmax+" ,"+lonmin+"<Longitude<"+lonmax+" ,"+altmin+"<Altitude<"+altmax+"}");	 
						}
						else if(idnotbox==true && locationnotbox==false)// not id && location
						{
							CurrentFilter.setText("Filtered by: {!(Device name="+id+")} || {"+latmin+"<Latitude<"+latmax+" ,"+lonmin+"<Longitude<"+lonmax+" ,"+altmin+"<Altitude<"+altmax+"}");
						}
						else if(idnotbox==false && locationnotbox==true)//id && not location
						{
							CurrentFilter.setText("Filtered by: {Device name="+id+"} || {!("+latmin+"<Latitude<"+latmax+" ,"+lonmin+"<Longitude<"+lonmax+" ,"+altmin+"<Altitude<"+altmax+")}");  
						}
						else//not id && not location
						{
							CurrentFilter.setText("Filtered by: {!(Device name="+id+")} || {!("+latmin+"<Latitude<"+latmax+" ,"+lonmin+"<Longitude<"+lonmax+" ,"+altmin+"<Altitude<"+altmax+")}");
						}
					}
				}
			}

			
		});
		btnActiveFilters.setBounds(10, 617, 94, 28);
		btnActiveFilters.setText("Active Filters");
		
		Button btnResetFilters = new Button(shlOopApplication, SWT.NONE);
		btnResetFilters.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				Glist.Data=Glist.DataBackUp;
				CurrentFilter.setText("Current Filter: reseted");
			}
		});
		btnResetFilters.setBounds(10, 651, 94, 28);
		btnResetFilters.setText("Reset Filters");
		
		Button btnSaveCsvFile = new Button(shlOopApplication, SWT.NONE);
		btnSaveCsvFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				InputCsv savecsvfile=new InputCsv(FolderPath.getText());
				try {
					savecsvfile.makefinaltable(Glist.Data);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		btnSaveCsvFile.setBounds(10, 685, 94, 28);
		btnSaveCsvFile.setText("Save CSV file");
		
		Button btnSaveKmlFile = new Button(shlOopApplication, SWT.NONE);
		btnSaveKmlFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MakeKML makekmlfile=new MakeKML(outputpath);
				try {
					makekmlfile.writetoKML(Glist.Data);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSaveKmlFile.setBounds(10, 714, 105, 28);
		btnSaveKmlFile.setText("Save KML file");
		
		Label lblAlgorithms = new Label(shlOopApplication, SWT.NONE);
		lblAlgorithms.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 13, SWT.BOLD | SWT.ITALIC));
		lblAlgorithms.setBounds(756, 31, 105, 14);
		lblAlgorithms.setText("ALGORITHMS");
		
		Label lblFirstAlgorithm = new Label(shlOopApplication, SWT.NONE);
		lblFirstAlgorithm.setBounds(642, 100, 90, 14);
		lblFirstAlgorithm.setText("Algorithm 1");
		
		Label lblMacAddress = new Label(shlOopApplication, SWT.NONE);
		lblMacAddress.setBounds(642, 133, 75, 14);
		lblMacAddress.setText("MAC address:");
		
		MacAlgo1 = new Text(shlOopApplication, SWT.BORDER);
		MacAlgo1.setBounds(727, 130, 84, 19);
		
		Label Algo1Lat = new Label(shlOopApplication, SWT.NONE);
		Algo1Lat.setBounds(644, 166, 167, 14);
		Algo1Lat.setText("LAT");
		
		Label Algo1Lon = new Label(shlOopApplication, SWT.NONE);
		Algo1Lon.setBounds(642, 193, 169, 14);
		Algo1Lon.setText("LON");
		
		Label Algo1Alt = new Label(shlOopApplication, SWT.NONE);
		Algo1Alt.setBounds(642, 220, 169, 14);
		Algo1Alt.setText("ALT");
		
		Button btnActive = new Button(shlOopApplication, SWT.NONE);
		btnActive.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String Mac=MacAlgo1.getText();
				Algorithems temp= new Algorithems(Glist.Data);
				Point answer=temp.MacLocation(Mac);
				Algo1Lat.setText("Lat: "+answer.getLat());
				Algo1Lon.setText("Lon: "+answer.getLon());
				Algo1Alt.setText("Alt: "+answer.getAlt());
				
			}
		});
		btnActive.setBounds(727, 93, 94, 28);
		btnActive.setText("ACTIVE");
		
		Label lblAlgorithm = new Label(shlOopApplication, SWT.NONE);
		lblAlgorithm.setBounds(642, 277, 75, 14);
		lblAlgorithm.setText("Algorithm 2");
		
		Label Algo2Lat = new Label(shlOopApplication, SWT.NONE);
		Algo2Lat.setBounds(642, 412, 169, 14);
		Algo2Lat.setText("LAT");
		
		Label Algo2Lon = new Label(shlOopApplication, SWT.NONE);
		Algo2Lon.setBounds(642, 434, 167, 14);
		Algo2Lon.setText("LON");
		
		Label Algo2Alt = new Label(shlOopApplication, SWT.NONE);
		Algo2Alt.setBounds(642, 456, 169, 14);
		Algo2Alt.setText("ALT");
		
		Button btnActive_1 = new Button(shlOopApplication, SWT.NONE);
		btnActive_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String mac1=Algo2Mac1.getText();
				String mac2=Algo2Mac2.getText();
				String mac3=Algo2Mac3.getText();
				int signal1=Integer.parseInt(Algo2Sig1.getText());
				int signal2=Integer.parseInt(Algo2Sig2.getText());
				int signal3=Integer.parseInt(Algo2Sig3.getText());
				Mac_Signal[] array=new Mac_Signal[3];
				array[0]=new Mac_Signal(mac1,signal1);
				array[1]=new Mac_Signal(mac2,signal2);
				array[2]=new Mac_Signal(mac3,signal3);
				Algorithems temp2=new Algorithems(Glist.Data);
				Point answer=temp2.GetUserLocation(array);
				Algo2Lat.setText("Lat: "+answer.getLat());
				Algo2Lon.setText("Lon: "+answer.getLon());
				Algo2Alt.setText("Alt: "+answer.getAlt());
			}
		});
		btnActive_1.setBounds(717, 272, 94, 28);
		btnActive_1.setText("ACTIVE");
		
		Label lblMac = new Label(shlOopApplication, SWT.NONE);
		lblMac.setBounds(642, 311, 59, 14);
		lblMac.setText("MAC1");
		
		Label lblMac_1 = new Label(shlOopApplication, SWT.NONE);
		lblMac_1.setBounds(644, 368, 59, 14);
		lblMac_1.setText("MAC3");
		
		Label lblMac_2 = new Label(shlOopApplication, SWT.NONE);
		lblMac_2.setBounds(644, 339, 59, 14);
		lblMac_2.setText("MAC2");
		
		Algo2Mac1 = new Text(shlOopApplication, SWT.BORDER);
		Algo2Mac1.setBounds(690, 306, 64, 19);
		
		Algo2Mac2 = new Text(shlOopApplication, SWT.BORDER);
		Algo2Mac2.setBounds(690, 334, 64, 19);
		
		Algo2Mac3 = new Text(shlOopApplication, SWT.BORDER);
		Algo2Mac3.setBounds(690, 368, 64, 19);
		
		Label lblSignal = new Label(shlOopApplication, SWT.NONE);
		lblSignal.setBounds(783, 311, 59, 14);
		lblSignal.setText("SIGNAL1");
		
		Label lblSignal_1 = new Label(shlOopApplication, SWT.NONE);
		lblSignal_1.setBounds(783, 339, 59, 14);
		lblSignal_1.setText("SIGNAL2");
		
		Label lblSignal_2 = new Label(shlOopApplication, SWT.NONE);
		lblSignal_2.setBounds(783, 368, 59, 14);
		lblSignal_2.setText("SIGNAL3");
		
		Algo2Sig1 = new Text(shlOopApplication, SWT.BORDER);
		Algo2Sig1.setBounds(845, 306, 64, 19);
		
		Algo2Sig2 = new Text(shlOopApplication, SWT.BORDER);
		Algo2Sig2.setBounds(845, 339, 64, 19);
		
		Algo2Sig3 = new Text(shlOopApplication, SWT.BORDER);
		Algo2Sig3.setBounds(845, 363, 64, 19);
		
		Button btnKeepFilters = new Button(shlOopApplication, SWT.NONE);
		btnKeepFilters.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				InputCsv savecsvfile=new InputCsv(FolderPath.getText());
				try {
					savecsvfile.makefinaltable(Glist.Data);
					MessageDialog.openConfirm(shlOopApplication, "notice!", "Filters Saved!");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnKeepFilters.setBounds(168, 617, 94, 28);
		btnKeepFilters.setText("Keep Filters");
		
		Button btnActiveKeptFilters = new Button(shlOopApplication, SWT.NONE);
		btnActiveKeptFilters.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				MessageDialog.openConfirm(shlOopApplication, "notice!", "Filter Used");
				//Todo  I need to ask here how to import saved filters.
			}
		});
		btnActiveKeptFilters.setBounds(161, 651, 122, 28);
		btnActiveKeptFilters.setText("Active Kept Filters ");
		
		Label lblConnectToServer = new Label(shlOopApplication, SWT.NONE);
		lblConnectToServer.setBounds(737, 499, 105, 14);
		lblConnectToServer.setText("Connect to server");
		
		Label lblInternetProtocol = new Label(shlOopApplication, SWT.NONE);
		lblInternetProtocol.setBounds(627, 528, 105, 14);
		lblInternetProtocol.setText("Internet Protocol:");
		
		Label lblPort = new Label(shlOopApplication, SWT.NONE);
		lblPort.setBounds(631, 554, 59, 14);
		lblPort.setText("Port:");
		
		Label lblUserName = new Label(shlOopApplication, SWT.NONE);
		lblUserName.setBounds(627, 589, 59, 14);
		lblUserName.setText("User name:");
		
		Label lblPassword = new Label(shlOopApplication, SWT.NONE);
		lblPassword.setBounds(627, 617, 59, 14);
		lblPassword.setText("Password:");
		
		Label lblDataBase = new Label(shlOopApplication, SWT.NONE);
		lblDataBase.setBounds(627, 651, 59, 14);
		lblDataBase.setText("Data Base:");
		
		Label lblTableName = new Label(shlOopApplication, SWT.NONE);
		lblTableName.setBounds(631, 692, 59, 14);
		lblTableName.setText("Table name:");
		
		IP = new Text(shlOopApplication, SWT.BORDER);
		IP.setBounds(734, 523, 127, 19);
		
		PORT = new Text(shlOopApplication, SWT.BORDER);
		PORT.setText("");
		PORT.setBounds(731, 549, 64, 19);
		
		UserName = new Text(shlOopApplication, SWT.BORDER);
		UserName.setText("");
		UserName.setBounds(737, 584, 64, 19);
		
		PASS = new Text(shlOopApplication, SWT.BORDER);
		PASS.setText("");
		PASS.setBounds(731, 612, 64, 19);
		
		DataBase = new Text(shlOopApplication, SWT.BORDER);
		DataBase.setText("");
		DataBase.setBounds(727, 646, 64, 19);
		
		TableName = new Text(shlOopApplication, SWT.BORDER);
		TableName.setText("");
		TableName.setBounds(727, 680, 64, 19);
		
		Button btnMergeData = new Button(shlOopApplication, SWT.NONE);
		btnMergeData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SQL sql=new SQL(IP.getText(),UserName.getText(),PASS.getText(),PORT.getText(),DataBase.getText());
				try {
					Connection myconnection= DriverManager.getConnection(sql.getURL(),sql.getUser(),sql.getPassword());
					Statement mystmt = myconnection.createStatement();
					ResultSet myrs = mystmt.executeQuery("Select * from " + TableName.getText());
					List<ArrayList<String>> newdata=new ArrayList<ArrayList<String>>();
					while(myrs.next())
					{
						ArrayList<String> innerlist = new ArrayList<String>();
						String time=myrs.getString("time");
						String device=myrs.getString("device");
						String Lat=myrs.getString("lat");
						String Lon=myrs.getString("lon");
						String Alt=myrs.getString("alt");
						innerlist.add(Lat);
						innerlist.add(Lon);
						innerlist.add(Alt);
						innerlist.add(device);
						innerlist.add(time);
						int size=Integer.parseInt(myrs.getString("number_of_ap"));
						for(int i=0; i<size; i++)
						{
							String mac=myrs.getString("mac"+i);
							String signal=myrs.getString("rssi"+i);
							String nothing="null";
							innerlist.add(nothing);
							innerlist.add(mac);
							innerlist.add(nothing);
							innerlist.add(signal);
						}
						newdata.add(innerlist);
						for(int i=0; i<newdata.size(); i++)
						{
							if(!(Glist.Data.contains(newdata.get(i))))
							{
								Glist.Data.add(newdata.get(i));
							}
						}
						}
							} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnMergeData.setBounds(717, 714, 94, 28);
		btnMergeData.setText("Merge data");
		
		
		
		

	}
}
