package com.make.money.by.ads;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

/**
 * Created by LUPHAM on 10/30/2016.
 */
public class MyBanner {
    private AdView admobBanner;
    private com.facebook.ads.AdView fbBanner;
    private RelativeLayout container;
    private AdConfig adConfig;
    private Context context;

    public MyBanner(Context context, AdConfig adConfig, RelativeLayout container) {
        this.context = context;
        this.adConfig = adConfig;
        this.container = container;
    }

    public void onResume() {
        if (admobBanner != null)
            admobBanner.resume();
    }

    public void onDestroy() {
        if (admobBanner != null)
            admobBanner.destroy();
        if (fbBanner != null)
            fbBanner.destroy();
    }

    public void onPause() {
        if (admobBanner != null)
            admobBanner.pause();
    }

    private void loadAndShowAdmob() {
        admobBanner = new AdView(context);
        admobBanner.setAdSize(AdSize.SMART_BANNER);
        admobBanner.setAdUnitId(adConfig.admobID);
        admobBanner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                container.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                if (adConfig.showAdmobFirst) {
                    loadAndShowFAN();
                } else {
                    container.setVisibility(View.GONE);
                }
            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup
            .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        admobBanner.setLayoutParams(params);
        container.removeAllViews();
        container.addView(admobBanner);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(adConfig.admobTestDeviceHash)
            .build();
        admobBanner.loadAd(adRequest);
    }

    private void loadAndShowFAN() {
        AdSettings.addTestDevice(adConfig.fanTestDeviceHash);
        fbBanner = new com.facebook.ads.AdView(context, adConfig.fanID, com.facebook.ads
            .AdSize.BANNER_HEIGHT_50);
        fbBanner.setAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                if (adConfig.showAdmobFirst) {
                    container.setVisibility(View.GONE);
                } else {
                    loadAndShowAdmob();
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                container.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClicked(Ad ad) {
            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup
            .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fbBanner.setLayoutParams(params);
        fbBanner.loadAd();
        container.removeAllViews();
        container.addView(fbBanner);
    }

    public void loadAd() {
        if (adConfig.showAdmobFirst) {
            loadAndShowAdmob();
        } else {
            loadAndShowFAN();
        }
    }
}
