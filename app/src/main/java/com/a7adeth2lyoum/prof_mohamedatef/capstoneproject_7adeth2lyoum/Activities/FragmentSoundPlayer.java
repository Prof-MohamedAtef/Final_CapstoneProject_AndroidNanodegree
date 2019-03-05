package com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.Activities;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.R;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Config;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.OptionsEntity;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.Activities.ArticleTypesListActivity.TwoPANEExtras_KEY;

/**
 * Created by Prof-Mohamed Atef on 1/10/2019.
 */

public class FragmentSoundPlayer extends Fragment implements View.OnClickListener{

    String AudioString;
    Uri AudioUri;
    public static OptionsEntity optionsEntity;
    public static String KEY_optionsEntity="Options";
    private ImageView audio_muted;
    private MediaPlayer mPlayer;
    private String fileName = null;
    private int lastProgress = 0;
    private Handler mHandler = new Handler();
    private boolean isPlaying = false;
    @BindView(R.id.imageViewPlay)
    ImageView imageViewPlay;
    @BindView(R.id.chronometerTimer)
    Chronometer chronometerTimer;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.linearLayoutPlay)
    LinearLayout linearLayoutPlay;
//    private FirebaseDataHolder firebaseDataHolder;

    private void stopPlaying() {
        try{
            mPlayer.release();
        }catch (Exception e){
            e.printStackTrace();
        }
        mPlayer = null;
        //showing the play button
        imageViewPlay.setImageResource(R.drawable.ic_play);
        chronometerTimer.stop();
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
//fileName is global string. it contains the Uri to the recently recorded audio.
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("LOG_TAG", "prepare() failed");
        }
        //making the imageview pause button
        imageViewPlay.setImageResource(R.drawable.ic_pause);
        seekBar.setProgress(lastProgress);
        mPlayer.seekTo(lastProgress);
        seekBar.setMax(mPlayer.getDuration());
        seekUpdation();
        chronometerTimer.start();

        /** once the audio is complete, timer is stopped here**/
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imageViewPlay.setImageResource(R.drawable.ic_play);
                isPlaying = false;
                chronometerTimer.stop();
            }
        });

        /** moving the track as per the seekBar's position**/
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if( mPlayer!=null && fromUser ){
                    //here the track's progress is being changed as per the progress bar
                    mPlayer.seekTo(progress);
                    //timer is being updated as per the progress of the seekbar
                    chronometerTimer.setBase(SystemClock.elapsedRealtime() - mPlayer.getCurrentPosition());
                    lastProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };

    private void seekUpdation() {
        if(mPlayer != null){
            int mCurrentPosition = mPlayer.getCurrentPosition() ;
            seekBar.setProgress(mCurrentPosition);
            lastProgress = mCurrentPosition;
        }
        mHandler.postDelayed(runnable, 100);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_audio_player, container, false);
        ButterKnife.bind(this, rootView);
        audio_muted=(ImageView)rootView.findViewById(R.id.audio_muted);
        audio_muted.setVisibility(View.GONE);
        initializeAudioPlayer();
        return rootView;
    }

    private void initializeAudioPlayer() {
        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios");
        if (file.exists()) {
            fileName = root.getAbsolutePath() + "/VoiceRecorderSimplifiedCoding/Audios/" + "1548717911705" + ".mp3";
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_optionsEntity,optionsEntity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            optionsEntity = (OptionsEntity) savedInstanceState.getSerializable(KEY_optionsEntity);
            AudioString=optionsEntity.getAudioFile();
            DisplayData(AudioString);
        } else if (savedInstanceState == null) {
            final Bundle bundle = getArguments();
            if (bundle != null) {
                if (Config.RetrieveFirebaseData){
//                    firebaseDataHolder=(FirebaseDataHolder) bundle.getSerializable(TwoPANEExtras_KEY);
//                    AudioString= firebaseDataHolder.getAudioURL();
                    DisplayData(AudioString);
                }else {
                    optionsEntity = (OptionsEntity) bundle.getSerializable(TwoPANEExtras_KEY);
                    AudioString=optionsEntity.getAudioFile();
                    DisplayData(AudioString);
                }
            }
        }
        imageViewPlay.setOnClickListener(this);
    }

    private void DisplayData(String audioString) {
//        if (AudioString!=null){
//        AudioUri = Uri.parse(AudioString);
//        playerView.setVisibility(View.VISIBLE);
//        initializePlayer(AudioUri);
        if (fileName!=null){
            linearLayoutPlay.setVisibility(View.VISIBLE);
        }else {
            audio_muted.setVisibility(View.VISIBLE);
            Drawable x =ContextCompat.getDrawable(getActivity(),R.drawable.audio_mute);
            Picasso.with(getActivity()).load(String.valueOf(x))
                    .error(R.drawable.audio_mute)
                    .into(audio_muted);
        }
    }

    @Override
    public void onClick(View v) {
        if (v==imageViewPlay){
            if (!isPlaying&&fileName!=null){
                isPlaying=true;
                startPlaying();
            }else {
                isPlaying=false;
                stopPlaying();
            }
        }
    }
}