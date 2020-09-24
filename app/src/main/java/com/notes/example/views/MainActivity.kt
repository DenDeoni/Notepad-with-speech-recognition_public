package com.notes.example.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.notes.example.R
import com.notes.example.alerts.ToastAlert
import com.notes.example.constans.SHARED_PREFERENCES
import com.notes.example.constans.STORAGE_CODE
import com.notes.example.constans.S_KEY
import com.notes.example.database.AppDatabase
import com.notes.example.database.ContentPOJO
import com.notes.example.database.DBChange
import com.notes.example.database.NotesList
import com.notes.example.interfaces.MainInterface
import com.notes.example.interfaces.MenuInterface
import com.notes.example.presenters.MainPresenter
import com.notes.example.presenters.MenuPresenter
import com.notes.example.utils.ShowHideProgress
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.from_cloud_frame.*
import kotlinx.android.synthetic.main.sort_change_frame.*
import kotlinx.android.synthetic.main.sort_date_frame.*
import kotlinx.android.synthetic.main.sort_title_frame.*
import kotlinx.android.synthetic.main.to_cloud_frame.*
import kotlinx.android.synthetic.main.user_mail_frame.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


class MainActivity :
        AppCompatActivity(),
        MainInterface,
        MenuInterface {
    private val mainPresenter = MainPresenter(this)
    private val menuPresenter = MenuPresenter(this, this)

    override var context: Context = this
    override lateinit var supportFRGM: FragmentManager
    override lateinit var sharedPrefs: SharedPreferences
    override lateinit var editor: SharedPreferences.Editor
    override lateinit var res: Resources
    override lateinit var intentMain: Intent
    override lateinit var appDatabase: AppDatabase
    override lateinit var builder: AlertDialog.Builder
    override lateinit var notesListRView: RecyclerView
    override lateinit var infoFragment: Fragment
    override lateinit var authFragment: Fragment
    override var id: Int? = null
    override var dateTimeCreated: String = ""
    override var dateTimeEdited: String = ""
    override var title: String = ""
    override var text: String = ""
    override lateinit var progressBar: ProgressBar
    override lateinit var buttonCreate: Button
    override lateinit var buttonAuthor: Button
    override lateinit var buttonInfoCLose: Button
    override lateinit var floatingActionButton: FloatingActionButton
    override lateinit var floatingInfoButton: FloatingActionButton
    override lateinit var showHideProgress: ShowHideProgress
    override lateinit var view: View
    override lateinit var drawer: DrawerLayout
    override lateinit var navView: NavigationView
    override lateinit var menu: Menu
    override lateinit var textViewUserMail: TextView
    override lateinit var btnByDateOrder: Button
    override lateinit var btnByChangeOrder: Button
    override lateinit var btnByTitleOrder: Button
    override lateinit var btnToCloud: Button
    override lateinit var btnFromCloud: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(this)
        AppEventsLogger.activateApp(this)
        setContentView(R.layout.activity_main)
        initFields()
        NotesList(this).notesListConnect()
        createButtonClick()
        authorButtonClick()
        menuButtonClick()
        infoButtonClick()
        drawerToggle()

    }

    @SuppressLint("CommitPrefEdits")
    override fun initFields() {
        res = this.resources
        intentMain = intent
        sharedPrefs = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        editor = sharedPrefs.edit()
        supportFRGM = supportFragmentManager
        progressBar = progress_bar
        progressBar.visibility = View.VISIBLE
        notesListRView = notes_list_recycler_view
        builder = AlertDialog.Builder(this)
        buttonCreate = button_create_note
        buttonAuthor = author_button
        floatingActionButton = floating_action_button
        floatingInfoButton = floating_info_button
        infoFragment = InfoMainFragment()
        authFragment = AuthFragment(this)
        appDatabase = AppDatabase.getInstance(this)
        drawer = drawer_layout
        navView = findViewById(R.id.nav_view)
        menu = navView.menu
    }

    override fun attachAdapter(notesList: ArrayList<ContentPOJO>) {
        mainPresenter.attachAdapter(notesList)
    }

    override fun buttonsMenuInit() {
        btnByDateOrder = btn_by_date_order
        btnByChangeOrder = btn_by_change_order
        btnByTitleOrder = btn_by_title_order
        btnFromCloud = btn_from_cloud
        btnToCloud = btn_to_cloud
        textViewUserMail = textView_user_mail
        ActivityCompat.invalidateOptionsMenu(this)
    }

    override fun drawerToggle() {
        menuPresenter.drawerToggle()
    }

    override fun menuButtonClick() {
        menuPresenter.btnMenuClick()
    }

    override fun buttonsSortingClick() {

        menuPresenter.buttonsSortingClick()
    }

    @ExperimentalCoroutinesApi
    override fun onResume() {
        super.onResume()
        println("S_KEY key: " + sharedPrefs.getString(S_KEY, ""))
/*        DBChange.DBChange.appDB
        println("DBChange.DBChange.appD: ${DBChange.DBChange.appDB}")*/
        if (DBChange.DBChange.dbWasChanged) renewListNotes()
    }

    override fun infoButtonClick() {
        mainPresenter.showInfo()
    }

    override fun onStop() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        }
        super.onStop()
    }

    override fun onBackPressed() {
        val ifFragmentIsLoaded = supportFRGM.findFragmentById(R.id.container_fragment) != null
        when {
            drawer.isDrawerOpen(GravityCompat.START) -> {
                drawer.closeDrawer(GravityCompat.START)
            }
            ifFragmentIsLoaded -> {
                mainPresenter.detachInfoFragment()
            }
            else -> {
                finish()
            }
        }
    }

    override fun createButtonClick() {
        mainPresenter.startCreatingActivity()
    }

    override fun authorButtonClick() {
        mainPresenter.btnAuthorClick()
    }

    override fun startEditActivity(id: Int?, dateTime: String, title: String, text: String, dateTimeEdit: String) {
        mainPresenter.editActivityLoad()
    }

    override fun createPDF() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf((Manifest.permission.WRITE_EXTERNAL_STORAGE))
                requestPermissions(permissions, STORAGE_CODE)
            } else {
                mainPresenter.createPDF(dateTimeCreated, title, text, dateTimeEdited)
            }
        } else {
            mainPresenter.createPDF(dateTimeCreated, title, text, dateTimeEdited)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mainPresenter.createPDF(dateTimeCreated, title, text, dateTimeEdited)
                } else {
                    ToastAlert(applicationContext, getString(R.string.permission_denied))
                            .toastAlert()
                }
            }
        }
    }

    override fun shareNote(title: String, text: String) {
        mainPresenter.shareNote(title, text)
    }

    override fun showToast(title: String, message: String) {
        showToast(title)
    }

    override fun hideProgress(view: View) {
        mainPresenter.hideProgress(view)
    }

    override fun showConfirmationDialog() {
        mainPresenter.showConfirmationDialog()
    }

    override fun contentNoteIntent() { // invoke from MainPresenter
        mainPresenter.contentActivityLoad()
    }

    private lateinit var stringMessageSuccess: String
    private fun showToast(title: String) {
        stringMessageSuccess = "${context.getString(R.string.note)}\"$title\" \n${context.getString(R.string.note_was_del)}"
        ToastAlert(context, stringMessageSuccess)
                .toastAlert()
    }

    override fun renewListNotes() {
        NotesList(this).notesListConnect()
        DBChange.DBChange.dbWasChanged = false // Todo
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.onDestroy()
    }

}

