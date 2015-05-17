package nu.here.is.teampinez.weatherwarning;

import android.widget.BaseAdapter;

import static nu.here.is.teampinez.weatherwarning.StationActivity.FIRST_COLUMN;
import static nu.here.is.teampinez.weatherwarning.StationActivity.SECOND_COLUMN;
import static nu.here.is.teampinez.weatherwarning.StationActivity.THIRD_COLUMN;
import static nu.here.is.teampinez.weatherwarning.StationActivity.FOURTH_COLUMN;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewAdapter extends BaseAdapter {

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;
    TextView txtFourth;

    public ListViewAdapter(Activity activity,ArrayList<HashMap<String, String>> list){
        super();
        this.activity   = activity;
        this.list       = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.station_view_list, null);

            txtFirst=(TextView) convertView.findViewById(R.id.stationName);
            txtSecond=(TextView) convertView.findViewById(R.id.tempRoad);
            txtThird=(TextView) convertView.findViewById(R.id.tempAir);
            txtFourth=(TextView) convertView.findViewById(R.id.windSpeed);

        }

        HashMap<String, String> map=list.get(position);
        txtFirst.setText(map.get(FIRST_COLUMN));
        txtSecond.setText(map.get(SECOND_COLUMN));
        txtThird.setText(map.get(THIRD_COLUMN));
        txtFourth.setText(map.get(FOURTH_COLUMN));

        return convertView;
    }


}
