package kz.khashtamov.kinozavr;

import java.util.ArrayList;
import java.util.Hashtable;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ScheduleArrayAdapter extends ArrayAdapter<ArrayList<Hashtable<String,String>>> {

	public ScheduleArrayAdapter(Context context, int resource,
			int textViewResourceId, ArrayList<Hashtable<String, String>>[] items) {
		super(context, resource, textViewResourceId, items);
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		return v;
	}

}
