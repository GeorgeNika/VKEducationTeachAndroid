package com.teachandroid.app.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.teachandroid.app.R;
import com.teachandroid.app.api.ApiFacade;
import com.teachandroid.app.api.SimpleResponseListener;
import com.teachandroid.app.data.Audio;

import java.util.ArrayList;
import java.util.List;


public class AudioActivity extends Activity {

    public static final String EXTRA_ALBUM_ID = "album_id";

    private static String durationToString(int duration) {
        int hours = duration / 3600;
        int minutes = (duration % 3600) / 60;
        int seconds = duration % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long albumId = getIntent().getLongExtra(EXTRA_ALBUM_ID, -1);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, AudioListFragment.create(albumId))
                    .commit();
        }
    }

    public static class AudioListFragment extends Fragment {

        private AudioAdapter audioAdapter;

        private ListView audioList;

        public static Fragment create(long albumId) {
            Bundle arg = new Bundle();
            arg.putLong(EXTRA_ALBUM_ID, albumId);

            Fragment fragment = new AudioListFragment();
            fragment.setArguments(arg);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.activity_audio, null);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            audioList = (ListView) view.findViewById(R.id.list_audio);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            audioAdapter = new AudioAdapter(getActivity(), new ArrayList<Audio>());


            audioList.setAdapter(audioAdapter);

            ApiFacade facade = new ApiFacade(getActivity());
            long albumId = getArguments().getLong(EXTRA_ALBUM_ID);
            facade.getAudio(albumId, new SimpleResponseListener<List<Audio>>() {
                @Override
                public void onResponse(final List<Audio> response) {
                    super.onResponse(response);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            audioAdapter.addAll(response);
                        }
                    });
                }
            });
        }
    }

    private static final class AudioAdapter extends ArrayAdapter<Audio> {

        public AudioAdapter(Context context, List<Audio> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.list_item_audio, null);
            }
            TextView titleView = (TextView) convertView.findViewById(R.id.text_title);
            TextView artistView = (TextView) convertView.findViewById(R.id.text_artist);
            TextView durationView = (TextView) convertView.findViewById(R.id.text_duration);

            Audio item = getItem(position);

            titleView.setText(item.getTitle());
            artistView.setText(item.getArtist());
            durationView.setText(durationToString(item.getDuration()));
            return convertView;
        }
    }
}
