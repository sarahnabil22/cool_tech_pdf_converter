package id.ac.umn.cool_tech_pdf_converter.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.pdftron.common.PDFNetException
import com.pdftron.pdf.Convert
import com.pdftron.pdf.PDFDoc
import com.pdftron.pdf.utils.Utils
import com.pdftron.sdf.SDFDoc

class PdftronHelper   {

    fun simpleDocxConvert(inputFilePath: String, outputFilePath: String? , onFinishAction:()->Unit , onErrorAction:(message : String)->Unit) {
            try {

                // perform the conversion with no optional parameters
                val pdfdoc = PDFDoc()
                Convert.officeToPdf(pdfdoc, inputFilePath , null)

                // save the result
                pdfdoc.save(outputFilePath , SDFDoc.SaveMode.INCREMENTAL, null)

                // And we're done!
                onFinishAction.invoke()
            } catch (e: PDFNetException) {
                onErrorAction.invoke(e.message ?: " error not found")


            }
        }

    fun simpleImageConvert(context : Context , inputFilePath: String, outputFilePath: String? , onFinishAction:()->Unit , onErrorAction:(message : String)->Unit) {
        try {
            val dialog = showAlertDialog(context , "loading" , "converting" ,false)
            dialog.show()
            // perform the conversion with no optional parameters
            val pdfdoc = PDFDoc()
            Convert.toPdf(pdfdoc, inputFilePath)

            // save the result
            pdfdoc.save(outputFilePath , SDFDoc.SaveMode.INCREMENTAL, null)

            // And we're done!
            dialog.dismiss()
            onFinishAction.invoke()
        } catch (e: PDFNetException) {
            onErrorAction.invoke(e.message ?: " error not found")


        }
    }

    fun showAlertDialog(context : Context , title : String , message : String , isCancelable : Boolean) : AlertDialog{
        val alertDialogBuilder = AlertDialog.Builder(context)
            .apply{
                setTitle(title)
                setMessage(message)
                setCancelable(isCancelable)
            }
        val dialog = alertDialogBuilder.create()
      return dialog
    }





}

