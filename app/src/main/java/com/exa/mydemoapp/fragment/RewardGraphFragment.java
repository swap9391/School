package com.exa.mydemoapp.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.exa.mydemoapp.HomeActivity;
import com.exa.mydemoapp.R;
import com.exa.mydemoapp.model.StudentRewardsModel;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.communication.IOnItemFocusChangedListener;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by midt-078 on 14/2/18.
 */

public class RewardGraphFragment extends CommonFragment {
    View view;
    PieChart pieChart;
    List<StudentRewardsModel> rewardModelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_reward_chart, container, false);
        pieChart = (PieChart) view.findViewById(R.id.piechart);

        rewardModelList = new ArrayList<>();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            rewardModelList = (ArrayList<StudentRewardsModel>) bundle.getSerializable("mylist");
        }
        loadData();
        pieChart.startAnimation();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        pieChart.startAnimation();
    }

    public void restartAnimation() {
        pieChart.startAnimation();
    }

    private void loadData() {
        int sportsPoint = 0, culturePoint = 0, interSchoolPoint = 0, acadamicPoint = 0, otherPoint = 0;

        for (StudentRewardsModel rewardModel : rewardModelList) {
            if (rewardModel.getRewardType().equals(getStringById(R.string.reward_sports))) {
                sportsPoint = sportsPoint + rewardModel.getPoints();
            } else if (rewardModel.getRewardType().equals(getStringById(R.string.reward_cultural))) {
                culturePoint = culturePoint + rewardModel.getPoints();
            } else if (rewardModel.getRewardType().equals(getStringById(R.string.reward_inter_school))) {
                interSchoolPoint = interSchoolPoint + rewardModel.getPoints();
            } else if (rewardModel.getRewardType().equals(getStringById(R.string.reward_academics))) {
                acadamicPoint = acadamicPoint + rewardModel.getPoints();
            } else if (rewardModel.getRewardType().equals(getStringById(R.string.reward_other))) {
                otherPoint = otherPoint + rewardModel.getPoints();
            }
        }
        int total = sportsPoint + culturePoint + interSchoolPoint + acadamicPoint + otherPoint;
        getMyActivity().toolbar.setTitle("Total Points " + total);
        pieChart.addPieSlice(new PieModel(getStringById(R.string.reward_sports), sportsPoint, Color.parseColor("#FE6DA8")));
        pieChart.addPieSlice(new PieModel(getStringById(R.string.reward_cultural), culturePoint, Color.parseColor("#56B7F1")));
        pieChart.addPieSlice(new PieModel(getStringById(R.string.reward_inter_school), interSchoolPoint, Color.parseColor("#CDA67F")));
        pieChart.addPieSlice(new PieModel(getStringById(R.string.reward_academics), acadamicPoint, Color.parseColor("#FED70E")));
        pieChart.addPieSlice(new PieModel(getStringById(R.string.reward_other), otherPoint, Color.parseColor("#8FCC85")));

        pieChart.setOnItemFocusChangedListener(new IOnItemFocusChangedListener() {
            @Override
            public void onItemFocusChanged(int _Position) {
                Log.d("PieChart", "Position: " + _Position);
            }
        });

    }

    private HomeActivity getMyActivity() {
        return (HomeActivity) getActivity();
    }
}
