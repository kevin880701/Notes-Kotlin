package com.lhr.notes_kotlin.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.lhr.notes_kotlin.AppConfig.BACKUP_PATH
import com.lhr.notes_kotlin.AppConfig.CSV_NAME
import com.lhr.notes_kotlin.AppConfig.FILES_PATH
import com.lhr.notes_kotlin.AppConfig.ZIP_NAME
import com.lhr.notes_kotlin.R
import com.lhr.notes_kotlin.databinding.ActivityMainBinding
import com.lhr.notes_kotlin.googleDriver.DriveServiceHelper
import com.lhr.notes_kotlin.sqlLite.NotesEntity
import com.lhr.notes_kotlin.sqlLite.SqlDatabase
import com.lhr.notes_kotlin.util.adapter.TextAdapter
import com.lhr.notes_kotlin.util.deleteAllFilesInDirectory
import com.lhr.notes_kotlin.util.manager.ZipManager.unzipFile
import com.lhr.notes_kotlin.util.manager.csvManager.sqlToCsv
import com.lhr.notes_kotlin.util.manager.ZipManager.zipFolder
import com.lhr.notes_kotlin.util.manager.csvManager.csvToSql
import com.lhr.notes_kotlin.view.base.BaseActivity
import com.lhr.notes_kotlin.viewmodel.implementations.NoteViewModelImpl
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class MainActivity : BaseActivity(), View.OnClickListener, TextAdapter.Listener {

    private var mDriveServiceHelper: DriveServiceHelper? = null
    private val viewModel: NoteViewModelImpl by lazy { ViewModelProvider(this)[NoteViewModelImpl::class.java] }
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    lateinit var textAdapter: TextAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 先確認登入GOOGLE帳戶
        requestSignIn()
        initView()
        bindViewModel()
    }

    private fun initView() {
        initRecyclerView()
        binding.widgetTitleBar.imageMenu.visibility = View.VISIBLE

        // 設置側滑選單點擊事件
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.make_backup -> {
                    sqlToCsv("$FILES_PATH/$CSV_NAME")
                    zipFolder(FILES_PATH, "$BACKUP_PATH/$ZIP_NAME")

                    // 先確認登入GOOGLE帳戶
                    requestSignIn()
                    // 上傳備份檔案
                    GlobalScope.launch {
                        uploadBackup()
                    }
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                }

                R.id.update_data -> {
                    // 先確認登入GOOGLE帳戶
                    requestSignIn()
                    // 選擇雲端硬碟檔案
                    val pickerIntent = mDriveServiceHelper!!.createFilePickerIntent()
                    chooseFile.launch(pickerIntent)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                }

                else -> false
            }
        }

        binding.widgetTitleBar.imageMenu.setOnClickListener(this)
        binding.imageAdd.setOnClickListener(this)
    }

    private fun initRecyclerView() {
        textAdapter = TextAdapter(this, this)
        binding.recyclerViewTextTitle.adapter = textAdapter
        binding.recyclerViewTextTitle.layoutManager = LinearLayoutManager(this)
    }


    private fun bindViewModel() {
        viewModel.getNotesLiveData.observe(this) { newNotesList ->
            textAdapter.submitList(newNotesList)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imageAdd -> {
                val intent = Intent(this, EditActivity::class.java)
                intent.putExtra("isAddPage", true)
                startActivity(intent)
            }

            R.id.imageMenu -> {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }

    override fun onItemClick(item: NotesEntity) {
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra("NotesEntity", item)
        intent.putExtra("isAddPage", false)
        startActivity(intent)
    }

    override fun onItemLongClick(item: NotesEntity) {
    }

    /**
     * 開啟頁面時取得資料庫資料
     */
    override fun onResume() {
        viewModel.loadAllNotes()
        super.onResume()
    }

    private suspend fun uploadBackup() {
        if (mDriveServiceHelper != null) {
            Log.d("uploadBackup", "Creating a file.")
            mDriveServiceHelper!!.createFile()

        }
    }

    val signInResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                handleSignInResult(data)
            }
        }
    }

    val chooseFile = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                // 先刪除舊有資料
                deleteAllFilesInDirectory(FILES_PATH)
                // 清除資料表
                SqlDatabase.getInstance().getNotesDao().clearTable()
                // 將選取的檔案存至BACKUP_PATH中
                val inputStream: InputStream? = contentResolver.openInputStream(data.data!!)
                if (inputStream != null) {
                    saveFileToDirectory(inputStream, BACKUP_PATH, getFileNameFromUri(data.data!!)!!)
                    inputStream.close()
                }
                // 將選取的檔案解壓縮到FILES_PATH中
                unzipFile(BACKUP_PATH +  File.separator + getFileNameFromUri(data.data!!), FILES_PATH)
                // 將CSV資料匯入資料庫
                csvToSql(FILES_PATH +  File.separator + CSV_NAME)
            }
        }
    }

    /**
     * Starts a sign-in activity using [.REQUEST_CODE_SIGN_IN].
     */
    private fun requestSignIn() {
        Log.d("requestSignIn", "Requesting sign-in")
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope(DriveScopes.DRIVE_FILE))
            .build()
        val client = GoogleSignIn.getClient(this, signInOptions)

        signInResult.launch(client.signInIntent)
    }

    /**
     * Handles the `result` of a completed sign-in activity initiated from [ ][.requestSignIn].
     */
    private fun handleSignInResult(result: Intent) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
            .addOnSuccessListener { googleAccount: GoogleSignInAccount ->
                Log.d("handleSignInResult", "Signed in as " + googleAccount.email)

                // Use the authenticated account to sign in to the Drive service.
                val credential = GoogleAccountCredential.usingOAuth2(
                    this, setOf(DriveScopes.DRIVE_FILE)
                )
                credential.selectedAccount = googleAccount.account
                val googleDriveService =
                    Drive.Builder(
                        AndroidHttp.newCompatibleTransport(),
                        GsonFactory(),
                        credential
                    )
                        .setApplicationName("Drive API Migration")
                        .build()

                // The DriveServiceHelper encapsulates all REST API and SAF functionality.
                // Its instantiation is required before handling any onClick actions.
                mDriveServiceHelper = DriveServiceHelper(googleDriveService)
            }
            .addOnFailureListener { exception: Exception? ->
                Log.e(
                    "addOnFailureListener",
                    "Unable to sign in.",
                    exception
                )
            }
    }


    private fun saveFileToDirectory(inputStream: InputStream, directoryPath: String, fileName: String) {
        val directory = File(directoryPath)
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, fileName)
        file.createNewFile()

        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        outputStream.close()
    }


    private fun getFileNameFromUri(uri: Uri): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                return displayName
            }
        }
        return null
    }
}