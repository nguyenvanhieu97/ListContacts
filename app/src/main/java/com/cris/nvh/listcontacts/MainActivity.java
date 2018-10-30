package com.cris.nvh.listcontacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ContactAdapter.OnItemClickListener {
	public static final String TEL = "tel";
	public static final String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS,
			Manifest.permission.CALL_PHONE};
	public static final String PERMISSION_NOT_GRANTED = "Permission is not granted";
	public static final int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;
	public static final int REQUEST_CODE = 10;
	private RecyclerView mRecyclerView;
	private ContactAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		requestPermission();
		mRecyclerView = findViewById(R.id.list_contacts);
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);
		mAdapter = new ContactAdapter(this, getListContacts());
		mRecyclerView.setAdapter(mAdapter);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_CODE) {
			if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED
					&& grantResults[1] == PackageManager.PERMISSION_GRANTED) {
			} else {
				Toast.makeText(this, PERMISSION_NOT_GRANTED, Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void requestPermission() {
		if (ContextCompat.checkSelfPermission(this, PERMISSIONS[0]) != PERMISSION_GRANTED ||
				ContextCompat.checkSelfPermission(this, PERMISSIONS[1]) != PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE);
		}
	}

	public ArrayList<Contact> getListContacts() {
		String id, name, phoneNumber, photo;
		ArrayList<Contact> contacts = new ArrayList<>();
		String[] projections = {ContactsContract.Contacts._ID};
		Uri contacUri = ContactsContract.Contacts.CONTENT_URI;
		Cursor cursor = getContentResolver()
				.query(contacUri, projections, null, null, null);
		if (cursor == null || cursor.getCount() == 0) return null;
		else {
			Cursor pCursor = null;
			String[] columns = {ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
					ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
					ContactsContract.CommonDataKinds.Phone.NUMBER};
			Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
			while (cursor.moveToNext()) {
				id = cursor.getString(cursor.getColumnIndex(projections[0]));

				// get contact data
				pCursor = getContentResolver().query(uri, columns,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
						new String[]{id}, null);
				pCursor.moveToNext();
				photo = pCursor.getString(pCursor.getColumnIndex(columns[0]));
				name = pCursor.getString(pCursor.getColumnIndex(columns[1]));
				phoneNumber = pCursor.getString(pCursor.getColumnIndex(columns[2]));
				contacts.add(new Contact(name, phoneNumber, photo));
			}
			pCursor.close();
		}
		cursor.close();
		return contacts;
	}

	@SuppressLint("MissingPermission")
	@Override
	public void onItemClick(Contact contact) {
		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.fromParts(TEL, contact.getPhoneNumber(), null));
		startActivity(intent);
	}

	@Override
	public void saveFavorite(int position, boolean isFavorite) {
		SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
		editor.putBoolean(String.valueOf(position), isFavorite);
		editor.commit();
	}

	@Override
	public boolean loadFavorite(int position) {
		return getPreferences(Context.MODE_PRIVATE).getBoolean(String.valueOf(position), false);
	}
}
