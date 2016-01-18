package hyltofthansen.ddhfregistrering.fragments.newitemfragments;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hyltofthansen.ddhfregistrering.R;

/**
 * http://developer.android.com/guide/topics/media/audio-capture.html
 */
public class NewItemSoundFragment extends Fragment {
    private static final String TAG = "NewItemSoundFragment";
    ImageButton b_record, b_play;
    TextView tv_filename;
    boolean playing = false, recording = false;

    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
//    private String newFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+createVoicePath();
    private String newFilePath;
    private SharedPreferences prefs;
    private String recordingPath;


    @Override
    public void onResume() {
        //Læs om der er gemt en optagelse i forvejen
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        recordingPath = prefs.getString("recording", "null");
        if (recordingPath != "null") {
//        if (recordingPath != null) {
            Log.d(TAG, "Læser recording: "+recordingPath);
            // Hvis der er en optagelse så vis filnavn i textview og initialiser afspilning med korrekt path
            tv_filename.setText("Valgt lydfil: "+recordingPath);
        }
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.newitem_sound, container, false); // sæt layout op

        tv_filename = (TextView) root.findViewById(R.id.newitem_voicefile);

        b_record = (ImageButton) root.findViewById(R.id.newitem_record);
        b_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!recording) {
                    startRecording();
                } else {
                    stopRecording();
                }
            }
        });

        b_play = (ImageButton) root.findViewById(R.id.newitem_play);
        b_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!playing) {
                    startPlaying();
                } else {
                    stopPlaying();
                }
            }
        });

        return root;
    }

    private void startRecording() {
        try {
            newFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()+createVoicePath();
            Log.d(TAG, "newFilePath :"+newFilePath);
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(newFilePath);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.prepare();
            mRecorder.start();
            b_record.setImageResource(R.drawable.ic_stop_black);
            recording = true;
        } catch (RuntimeException e) {
            Toast.makeText(getContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed i startRecording()");
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(recordingPath);
            mPlayer.prepare();
            mPlayer.start();
            b_play.setImageResource(R.drawable.ic_stop_black);
            playing = true;
        } catch (IOException e) {
            Toast.makeText(getContext(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
            Log.e(TAG, "prepare() failed i startPlaying()");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
        b_play.setImageResource(R.drawable.ic_play_arrow_black);
        playing = false;
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        recordingPath = newFilePath;
        tv_filename.setText(recordingPath);
        tv_filename.setVisibility(View.VISIBLE);
        b_record.setImageResource(R.drawable.ic_mic_black);
        recording = false;
        // Gem path til optagelse
        SharedPreferences.Editor prefedit = prefs.edit();
        Log.d(TAG, "Gemmer recording: " + recordingPath.toString());
        prefedit.putString("recording", recordingPath.toString());
        prefedit.commit();
    }

    private static String createVoicePath() {
        // Create an file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "/REC_" + timeStamp + ".3GP";
        return imageFileName;
    }

}
