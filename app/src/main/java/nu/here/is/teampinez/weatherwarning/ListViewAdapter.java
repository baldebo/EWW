package nu.here.is.teampinez.weatherwarning;

import nu.here.is.teampinez.weatherwarning.parser.Station;

import android.widget.BaseAdapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    public ArrayList<Station> list;
    Activity activity;
    TextView stationName;
    TextView tempRoad;
    TextView tempAir;
    TextView windSpeed;

    public ListViewAdapter(Activity activity,ArrayList<Station> list){
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

            stationName=(TextView) convertView.findViewById(R.id.stationName);
            tempRoad=(TextView) convertView.findViewById(R.id.tempRoad);
            tempAir=(TextView) convertView.findViewById(R.id.tempAir);
            windSpeed=(TextView) convertView.findViewById(R.id.windSpeed);

        }


//        HashMap<String, String> map=list.get(position);
//        try {
//            if(!map.get(THIRD_COLUMN).equals("N/A")) {
//                double bob = Double.parseDouble(map.get(THIRD_COLUMN).substring(0, map.get(THIRD_COLUMN).length()-2));
//                if (bob < 9){
//                    tempAir.setTextColor(Color.parseColor("#FF0000"));
//                }else {
//                    tempAir.setTextColor(Color.parseColor("#000000"));
//                }
//            }
//        } catch (NumberFormatException e) {
//            Log.i(getClass().getName(), e.toString());
//        }

        Station s = list.get(position);
        stationName.setText(s.name);
        tempRoad.setText(s.roadTemp);
        tempAir.setText(s.airTemp);
        windSpeed.setText(s.windSpeed);

        return convertView;
    }
}
