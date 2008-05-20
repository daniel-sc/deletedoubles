import java.io.IOException;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.util.ServiceException;


public class doDeleteDoubles extends Thread {
	
	HashMap<String, CalendarEventEntry> doubles = null;
	JLabel label = null;
	JProgressBar pb = null;
	
	
	public void run() {
		label.setText("deleting doubles");
		try {
			//doubles = dd.findDoubles(calId);
			pb.setMinimum(0);
			pb.setMaximum(doubles.size());
			pb.setValue(0);
			
			for (CalendarEventEntry e : doubles.values()) {
				e.delete();
				pb.setValue(pb.getValue()+1);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		label.setText("deleted " + doubles.size() + " doubles");
		
	}
	
	public doDeleteDoubles(HashMap<String, CalendarEventEntry> doubles1, JLabel label1, JProgressBar pb1) {
		label = label1;
		doubles = doubles1;
		pb = pb1;
	}

	public doDeleteDoubles() {
		// TODO Auto-generated constructor stub
	}

	public doDeleteDoubles(Runnable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public doDeleteDoubles(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public doDeleteDoubles(ThreadGroup arg0, Runnable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public doDeleteDoubles(ThreadGroup arg0, String arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public doDeleteDoubles(Runnable arg0, String arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public doDeleteDoubles(ThreadGroup arg0, Runnable arg1, String arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	public doDeleteDoubles(ThreadGroup arg0, Runnable arg1, String arg2, long arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

}
