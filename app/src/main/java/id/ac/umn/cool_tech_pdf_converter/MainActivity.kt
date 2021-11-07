package id.ac.umn.cool_tech_pdf_converter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.ac.umn.cool_tech_pdf_converter.databinding.ActivityMainBinding
import id.ac.umn.cool_tech_pdf_converter.utils.ConverterType


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initAction()
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
    }



}