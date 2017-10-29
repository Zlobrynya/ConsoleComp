package com.zlobrynya.compconsole.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zlobrynya.compconsole.MainActivity;
import com.zlobrynya.compconsole.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment {

    public VideoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoFragment newInstance(String param1, String param2) {
        VideoFragment fragment = new VideoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_video, null);
        Log.i("LOG","StartVideo");
        Button volumeUp = (Button) v.findViewById(R.id.volumeUp);
        Button volumeMute = (Button) v.findViewById(R.id.volumeMute);
        Button volumeDown = (Button) v.findViewById(R.id.volumeDown);
        Button space = (Button) v.findViewById(R.id.space);
        Button buttonLeft = (Button) v.findViewById(R.id.buttonLeft);
        Button buttonRigth = (Button) v.findViewById(R.id.buttonRight);
        Button buttonNext = (Button) v.findViewById(R.id.buttonNext);
        Button buttonPrev = (Button) v.findViewById(R.id.buttonPrev);

        buttonRigth.setOnClickListener(onClickListener);
        buttonLeft.setOnClickListener(onClickListener);
        buttonNext.setOnClickListener(onClickListener);
        buttonPrev.setOnClickListener(onClickListener);
        space.setOnClickListener(onClickListener);
        volumeDown.setOnClickListener(onClickListener);
        volumeMute.setOnClickListener(onClickListener);
        volumeUp.setOnClickListener(onClickListener);

        return v;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.volumeDown:
                    try {
                        MainActivity.blockingQueue.put(MainActivity.KEY+","+MainActivity.KEY_VOLUME_DOWN_CODE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.volumeMute:
                    try {
                        MainActivity.blockingQueue.put(MainActivity.KEY+","+MainActivity.KEY_VOLUME_MUTE_CODE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.volumeUp:
                    try {
                        MainActivity.blockingQueue.put(MainActivity.KEY+","+MainActivity.KEY_VOLUME_UP_CODE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.space:
                    try {
                        MainActivity.blockingQueue.put(MainActivity.KEY+","+MainActivity.KEY_PAUSE_CODE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.buttonRight:
                    try {
                        MainActivity.blockingQueue.put(MainActivity.KEY+","+MainActivity.KEY_RIGHT_CODE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.buttonLeft:
                    try {
                        MainActivity.blockingQueue.put(MainActivity.KEY+","+MainActivity.KEY_LEFT_CODE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.buttonPrev:
                    try {
                        MainActivity.blockingQueue.put(MainActivity.KEY+","+MainActivity.KEY_MEDIA_PREV_CODE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.buttonNext:
                    try {
                        MainActivity.blockingQueue.put(MainActivity.KEY+","+MainActivity.KEY_MEDIA_NEXT_CODE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

}
