package id.ac.umn.cool_tech_pdf_converter.utils

import android.content.ContentValues.TAG
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Image
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.*

class ConverterHelper {
    fun convertWordToPdf(inputFilePath : String , outputFilePath : String , context : Context ) {
        try {
            val document = Document()
            val output = FileOutputStream(File(outputFilePath))

            PdfWriter.getInstance(document, output)
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
            document.close()
        }
        catch(exception : Exception){
            Toast.makeText(context , exception.message , Toast.LENGTH_SHORT).show()
        }
    }

    fun convertImageToPdf(inputFilePath : String , outputFilePath: String , context: Context){
 try{
     val document = Document()
     val output = FileOutputStream(File(outputFilePath))
     PdfWriter.getInstance(document , output)
     document.open()
     val image = Image.getInstance(inputFilePath)
     document.add(image)
     document.close()
     showAlertDialog(context)
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

fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
    var cursor: Cursor? = null
    return try {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        cursor = context.contentResolver.query(contentUri, proj, null, null, null)
        val index: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        if (index != null) {
            cursor?.getString(index)
        } else null
    }

        catch (e: Exception) {
            Log.e(TAG, "getRealPathFromURI Exception : $e")
            ""
        } finally {
            cursor?.close()
        }
}

