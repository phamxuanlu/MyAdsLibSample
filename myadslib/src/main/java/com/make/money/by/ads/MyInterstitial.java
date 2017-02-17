package com.make.money.by.ads;

import android.content.Context;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by LUPHAM on 10/30/2016.
 */
public class MyInterstitial {
    private Context context;
    private AdConfig config;
    private InterstitialAd admobInterstitial;
    private com.facebook.ads.InterstitialAd fbInterstitial;
    private AdCallback adCallback;

    public MyInterstitial(Context context, AdConfig config) {
        this.config = config;
        this.context = context;
    }

    private void loadAdmobInterstitial() {
        admobInterstitial = new InterstitialAd(context);
        admobInterstitial.setAdUnitId(config.admobID);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(config.admobTestDeviceHash)
            .build();
        admobInterstitial.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                if (config.showAdmobFirst) {
                    loadFANInterstitial();
                }
            }
        });
        admobInterstitial.loadAd(adRequest);
    }

    private void loadFANInterstitial() {
        AdSettings.addTestDevice(config.fanTestDeviceHash);
        fbInterstitial = new com.facebook.ads.InterstitialAd(context, config.fanID);
        fbInterstitial.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                if (!config.showAdmobFirst) {
                    loadAdmobInterstitial();
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
            }

            @Override
            public void onAdClicked(Ad ad) {
            }
        });
        fbInterstitial.loadAd();
    }

    public void loadAd() {
        if (config.showAdmobFirst) {
            loadAdmobInterstitial();
        } else {
            loadFANInterstitial();
        }
    }

    private void showAdmobAd() {
        if (admobInterstitial != null && admobInterstitial.isLoaded()) {
            admobInterstitial.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    if (adCallback != null) {
                        adCallback.doNext();
                    }
                }
            });
            admobInterstitial.show();
            config.showAdmobFirst = !config.showAdmobFirst;
            loadAd();
        } else if (fbInterstitial != null && fbInterstitial.isAdLoaded()) {
            showFANAd();
        } else {
            if (adCallback != null) {
                adCallback.doNext();
            }
        }
    }

    private void showFANAd() {
        if (fbInterstitial != null && fbInterstitial.isAdLoaded()) {
            fbInterstitial.setAdListener(new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    if (adCallback != null) {
                        adCallback.doNext();
                    }
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                }

                @Override
                public void onAdLoaded(Ad ad) {
                }

                @Override
                public void onAdClicked(Ad ad) {
                }
            });
            fbInterstitial.show();
            config.showAdmobFirst = !config.showAdmobFirst;
            loadAd();
        } else if (admobInterstitial!=null && admobInterstitial.isLoaded()) {
            showAdmobAd();
        } else {
            if (adCallback != null) {
                adCallback.doNext();
            }
        }
    }

    public void showAd(AdCallback callback) {
        this.adCallback = callback;
        if (config.showAdmobFirst) {
            showAdmobAd();
        } else {
            showFANAd();
        }
    }

    public void onDestroy() {
        if (fbInterstitial != null)
            fbInterstitial.destroy();
    }
}
