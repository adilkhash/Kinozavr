package kz.khashtamov.kinozavr;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Hashtable;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class KinoAPI {
	final String hostname = "http://api.kinozavr.kz/ru/";
	private HttpClient httpclient = new DefaultHttpClient();
	private Context context;
	
	KinoAPI(Context context) {
		this.context = context;
	}
	
	private String retrieveJson(String url) {
		try {
			HttpGet get = new HttpGet(url);
			HttpResponse response = httpclient.execute(get);
			HttpEntity entity = response.getEntity();
			StringBuilder builder = new StringBuilder();
			InputStream stream = entity.getContent();
			Reader reader = new InputStreamReader(stream, "utf-8");
			
			int l;
			char[] tmp = new char[1024];
			
			while ((l = reader.read(tmp)) != -1)
				builder.append(tmp, 0, l);
			
			stream.close();
			reader.close();
			
			return builder.toString();
			
		} catch (Exception e) {
			Log.e("HttpClient", e.getMessage());
			Toast.makeText(context, "Ошибка соединения. Попробуйте позже!", Toast.LENGTH_LONG);
		}
		return "";
	}
	
	public ArrayList<Hashtable<String,String>> getCinemas(String id) {
		if (id == null) return null;
		String cinemaUrl = hostname + "cinemas/city/" + id;
		
		try {
			JSONArray jsonArray = new JSONArray(retrieveJson(cinemaUrl));
			ArrayList<Hashtable<String,String>> cinemas = new ArrayList<Hashtable<String,String>>();

			for (int i = 0; i < jsonArray.length(); i++) {
				Hashtable<String,String> dict = new Hashtable<String,String>();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				dict.put("id", jsonObject.getString("id"));
				dict.put("title", jsonObject.getString("title"));
				cinemas.add(dict);
			}
			return cinemas;
		} catch (JSONException e) {
			Log.e("JSON Error", e.getMessage());
		}
		return new ArrayList<Hashtable<String,String>>();
	}
	
	public ArrayList<Hashtable<String,String>> getFilms(String id, boolean isCity) {
		// isCity if true retrieves list of all movies in the city
		// if false all movie in one particular cinema
		String url = hostname;
//		Log.d("API", "FILMS");
		if (isCity)
			url += "films/city/" + id;
		else
			url += "films/cinema/" + id;
		
		try {
			JSONArray jsonArray = new JSONArray(retrieveJson(url));
			ArrayList<Hashtable<String,String>> films = new ArrayList<Hashtable<String,String>>();

			for (int i = 0; i < jsonArray.length(); i++) {
				Hashtable<String,String> dict = new Hashtable<String,String>();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				dict.put("id", jsonObject.getString("id"));
				dict.put("title", jsonObject.getString("title"));
				dict.put("cover", jsonObject.getString("cover"));
				dict.put("rating", jsonObject.getString("rating"));
				dict.put("description", jsonObject.getString("descr"));
				dict.put("actors", jsonObject.getString("actors"));
				
				//Log.d("ACTORS", jsonObject.getString("actors"));
				
				dict.put("cover", jsonObject.getString("cover"));
				films.add(dict);
			}
			return films;
		} catch (JSONException e) {
			Log.e("JSON Error", e.getMessage());
		} catch (Exception e) {
			Log.e("JSON", e.getMessage());
		}

		return new ArrayList<Hashtable<String,String>>();
	}
	
	public ArrayList<Hashtable<String, String>> getSchedule(String cityId) {
		String scheduleUrl = hostname + "schedule/city/" + cityId;
//		Log.d("API", "SCHEDULE");
		try {
			JSONArray jsonArray = new JSONArray(retrieveJson(scheduleUrl));
			ArrayList<Hashtable<String,String>> schedule = new ArrayList<Hashtable<String,String>>();
			
			for (int i = 0; i < jsonArray.length(); i++) {
				Hashtable<String,String> dict = new Hashtable<String,String>();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				dict.put("cinema_id", jsonObject.getString("cinema_id"));
				dict.put("film_id", jsonObject.getString("film_id"));
				dict.put("date", jsonObject.getString("date"));
				dict.put("price_children", jsonObject.getString("price_children"));
				dict.put("price_student", jsonObject.getString("price_student"));
				dict.put("price_adult", jsonObject.getString("price_adult"));
				schedule.add(dict);
			}
			
			return schedule;
		} catch (JSONException e) {
			Log.e("JSON Error", e.getMessage());
		}
		return new ArrayList<Hashtable<String,String>>();
	}
}
