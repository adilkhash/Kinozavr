package kz.khashtamov.kinozavr;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

public class ScheduleActivity extends Activity {
	
	private ArrayList<HashMap<String,String>> cinemas;
	private ArrayList<HashMap<String,String>> schedule;
	private ExpandableListAdapter adapter;
	
	String filmId;
	
	@SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule);
		
		Bundle bundle = getIntent().getExtras();

		cinemas = (ArrayList<HashMap<String, String>>) bundle.getSerializable("cinemas");
		schedule = (ArrayList<HashMap<String, String>>) bundle.getSerializable("schedule");
		filmId = bundle.getString("film_id");
		
		String filmTitle = bundle.getString("film_title");
		
		setTitle(filmTitle);
		
		ExpandableListView eListView = (ExpandableListView) findViewById(R.id.exListView);
		adapter = new ScheduleExpandableAdapter(this, sortScheduleByCinema(), cinemas);
		
		eListView.setAdapter(adapter);
	}
	
/*	private ArrayList<String> showCinemaByFilm() {
		ArrayList<HashMap<String,ArrayList<HashMap<String,String>>>> schedule = sortScheduleByCinema();
		ArrayList<String> titles = new ArrayList<String>();

		for (int i=0; i < schedule.size(); i++) {
			String cinema = schedule.get(i).toString();
			String id = cinema.substring(cinema.indexOf("{")+1, cinema.indexOf("="));
			for (int j=0; j < cinemas.size(); j++) {
				if (cinemas.get(j).get("id").equals(id))
					titles.add(cinemas.get(j).get("title"));
			}
		}
		return titles;
	}*/
	
	private ArrayList<HashMap<String,ArrayList<HashMap<String,String>>>> sortScheduleByCinema() {
		
		ArrayList<HashMap<String,String>> oldSorted = sortScheduleByFilm();
		ArrayList<HashMap<String,ArrayList<HashMap<String,String>>>> sortedByCinemaList =
			new ArrayList<HashMap<String,ArrayList<HashMap<String,String>>>>();
		HashMap<String,ArrayList<HashMap<String,String>>> rootMap = 
			new HashMap<String,ArrayList<HashMap<String,String>>>();
		
		ArrayList<HashMap<String,String>> newInsideList = new ArrayList<HashMap<String,String>>();
		
		newInsideList.add(oldSorted.get(0));
		rootMap.put(oldSorted.get(0).get("cinema_id"), newInsideList);
		sortedByCinemaList.add(rootMap);
		oldSorted.remove(0);

		for (int i = 0; i < sortedByCinemaList.size(); i++) {
			for (int j = 0; j < oldSorted.size(); j++) {
				Integer comparedTime = Integer.parseInt(oldSorted.get(j).get("date").split(" ")[1].replace(":", ""));
				String key = oldSorted.get(j).get("cinema_id");
				
				if (sortedByCinemaList.get(i).containsKey(key)) {
					
					for (int a = 0; a < sortedByCinemaList.get(i).get(key).size(); a++) {
						HashMap<String,String> cursor = sortedByCinemaList.get(i).get(key).get(a); // current element in list
						Integer currentTime = Integer.parseInt(cursor.get("date").split(" ")[1].replace(":", "")); // its date
						
						if (currentTime >= comparedTime) 
						{
							sortedByCinemaList.get(i).get(key).add(a, oldSorted.get(j));
							oldSorted.remove(j);
							j--;
							break;
						} else {
							if ((a+1) == sortedByCinemaList.get(i).get(key).size()) {
								sortedByCinemaList.get(i).get(key).add(oldSorted.get(j));
								oldSorted.remove(j);
								a++;
								j--;
							}
							continue;
						}
					}
				}
			}
			if (!oldSorted.isEmpty()) {
				ArrayList<HashMap<String, String>> tempInsideList = new ArrayList<HashMap<String, String>>();
				HashMap<String, ArrayList<HashMap<String, String>>> tempMap = new HashMap<String, ArrayList<HashMap<String, String>>>();
				tempInsideList.add(oldSorted.get(0));
				tempMap.put(oldSorted.get(0).get("cinema_id"), tempInsideList);
				sortedByCinemaList.add(tempMap);
				oldSorted.remove(0);
			} else break;
		}
		return sortedByCinemaList;
	}
	
	private ArrayList<HashMap<String,String>> sortScheduleByFilm() {
		
		ArrayList<HashMap<String,String>> sorted = new ArrayList<HashMap<String,String>>();
		
		for (int i=0; i < schedule.size(); i++) {
			HashMap<String,String> cursor = schedule.get(i);
			if (cursor.get("film_id").equals(filmId))
				sorted.add(cursor);
		}
		return sorted;
	}
}
