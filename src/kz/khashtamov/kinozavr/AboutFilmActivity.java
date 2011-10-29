package kz.khashtamov.kinozavr;

import java.io.File;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutFilmActivity extends Activity {
	private ProgressDialog pd;
	private Bitmap getBitmap(String bitmapUrl) {
		//String filename = String.valueOf(bitmapUrl.hashCode());
		//File imageFile = new File(cacheDir, filename);	
		//Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
		
		//if (bitmap != null)
		//	return bitmap;

		try {
			URL url = new URL(bitmapUrl);	
		    Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
		    //writeFile(bitmap, imageFile);
		    return bitmap;
		} 
		catch(Exception ex) {
			   Log.e("error", ex.getMessage());
			   return null;
		}
	}
	
	class AsyncDownloader extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(String ... url) {
			return getBitmap(url[0]);
		}
		
		protected void onPostExecute(Bitmap result) {
			pd.dismiss();
			ImageView cover = (ImageView)findViewById(R.id.filmCover);
			cover.setImageBitmap(result);
		}
	};
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutfilm);
        
        Bundle bundle = getIntent().getExtras();
        
        TextView desc = (TextView)findViewById(R.id.filmDesc);
        TextView title = (TextView)findViewById(R.id.filmTitle);
        TextView actors = (TextView)findViewById(R.id.filmActors);
        
        pd = ProgressDialog.show(AboutFilmActivity.this, "Загрузка", "Идет получение постера");
        
        AsyncDownloader async = new AsyncDownloader();
        async.execute(bundle.getString("film_cover"));
        
        desc.setText(Html.fromHtml("<b>Описание: </b><br/>" + bundle.getString("film_desc")));
        title.setText(bundle.getString("film_title"));
        actors.setText(Html.fromHtml("<b>В ролях: </b>" + bundle.getString("film_actors")));
        
	}

}
