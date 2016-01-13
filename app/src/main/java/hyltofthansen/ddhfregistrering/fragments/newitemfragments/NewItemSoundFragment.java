package hyltofthansen.ddhfregistrering.fragments.newitemfragments;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;

import hyltofthansen.ddhfregistrering.R;

/**
 * Created by hylle on 07-01-2016.
 * http://developer.android.com/guide/topics/media/audio-capture.html
 */
public class NewItemSoundFragment extends Fragment {
    private static final String TAG = "NewItemSoundFragment";
    Button b_start, b_play;
    boolean playing = false, recording = false;

    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    private static String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/audiorecordtest.3gp";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.newitem_sound_fragment, container, false); // sæt layout op

        b_start = (Button) root.findViewById(R.id.newitem_record);
        b_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!recording) {
                    startRecording();
                    b_start.setText("Stop optagelse");
                    recording = true;
                } else {
                    stopRecording();
                    b_start.setText("Start optagelse");
                    recording = false;
                }
            }
        });

        b_play = (Button) root.findViewById(R.id.newitem_play);
        b_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!playing) {
                    startPlaying();
                    b_play.setText("Stop afspilning");
                    playing = true;
                } else {
                    stopPlaying();
                    b_play.setText("Afspil optagelse");
                    playing = false;
                }
            }
        });

        return root;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed i startRecording()");
        }

        mRecorder.start();
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed i startPlaying()");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

}
