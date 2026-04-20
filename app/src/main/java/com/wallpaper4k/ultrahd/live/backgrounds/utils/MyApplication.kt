package com.wallpaper4k.ultrahd.live.backgrounds.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.google.firebase.FirebaseApp
import com.wallpaper4k.ultrahd.live.backgrounds.utils.ads.MyInterstitialAds
import com.wallpaper4k.ultrahd.live.backgrounds.utils.ads.RewardAds
import java.util.Locale

class MyApplication : Application() {
    var intAd: MyInterstitialAds? = null
    var rewardAd: RewardAds? = null
    override fun onCreate() {
        FirebaseApp.initializeApp(this)


        setLanguage()




        super.onCreate()
    }

    private fun setLanguage() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
                val sessionManager = SessionManager(activity)
                val locale = Locale(sessionManager.getLanguage())
                Locale.setDefault(locale)
                val resources = activity.resources
                val configuration = resources.configuration
                configuration.setLocale(locale)
                configuration.setLayoutDirection(locale)
                resources.updateConfiguration(configuration, resources.displayMetrics)
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {
                val sessionManager = SessionManager(activity)
                val locale = Locale(sessionManager.getLanguage())
                Locale.setDefault(locale)
                val resources = activity.resources
                val configuration = resources.configuration
                configuration.setLocale(locale)
                configuration.setLayoutDirection(locale)
                resources.updateConfiguration(configuration, resources.displayMetrics)
            }

            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })

    }


    fun initializeIntAd(context: Activity) {
        if (intAd == null) {
            intAd = MyInterstitialAds(context.applicationContext, false, object : MyInterstitialAds.OnShow {

                override fun onShow() {
                    Log.i("TAG", "Interstitial shown")
                }

                override fun onFailed() {
                    Log.i("TAG", "Interstitial failed")
                    intAd?.initAds(false) // retry loading
                }

                override fun onClosed() {
                    Log.i("TAG", "Interstitial closed")

                    // 🔁 Reload next ad
                    intAd?.initAds(false)
                }
            })

            // 👉 Load first time
            intAd?.initAds(false)
        }
    }

    public fun initializeRewardAd(context: Activity) {
        if (rewardAd == null) {
            rewardAd = RewardAds(context)
        }

    }

}