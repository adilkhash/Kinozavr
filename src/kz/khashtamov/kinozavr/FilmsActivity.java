package kz.khashtamov.kinozavr;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FilmsActivity extends ListActivity {
	
	private ArrayList<HashMap<String,String>> films;
	private ArrayList<HashMap<String,String>> schedule;
	private ArrayList<HashMap<String,String>> cinemas;
	
	String cityId;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cinema);
        
        Bundle bundle = getIntent().getExtras();

		cinemas = (ArrayList<HashMap<String,String>>) bundle.getSerializable("cinemas");
		films = (ArrayList<HashMap<String,String>>) bundle.getSerializable("films");
		schedule = (ArrayList<HashMap<String,String>>) bundle.getSerializable("schedule");
		
		String cityTitle = bundle.getString("city_title");
		setTitle(cityTitle);

        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, 
        		android.R.id.text1, getFilms()));
	}
	
	@Override 
 	public void onListItemClick(ListView parent, View v, int position, long id) {
		Intent intent = new Intent().setClass(getApplicationContext(), MainTab.class);//ScheduleActivity.class);
		intent.putExtra("schedule", schedule);
		intent.putExtra("cinemas", cinemas);
		intent.putExtra("film_id", films.get(position).get("id"));
		intent.putExtra("film_title", films.get(position).get("title"));
		
		// for about film activity
		intent.putExtra("film_cover", films.get(position).get("cover"));
		intent.putExtra("film_desc", films.get(position).get("description"));
		intent.putExtra("film_rating", films.get(position).get("rating"));
		intent.putExtra("film_actors", films.get(position).get("actors"));
		
		startActivity(intent);
	}

	private ArrayList<String> getFilms() {
		if (films == null) return new ArrayList<String>();
		ArrayList<String> titles = new ArrayList<String>();
		for (int i=0; i < films.size(); i++)
			titles.add(films.get(i).get("title")); 
		return titles;
	}
}