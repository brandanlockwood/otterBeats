package com.dimao.otterbeats;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;

import org.json.JSONObject;

public class MainActivity extends Activity implements
        PlayerNotificationCallback, ConnectionStateCallback {

    private IOSocket socket;
    private boolean musicPaused = false;

    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "25076b93d71740f1975cd641cf486e9a";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "moodtunes://callback";

    private static final String SPOTIFY_URI_BEG = "spotify:user:spotify:playlist:";

    // Request code that will be passed together with authentication result to the onAuthenticationResult callback
    // Can be any integer
    private static final int REQUEST_CODE = 1337;

    private Player mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        addListenerOnButton1();
        addListenerOnButton2();
        addListenerOnButton3();
        addListenerOnButton4();
        addListenerOnButton5();
        addListenerOnButton6();
        addListenerOnButton7();
        MoodInterpreter.getInstance(getApplicationContext());

        connect();
    }

    public void addListenerOnButton1() {

        Button button = (Button) findViewById(R.id.happyButton);

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String real = SPOTIFY_URI_BEG + "1B9o7mER9kfxbmsRH9ko4z";
                mPlayer.play(real);
                //mPlayer.setShuffle(true);

            }

        });
    }

    public void addListenerOnButton2() {

        ImageButton button = (ImageButton) findViewById(R.id.skipButton);

        button.setOnClickListener(new OnClickListener()  {

            @Override
            public void onClick(View arg0) {
                Log.d("Main", "hit skip");
                String next = SPOTIFY_URI_BEG;
                if(!MoodInterpreter.getInstance().getUpdate().equals("")) {
                    String nextStation = SPOTIFY_URI_BEG + MoodInterpreter.getInstance().getUpdate();
                    Log.d("station", nextStation);
                    mPlayer.play(nextStation);
                }
                else {
                    mPlayer.skipToNext();
                }
            }

        });
    }

    public void addListenerOnButton3() {

        ImageButton button = (ImageButton) findViewById(R.id.prevButton);

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                mPlayer.skipToPrevious();
            }

        });
    }

/*    public void addListenerOnButton4() {

        button = (Button) findViewById(R.id.pauseButton);

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                mPlayer.pause();
                musicPaused = true;
            }

        });
    }

    public void addListenerOnButton5() {

        button = (Button) findViewById(R.id.playButton);

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(musicPaused) {
                    mPlayer.resume();
                    musicPaused = false;
                }
            }

        });
    }*/

    public void addListenerOnButton4() {

        ToggleButton toggle = (ToggleButton) findViewById(R.id.playPauseButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayer.pause();
                } else {
                    mPlayer.resume();
                }
            }
        });
    }

    public void addListenerOnButton5() {

        ToggleButton toggle = (ToggleButton) findViewById(R.id.repeatButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayer.setRepeat(true);
                } else {
                    mPlayer.setRepeat(false);
                }
            }
        });
    }
    public void addListenerOnButton7() {

        ToggleButton toggle = (ToggleButton) findViewById(R.id.elevateMaintainButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //elevate
                    MoodInterpreter.getInstance(getApplicationContext()).setMode(2);
                } else {
                    //maintain
                    MoodInterpreter.getInstance(getApplicationContext()).setMode(1);
                }
            }
        });
    }

    public void addListenerOnButton6() {

        ToggleButton toggle = (ToggleButton) findViewById(R.id.shuffleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPlayer.setShuffle(true);
                } else {
                    mPlayer.setShuffle(false);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);


        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                    @Override
                    public void onInitialized(Player player) {
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addPlayerNotificationCallback(MainActivity.this);
                        mPlayer.play("spotify:user:spotify:playlist:1B9o7mER9kfxbmsRH9ko4z");
                        mPlayer.pause();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        //Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    @Override
    public void onLoggedIn() {
        //Log.d("MainActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        //Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        //Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        //Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        //Log.d("MainActivity", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        Log.d("MainActivity", "Playback event received: " + eventType.name());
        switch (eventType.name()) {
            // Handle event type as necessary
            case "TRACK_END":
                //check emotional state
                //pick next station or continue
                String next = SPOTIFY_URI_BEG;
                if(!MoodInterpreter.getInstance().getUpdate().equals("")) {
                    String nextStation = SPOTIFY_URI_BEG + MoodInterpreter.getInstance().getUpdate();
                    mPlayer.play(nextStation);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails) {
        Log.d("MainActivity", "Playback error received: " + errorType.name());
        switch (errorType) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }


    void connect() {

        socket = new IOSocket("http://aipservers.com:3001", new MessageCallback() {

            @Override
            public void onMessage(String message) {
                // Handle simple messages
            }

            @Override
            public void onConnect() {
                // Socket connection opened
            }

            @Override
            public void onDisconnect() {
                // Socket connection closed
            }

            @Override
            public void on(String event, JSONObject... data) {

                Log.i("logging", event);
                int temp = 0;
                switch (event){
                    case "excitement": temp = 1;
                        break;
                    case "engagement": temp = 2;
                        break;
                    case "bored": temp = 3;
                        break;
                    case "frustration": temp = 4;
                        break;
                    case "meditation": temp = 5;
                        break;
                }
                MoodInterpreter.getInstance().setInitMood(temp);
                MoodInterpreter.getInstance().setCurrentMood(temp);
            }

            @Override
            public void onMessage(JSONObject json) {

            }

            @Override
            public void onConnectFailure() {

            }
        });

        socket.connect();
    }


    /*
    void connect(){
        String initEm = FakeEmotions.getInstance().getStartingEmotion();
        int temp = 0;
        switch (initEm){
            case "EXCITEMENT": temp = 1;
                break;
            case "ENGAGEMENT": temp = 2;
                break;
            case "BOREDOM": temp = 3;
                break;
            case "FRUSTRATION": temp = 4;
                break;
            case "MEDITATION": temp = 5;
                break;
        }
        MoodInterpreter.getInstance().setInitMood(temp);
    }
    */
}
