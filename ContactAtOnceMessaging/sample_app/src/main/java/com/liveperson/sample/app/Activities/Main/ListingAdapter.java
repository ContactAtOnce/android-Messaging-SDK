package com.liveperson.sample.app.Activities.Main;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liveperson.sample.app.R;

import java.util.ArrayList;

/**
 * Created by abratcher on 1/22/18.
 */

public class ListingAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ListingEntry[] listingEntries;
	private IChatButtonResponse delegate;

	public ListingAdapter(Context context, ListingEntry[] listingEntries, IChatButtonResponse delegate) {
		this.listingEntries = listingEntries;
		this.delegate = delegate;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setEntries(ListingEntry[] listingEntries) {
		this.listingEntries = listingEntries;
		notifyDataSetInvalidated();
	}

	@Override
	public int getCount() {
		return listingEntries.length;
	}

	@Override
	public Object getItem(int position) {
		return listingEntries[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.main_listview_entry, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.carDescription = (TextView) convertView.findViewById(R.id.car_description);
			viewHolder.chatButton = (AppCompatImageButton) convertView.findViewById(R.id.chat_button);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final ListingEntry listingEntry = listingEntries[position];
		viewHolder.carDescription.setText(listingEntry.name);

		viewHolder.chatButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				delegate.chat(listingEntry);
			}
		});

		return convertView;
	}

	private static class ViewHolder {
		public TextView carDescription;
		public AppCompatImageButton chatButton;
	}
}
