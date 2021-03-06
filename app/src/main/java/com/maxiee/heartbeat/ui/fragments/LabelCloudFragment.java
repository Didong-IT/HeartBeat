package com.maxiee.heartbeat.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.maxiee.heartbeat.R;
import com.maxiee.heartbeat.common.cloudview.CloudView;
import com.maxiee.heartbeat.database.utils.LabelUtils;
import com.maxiee.heartbeat.ui.LabelDetailActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by maxiee on 15-6-24.
 */
public class LabelCloudFragment extends Fragment{

    @Bind(R.id.cloud_view)      CloudView           mCloudView;
    @Bind(R.id.cloud_layout)    NestedScrollView    mCloudLayout;
    @Bind(R.id.empty)           RelativeLayout      mEmptyLayout;
    @Bind(R.id.image_empty)     ImageView           mImageEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_label_cloud, container, false);
        ButterKnife.bind(this, v);

        updateCloud();

        return v;
    }

    public void updateCloud() {

        Map<Long,Integer> allLabels = LabelUtils.getFreq(getActivity());
        List<Pair<String, Integer>> labels = new LinkedList<>();

        if (allLabels != null) {
            ArrayList<Map.Entry<Long, Integer>> list = new ArrayList<>(allLabels.entrySet());
            for (Map.Entry<Long, Integer> label: list) {
                labels.add(new Pair<String, Integer>(
                        LabelUtils.getLabelByLabelId(getActivity(), label.getKey()).getLabel(),
                        label.getValue()));
            }
        }

        if (!labels.isEmpty()) {
            mCloudLayout.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
            mCloudView.addLabels(labels);
            mCloudView.setOnLabelClickListener(new CloudView.OnLabelClickListener() {
                @Override
                public void onClick(String label) {
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_MAIN);
                    i.setClass(getActivity(), LabelDetailActivity.class);
                    i.putExtra("tag_text", label);
                    startActivity(i);
                }
            });
        } else {
            mCloudLayout.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(R.drawable.empty_bg1).into(mImageEmpty);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCloud();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
