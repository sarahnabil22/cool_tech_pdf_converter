package id.ac.umn.cool_tech_pdf_converter.utils

import android.icu.util.TimeUnit.values
import java.lang.reflect.Type
import java.time.chrono.JapaneseEra.values

enum class ConverterType(val type:Int) {
    WORD_TO_PDF(1) , IMAGE_TO_PDF(2) , CUSTOMIZE_PDF(3) , PDF_TO_WORD(4) , PDF_TO_IMAGE(5)

}