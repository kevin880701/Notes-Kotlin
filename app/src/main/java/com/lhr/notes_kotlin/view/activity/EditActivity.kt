package com.lhr.notes_kotlin.view.activity

import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.lhr.notes_kotlin.AppConfig
import com.lhr.notes_kotlin.R
import com.lhr.notes_kotlin.databinding.ActivityEditBinding
import com.lhr.notes_kotlin.sqlLite.NotesEntity
import com.lhr.notes_kotlin.view.base.BaseActivity
import com.lhr.notes_kotlin.util.dialog.DeleteFolderDialog
import com.lhr.notes_kotlin.util.dialog.ImageChangeDialog
import com.lhr.notes_kotlin.util.loadImageWithGlide
import com.lhr.notes_kotlin.util.generateRandomNumber
import com.lhr.notes_kotlin.util.scrollView.ScrollBindHelper
import com.lhr.notes_kotlin.viewmodel.implementations.EditViewModelImpl
import java.io.File


class EditActivity : BaseActivity(), View.OnClickListener, ImageChangeDialog.Listener,
    DeleteFolderDialog.Listener {
    companion object {
        const val IMAGE_REQUEST_CODE = 300
    }

    private val viewModel: EditViewModelImpl by lazy { ViewModelProvider(this)[EditViewModelImpl::class.java] }
    private var _binding: ActivityEditBinding? = null
    val binding get() = _binding!!

    // EditText 的最大高度
    var maxHeight = 0

    // 判斷是新增還是讀取舊資料
    private var isAddPage = false

    // 判斷有無圖片
    private var isAddImage = false
    private lateinit var notesEntity: NotesEntity
    private lateinit var scrollBindHelper: ScrollBindHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 檢查版本判斷接收資料方式
        notesEntity = if (intent.hasExtra("NotesEntity")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra("NotesEntity", NotesEntity::class.java) as NotesEntity
            } else {
                intent.getSerializableExtra("NotesEntity") as NotesEntity
            }
        } else {
            NotesEntity()
        }
        // 判斷是新增頁面還是修改頁面
        if (intent.hasExtra("isAddPage")) {
            isAddPage = intent.getBooleanExtra("isAddPage", false)
        }
        // 如果紀錄isImage為1代表原本有圖片
        isAddImage = (notesEntity.image == 1)

        initView()
    }

    private fun initView() {
        binding.widgetTitleBar.imageBack.visibility = View.VISIBLE
        if (isAddPage) {  //如果是新增頁面
            binding.widgetTitleBar.textTitle.text = resources.getString(R.string.add_content)
            binding.widgetTitleBar.imageSave.visibility = View.VISIBLE
        } else {  //如果是修改頁面
            binding.widgetTitleBar.textTitle.text = resources.getString(R.string.edit_content)
            binding.widgetTitleBar.imageSave.visibility = View.VISIBLE
            binding.widgetTitleBar.imageDelete.visibility = View.VISIBLE
            binding.editTitle.text = Editable.Factory.getInstance().newEditable(notesEntity?.title)
            binding.editContent.text =
                Editable.Factory.getInstance().newEditable(notesEntity?.content)
        }

        // 如果預設有圖片則加入至ImageView中
        if (isAddImage) {
            val folderPath = File(AppConfig.FILES_PATH, notesEntity.number)
            val imageFile = File(folderPath, "image.png")
            if (imageFile.exists()) {
                binding.imageViewPicture.loadImageWithGlide(
                    Uri.fromFile(imageFile),
                    binding.imageViewPicture
                )
            }
        }
        // 綁定SeekBar和scrollView
        scrollBindHelper = ScrollBindHelper(binding.seekBar, binding.scrollView)
        scrollBindHelper.bind(binding.seekBar, binding.scrollView)
        // 必須在EditText建構完成後才取的到高度
        binding.editContent.viewTreeObserver.addOnGlobalLayoutListener {
            // 獲取 EditText 的最大高度
            maxHeight = binding.editContent.height
            // 獲取 EditText 行數的最大高度
            val layout = binding.editContent.layout
            // 如果EditText超過了最大行數，顯示SeekBar
            binding.seekBar.visibility =
                if (layout.height > maxHeight) View.VISIBLE else View.INVISIBLE
        }
        binding.editContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val layout = binding.editContent.layout
                // 如果EditText超過了最大行數，顯示SeekBar
                binding.seekBar.visibility =
                    if (layout.height > maxHeight) View.VISIBLE else View.INVISIBLE
            }
        })
        binding.widgetTitleBar.imageSave.setOnClickListener(this)
        binding.widgetTitleBar.imageDelete.setOnClickListener(this)
        binding.widgetTitleBar.imageBack.setOnClickListener(this)
        binding.imageViewPicture.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imageSave -> {
                if (isAddPage) {
                    notesEntity = NotesEntity()
                    notesEntity.title = binding.editTitle.text.toString()
                    notesEntity.content = binding.editContent.text.toString()
                    notesEntity.number = generateRandomNumber()
                    notesEntity.image = if (isAddImage) 1 else 0
                    notesEntity.recyclerPosition = 1
                    viewModel.save(notesEntity)
                    viewModel.saveLocalNote(notesEntity, this, binding.imageViewPicture)
                } else {
                    notesEntity.title = binding.editTitle.text.toString()
                    notesEntity.content = binding.editContent.text.toString()
                    notesEntity.image = if (isAddImage) 1 else 0
                    viewModel.updateRecord(notesEntity)
                    viewModel.saveLocalNote(notesEntity, this, binding.imageViewPicture)
                }
                finish()
            }

            R.id.imageDelete -> {
                val deleteFolderDialog = DeleteFolderDialog(this)
                deleteFolderDialog.show(supportFragmentManager, "DeleteFolderDialog")
            }

            R.id.imageBack -> {
                finish()
            }

            R.id.imageViewPicture -> {
                val imageChangeDialog = ImageChangeDialog(this)
                imageChangeDialog.show(supportFragmentManager, "ImageChangeDialog")
            }
        }
    }

    /**
     * 刪除圖片
     */
    override fun onDeleteImage() {
        isAddImage = false
        // 恢復預設圖片
        binding.imageViewPicture.setImageDrawable(getDrawable(R.drawable.picture))
        // 恢復Tint
        binding.imageViewPicture.setColorFilter(
            ContextCompat.getColor(this, R.color.seed),
            PorterDuff.Mode.SRC_IN
        )
    }

    /**
     * 從相簿選取圖片
     */
    override fun onChangeImage() {
        val imageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            changeImageResult.launch(imageIntent)
        } else {
            ActivityCompat.startActivityForResult(
                this,
                imageIntent,
                IMAGE_REQUEST_CODE,
                null
            )
        }
    }

    /**
     * 選取完圖片後執行
     */
    private val changeImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val myData: Intent? = result.data
                if (myData != null) {
                    setImage(myData.data)
                }
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            // 選取完圖片後執行
            setImage(intent?.data)
        }
    }

    /**
     * 將選取的圖片套入ImageView
     * @param uri 被選取的圖片uri
     */
    private fun setImage(uri: Uri?) {
        var uri = uri
        // 需移除Tint，不然會覆蓋圖片
        binding.imageViewPicture.clearColorFilter()
        binding.imageViewPicture.loadImageWithGlide(uri!!, binding.imageViewPicture)
        isAddImage = true
    }

    /**
     * 確認是否刪除檔案Dialog點擊確認
     */
    override fun onConfirmClick() {
        notesEntity.let {
            viewModel.deleteRecord(notesEntity.number)
        }
        viewModel.deleteLocalFolder(this, notesEntity)
        finish()
    }
}