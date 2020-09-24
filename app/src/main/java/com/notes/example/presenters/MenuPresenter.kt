package com.notes.example.presenters

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.notes.example.R
import com.notes.example.alerts.AlertDialogFragment
import com.notes.example.alerts.ConfirmationListenerButtons
import com.notes.example.constans.*
import com.notes.example.interfaces.MainInterface
import com.notes.example.interfaces.MenuInterface
import com.notes.example.network.FireStoreNetwork
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MenuPresenter(private val mainInterface: MainInterface, private val menu: MenuInterface) :
        NavigationView.OnNavigationItemSelectedListener, ConfirmationListenerButtons {
    val context = mainInterface.context
    val res = mainInterface

    private fun initWhenMenuOpen() {
        openCloseMenu()
        mainInterface.buttonsMenuInit()
        btnSortDateSetBg()
        btnSortChangeSetBg()
        btnSortTitleSetBg()
        buttonsSortingClick()
        buttonsMenuSetChk()
        changeBtnSignOut()
    }

    fun btnMenuClick() {
        mainInterface.floatingActionButton.setOnClickListener {
            initWhenMenuOpen()
            refreshMenu()
        }
    }

    @SuppressLint("RestrictedApi")
    fun refreshMenu() {
        if (menu.drawer.isDrawerOpen(GravityCompat.START)) {
            initWhenMenuOpen()
            invalidateOptionsMenu(getActivity(mainInterface.context))
            buttonsMenuSetChk()
        }
    }

    private fun openCloseMenu() {
        navItemsVisible()
        if (menu.drawer.isDrawerOpen(GravityCompat.START)) {
            menu.drawer.closeDrawer(GravityCompat.START)
        } else {
            menu.drawer.openDrawer(GravityCompat.START)
            menu.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        mainInterface.editor.putString(S_KEY, "").commit()
        refreshMenu()
    }

    private fun isLogin(): Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        return user != null
    }

    private fun navItemsVisible() {

        menu.menu.findItem(R.id.back_up).isVisible = false
        menu.menu.findItem(R.id.btn_sign_in).isVisible = false
        menu.menu.findItem(R.id.btn_sign_out).isVisible = false

        if (isLogin()) {
            menu.menu.findItem(R.id.back_up).isVisible = true
            menu.menu.findItem(R.id.btn_sign_in).isVisible = false
            menu.menu.findItem(R.id.btn_sign_out).isVisible = true
            menu.menu.findItem(R.id.user_mail_item).isVisible = true
        } else {
            menu.menu.findItem(R.id.back_up).isVisible = false
            menu.menu.findItem(R.id.btn_sign_in).isVisible = true
            menu.menu.findItem(R.id.btn_sign_out).isVisible = false
            menu.menu.findItem(R.id.user_mail_item).isVisible = false
        }
    }

    private fun changeBtnSignOut() {
        val userMail = mainInterface.sharedPrefs.getString(USER_MAIL, "")
        menu.textViewUserMail.text = userMail
        menu.menu.findItem(R.id.user_mail_item).isEnabled = false
    }

    private fun showSignInFragment() {
        mainInterface.supportFRGM.beginTransaction()
                .add(R.id.container_fragment, mainInterface.authFragment)
                .commit()
        //Authorization(mainInterface.context as MainActivity).showSignInOptions() // TODO close ActivityForResult
    }

    private fun buttonsMenuChkClear() {
        menu.menu.findItem(R.id.sort_by_date).isChecked = false
        menu.menu.findItem(R.id.sort_by_changes).isChecked = false
        menu.menu.findItem(R.id.sort_by_title).isChecked = false
    }

    private fun buttonsMenuSetChk() {
        buttonsMenuChkClear()
        when (mainInterface.sharedPrefs.getString(SORT_BY, BY_DATE)) {
            BY_DATE -> menu.menu.findItem(R.id.sort_by_date).isChecked = true
            BY_CHANGE -> menu.menu.findItem(R.id.sort_by_changes).isChecked = true
            BY_TITLE -> menu.menu.findItem(R.id.sort_by_title).isChecked = true
        }
    }

    private fun btnSortDateSetBg() {
        val sort = mainInterface.sharedPrefs.getBoolean(SORT_BY_DATE_ASC, true)
        menu.btnByDateOrder.setBackgroundResource(R.drawable.ic_desc)
        if (sort) menu.btnByDateOrder.setBackgroundResource(R.drawable.ic_asc)
    }

    private fun btnSortChangeSetBg() {
        val sort = mainInterface.sharedPrefs.getBoolean(SORT_BY_CHANGE_ASC, true)
        menu.btnByChangeOrder.setBackgroundResource(R.drawable.ic_desc)
        if (sort) menu.btnByChangeOrder.setBackgroundResource(R.drawable.ic_asc)
    }

    private fun btnSortTitleSetBg() {
        val sort = mainInterface.sharedPrefs.getBoolean(SORT_BY_TITLE_ASC, true)
        menu.btnByTitleOrder.setBackgroundResource(R.drawable.ic_z_a)
        if (sort) menu.btnByTitleOrder.setBackgroundResource(R.drawable.ic_a_z)
    }

    fun drawerToggle() {
        val toggle = ActionBarDrawerToggle(
                mainInterface.context as Activity?, menu.drawer, null, 0, 0)
        menu.drawer.addDrawerListener(toggle)
        toggle.syncState()
        menu.navView.setNavigationItemSelectedListener(this)
        menu.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    fun buttonsSortingClick() {

        menu.btnByDateOrder.setOnClickListener {
            val order = mainInterface.sharedPrefs.getBoolean(SORT_BY_DATE_ASC, true)
            mainInterface.editor.putBoolean(SORT_BY_DATE_ASC, true).commit()
            if (order) mainInterface.editor.putBoolean(SORT_BY_DATE_ASC, false).commit()
            btnSortDateSetBg()
        }
        menu.btnByChangeOrder.setOnClickListener {
            val order = mainInterface.sharedPrefs.getBoolean(SORT_BY_CHANGE_ASC, true)
            mainInterface.editor.putBoolean(SORT_BY_CHANGE_ASC, true).commit()
            if (order) mainInterface.editor.putBoolean(SORT_BY_CHANGE_ASC, false).commit()
            btnSortChangeSetBg()
        }
        menu.btnByTitleOrder.setOnClickListener {
            val order = mainInterface.sharedPrefs.getBoolean(SORT_BY_TITLE_ASC, true)
            mainInterface.editor.putBoolean(SORT_BY_TITLE_ASC, true).commit()
            if (order) mainInterface.editor.putBoolean(SORT_BY_TITLE_ASC, false).commit()
            btnSortTitleSetBg()
        }

    }

    @ExperimentalStdlibApi
    private fun writeToCloud() {
        FireStoreNetwork(mainInterface).choiceAction(WRITE_CLOUD)
    }

    @ExperimentalStdlibApi
    private fun readFromCloud() {
        FireStoreNetwork(mainInterface).choiceAction(READ_CLOUD)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.sort_by_date -> {
                mainInterface.editor.putString(SORT_BY, BY_DATE).commit()
                val order = mainInterface.sharedPrefs.getBoolean(SORT_BY_DATE_ASC, true)
                mainInterface.editor.putBoolean(ASC, order).commit()
            }
            R.id.sort_by_changes -> {
                mainInterface.editor.putString(SORT_BY, BY_CHANGE).commit()
                val order = mainInterface.sharedPrefs.getBoolean(SORT_BY_CHANGE_ASC, true)
                mainInterface.editor.putBoolean(ASC, order).commit()
            }
            R.id.sort_by_title -> {
                mainInterface.editor.putString(SORT_BY, BY_TITLE).commit()
                val order = mainInterface.sharedPrefs.getBoolean(SORT_BY_TITLE_ASC, true)
                mainInterface.editor.putBoolean(ASC, order).commit()
            }
            R.id.to_cloud_item -> {
                showConfirmationDialog(WRITE_CLOUD)
            }
            R.id.from_cloud_item -> {
                showConfirmationDialog(READ_CLOUD)
            }
            R.id.btn_sign_in -> {
                showSignInFragment()
            }
            R.id.btn_sign_out -> {
                signOut()
            }
        }
        mainInterface.renewListNotes()
        openCloseMenu()
        menu.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        return true
    }

    var message: String = ""
    private fun showConfirmationDialog(flag: String) {

        if (flag == READ_CLOUD) message = mainInterface.context.getString(R.string.want_replace_db)
        if (flag == WRITE_CLOUD) message = mainInterface.context.getString(R.string.want_replace_cloud)

        AlertDialogFragment(mainInterface.context, message, this)
                .onCreateDialog(Bundle()).show()
        AlertDialogFragment(mainInterface.context, message, this)
                .setBtnColor()
    }

    @ExperimentalStdlibApi
    @ExperimentalCoroutinesApi
    override fun positiveButtonClicked() {
        if (message == mainInterface.context.getString(R.string.want_replace_db)) readFromCloud()
        if (message == mainInterface.context.getString(R.string.want_replace_cloud)) writeToCloud()
    }

    override fun negativeButtonClicked() {

    }

}