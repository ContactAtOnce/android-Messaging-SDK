package com.liveperson.sample.app.Activities.Main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.liveperson.sample.app.Activities.SettingsActivity;
import com.liveperson.sample.app.MessagingExperience;
import com.liveperson.sample.app.R;
import com.liveperson.sample.app.Utils.SampleAppStorage;

/**
 * Created by abratcher on 1/22/18.
 */

public class MainActivity extends AppCompatActivity implements IChatButtonResponse {
	private ListView listView;
	private ListingEntry[] listings;
	private ListingAdapter adapter;
	private ListingsManager listingsManager;
	private SampleAppStorage storage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		storage = SampleAppStorage.getInstance(this);

		setTitle("Cars For Sale");
		setContentView(R.layout.activity_main);
		setupListings();
		Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
		setSupportActionBar(toolbar);
		MessagingExperience.getInstance().handlePush(getIntent());
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (storage.getCaoApplicationId() == -1 || storage.getCAOProviderId() == -1 || storage.getCaoBaseDomain() == "") {
			showSettingsActivity();
			return;
		}

		MessagingExperience.getInstance().initialize(MainActivity.this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		MessagingExperience.getInstance().handlePush(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();;
		inflater.inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case R.id.main_settings:
				showSettingsActivity();
				break;

			case R.id.main_add_listing:
				showAddListingDialog();
				break;

			case R.id.main_show_all:
				MessagingExperience.getInstance().showAllConversations();
				break;

			default:
				break;
		}

		return true;
	}

	private void setupListings() {
		listingsManager = new ListingsManager(getApplicationContext());
		listView = (ListView) findViewById(R.id.main_listview);

		listings = listingsManager.listings();
		String[] listingNames = new String[listings.length];
		for (int i = 0; i < listings.length; i++) {
			listingNames[i] = listings[i].name;
		}

		adapter = new ListingAdapter(this, listings, this);
		listView.setAdapter(adapter);
	}

	private void showAddListingDialog() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.add_listing);
		LayoutInflater inflater = this.getLayoutInflater();
		final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_add_listing, null);

		builder.setView(layout);
		builder.setCancelable(true);
		final MainActivity activity = this;
		DialogInterface.OnClickListener saveListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				EditText name = (EditText) layout.findViewById(R.id.dialog_name);
				EditText referenceId = (EditText) layout.findViewById(R.id.dialog_reference_id);
				EditText intialUserText = (EditText) layout.findViewById(R.id.dialog_initial_user_text);

				ListingEntry entry = new ListingEntry();
				entry.name = name.getText().toString().trim();
				entry.referenceID = referenceId.getText().toString().trim();
				entry.initialUserText = intialUserText.getText().toString().trim();
				activity.saveListing(entry);
			}
		};

		DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.cancel();
			}
		};

		builder.setPositiveButton(R.string.save, saveListener);
		builder.setNegativeButton(R.string.cancel, cancelListener);

		Dialog dialog = builder.create();
		dialog.show();
	}

	private void saveListing(ListingEntry entry) {
		listingsManager.addListing(entry);
		adapter.setEntries(listingsManager.listings());
	}

	private void showSettingsActivity() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	@Override
	public void chat(final ListingEntry listingEntry) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				MessagingExperience.getInstance().showConversation(listingEntry.referenceID, listingEntry.name, listingEntry.initialUserText);
			}
		});
	}
}