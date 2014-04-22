package edu.umich.quarto;

//import com.scringo.Scringo;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Build;

public class MainActivity extends Activity {

//	private Scringo scringo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		Scringo.setAppId("kyaGXy5p7TYEf48ZKhZZFIgQKErmOP3v");
//		scringo = new Scringo(this);
//		scringo.init();
//		scringo.addSidebar();
//		Scringo.setDebugMode(true);
		
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		Button playWithStrangerBtn = (Button)findViewById(R.id.backToStart1);
		playWithStrangerBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, QuartoGame.class);
				startActivity(intent);
			}
		});
		
		Button playWithFriendsBtn = (Button)findViewById(R.id.toTutorial2);
		playWithFriendsBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, QuartoGame.class);
				startActivity(intent);
			}
		});
		
		Button tutorialBtn = (Button)findViewById(R.id.Button01);
		tutorialBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, TutorialFirst.class);
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onStart() {
		super.onStart();
//		scringo.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
//		scringo.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
}


