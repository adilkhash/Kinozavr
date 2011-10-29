package kz.khashtamov.kinozavr;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;


public class MainTab extends TabActivity {
	public void onCreate(Bundle b) {
		super.onCreate(b);
		
		setContentView(R.layout.maintab);
		
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
	    Intent intent;
	    Bundle bundle = getIntent().getExtras();
	    
	    String filmTitle = bundle.getString("film_title");
	    String filmDesc = bundle.getString("film_desc");
	    
	    setTitle(filmTitle);
	    
	    //Log.d("DESC", filmDesc);
	    
	    String filmCover = bundle.getString("film_cover");
	    String filmRating = bundle.getString("film_rating");
	    String filmActors = bundle.getString("film_actors");
	    
	    intent = new Intent().setClass(this, ScheduleActivity.class);
	    
		intent.putExtra("schedule", bundle.getSerializable("schedule"));
		intent.putExtra("cinemas", bundle.getSerializable("cinemas"));
		intent.putExtra("film_id", bundle.getString("film_id"));
		
		intent.putExtra("film_title", filmTitle);
		
	    spec = tabHost.newTabSpec("schedule").setIndicator("Расписание").setContent(intent);
	    tabHost.addTab(spec);
	    //tabHost.setCurrentTab(0);

	    intent = new Intent().setClass(this, AboutFilmActivity.class);
	    
		intent.putExtra("film_cover", filmCover);
		intent.putExtra("film_desc", filmDesc);
		intent.putExtra("film_rating", filmRating);
		intent.putExtra("film_title", filmTitle);
		intent.putExtra("film_actors", filmActors);
		
	    spec = tabHost.newTabSpec("about").setIndicator("О фильме").setContent(intent);
	    tabHost.addTab(spec);

	    
	    tabHost.setCurrentTab(0);
	}
}
