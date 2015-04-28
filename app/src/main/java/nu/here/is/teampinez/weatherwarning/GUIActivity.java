package nu.here.is.teampinez.weatherwarning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class GUIActivity extends Activity {
    private static ImageButton imgButton_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreenactivity);
        OnClickButtonListener();
    }

    public void OnClickButtonListener () {
        imgButton_search = (ImageButton)findViewById(R.id.imgBtnSettings);
        imgButton_search.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("nu.here.is.teampinez.weatherwarning.SettingsActivity");
                        startActivity(intent);
                    }
                }
        );
    }

    public void openSearch(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}
