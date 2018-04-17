package com.greenapex.callhelper.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.greenapex.callhelper.Adpter.adpterContactSelect;
import com.greenapex.callhelper.Model.conctactPojo;
import com.greenapex.callhelper.R;
import com.greenapex.callhelper.Util.CommonUtils;
import com.greenapex.callhelper.Util.Pref;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.greenapex.callhelper.Util.CommonUtils.context;

public class ContactList extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView txtMsg;
    Button btnDone;
    Toolbar toolbar;
    SearchView searchView;

    ArrayList<conctactPojo> mItems = new ArrayList<>();
    ArrayList<conctactPojo> mItems1 = new ArrayList<>();
    adpterContactSelect mAdapter;
    Cursor cursor;
    String contactNumber, contactName, contactImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        recyclerView = (RecyclerView) findViewById(R.id.contactList_recycler);
        btnDone = (Button) findViewById(R.id.btnDone);
        toolbar = (Toolbar) findViewById(R.id.toolbar_contactList);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        txtMsg = (TextView) findViewById(R.id.txt_ContactListMsg);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                contactNumber = Pref.getValue(ContactList.this, "contactNumber", "");
                contactName = Pref.getValue(ContactList.this, "contactName", "");
                contactImage = Pref.getValue(ContactList.this, "contactImage", "");

                Log.e("contactImage", "" + contactImage);

                if (contactName.isEmpty() && contactNumber.isEmpty() && contactImage.isEmpty()) {
                    Toast.makeText(ContactList.this, "PLease Select the Contact", Toast.LENGTH_SHORT).show();
                } else {
                    Pref.setValue(ContactList.this, "contactNumber1", contactNumber);
                    Pref.setValue(ContactList.this, "contactName1", contactName);
                    Pref.setValue(ContactList.this, "contactImage1", contactImage);
                    finish();
                }
            }
        });

        contactGet();
    }

    public void contactGet() {

        new MyAsyncTask(this).execute("");
    }

    class MyAsyncTask extends AsyncTask<String, String, String> {

        Activity mContex;

        public MyAsyncTask(Activity contex) {
            this.mContex = contex;
        }

        @Override
        protected void onPreExecute() {
            CommonUtils.showProgress(getApplicationContext());
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            contact_detail();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            removeDuplicates(mItems);
            CommonUtils.dismissProgress();
        }
    }

    public void contact_detail() {

        String[] PROJECTION = new String[]{
                ContactsContract.RawContacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Photo.CONTACT_ID};

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String filter = "" + ContactsContract.CommonDataKinds.Phone.NUMBER + " = "
                + ContactsContract.CommonDataKinds.Phone.NUMBER;
        String order = ContactsContract.Contacts.DISPLAY_NAME + " ASC";// LIMIT " + limit + " offset " + lastId + "";

        cursor = getContentResolver().query(uri, PROJECTION, filter, null, order);


        // cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        int name = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int number = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        int photoId = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);
        while (cursor.moveToNext()) {
            String callName = cursor.getString(name);
            String phNumber = cursor.getString(number);
            String photoUri = cursor.getString(photoId);
            phNumber = phNumber.replaceAll("\\(", "");
            phNumber = phNumber.replaceAll("\\) ", "");
            phNumber = phNumber.replaceAll("\\-", "");
            phNumber = phNumber.replace(" ", "");


            conctactPojo callLogItem = new conctactPojo();
            callLogItem.setContactName(callName);
            callLogItem.setContactNumber(phNumber);
            callLogItem.setCallPhotoUri(photoUri);

            mItems.add(callLogItem);

            if (mItems.isEmpty()) {
                txtMsg.setVisibility(View.VISIBLE);
            }
        }
        cursor.close();
    }

    public void removeDuplicates(List<conctactPojo> list) {
        Set set = new TreeSet(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if (((conctactPojo) o1).getContactNumber().equalsIgnoreCase(((conctactPojo) o2).getContactNumber())) {
                    return 0;
                }
                return 1;
            }
        });
        set.addAll(list);

        final ArrayList newList = new ArrayList(set);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new adpterContactSelect(getApplicationContext(), newList);
        recyclerView.setAdapter(mAdapter);
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    mAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_one_menu, menu);
        MenuItem search = menu.findItem(R.id.action_search_one);
        searchView = (SearchView) MenuItemCompat.getActionView(search);
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchEditText, R.drawable.cursor); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
        }
        searchEditText.setTextColor(ContextCompat.getColor(this, R.color.white));
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHint("Search Contact Number");
        return true;
    }
}
