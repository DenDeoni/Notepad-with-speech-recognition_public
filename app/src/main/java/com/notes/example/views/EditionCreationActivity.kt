package com.notes.example.views

import android.Manifest.permission.RECORD_AUDIO
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.SpeechRecognizer.*
import android.text.Editable
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.textfield.TextInputEditText
import com.notes.example.R
import com.notes.example.alerts.ToastAlert
import com.notes.example.constans.*
import com.notes.example.interfaces.EditCreateInterface
import com.notes.example.presenters.EditCreatePresenter
import com.notes.example.utils.DateTime
import kotlinx.android.synthetic.main.activity_edit_create.*
import java.util.*


class EditionCreationActivity : AppCompatActivity(), EditCreateInterface, RecognitionListener, View.OnClickListener {

    private var editCreatePresenter: EditCreatePresenter? = null

    override var context: Context = this
    override lateinit var supportFRGM: FragmentManager
    override var intents = Intent()
    override var id: Int? = null
    override var dateTimeCreate = ""
    override var dateTimeEdit = ""
    override var title: String = ""
    lateinit var speech: SpeechRecognizer
    private var recognizerIntent: Intent? = null
    override var text = ""
    override lateinit var loadedTitle: String
    override lateinit var loadedText: String
    override lateinit var buttonClose: ImageView
    override lateinit var buttonSave: Button
    override lateinit var buttonSaveCopy: Button
    override lateinit var buttonEraseTitle: Button
    override lateinit var buttonEraseText: Button
    override lateinit var buttonCreatePDF: Button
    override lateinit var buttonShare: Button
    override lateinit var progressBar: ProgressBar
    override lateinit var recProgressTitle: ProgressBar
    override lateinit var recProgressText: ProgressBar
    override lateinit var micTitle: ToggleButton
    override lateinit var micText: ToggleButton
    override lateinit var inputTitle: TextInputEditText
    override lateinit var inputText: TextInputEditText
    override lateinit var spinnerLangSelect: Spinner
    override lateinit var micButton: ToggleButton
    override lateinit var infoButton: Button
    override lateinit var infoFragment: Fragment
    lateinit var sharedPrefs: SharedPreferences
    lateinit var langLocal: String
    lateinit var langSelected: String
    private var isReadyForSpeech: Boolean = false
    private var itemNum: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_create)
        initFields()
        langSelectAdapter()
        whenActivityLoaded()
        buttonsAssign()
        clickBtnShareNote()
        buttonCreatePDFonClick()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                if (!isReadyForSpeech) {
                    micButton = mic_button_title
                    micButton.isEnabled = false
                    requestPermission()
                    hideSoftKeyBoard()
                }
                return true
            }
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                if (!isReadyForSpeech) {
                    micButton = mic_button_text
                    micButton.isEnabled = false
                    requestPermission()
                    hideSoftKeyBoard()
                }
                return true
            }
            KeyEvent.KEYCODE_BACK -> {
                editCreatePresenter?.detachInfoFragment()
                return true
            }
        }
        return onKeyUp(keyCode, event)
    }

    private fun hideSoftKeyBoard() {
        val view: View = (if (currentFocus == null) View(this) else currentFocus) as View
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            Toast.makeText(this, "Volume Up Pressed", Toast.LENGTH_SHORT).show()
            return true
        }
        return onKeyLongPress(keyCode, event)
    }

    private fun initFields() {
        editCreatePresenter = EditCreatePresenter(this)
        supportFRGM = supportFragmentManager
        buttonClose = button_close
        progressBar = progress_bar
        recProgressTitle = rec_progress_bar_title
        recProgressText = rec_progress_bar_text
        micTitle = mic_button_title
        micText = mic_button_text
        buttonSave = button_save
        buttonSaveCopy = button_save_copy
        buttonEraseTitle = button_erase_title
        buttonEraseText = button_erase_text
        inputTitle = input_title
        inputText = input_text
        recProgressTitle.visibility = View.INVISIBLE
        recProgressText.visibility = View.INVISIBLE
        progressBar.visibility = View.INVISIBLE
        buttonClose.setOnClickListener { close() }
        micButton = mic_button_text
        infoButton = button_info_edit_create
        buttonCreatePDF = button_create_pdf
        buttonShare = button_share_note
        spinnerLangSelect = spinner_lang_select
        buttonEraseTitle.setOnClickListener { eraseTextField(inputTitle) }
        buttonEraseText.setOnClickListener { eraseTextField(inputText) }
        micTitle.setOnClickListener { onClick(mic_button_title) }
        micText.setOnClickListener { onClick(mic_button_text) }
        infoButton.setOnClickListener { onClick(button_info_edit_create) }
        progressBar.visibility = View.INVISIBLE
        sharedPrefs = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        langLocal = Locale.getDefault().toLanguageTag()
        langSelected = sharedPrefs.getString("langSelected", "english").toString()
        itemNum = sharedPrefs.getInt("itemNum", langLocalToItemSpinner(langLocal))
        speech = createSpeechRecognizer(this)

        infoFragment = InfoEditCreateFragment()
    }

    private fun langLocalToItemSpinner(langLocal: String): Int {
        langSelected = sharedPrefs.getString("langSelected", langLocal).toString()
        return when (langSelected) {
            "en-US" -> 0
            "ru-RU" -> 1
            "cs-CZ" -> 2
            "de-DE" -> 3
            "fr-FR" -> 4
            "it-IT" -> 5
            "es-ES" -> 6
            "pl-PL" -> 7
            "sk-SK" -> 8
            "zh-CN" -> 9
            else -> 0
        }
    }

    override fun buttonsAssign() {
        editCreatePresenter?.buttonsAssign()
    }

    override fun intentResponse() {
        val intent = Intent()
        setResult(SAVE_CHANGES, intent)
    }

    override fun close() {
        finish()
    }

    override fun shareNote() {
        editCreatePresenter?.shareNote(title, text)
    }

    private fun clickBtnShareNote() { // TODO
        editCreatePresenter?.clickBtnShareNote()
    }

    private fun buttonCreatePDFonClick() {
        buttonCreatePDF.setOnClickListener {
            val dateTime = DateTime().getDateTimeString()
            val title = inputTitle.text.toString()
            val text = inputText.text.toString()
            createPDF(dateTime, title, text)
        }
    }

    private fun langSelectAdapter() {
        val adapter = ArrayAdapter.createFromResource(this, R.array.lang_list, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLangSelect.adapter = adapter
        spinnerLangSelect.setSelection(itemNum)
        spinnerLangSelect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                itemNum = spinnerLangSelect.selectedItemPosition
                langSelected = spinnerLangSelect.selectedItem.toString()
                sharedPrefs.edit().putString("langSelected", langSelected).apply()
                sharedPrefs.edit().putInt("itemNum", itemNum).apply()
                recognizeIntents()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun whenActivityLoaded() {
        if (intent.hasExtra(NEW_NOTE)) {
            buttonSaveCopy.visibility = View.GONE
        }
        if (intent.hasExtra(ID)) {
            id = intent.getIntExtra(ID, 0)
        }
        intent.getStringExtra(DATE_TIME)?.let { dateTimeCreate = it }
        intent.getStringExtra(TITLE)?.let { title = it }
        intent.getStringExtra(TEXT)?.let { text = it }
        intent.getStringExtra(DATE_TIME_EDIT)?.let { dateTimeEdit = it }
        input_title.text = Editable.Factory.getInstance().newEditable(title)
        input_text.text = Editable.Factory.getInstance().newEditable(text)
        loadedTitle = title
        loadedText = text
    }

    override fun onClick(v: View) {
        micButtonAssign(v)
        micButton.isEnabled = false
        requestPermission()
    }

    private fun micButtonAssign(v: View) {
        micButton = if (v.id == R.id.mic_button_title) {
            mic_button_title
        } else {
            mic_button_text
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this@EditionCreationActivity, arrayOf(RECORD_AUDIO, WRITE_EXTERNAL_STORAGE), REQUEST_RECORD_PERMISSION)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_RECORD_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                speechRecognizerStart()
            } else {
                viewsStateReady()
                ToastAlert(context, getString(R.string.permission_denied))
                        .toastAlert()
            }
        }
        when (requestCode) {
            STORAGE_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    title = inputTitle.editableText.toString()
                    text = inputText.editableText.toString()
                    editCreatePresenter?.createPDF(dateTimeCreate, title, text)
                } else {
                    ToastAlert(context, getString(R.string.permission_denied))
                            .toastAlert()
                }
            }
        }
    }

    private fun speechRecognizerStart() {
        speech.setRecognitionListener(this)
        speech.startListening(recognizerIntent)
        recognizeIntents()
        whenRecSetUI()
    }

    private fun recognizeIntents() {
        langSelected = sharedPrefs.getString("langSelected", "en-US").toString()
        val lang: String = when (langSelected) {
            "english" -> "en-US"
            "русский" -> "ru-RU"
            "čeština" -> "cs-CZ"
            "deutsch" -> "de-DE"
            "français" -> "fr-FR"
            "italiano" -> "it-IT"
            "español" -> "es-ES"
            "polskie" -> "pl-PL"
            "slovenský" -> "sk-SK"
            "中文" -> "zh-CN"
            else -> "en-US"
        }
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent?.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-US")
        recognizerIntent?.putExtra(RecognizerIntent.EXTRA_LANGUAGE, lang)
        //recognizerIntent?.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE,true)
        recognizerIntent?.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        recognizerIntent?.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        recognizerIntent?.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 3000)
//        recognizerIntent?.putExtra("android.speech.extra.DICTATION_MODE", true)
//        recognizerIntent?.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
    }

    private fun whenRecSetUI() {
        if (micButton == findViewById(R.id.mic_button_title)) {
            whenTitleRecSetUI()
        } else {
            whenTextRecSetUI()
        }
    }

    private fun whenTitleRecSetUI() {
        recProgressTitle.visibility = View.VISIBLE
        recProgressTitle.isIndeterminate = true
        micTitle.background = getDrawable(R.drawable.mic_orange)
        recProgressText.isIndeterminate = false
        recProgressText.visibility = View.INVISIBLE
        micText.isEnabled = false
    }

    private fun whenTextRecSetUI() {
        recProgressText.visibility = View.VISIBLE
        recProgressText.isIndeterminate = true
        micText.background = getDrawable(R.drawable.mic_orange)
        recProgressTitle.isIndeterminate = false
        recProgressTitle.visibility = View.INVISIBLE
        micTitle.isEnabled = false
    }


    private fun eraseTextField(field: TextInputEditText?) {
        field?.setText("")
    }

    override fun onReadyForSpeech(arg0: Bundle) {
        isReadyForSpeech = true
    }

    override fun onBeginningOfSpeech() {
        isReadyForSpeech = true
        if (micButton == mic_button_title) {
            recProgressTitle.isIndeterminate = false
            recProgressTitle.max = 10
        } else {
            recProgressText.isIndeterminate = false
            recProgressText.max = 10
        }
    }

    override fun onRmsChanged(rmsDB: Float) {
        if (micButton == mic_button_title) {
            recProgressTitle.progress = rmsDB.toInt()
        } else {
            recProgressText.progress = rmsDB.toInt()
        }
    }

    override fun onResults(results: Bundle) {
        val resultsRec = results.getStringArrayList(RESULTS_RECOGNITION)
        var text = ""
        for (result in resultsRec!!) text = result

        if (micButton == mic_button_title) {
            if (inputTitle.text!!.isEmpty()) inputTitle.text = inputTitle.editableText.append(text)
            else inputTitle.text = inputTitle.editableText.append(" $text")
        } else {
            if (inputText.text!!.isEmpty()) inputText.text = inputText.editableText.append(text)
            else inputText.text = inputText.editableText.append(" $text")
        }
        viewsStateReady()
        speech.destroy()
    }

    override fun onEndOfSpeech() {

    }

    private fun viewsStateReady() {
        micButton.isEnabled = true
        isReadyForSpeech = false
        recProgressTitle.isIndeterminate = true
        micTitle.isEnabled = true
        micText.isEnabled = true
        micTitle.background = getDrawable(R.drawable.mic_black)
        micText.background = getDrawable(R.drawable.mic_black)
        recProgressTitle.isIndeterminate = false
        recProgressText.isIndeterminate = false
        recProgressTitle.visibility = View.INVISIBLE
        recProgressText.visibility = View.INVISIBLE
        // recProgressText.isIndeterminate = true
    }

    override fun onError(errorCode: Int) {
        val errorMessage = getErrorText(errorCode)
        if (/*errorMessage == getString(R.string.no_match) &&*/ !isReadyForSpeech) {
            speech.destroy()
            speechRecognizerStart()
        } else {
            ToastAlert(applicationContext, errorMessage).toastAlert()
            speech.destroy()
            viewsStateReady()
        }
    }

    override fun onBufferReceived(buffer: ByteArray) {}
    override fun onPointerCaptureChanged(hasCapture: Boolean) {}
    override fun onEvent(arg0: Int, arg1: Bundle) {}
    override fun onPartialResults(arg0: Bundle) {}

    private fun getErrorText(errorCode: Int): String {
        return when (errorCode) {
            ERROR_AUDIO -> getString(R.string.audio_recording_error)
            ERROR_CLIENT -> getString(R.string.client_side_error)
            ERROR_INSUFFICIENT_PERMISSIONS -> getString(R.string.no_permissions)
            ERROR_NETWORK -> getString(R.string.network_error)
            ERROR_NETWORK_TIMEOUT -> getString(R.string.network_timeout)
            ERROR_NO_MATCH -> getString(R.string.no_match)
            ERROR_RECOGNIZER_BUSY -> getString(R.string.recognition_service_busy)
            ERROR_SERVER -> getString(R.string.error_from_server)
            ERROR_SPEECH_TIMEOUT -> getString(R.string.no_speech_input)
            else -> getString(R.string.didnt_understand)
        }
    }

    override fun onStop() {
        super.onStop()
        if (isReadyForSpeech) {
            speech.destroy()
        }
    }

    override fun createPDF(dateTime: String, title: String, text: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf((WRITE_EXTERNAL_STORAGE))
                requestPermissions(permissions, STORAGE_CODE)
            } else {
                editCreatePresenter?.createPDF(dateTime, title, text)
            }
        } else {
            editCreatePresenter?.createPDF(dateTime, title, text)
        }
    }

/*    override fun onBackPressed() {
        super.onBackPressed()
             editCreatePresenter?.detachInfoFragment()
    }*/
}