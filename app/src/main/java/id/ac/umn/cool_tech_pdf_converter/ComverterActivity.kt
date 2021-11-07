package id.ac.umn.cool_tech_pdf_converter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import id.ac.umn.cool_tech_pdf_converter.MainActivity.Companion.KEY_CONVERTER_TYPE
import id.ac.umn.cool_tech_pdf_converter.databinding.ActivityComverterBinding
import id.ac.umn.cool_tech_pdf_converter.databinding.ActivityMainBinding
import id.ac.umn.cool_tech_pdf_converter.utils.ConverterType

class ComverterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityComverterBinding
    private var type : ConverterType = ConverterType.WORD_TO_PDF
    private var selectedFile : ArrayList<Uri> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComverterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initIntent()
        initAction()

    }

    fun initIntent(){
       val converterType = intent.getIntExtra(KEY_CONVERTER_TYPE , 0)
        type = when(converterType ){
            ConverterType.WORD_TO_PDF.type -> ConverterType.WORD_TO_PDF
            ConverterType.IMAGE_TO_PDF.type -> ConverterType.IMAGE_TO_PDF
            ConverterType.CUSTOMIZE_PDF.type -> ConverterType.CUSTOMIZE_PDF
            ConverterType.PDF_TO_WORD.type -> ConverterType.PDF_TO_WORD
            ConverterType.PDF_TO_IMAGE.type -> ConverterType.PDF_TO_IMAGE
            else -> ConverterType.WORD_TO_PDF
        }
    setUpToolbarTitle()
    }

    fun setUpToolbarTitle(){
        val convertTitle = when(type){
            ConverterType.WORD_TO_PDF -> "word to pdf"
            ConverterType.IMAGE_TO_PDF -> "image to pdf"
            ConverterType.CUSTOMIZE_PDF -> "customize pdf"
            ConverterType.PDF_TO_WORD -> "pdf to word"
            ConverterType.PDF_TO_IMAGE -> "pdf to image"
        }
        binding.textViewTitle.text = convertTitle
    }

    fun selectFile(requestCode:Int){
        FilePickerBuilder.instance
            .setMaxCount(1) //optional
            .setActivityTheme(R.style.LibAppTheme) //optional
            .pickFile(this, requestCode);


    }

    fun initAction(){
        binding.buttonInsertFile1.setOnClickListener{
            selectFile(FILE1_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == FILE1_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK && data!=null ){
                val file = data.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_DOCS).orEmpty()
                selectedFile.addAll(file)
                binding.buttonInsertFile1.text = "change file"
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object{
        const val FILE1_REQUEST_CODE = 100
    }









}