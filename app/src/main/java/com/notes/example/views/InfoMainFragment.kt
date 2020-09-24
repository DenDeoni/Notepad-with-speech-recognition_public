package com.notes.example.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.notes.example.R
import kotlinx.android.synthetic.main.fragment_main_info.*

class InfoMainFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        button_fragment_close.setOnClickListener{
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        textView_privacy_policy.setOnClickListener {
            val urlString = getString(R.string.privacy_policy_url)
            val openUrl = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
            if (openUrl.resolveActivity(activity!!.packageManager) != null) {
                startActivity(Intent.createChooser(openUrl, getString(R.string.open_by)))
            }
        }
    }
}