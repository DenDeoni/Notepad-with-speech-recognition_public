package com.notes.example.database

import android.app.Application
import com.notes.example.interfaces.MainInterface
import kotlin.properties.Delegates

class DBChange() {
/*var appDB by Delegates.observable(AppDatabase.getInstance(Application()).inTransaction()){
    prop, old, new ->
    main.renewListNotes()
}*/
    object DBChange {
        var dbWasChanged = false // ToDo
       /* var refreshListListeners = ArrayList<() -> Unit>()
        var appDatabase = AppDatabase.getInstance(Application()).inTransaction()

        // fires off every time value of the property changes
        main.renewListNotes() by Delegates.observable(appDatabase) { _, _, _ ->
            println("appDatabase 2: $appDatabase")
            // do your stuff here
            refreshListListeners.forEach {
                it()
                main.renewListNotes()
            }
        }*/
    }

}