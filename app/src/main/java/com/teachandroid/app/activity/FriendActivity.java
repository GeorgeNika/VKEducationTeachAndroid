package com.teachandroid.app.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.teachandroid.app.R;
import com.teachandroid.app.activity.fragment.FriendDetailsFragment;
import com.teachandroid.app.activity.fragment.FriendListFragment;
import com.teachandroid.app.data.Friend;

public class FriendActivity extends ActionBarActivity implements FriendListFragment.OnFragmentInteractionListener {

    private static final String TAG = FriendActivity.class.getSimpleName();
    private boolean isTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend);
        isTwoPane = getResources().getBoolean(R.bool.is_friend_two_pane);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.frame_friend_list, FriendListFragment.newInstance())
                    .commit();
        }
        Log.i(TAG, "ON_CREATE");
    }


    @Override
    public void onFragmentInteraction(Friend friend) {
        if (isTwoPane) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_friend_details, FriendDetailsFragment.getInstance(friend))
                    .commit();
        } else {
            Log.i(TAG, "ON_FRAGMENT_INTERACTION_ONE_PANE");
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_friend_list, FriendDetailsFragment.getInstance(friend))
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack(null, 0);
        } else {
            super.onBackPressed();
        }

    }
}
