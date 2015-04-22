package com.teachandroid.app.activity.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.teachandroid.app.R;
import com.teachandroid.app.api.service.ApiService;
import com.teachandroid.app.data.Friend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor Kuzmenko on 16.03.2015.
 */
public class FriendListFragment extends Fragment {

    private static final String TAG = FriendListFragment.class.getSimpleName();

    private FriendAdapter friendAdapter;
    private LocalBroadcastManager broadcastManager;
    private ListView friendList;
    private OnFragmentInteractionListener mListener;

    private BroadcastReceiver friendsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(ApiService.EXTRA_SERVICE_RESPONSE)) {
                List<Friend> sFriendsList = intent.getParcelableArrayListExtra(ApiService.EXTRA_SERVICE_RESPONSE);
                Log.i("Broadcast", "Broadcast is working");
                friendAdapter.clear();
                friendAdapter.addAll(sFriendsList);
            }
        }
    };


    public FriendListFragment() {

    }

    public static FriendListFragment newInstance() {
        FriendListFragment fragment = new FriendListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend_list, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        friendList = (ListView) view.findViewById(R.id.friend_listview);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        friendAdapter = new FriendAdapter(getActivity(), new ArrayList<Friend>());
        friendList.setAdapter(friendAdapter);

        Intent intent = new Intent(getActivity(), ApiService.class);
        intent.putExtra(ApiService.EXTRA_SERVICE_REQUEST, ApiService.REQUEST_GET_FRIENDS);
        Log.i(TAG, "ON_CREATE");

        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend friend = (Friend) friendList.getItemAtPosition(position);
                mListener.onFragmentInteraction(friend);
            }

        });

        getActivity().startService(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        broadcastManager.registerReceiver(friendsReceiver,
                new IntentFilter(ApiService.REQUEST_GET_FRIENDS));
    }

    @Override
    public void onPause() {
        super.onPause();
        broadcastManager.unregisterReceiver(friendsReceiver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Friend friend);
    }

    public static final class FriendAdapter extends ArrayAdapter<Friend> {

        public FriendAdapter(Context context, List<Friend> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.list_item_friend, null);
            }

            ImageView image100 = (ImageView) convertView.findViewById(R.id.imageview_image100);
            TextView idView = (TextView) convertView.findViewById(R.id.text_user_id);
            TextView firstNameView = (TextView) convertView.findViewById(R.id.text_user_first_name);
            TextView lastNameView = (TextView) convertView.findViewById(R.id.text_user_last_name);

            Friend friend = getItem(position);

            ImageLoader.getInstance().displayImage(friend.getPhoto100(), image100);
            idView.setText(Long.toString(friend.getUserId()));
            firstNameView.setText(friend.getFirstName());
            lastNameView.setText(friend.getLastName());

            return convertView;
        }
    }

}
