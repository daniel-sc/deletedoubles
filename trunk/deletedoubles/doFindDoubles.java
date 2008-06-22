import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;

import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.util.ServiceException;


public class doFindDoubles extends Thread {
	
	DeleteDoubles dd = null;
	HashMap<String, CalendarEventEntry> doubles = null;
	String calId = null;
	JLabel label = null;
	JButton button = null;
	
	public HashMap<String, CalendarEventEntry> getDoubles() {
		return doubles;
	}
	
	public void run() {
		if(dd==null) {
			return;
		}
		label.setText("searching doubles");
		try {
			doubles = dd.findDoubles(calId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		label.setText("found " + doubles.size() + " doubles");
		button.setEnabled(true);
	}
	
	public doFindDoubles(DeleteDoubles dd1, String calId1, JLabel label1, JButton button1) {
		dd = dd1;
		calId = calId1;
		label = label1;
		button = button1;
	}

	public doFindDoubles() {
		// TODO Auto-generated constructor stub
	}

	public doFindDoubles(Runnable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public doFindDoubles(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public doFindDoubles(ThreadGroup arg0, Runnable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public doFindDoubles(ThreadGroup arg0, String arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public doFindDoubles(Runnable arg0, String arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public doFindDoubles(ThreadGroup arg0, Runnable arg1, String arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	public doFindDoubles(ThreadGroup arg0, Runnable arg1, String arg2, long arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

}
