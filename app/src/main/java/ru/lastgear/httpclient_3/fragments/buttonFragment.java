package ru.lastgear.httpclient_3.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.lastgear.httpclient_3.R;

/**
 * Created by October on 29.01.2017.
 */

public class buttonFragment extends Fragment implements View.OnClickListener{

    Button bt1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.button_fragment, container, false);

        bt1 = (Button)view.findViewById(R.id.button);
        bt1.setOnClickListener(this);



        return  view;
    }


    @Override
    public void onClick(View v) {
        bt1.setText("sdfsd");
    }
}
