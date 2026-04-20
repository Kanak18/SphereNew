package com.wallpaper4k.ultrahd.live.backgrounds.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.wallpaper4k.ultrahd.live.backgrounds.utils.SessionManager
import java.util.Locale


open class BaseFragment : Fragment() {
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(requireActivity())
        setLocal()
    }

    private fun setLocal() {
        val sessionManager = SessionManager(requireActivity())
        val locale = Locale(sessionManager.getLanguage())
        Locale.setDefault(locale)
        val resources = requireActivity().resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

}