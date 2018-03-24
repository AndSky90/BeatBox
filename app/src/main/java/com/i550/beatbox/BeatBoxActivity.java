package com.i550.beatbox;

import android.support.v4.app.Fragment;

public class BeatBoxActivity extends SingleFragmentActiviy {

    @Override
    protected Fragment createFragment() {
        return BeatBoxFragment.newInstance();
    }
}
