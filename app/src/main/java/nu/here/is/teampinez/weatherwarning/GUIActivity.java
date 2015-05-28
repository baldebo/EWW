package nu.here.is.teampinez.weatherwarning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;

public class GUIActivity extends Activity implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private final String TTS_ERROR_MSG = "Distraction level too high";
    private final String TOAST_ERROR_MSG = "Unavailable while driving";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreenactivity);

        tts = new TextToSpeech(this, this);
        new AGAListener().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        tts.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts.stop();
        tts.shutdown();
    }

    @Override
    public void onInit(int i) {
        if(i == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.getDefault());
        }
    }

    /**
     * These methods facilitate changes between activities
     **/

    public void openDriveView(View view) {
        Intent intent = new Intent(this, DriveActivity.class);
        startActivity(intent);
    }

    public void openStationView(View view) {
        if(AGAValues.IN_MOTION == 1) {
            if(AGAValues.DISTRACTION_LEVEL < 3) {
                Intent intent = new Intent(this, StationActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), TOAST_ERROR_MSG, Toast.LENGTH_LONG).show();
                //noinspection deprecation
                tts.speak(TTS_ERROR_MSG, TextToSpeech.QUEUE_FLUSH, null);
            }
        } else {
            Intent intent = new Intent(this, StationActivity.class);
            startActivity(intent);
        }
    }
}
