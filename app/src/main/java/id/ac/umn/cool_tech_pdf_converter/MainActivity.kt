package id.ac.umn.cool_tech_pdf_converter

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.pdftron.pdf.config.ViewerConfig
import com.pdftron.pdf.controls.DocumentActivity
import id.ac.umn.cool_tech_pdf_converter.databinding.ActivityMainBinding
import id.ac.umn.cool_tech_pdf_converter.utils.ConverterType
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initAction()
        if(checkPermission()){
            requestPermission()
        }

    }

    fun requestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
        try{
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.addCategory("android.intent.category.DEFAULT")
            intent.setData(Uri.parse(String.format("package:%s" , applicationContext.packageName)))
            startActivityForResult(intent , PERMISSION_REQUEST_CODE)
        }
        catch(exception : Exception ){
            val intent = Intent()
           intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
            startActivityForResult(intent , PERMISSION_REQUEST_CODE)
        }
        }

        else{
            ActivityCompat.requestPermissions(this , permissions , PERMISSION_REQUEST_CODE )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
             if(requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()){
                 if(grantResults.all{it == PackageManager.PERMISSION_GRANTED }){
                    //permission granted
                 }
                 else{
                     Toast.makeText(this , "need permission access" , Toast.LENGTH_SHORT).show()
                 }


             }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PERMISSION_REQUEST_CODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if( Environment.isExternalStorageManager()){
                //permission granted
            }
            else{
                Toast.makeText(this , "need permission access" , Toast.LENGTH_SHORT).show()
            }

        }

    }

    fun checkPermission():Boolean{
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager()
        }
        else{
            val readStorage = ContextCompat.checkSelfPermission(this , Manifest.permission.READ_EXTERNAL_STORAGE)
            val writeStorage = ContextCompat.checkSelfPermission(this , Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return readStorage == PackageManager.PERMISSION_GRANTED && writeStorage == PackageManager.PERMISSION_GRANTED
        }
    }

    fun initAction() {
        binding.buttonWordToPdf.setOnClickListener{
            navigateToConvertPage(ConverterType.WORD_TO_PDF.type)
        }
        binding.buttonImageToPdf.setOnClickListener{
            navigateToConvertPage(ConverterType.IMAGE_TO_PDF.type)
        }
        binding.buttonPdfToImage.setOnClickListener{
            navigateToConvertPage(ConverterType.PDF_TO_IMAGE.type)
        }
        binding.buttonPdfToWord.setOnClickListener{
            navigateToConvertPage(ConverterType.PDF_TO_WORD.type)
        }
    }

    fun navigateToConvertPage(type:Int){
        val intent = Intent(this , ComverterActivity::class.java)
        intent.putExtra(KEY_CONVERTER_TYPE , type)
        startActivity(intent)
    }



    companion object{
        const val KEY_CONVERTER_TYPE= "KEY_CONVERTER_TYPE"
        const val PERMISSION_REQUEST_CODE = 22
    }



}