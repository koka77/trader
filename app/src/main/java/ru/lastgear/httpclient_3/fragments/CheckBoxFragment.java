package ru.lastgear.httpclient_3.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import ru.lastgear.httpclient_3.R;

/**
 * Created by October on 29.01.2017.
 */

public class CheckBoxFragment extends Fragment {

    ListView lv1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.checkbox_fragment, container, false);

        lv1 = (ListView)view.findViewById(R.id.lv1);

        final String[] catNames = new String[] {
                "Рыжик", "Барсик", "Мурзик", "Мурка", "Васька", "Томасина", "Кристина", "Пушок", "Дымка", "Кузя","Китти", "Масяня", "Симба" };
        // используем адаптер данных
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, catNames);
        lv1.setAdapter(adapter);

        return  view;
    }
}
