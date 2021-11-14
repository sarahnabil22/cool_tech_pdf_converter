package id.ac.umn.cool_tech_pdf_converter.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.*

class ConverterHelper {
    fun convertWordToPdf(inputFilePath : String , outputFilePath : String , context : Context ) {
        try {
            val document = Document()
            PdfWriter.getInstance(document, FileOutputStream(outputFilePath))
            document.open()
            val newFile = File(inputFilePath)
            if (newFile.exists()) {
                val fileInputStream = FileInputStream(newFile)
                val dataInputStream = DataInputStream(fileInputStream)
                val inputStreamReader = InputStreamReader(dataInputStream)
                val bufferReader = BufferedReader(inputStreamReader)
                val line = bufferReader.readLine()
                while (line != null) {
                    val paragraph = Paragraph(line + "\n")
                    paragraph.alignment = Element.ALIGN_JUSTIFIED
                    document.add(paragraph)
                }
                showAlertDialog(context)
            }
            else{
                Toast.makeText(context , "file not exists" , Toast.LENGTH_SHORT).show()
            }
        }
        catch(exception : Exception){
            Toast.makeText(context , exception.message , Toast.LENGTH_SHORT).show()
        }
    }


    fun showAlertDialog(context : Context){
         val alertDialogBuilder = AlertDialog.Builder(context)
             .apply{
                 setTitle("loading")
                 setMessage("converting")
                 setCancelable(false)
             }
        val dialog = alertDialogBuilder.create()
        dialog.show()
    }
}