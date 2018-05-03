package com.liveperson.sample.app.Activities.Main;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by abratcher on 1/26/18.
 */

public class ListingsManager {
	private final String LISTINGS = "listings";
	private final String NAME_KEY = "name";
	private final String REFERENCEID_KEY = "referenceID";
	private final String INITIALUSERTEXT_KEY = "initialUserText";

	private final SharedPreferences preferences;

	ListingsManager(Context context) {
		preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
	}

	ListingEntry[] listings() {
		ArrayList<ListingEntry> storedListings = new ArrayList<>();
		String listingsJson = preferences.getString(LISTINGS,"");
		try {
			JSONArray jsonarray = new JSONArray(listingsJson);
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject object = jsonarray.getJSONObject(i);
				ListingEntry entry = new ListingEntry();
				entry.name = object.getString(NAME_KEY);
				entry.referenceID = object.getString(REFERENCEID_KEY);
				entry.initialUserText = object.getString(INITIALUSERTEXT_KEY);

				storedListings.add(entry);
			}

		} catch (JSONException e) { e.printStackTrace(); }


		ListingEntry[] listingArray = new ListingEntry[storedListings.size()];
		storedListings.toArray(listingArray);
		return listingArray;
	}

	void addListing(ListingEntry newEntry) {
		if (newEntry.name.length() == 0 || newEntry.referenceID.length() == 0) {
			return;
		}
		
		ListingEntry storedListings[] = listings();
		ArrayList<ListingEntry> modifiedList = new ArrayList<>();
		for (int i = 0; i < storedListings.length; i++) {
			modifiedList.add(storedListings[i]);
		}

		modifiedList.add(newEntry);

		ListingEntry listings[] = new ListingEntry[modifiedList.size()];
		modifiedList.toArray(listings);

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < listings.length; i++) {
			ListingEntry entry = listings[i];
			try {
				JSONObject object = new JSONObject();

				object.put(NAME_KEY, entry.name);
				object.put(REFERENCEID_KEY, entry.referenceID);
				object.put(INITIALUSERTEXT_KEY, entry.initialUserText);

				jsonArray.put(object);
			} catch (JSONException e) { e.printStackTrace(); }
		}

		preferences.edit().putString(LISTINGS, jsonArray.toString()).apply();
	}
}