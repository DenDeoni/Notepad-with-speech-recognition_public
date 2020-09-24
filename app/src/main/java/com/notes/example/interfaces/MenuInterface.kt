package com.notes.example.interfaces

import android.view.Menu
import android.widget.Button
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

interface MenuInterface {
    val drawer: DrawerLayout
    val navView: NavigationView
    val menu: Menu
    var btnByDateOrder: Button
    var btnByChangeOrder: Button
    var btnByTitleOrder: Button
    var btnToCloud: Button
    var btnFromCloud: Button
    var textViewUserMail: TextView

    fun drawerToggle()
    fun buttonsSortingClick()
}