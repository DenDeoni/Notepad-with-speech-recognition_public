package com.notes.example.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.notes.example.R
import com.notes.example.R.layout
import com.notes.example.R.style
import com.notes.example.alerts.ToastAlert
import com.notes.example.constans.*
import com.notes.example.interfaces.MainInterface
import com.notes.example.network.SecondDB
import kotlinx.android.synthetic.main.fragment_main_info.*

class AuthFragment(private val main: MainInterface) : Fragment() {
    private lateinit var providers: List<AuthUI.IdpConfig>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(layout.fragment_auth, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        main.initFields()
        button_fragment_close.setOnClickListener {
            close()
        }
        providers = listOf(
                AuthUI.IdpConfig.EmailBuilder().build()/*,
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.FacebookBuilder().build()*/
        )
        showSignInOptions()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTH_REQUESTS_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val userID = FirebaseAuth.getInstance().currentUser!!.uid
                val userMail = FirebaseAuth.getInstance().currentUser?.email
                val userName = FirebaseAuth.getInstance().currentUser?.displayName
                main.editor.putBoolean(IS_LOGIN, true).commit()
                main.editor.putString(USER_ID, userID).commit()
                main.editor.putString(USER_MAIL, userMail).commit()
                main.editor.putString(USER_NAME, userName).commit()
                if (main.sharedPrefs.getString(S_KEY, "") == null) {
                    println("S_KEY == null")
                    main.editor.putString(S_KEY, "").commit()
                }
                SecondDB(main).readDB()
                close()
            } else {
                var message = getString(R.string.auth_cancel)
                response?.error?.message?.let { message = it }
                activity?.let { ToastAlert(it, message).toastAlert() }
                if (response?.error?.message == null) {
                    close()
                }
            }
        }
    }

    private fun close() {
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }

    private fun showSignInOptions() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(style.AuthPageTheme)
                .setLogo(R.drawable.logo_app)
                .build(), AUTH_REQUESTS_CODE)
    }

}