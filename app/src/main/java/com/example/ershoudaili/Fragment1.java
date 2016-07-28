package com.example.ershoudaili;



import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

public class Fragment1 extends Fragment
{
    private HashMap<Integer, String> map1;
    private TextView color;
    private TextView weight;
    private TextView material;
    private TextView size;
    private TextView producing_area;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment1, container, false);
        Intent intent2 = getActivity().getIntent();
        Bundle bundle = intent2.getExtras();
        map1 = ((Product_display)getActivity()).getmap();
        color = (TextView)view.findViewById(R.id.color);
        weight = (TextView)view.findViewById(R.id.weight);
        material = (TextView)view.findViewById(R.id.material);
        size = (TextView)view.findViewById(R.id.size);
        //producing_area = (TextView)view.findViewById(R.id.producing_area);
        if(map1.get(4) != null) {
            color.setText(map1.get(4));
        }
        if(map1.get(5) != null) {
            weight.setText(map1.get(5));
        }
        if(map1.get(6) != null) {
            material.setText(map1.get(6));
        }
        if(map1.get(7) != null) {
            size.setText(map1.get(7));
        }
//        if(map1.get(8) != null) {
//            producing_area.setText(map1.get(8));
//        }
        return view;
    }
}
