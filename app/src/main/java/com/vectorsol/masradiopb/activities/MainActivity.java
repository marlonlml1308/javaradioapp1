package com.vectorsol.masradiopb.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.vectorsol.masradiopb.Config;
import com.vectorsol.masradiopb.R;
import com.vectorsol.masradiopb.services.PlaybackStatus;
import com.vectorsol.masradiopb.services.RadioManager;
import com.vectorsol.masradiopb.services.metadata.Metadata;
import com.vectorsol.masradiopb.services.parser.UrlParser;
import com.vectorsol.masradiopb.utilities.PermissionsFragment;
import com.vectorsol.masradiopb.utilities.Tools;

import java.util.List;
import es.claucookie.miniequalizerlibrary.EqualizerView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PermissionsFragment, Tools.EventListener {

    RadioManager radioManager;
    String radio_url = Config.RADIO_STREAM_URL;
    RoundedImageView albumArtView;
    ImageView bgImageView;
    FloatingActionButton buttonPlayPause;
    EqualizerView equalizerView;
    Tools tools;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tools = new Tools(this);
        initializeUIElements();

        new Handler().postDelayed(() -> buttonPlayPause.performClick(), 1000);

        albumArtView.setImageResource(Tools.BACKGROUND_IMAGE_ID);


        radioManager = RadioManager.with();

        AsyncTask.execute(() -> {
            radio_url = (UrlParser.getUrl(radio_url));
            this.runOnUiThread(() -> {

            });
        });

        if (isPlaying()) {
            onAudioSessionId(RadioManager.getService().getAudioSessionId());
        }

       // WebView web = findViewById(R.id.webView1);
      //  WebSettings webSettings = web.getSettings();
      //  webSettings.setJavaScriptEnabled(true);
       // web.setWebViewClient(new Callback());
       // web.loadUrl("https://www3.cbox.ws/box/?boxid=3503950&boxtag=t4tp2h");



        Button button12 = findViewById(R.id.buttonShow);
        button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                MainActivity.this, R.style.BottomSheetDialogTheme
               );
              // WebView web = findViewById(R.id.webView1);
              // web.loadUrl("https://www3.cbox.ws/box/?boxid=3503950&boxtag=t4tp2h");

                View bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(
                                R.layout.layout_bottom_sheet,
                                (RelativeLayout) findViewById(R.id.bottomSheetContainer)

                        );
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

    }


    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return false;
        }
    }
    public void irFacebook(View view){
        Intent i = new Intent( Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/MasRadioPb"));
        startActivity(i);
    }
    public void irInstagram(View view){
        Intent i = new Intent( Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/masradiopb"));
        startActivity(i);
    }
    public void irYoutube(View view){
        Intent i = new Intent( Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCAvUKBmVQEzKT0vXH6pu4Dg"));
        startActivity(i);
    }
    public void irWhatsapp(View view){
        Intent i = new Intent( Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+57320%203334162&text=Muchas%20bendiciones%20Masradiopb,%20estoy%20en%20sintonia%20desde..."));
        startActivity(i);
    }
    public void irWeb(View view){
        Intent i = new Intent( Intent.ACTION_VIEW, Uri.parse("https://www.ipucpatiobonito.com"));
        startActivity(i);
    }
    @Override
    public void onEvent(String status) {
        switch (status) {
            case PlaybackStatus.LOADING:
                progressBar.setVisibility(View.VISIBLE);
                break;
            case PlaybackStatus.ERROR:
                makeSnackBar(R.string.error_retry);
                break;
        }

        if (!status.equals(PlaybackStatus.LOADING))
            progressBar.setVisibility(View.INVISIBLE);
        updateButtons();

    }

    @Override
    public void onAudioSessionId(Integer i) {
    }

    @Override
    public void onStart() {
        super.onStart();
        Tools.registerAsListener(this);
    }

    @Override
    public void onStop() {
        Tools.unregisterAsListener(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (!radioManager.isPlaying())
            radioManager.unbind(MainActivity.this);
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateButtons();
        radioManager.bind(MainActivity.this);
    }

    private void initializeUIElements() {
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setVisibility(View.VISIBLE);

        equalizerView = findViewById(R.id.equalizer_view);

        albumArtView = findViewById(R.id.radio_album);
        bgImageView = findViewById(R.id.bg_image_view);

        if(Config.ALBUM_ART_DISK){
            albumArtView.setOval(true);
        }else {
            albumArtView.setOval(false);
        }

        buttonPlayPause = findViewById(R.id.playBtn);
        buttonPlayPause.setOnClickListener(this);

        equalizerView.stopBars();
        updateButtons();
    }

    private void updateButtons() {
        if (isPlaying() || progressBar.getVisibility() == View.VISIBLE) {
            //If another stream is playing, show this in the layout
            if (RadioManager.getService() != null && radio_url != null && !radio_url.equals(RadioManager.getService().getStreamUrl())) {
                buttonPlayPause.setImageResource(R.drawable.ic_play_white);
                //TODO findViewById(R.id.already_playing_tooltip).setVisibility(View.VISIBLE);
                //If this stream is playing, adjust the buttons accordingly
            } else {
                if (RadioManager.getService() != null && RadioManager.getService().getMetaData() != null) {
                    onMetaDataReceived(RadioManager.getService().getMetaData(), RadioManager.getService().getAlbumArt());
                }
                buttonPlayPause.setImageResource(R.drawable.ic_pause_white);
                //TODO findViewById(R.id.already_playing_tooltip).setVisibility(View.GONE);
            }
        } else {
            //If this stream is paused, adjust the buttons accordingly
            buttonPlayPause.setImageResource(R.drawable.ic_play_white);
            //TODO findViewById(R.id.already_playing_tooltip).setVisibility(View.GONE);

            updateMediaInfoFromBackground(null, null);
        }

        if (isPlaying()) {
            equalizerView.animateBars();
        } else {
            equalizerView.stopBars();
        }

    }

    @Override
    public void onClick(View v) {
        requestStoragePermission();
    }

    private void startStopPlaying() {
        radioManager.playOrPause(radio_url);
        updateButtons();

    }

    private void stopService() {
        radioManager.stopServices();
        Tools.unregisterAsListener(this);
    }


    //@param info - the text to be updated. Giving a null string will hide the info.
    public void updateMediaInfoFromBackground(String info, Bitmap image) {

        TextView nowPlayingTitle = findViewById(R.id.now_playing);
        TextView nowPlaying = findViewById(R.id.radio_description);

        if (info != null)
            nowPlaying.setText(info);

        if (info != null && nowPlayingTitle.getVisibility() == View.GONE) {
            nowPlayingTitle.setVisibility(View.VISIBLE);
            nowPlaying.setVisibility(View.VISIBLE);
        } else if (info == null) {
            nowPlayingTitle.setVisibility(View.VISIBLE);
            nowPlayingTitle.setText(R.string.now_playing);
            nowPlaying.setVisibility(View.VISIBLE);
            nowPlaying.setText(R.string.app_name);
        }

        if (image != null) {
            albumArtView.setImageBitmap(image);

        } else {
            albumArtView.setImageResource(Tools.BACKGROUND_IMAGE_ID);

        }

    }

    @Override
    public String[] requiredPermissions() {
        return new String[]{Manifest.permission.READ_PHONE_STATE};
    }

    @Override
    public void onMetaDataReceived(Metadata meta, Bitmap image) {
        //Update the mediainfo shown above the controls
        String artistAndSong = null;
        if (meta != null && meta.getArtist() != null)
            artistAndSong = meta.getArtist() + " - " + meta.getSong();
        updateMediaInfoFromBackground(artistAndSong, image);
    }

    private boolean isPlaying() {
        return (null != radioManager && null != RadioManager.getService() && RadioManager.getService().isPlaying());
    }


    private void makeSnackBar(int text) {
        Snackbar bar = Snackbar.make(buttonPlayPause, text, Snackbar.LENGTH_SHORT);
        bar.show();
        ((TextView) bar.getView().findViewById(R.id.snackbar_text)).setTextColor(getResources().getColor(R.color.white));
    }

    public void onBackPressed() {
        exitDialog();
    }

    public void exitDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle(R.string.app_name);
        dialog.setMessage(getResources().getString(R.string.message));
        dialog.setPositiveButton(getResources().getString(R.string.quit), (dialogInterface, i) -> {
            stopService();
            MainActivity.this.finish();
        });
        dialog.setNegativeButton(getResources().getString(R.string.minimize), (dialogInterface, i) -> minimizeApp());
        dialog.setNeutralButton(getResources().getString(R.string.cancel), (dialogInterface, i) -> {
        });
        dialog.show();
    }


    public void minimizeApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void requestStoragePermission() {
        Dexter.withActivity(MainActivity.this)
                .withPermissions(Manifest.permission.READ_PHONE_STATE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted

                        if (report.areAllPermissionsGranted()) {
                            if (!isPlaying()) {
                                if (radio_url != null) {
                                    startStopPlaying();
                                    //TODO showInterstitialAd();
                                    //Check the sound level
                                    AudioManager audioManager = (AudioManager) MainActivity.this.getSystemService(Context.AUDIO_SERVICE);
                                    int volume_level = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                                    if (volume_level < 2) {
                                        makeSnackBar(R.string.volume_low);
                                    }
                                } else {
                                    //The loading of urlToPlay should happen almost instantly, so this code should never be reached
                                    makeSnackBar(R.string.error_retry_later);
                                }
                            } else {
                                startStopPlaying();
                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(error -> Toast.makeText(MainActivity.this, "Error occurred! " + error.toString(), Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

}