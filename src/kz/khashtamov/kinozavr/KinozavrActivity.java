package kz.khashtamov.kinozavr;

import java.util.ArrayList;
import java.util.Hashtable;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class KinozavrActivity extends Activity {
	ArrayList<Hashtable<String,String>> films;
	ArrayList<Hashtable<String,String>> schedule;
	ArrayList<Hashtable<String,String>> cinemas;
	
	private ProgressDialog pd;
	String cityId;
	
	final String[] cities = {"Алматы",
     		"Астана",
     		"Актау",
     		"Актобе",
     		"Атырау",
     		"Аксай",
     		"Караганда",
     		"Кокшетау",
     		"Костанай",
     		"Кызылорда",
     		"Павлодар",
     		"Петропавловск",
     		"Талдыкорган",
     		"Тараз",
     		"Темиртау",
     		"Уральск",
     		"Усть-Каменогорск",
     		"Шымкент",
     		"Экибастуз",
     		"Рудный"};

	class AsyncDownloader extends AsyncTask<String, Void, Void> {
		public ProgressDialog pd;
		@Override
		protected Void doInBackground(String ... cityId) {
			KinoAPI api = new KinoAPI(getApplicationContext());
			
	        films = api.getFilms(cityId[0], true);
	        cinemas = api.getCinemas(cityId[0]);
	        schedule = api.getSchedule(cityId[0]);

			return null;
		}
		
		protected void onPostExecute(Void result) {
			pd.dismiss();
			
			Intent intent = new Intent().setClass(KinozavrActivity.this, FilmsActivity.class);
			
			intent.putExtra("films", films);
			intent.putExtra("cinemas", cinemas);
			intent.putExtra("schedule", schedule);
			
			//if (cityId.equals("22")) cityId = 
			intent.putExtra("city_title", cities[Integer.parseInt(cityId)-1]);
			
			startActivity(intent);
		}
	};
	
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nInfo = cm.getActiveNetworkInfo();
		if (nInfo != null && nInfo.isConnected())
			return true;
		else
			return false;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ListView list = (ListView) findViewById(R.id.cityList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, cities);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg) {
				
				if (!isOnline()) {
					Toast.makeText(getApplicationContext(), "Необходимо подключение к Интернет", Toast.LENGTH_LONG).show();
					return;
				}
				pd = ProgressDialog.show(KinozavrActivity.this, "Загрузка", "Идет получение данных..");

				if (position == 10) position = 11;
				if (position == 19) position = 21;
				
				cityId = Integer.toString(position+1);
				
				AsyncDownloader retriever = new AsyncDownloader();
				retriever.pd = pd;
				retriever.execute(cityId);
				
			}
		});
    }
}