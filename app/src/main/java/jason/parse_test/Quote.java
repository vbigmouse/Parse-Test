package jason.parse_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.List;


/**
 * Created by vbigmouse on 2018/4/17.
 */

public class Quote extends AppCompatActivity {

	private GestureDetectorCompat mDetectorCompat;

	@Override
	protected  void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quote);

		mDetectorCompat = new GestureDetectorCompat(Quote.this, new QuoteGestureListener());

		TextView tx_quotes = findViewById(R.id.tx_quote);
		tx_quotes.setText(R.string.quotes);
		ListView listView = findViewById(R.id.list_view_quote);

		final QuoteAdapter<ParseObject> adapter = new QuoteAdapter<>(Quote.this,
				new ParseQueryAdapter.QueryFactory<ParseObject>() {
					@Override
					public ParseQuery<ParseObject> create() {
						ParseQuery<ParseObject> query = ParseQuery.getQuery("Quote")
								//.orderByAscending("start_time")
								.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
						return query;
					}

				}, R.layout.quote_item_detail);
		adapter.setObjectsPerPage(3);
		adapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<ParseObject>() {
			@Override
			public void onLoading() {
				Toast.makeText(Quote.this, "on Loading", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLoaded(List<ParseObject> objects, Exception e) {
				//Toast.makeText(Quote.this, "Loaded", Toast.LENGTH_SHORT).show();
				if (e != null && e instanceof ParseException
						&& ((ParseException) e).getCode() != ParseException.CACHE_MISS) {

					Toast.makeText(Quote.this, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});
		listView.setAdapter(adapter);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDetectorCompat.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	class QuoteGestureListener extends GestureDetector.SimpleOnGestureListener
	{
		/* handle swipe left back to discover main page */
		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX,
		                       float velocityY) {
			if (event2.getX() > event1.getX()) {
				Intent intent = new Intent(Quote.this, Discover.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_right_out, R.anim.slide_right_in);
			}

			return true;

		}
	}
}
