package nu.here.is.teampinez.weatherwarning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class GUIActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreenactivity);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}
