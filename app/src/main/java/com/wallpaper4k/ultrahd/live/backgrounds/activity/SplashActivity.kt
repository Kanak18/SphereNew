package com.wallpaper4k.ultrahd.live.backgrounds.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.wallpaper4k.ultrahd.live.backgrounds.R
import com.wallpaper4k.ultrahd.live.backgrounds.databinding.ActivitySplashBinding
import com.wallpaper4k.ultrahd.live.backgrounds.utils.Const
import com.wallpaper4k.ultrahd.live.backgrounds.utils.MyPlayStoreBilling
import com.wallpaper4k.ultrahd.live.backgrounds.utils.RetrofitClient
import com.wallpaper4k.ultrahd.live.backgrounds.utils.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SplashActivity : BaseActivity() {

    lateinit var binding: ActivitySplashBinding
    lateinit var disposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        disposable = CompositeDisposable()
        sessionManager = SessionManager(this)
        callApiForSettingsData()
        fetchSubscriptionDetails()

    }

    private fun fetchSubscriptionDetails() {
        var myPlayStoreBilling =
            MyPlayStoreBilling(this, object : MyPlayStoreBilling.OnPurchaseComplete {
                override fun onConnected(isConnect: Boolean) {

                }

                override fun onPurchaseResult(isPurchaseSuccess: Boolean) {

                }

                override fun onError(hasError: Boolean) {
                }
            })

        myPlayStoreBilling.isSubscriptionRunning { isPurchased ->
            sessionManager.setPremium(isPurchased)
        }
    }

    private fun callApiForSettingsData() {

        val hashMap = HashMap<String, Any>()

        hashMap[Const.ApiParams.publisher_id] = Const.Key.PREF_NAME

        disposable.add(
            RetrofitClient.service.fetchSettings(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnTerminate {}
                .doOnError { throwable -> }
                .subscribe { getAllData, throwable ->
                    if (getAllData != null && getAllData.status == true) {
                        getAllData.admob?.let { it -> sessionManager.saveAdmob(it.first { it.type == 1 }) }
                        sessionManager.saveSubscriptions(getAllData.subscriptionPackages)
                        sessionManager.saveCategories(getAllData.categories)
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this@SplashActivity,
                            getString(R.string.something_went_wrong),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        )
    }

}