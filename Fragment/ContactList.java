package com.greenapex.callhelper.Fragment;


import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.greenapex.callhelper.Adpter.adpterContact;
import com.greenapex.callhelper.Adpter.adpterContactSelect;
import com.greenapex.callhelper.Model.conctactPojo;
import com.greenapex.callhelper.R;
import com.greenapex.callhelper.Util.AlphabetItem;
import com.greenapex.callhelper.Util.CommonUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactList extends Fragment {

    IndexFastScrollRecyclerView recyclerView;
    TextView txtMsg;
    ArrayList<conctactPojo> mItems = new ArrayList<>();
    adpterContact mAdapter;
    Cursor cursor;
    conctactPojo cPojo;
    private List<AlphabetItem> mAlphabetItems;
    ProgressBar progressBar;
 //   isContactvisible = false

    public static ContactList newInstance() {
        ContactList fragment = new ContactList();
        return fragment;
    }

    public ContactList() {
        // Required empty public constructor
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // true
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        recyclerView = (IndexFastScrollRecyclerView) view.findViewById(R.id.contact_recycler);
        txtMsg = (TextView) view.findViewById(R.id.txt_msg);

//
//        if (ContextCompat.checkSelfPermission(getContext(),
//                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getContext(),
//                    Manifest.permission.READ_CONTACTS)) {
//                ActivityCompat.requestPermissions((Activity) getContext(),
//                        new String[]{Manifest.permission.READ_CONTACTS}, 1);
//            } else {
//                ActivityCompat.requestPermissions((Activity) getContext(),
//                        new String[]{Manifest.permission.READ_CONTACTS}, 1);
//            }
//        } else {
//            //Do stuff
//
//            contact_detail();
//        }
//        if (ContextCompat.checkSelfPermission((Activity) getContext(),
//                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getContext(),
//                    Manifest.permission.CALL_PHONE)) {
//                ActivityCompat.requestPermissions((Activity) getContext(),
//                        new String[]{Manifest.permission.CALL_PHONE}, 1);
//            } else {
//                ActivityCompat.requestPermissions((Activity) getContext(),
//                        new String[]{Manifest.permission.CALL_PHONE}, 1);
//            }
//        } else {
//            //do nothing
//        }
        bindGridview();
        return view;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        switch (requestCode) {
//            case 1: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (ContextCompat.checkSelfPermission(getContext(),
//                            Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
//                        //Do stuff
//                        contact_detail();
//                    }
//                    if (ContextCompat.checkSelfPermission(getContext(),
//                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
//                        contact_detail();
//                    } else {
//                        Toast.makeText(getContext(), "No Permission Granted!!", Toast.LENGTH_SHORT).show();
//                    }
//                    return;
//                }
//            }
//        }
//    }

    public void bindGridview() {

        new MyAsyncTask(getActivity()).execute("");
    }

    class MyAsyncTask extends AsyncTask<String, String, String> {

        Activity mContex;

        public MyAsyncTask(Activity contex) {
            this.mContex = contex;
        }

        @Override
        protected void onPreExecute() {
            CommonUtils.showProgress(getContext());
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

        cursor = getContext().getContentResolver().query(uri, PROJECTION, filter, null, order);

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
            //Alphabet fast scroller data
            mAlphabetItems = new ArrayList<>();
            List<String> strAlphabets = new ArrayList<>();
            for (int i = 0; i < mItems.size(); i++) {
                String name1 = mItems.get(i).getContactName();
                if (name1 == null || name1.trim().isEmpty())
                    continue;

                String word = name1.substring(0, 1);
                if (!strAlphabets.contains(word)) {
                    strAlphabets.add(word);
                    mAlphabetItems.add(new AlphabetItem(i, word, false));
                }
            }
        }
        cursor.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
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

        final List newList = new ArrayList(set);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new adpterContact(getContext(),newList);
        recyclerView.setAdapter(mAdapter);
        initialiseUI();
    }

    protected void initialiseUI() {
        recyclerView.setIndexTextSize(15);
        recyclerView.setIndexBarColor("#e0c907");
        recyclerView.setIndexBarCornerRadius(0);
        recyclerView.setIndexBarTransparentValue((float) 0.4);
        recyclerView.setIndexbarMargin(0);
        recyclerView.setIndexbarWidth(50);
        recyclerView.setPreviewPadding(0);
        recyclerView.setIndexBarTextColor("#848484");

        recyclerView.setIndexBarVisibility(true);
        recyclerView.setIndexbarHighLateTextColor("#33334c");
        recyclerView.setIndexBarHighLateTextVisibility(true);
    }

}
