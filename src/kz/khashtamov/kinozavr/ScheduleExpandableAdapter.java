package kz.khashtamov.kinozavr;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;


public class ScheduleExpandableAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<HashMap<String,ArrayList<HashMap<String,String>>>> groups;
	private ArrayList<HashMap<String, String>> cinemas;
	
	ScheduleExpandableAdapter(Context context, ArrayList<HashMap<String, 
			ArrayList<HashMap<String,String>>>> groups, ArrayList<HashMap<String, String>> cinemas) {
		this.context = context;
		this.groups = groups;
		this.cinemas = cinemas;
		//this.children = children;
	}
	
	private String getKeyValue(int i) {
		String cinema = groups.get(i).toString();
		String key = cinema.substring(cinema.indexOf("{")+1, cinema.indexOf("="));
		return key;
	}
	
	private String showCinemaByKey(String key) {
		for (int j=0; j < cinemas.size(); j++) {
			if (cinemas.get(j).get("id").equals(key))
				return cinemas.get(j).get("title");
		}
		return " ";
	}
	
	@Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		String key = getKeyValue(groupPosition);
		return groups.get(groupPosition).get(key);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {
		ArrayList<HashMap<String,String>> cursor = (ArrayList<HashMap<String,String>>) getChild(groupPosition, childPosition);
		
		if (convertView == null) {
			LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflator.inflate(R.layout.child_view, null);
		}
		
		TextView time = (TextView) convertView.findViewById(R.id.timeTextView);
		TextView priceAdult = (TextView) convertView.findViewById(R.id.adultPriceTextView);
		TextView priceStudent = (TextView) convertView.findViewById(R.id.studentPriceTextView);
		TextView priceChild = (TextView) convertView.findViewById(R.id.childPriceTextView);
		
		time.setText(cursor.get(childPosition).get("date").split(" ")[1]);
		priceAdult.setText(cursor.get(childPosition).get("price_adult"));
		priceStudent.setText(cursor.get(childPosition).get("price_student"));
		priceChild.setText(cursor.get(childPosition).get("price_children"));
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		String key = getKeyValue(groupPosition);
		return groups.get(groupPosition).get(key).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
		
		String title = showCinemaByKey(getKeyValue(groupPosition));
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.group_view, null);
		}
		
		TextView tv = (TextView) convertView.findViewById(R.id.groupTextView);
		tv.setText(title);

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

}
