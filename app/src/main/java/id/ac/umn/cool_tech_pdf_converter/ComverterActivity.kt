package id.ac.umn.cool_tech_pdf_converter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.widget.Toast
import com.google.android.gms.cast.framework.media.ImagePicker
import com.pdftron.pdf.config.ViewerConfig
import com.pdftron.pdf.controls.DocumentActivity
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import id.ac.umn.cool_tech_pdf_converter.MainActivity.Companion.KEY_CONVERTER_TYPE
import id.ac.umn.cool_tech_pdf_converter.databinding.ActivityComverterBinding
import id.ac.umn.cool_tech_pdf_converter.databinding.ActivityMainBinding
import id.ac.umn.cool_tech_pdf_converter.utils.ConverterHelper
import id.ac.umn.cool_tech_pdf_converter.utils.ConverterType
import id.ac.umn.cool_tech_pdf_converter.utils.PdftronHelper
import id.ac.umn.cool_tech_pdf_converter.utils.getRealPathFromURI
import java.io.File

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
            selectFileOrImage(FILE1_REQUEST_CODE)
        }
        binding.buttonInsertFile2.setOnClickListener{
            selectFileOrImage(FILE2_REQUEST_CODE)
        }
        binding.buttonInsertFile3.setOnClickListener{
            selectFileOrImage(FILE3_REQUEST_CODE)
        }
        binding.buttonInsertFile4.setOnClickListener{
            selectFileOrImage(FILE4_REQUEST_CODE)
        }
        binding.buttonInsertFile5.setOnClickListener{
            selectFileOrImage(FILE5_REQUEST_CODE)
        }
        binding.buttonConvert.setOnClickListener{
            val helper = PdftronHelper()
            selectedFile.forEach{
                val uploadedFile = File(getRealPathFromURI(this , it))
                val outputPath =   "${getExternalFilesDir(null)}/" + uploadedFile.nameWithoutExtension + ".pdf"
                if(type == ConverterType.WORD_TO_PDF){
                    helper.simpleDocxConvert(
                        inputFilePath = uploadedFile.path ,
                        outputFilePath = outputPath ,
                        onFinishAction = {

                            Toast.makeText(this , "file converted" , Toast.LENGTH_SHORT).show()
                            openLocalFile(outputPath)
                        } ,

                        onErrorAction = {

                            Toast.makeText(this , "error $it" , Toast.LENGTH_SHORT).show()
                        })
                }
                else{
                    helper.simpleImageConvert(
                        context = this ,
                        inputFilePath = uploadedFile.path ,
                        outputFilePath = outputPath ,
                        onFinishAction = {

                            Toast.makeText(this , "file converted" , Toast.LENGTH_SHORT).show()
                            openLocalFile(outputPath)
                        } ,

                        onErrorAction = {

                            Toast.makeText(this , "error $it" , Toast.LENGTH_SHORT).show()
                        })
                }


            }
        }
    }

    fun selectFileOrImage(requestCode: Int){
        when(type){
            ConverterType.WORD_TO_PDF -> selectFile(requestCode)
            ConverterType.IMAGE_TO_PDF -> selectImage(requestCode)
            ConverterType.PDF_TO_WORD -> selectFile(requestCode)
            ConverterType.PDF_TO_IMAGE -> selectFile(requestCode)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK && data!=null ){
            val file = data.getParcelableArrayListExtra<Uri>(
                if(type == ConverterType.WORD_TO_PDF){
                    FilePickerConst.KEY_SELECTED_DOCS
                }
            else{
                    FilePickerConst.KEY_SELECTED_MEDIA
            }
               ).orEmpty()
            selectedFile.addAll(file)
            val uploadedFile = File(file[0].path)
            when(requestCode){
                FILE1_REQUEST_CODE  -> binding.buttonInsertFile1.text = uploadedFile.name
                FILE2_REQUEST_CODE  -> binding.buttonInsertFile2.text = uploadedFile.name
                FILE3_REQUEST_CODE -> binding.buttonInsertFile3.text = uploadedFile.name
                FILE4_REQUEST_CODE  -> binding.buttonInsertFile4.text = uploadedFile.name
                FILE5_REQUEST_CODE  -> binding.buttonInsertFile5.text = uploadedFile.name
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun openLocalFile(path: String) {
        // Set the cache location using the config to store the cache file
        val config =
            ViewerConfig.Builder().openUrlCachePath(this.getCacheDir().getAbsolutePath()).build();

        val uri = Uri.fromFile(File(path))

        val intent: Intent =
            DocumentActivity.IntentBuilder.fromActivityClass(this, DocumentActivity::class.java)
                .withUri(uri)
                .usingConfig(config)
                .usingTheme(R.style.PDFTronAppTheme)
                .build()
        startActivity(intent)
    }

    fun selectImage(requestCode: Int){
        FilePickerBuilder.instance
            .setMaxCount(1) //optional
            .setActivityTheme(R.style.LibAppTheme) //optional
            .pickPhoto(this, requestCode  );
    }

    companion object{
        const val FILE1_REQUEST_CODE = 100
        const val FILE2_REQUEST_CODE = 80
        const val FILE3_REQUEST_CODE = 90
        const val FILE4_REQUEST_CODE = 92
        const val FILE5_REQUEST_CODE = 82


    }









}