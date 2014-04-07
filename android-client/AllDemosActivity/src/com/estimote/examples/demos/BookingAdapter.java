package com.estimote.examples.demos;

import java.util.ArrayList;

import com.estimote.sdk.Beacon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BookingAdapter extends BaseAdapter {

//	private ArrayList<Beacon> beacons;
	private LayoutInflater inflater;

	public BookingAdapter(Context context) {
		this.inflater = LayoutInflater.from(context);
		//this.beacons = new ArrayList<Beacon>();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
