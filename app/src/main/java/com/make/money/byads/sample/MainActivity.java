package com.make.money.byads.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.make.money.by.ads.AdCallback;
import com.make.money.by.ads.AdConfig;
import com.make.money.by.ads.MyBanner;
import com.make.money.by.ads.MyInterstitial;

/**
 * Created by pham.xuan.lu on 2/17/17.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //Banner
    private RelativeLayout adContainer;
    private MyBanner banner;
    //Interstitial
    private MyInterstitial interstitial;
    private Button btnShowAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adContainer = (RelativeLayout) findViewById(R.id.adContainer);
        btnShowAd = (Button) findViewById(R.id.btnShowAd);
        btnShowAd.setOnClickListener(this);
        setupBanner();
        setupInterstitial();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShowAd:
                if (interstitial != null) {
                    interstitial.showAd(new AdCallback() {
                        @Override
                        public void doNext() {
                            showNextActivity();
                        }
                    });
                } else {
                    showNextActivity();
                }
                break;
        }
    }

    private void showNextActivity() {
        Intent intent = new Intent(this, NextActivity.class);
        startActivity(intent);
    }

    private void setupInterstitial() {
        AdConfig config = new AdConfig();
        config.admobID = "ca-app-pub-3940256099942544/1033173712";
        config.fanID = "aa_bbb_ccc";
        config.admobTestDeviceHash = "29CA657877FF8A5D89AFF8511D5C5E74";
        config.fanTestDeviceHash = "e231e3b445930b560ccc6ef299866e5e";
        config.showAdmobFirst = true; //Config
        interstitial = new MyInterstitial(this, config);
        interstitial.loadAd();
    }

    private void setupBanner() {
        AdConfig bannerConfig = new AdConfig();
        bannerConfig.admobID = "ca-app-pub-3940256099942544/6300978111";
        bannerConfig.fanID = "aa_bbb_ccc";
        bannerConfig.admobTestDeviceHash = "29CA657877FF8A5D89AFF8511D5C5E74";
        bannerConfig.fanTestDeviceHash = "e231e3b445930b560ccc6ef299866e5e";
        bannerConfig.showAdmobFirst = false; //Config
        banner = new MyBanner(this, bannerConfig, adContainer);
        banner.loadAd();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (banner != null)
            banner.onResume();
    }

    @Override
    protected void onPause() {
        if (banner != null)
            banner.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (banner != null)
            banner.onDestroy();
        if (interstitial != null)
            interstitial.onDestroy();
        super.onDestroy();
    }
}
