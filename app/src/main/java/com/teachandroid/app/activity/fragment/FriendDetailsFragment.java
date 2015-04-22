package com.teachandroid.app.activity.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.teachandroid.app.R;
import com.teachandroid.app.data.Friend;

/**
 * Created by Igor Kuzmenko on 16.03.2015.
 *
 */
public class FriendDetailsFragment extends Fragment {

    private static final String TAG = FriendDetailsFragment.class.getSimpleName();

    private static final String FRIEND = "friend";
    private FriendHelper friendHelper;

    public FriendDetailsFragment() {

    }

    public static FriendDetailsFragment getInstance(Friend friend) {
        FriendDetailsFragment fragment = new FriendDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(FRIEND, friend);
        fragment.setArguments(args);
        Log.i(TAG, friend.toString());
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend_details, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        friendHelper = new FriendHelper();
        friendHelper.setImage((ImageView) view.findViewById(R.id.imageview_image100));
        friendHelper.setId((TextView) view.findViewById(R.id.text_user_id));
        friendHelper.setFirstName((TextView) view.findViewById(R.id.text_user_first_name));
        friendHelper.setLastName((TextView) view.findViewById(R.id.text_user_last_name));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Friend friend = this.getArguments().getParcelable(FRIEND);
        Log.i(TAG, friend.toString());
        ImageLoader.getInstance().displayImage(friend.getPhoto200Orig(), friendHelper.getImage());
        friendHelper.getId().setText(Long.toString(friend.getUserId()));
        friendHelper.getFirstName().setText(friend.getFirstName());
        friendHelper.getLastName().setText(friend.getLastName());
    }

    private class FriendHelper {
        private ImageView image;
        private TextView id;
        private TextView firstName;
        private TextView lastName;

        public ImageView getImage() {
            return image;
        }

        public void setImage(ImageView image) {
            this.image = image;
        }

        public TextView getId() {
            return id;
        }

        public void setId(TextView id) {
            this.id = id;
        }

        public TextView getFirstName() {
            return firstName;
        }

        public void setFirstName(TextView firstName) {
            this.firstName = firstName;
        }

        public TextView getLastName() {
            return lastName;
        }

        public void setLastName(TextView lastName) {
            this.lastName = lastName;
        }
    }
}
