import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JProgressBar;

import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.util.ServiceException;

import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;

public class DeleteDoubles {
	/**
	 * maximum difference of start and end time for matching events.
	 */
	private static final int MAX_TIME_DIFF = 362000; //~6 minutes
	/**
	 * google-service - used for all communication.
	 */
	CalendarService myService;
	JProgressBar progressBar = null;
	
	/**
	 * instance for getting string-similarities
	 */
	Levenshtein distance = new Levenshtein();
	
	/**
	 * Finds all doubles of the calendar 'calID'.
	 * @param calID
	 * @return a HashMap (eventId -> event) of doubles.
	 * @throws IOException
	 * @throws ServiceException
	 */
	public HashMap<String, CalendarEventEntry> findDoubles(String calID) throws IOException, ServiceException {
		if(progressBar != null) progressBar.setIndeterminate(true);
		
		URL feedUrl = new URL("http://www.google.com/calendar/feeds/"+calID+"/private/full");
		
		CalendarEventFeed myFeed = myService.getFeed(feedUrl, CalendarEventFeed.class);
		
		System.out.println("kalendertitel:"+myFeed.getTitle().getPlainText());
		System.out.println("Total results: " + myFeed.getTotalResults()); 
		System.out.println("items per page: " + myFeed.getItemsPerPage());
		
		int currStart = 1;
		int total = myFeed.getTotalResults();
		
		HashMap<String, CalendarEventEntry> doubles = new HashMap<String, CalendarEventEntry>();
		
		//neu: erst alle einträge holen und dann lokal doppelte suchen
		HashSet<CalendarEventEntry> allEvents = new HashSet<CalendarEventEntry>();
		while(currStart <= total) {

			CalendarQuery myQuery2 = new CalendarQuery(feedUrl);
			myQuery2.setStartIndex(currStart);
			myQuery2.setMaxResults(total); //es geht wohl am schnellsten, wenn alle elemente auf einmal geholt werden.
			myFeed = myService.query(myQuery2, CalendarEventFeed.class);

			currStart += myFeed.getEntries().size();
			
			System.out.println("read: "+ (currStart-1) + "/" + total);
			
			allEvents.addAll(myFeed.getEntries());
		}
		
		if(progressBar != null) {
			progressBar.setIndeterminate(false);
			progressBar.setMinimum(0);
			progressBar.setMaximum(total);
		}
		
		//loop through all elements and search for doubles;
		for (CalendarEventEntry e : allEvents) {
			if(progressBar!=null) progressBar.setValue(progressBar.getValue()+1);
			//if element is alreade detected to be a double - continue.
			if(doubles.containsKey(e.getId())) {
				//System.out.println("skipped: "+e.getTitle().getPlainText()+"@"+e.getLocations().get(0).getValueString());
				continue;
			}
			
			doubles.putAll(findDoubles(e , allEvents));			
		}

		return doubles;
	}
	/**
	 * Testet ob der Termin 'e' ein Dublikat in 'allEvents' hat. 
	 * Checks wether the Levensthein distance of the titles is <= 2
	 * and wether the start and end times are within MAX_TIME_DIFF.
	 * @return eine Map (dublikat.getId() -> dublikat) mit allen
	 *  Dublikaten des Events 'e'. M�glicherweise leer!
	 */
	private Map<? extends String, ? extends CalendarEventEntry> findDoubles(CalendarEventEntry e, HashSet<CalendarEventEntry> allEvents) {
		Map<String, CalendarEventEntry> map = new HashMap<String, CalendarEventEntry>();

		for (CalendarEventEntry entry : allEvents) {
			//check for similarity:
			if( distance.getUnNormalisedSimilarity(entry.getTitle().getPlainText(), e.getTitle().getPlainText()) > 2 ) continue;
			if(entry.getTimes().size()>0 && e.getTimes().size()>0) {
				if( Math.abs(entry.getTimes().get(0).getStartTime().getValue()
						        -e.getTimes().get(0).getStartTime().getValue()) > MAX_TIME_DIFF) continue;
				//Could be skipped for  performance reasons: TODO
				if( Math.abs(entry.getTimes().get(0).getEndTime().getValue()
				                -e.getTimes().get(0).getEndTime().getValue()) > MAX_TIME_DIFF) continue; 
			} else if( entry.getTimes().size()>0 || e.getTimes().size()>0 ) continue; //if one has a time and the other not, they are not equal.
			if(entry.getId()==e.getId()) continue; //better: later for performance
			map.put(entry.getId(), entry);
			//System.out.println("found double!");
			//System.out.println(entry.getTitle().getPlainText()+" and "+ e.getTitle().getPlainText() +" has similarity: "+
			//		distance.getUnNormalisedSimilarity(entry.getTitle().getPlainText(), e.getTitle().getPlainText()));
			
		}
		return map;
	}

	public void setProgressBar(JProgressBar pb) {
		progressBar = pb;
	}
	
	/**
	 * Führt das Programm als Konsolenprogramm aus
	 * @param args
	 * @throws IOException
	 * @throws ServiceException
	 */
	public static void main(String[] args) throws IOException, ServiceException {
		DeleteDoubles myInstance = new DeleteDoubles("d8schreiber","password");
		
		System.out.println("Coose calendar:");
		HashMap<String, CalendarEntry> cals = myInstance.getCalendars();
		ArrayList<String> idArr = new ArrayList<String>(cals.keySet());
		for (int i=0; i<idArr.size(); i++) {
			System.out.println("("+i+")  "+cals.get(idArr.get(i)).getTitle().getPlainText() + "(" + idArr.get(i) + ")");
			//System.out.println("\t" + cals.get(id).getSummary().getPlainText());
		}
		
		int input = System.in.read();
		
		//System.out.println("input:"+ input);
		
		HashMap<String, CalendarEventEntry> doubles = myInstance.findDoubles(idArr.get(input-48));
		System.out.println("deleting: (" + doubles.size() + ")");
		for (CalendarEventEntry e : doubles.values()) {
			System.out.println(e.getTitle().getPlainText() + "@" + e.getLocations().get(0).getValueString());
			e.delete();
		}
	}
	
	/**
	 * @return HashMap with CalendarEntry 's and the corresponding id's as used for findDoubles.
	 * @throws IOException
	 * @throws ServiceException
	 */
	public HashMap<String, CalendarEntry> getCalendars() throws IOException, ServiceException {
		URL feedUrl = new URL("http://www.google.com/calendar/feeds/default/allcalendars/full");
		CalendarFeed calFeed = myService.getFeed(feedUrl, CalendarFeed.class);
		
		HashMap<String, CalendarEntry> retMap = new HashMap<String, CalendarEntry>(calFeed.getEntries().size());
		
		for (CalendarEntry e : calFeed.getEntries()) {
			String id = e.getId().substring(e.getId().lastIndexOf("/")+1);
			retMap.put(id, e);
		}
		return retMap;
	}
	
	DeleteDoubles(String user, String password) throws IOException, ServiceException {
		myService = new CalendarService("deleteDoubles-0.1");
		myService.setUserCredentials(user, password);
	}
	/**
	 * Compares strings a and b.
	 * @param a
	 * @param b
	 * @return the mean distance between the strings. 0=equal
	 */
	public double similarity(String a, String b) {
		double result = 0;
		//make a the longer string:
		if(a.length() < b.length()) {
			String c = a;
			a = b;
			b = c;
		}
		
		for(int i=0; i<b.length(); i++) {
			result += Math.abs(a.charAt(i)-b.charAt(i));
		}
		
		return result/(double)b.length();
	}
}

