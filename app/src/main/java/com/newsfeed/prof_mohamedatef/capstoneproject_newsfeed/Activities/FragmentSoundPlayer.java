package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
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

import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.R;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Config;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase.FirebaseDataHolder;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.OptionsEntity;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.ArticlesEntity;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.OtherTypes_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.TwoPANEExtras_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.PublishToNewsFeed.KEY_FIREBASE;

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
    private FirebaseDataHolder firebaseDataHolder;
    private ArticlesEntity articlesEntity;

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

    private void startPlaying(Uri audioUri) {
        mPlayer = new MediaPlayer();
        try {
//fileName is global string. it contains the Uri to the recently recorded audio.
            mPlayer.setDataSource(audioUri.toString());
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
//        initializeAudioPlayer();
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
        if (Config.RetrieveFirebaseData){
            outState.putSerializable(KEY_FIREBASE, firebaseDataHolder);
        }else {
            outState.putSerializable(OtherTypes_KEY, articlesEntity);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            firebaseDataHolder = (FirebaseDataHolder) savedInstanceState.getSerializable(KEY_FIREBASE);
            AudioString =firebaseDataHolder.getAudiourl();
            DisplayData(AudioString);
        } else if (savedInstanceState == null) {
            final Bundle bundle = getArguments();
            if (bundle != null) {
                if (Config.RetrieveFirebaseData){
                    firebaseDataHolder=(FirebaseDataHolder) bundle.getSerializable(KEY_FIREBASE);
                    AudioString= firebaseDataHolder.getAudiourl();
                    DisplayData(AudioString);
                }else {
                    articlesEntity= (ArticlesEntity) bundle.getSerializable(OtherTypes_KEY);
                    AudioString=firebaseDataHolder.getAudiourl();
                    DisplayData(AudioString);
                }
            }
        }
        imageViewPlay.setOnClickListener(this);
    }

    private void DisplayData(String audioString) {
        if (audioString!=null) {
            AudioUri = Uri.parse(audioString);
            Config.AudioUri=AudioUri;
        }
//        playerView.setVisibility(View.VISIBLE);
//        startPlaying(AudioUri);
        if (AudioUri!=null){
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
            if (!isPlaying&&AudioUri!=null){
                isPlaying=true;
                startPlaying(AudioUri);
            }else {
                isPlaying=false;
                stopPlaying();
            }
        }
    }
}