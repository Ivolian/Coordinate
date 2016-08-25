package com.unicorn.coordinate.base;

import org.greenrobot.eventbus.EventBus;

public abstract class EventActivity extends BaseActivity {

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

}
