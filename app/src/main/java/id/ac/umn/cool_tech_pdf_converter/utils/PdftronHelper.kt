package id.ac.umn.cool_tech_pdf_converter.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.pdftron.actions.easypdf.EasyPdfService
import com.pdftron.common.PDFNetException
import com.pdftron.pdf.Convert
import com.pdftron.pdf.PDFDoc
import com.pdftron.pdf.PDFDraw
import com.pdftron.pdf.PDFNet
import com.pdftron.pdf.utils.Utils
import com.pdftron.sdf.SDFDoc
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

class PdftronHelper   {
    val scope = CoroutineScope(Job() + Dispatchers.IO)

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

    fun initializePdfService(){
        val clientId = "1eb210be402844c287b4464026eb89cb"
        val clientSecret = "335F8FC64EC58B460E1420B1EE6B253DEB81EAA09936DEAFF3B14350B5EC8558"
        val workflowId = "0000000000000001"
        EasyPdfService.initialize(clientId, clientSecret)
        EasyPdfService.setJobId(EasyPdfService.Type.PDF_TO_WORD, workflowId)
    }



  fun pdfToWord(inputFilePath: String, ouputFilePath: String ,   onFinishAction:()->Unit , onErrorAction:(message : String)->Unit)  {
      scope.launch{
          initializePdfService()

          val inputFile = File(inputFilePath)
          if (inputFile.exists().not()){
              inputFile.mkdirs()
          }

          val outputFile = File(ouputFilePath)
          if (outputFile.exists().not()){
              outputFile.mkdirs()
          }else if(outputFile.isDirectory.not() && outputFile.canWrite()){
              outputFile.delete()
              outputFile.mkdirs()
          }
          EasyPdfService.startPdf2WordJob(inputFile,outputFile)

              .subscribe({ s: String ->
                  // do something with the converted file
                  onFinishAction.invoke()
              }, { throwable: Throwable? ->
                  // handle error
                  onErrorAction.invoke(throwable?.message ?: "unknown error")

              })
      }


    }

    fun simplePdfToWordConvert(context : Context , inputFilePath: String, outputFilePath: String? , onFinishAction:()->Unit , onErrorAction:(message : String)->Unit) {
        try {
            val dialog = showAlertDialog(context, "loading", "converting", false)
            dialog.show()
            // perform the conversion with no optional parameters
           //PDFNet.addResourceSearchPath()
            val pdfdoc = PDFDoc()
            Convert.toWord(inputFilePath , outputFilePath)

            // save the result
            pdfdoc.save(outputFilePath, SDFDoc.SaveMode.INCREMENTAL, null)

            // And we're done!
            dialog.dismiss()
            onFinishAction.invoke()
        } catch (e: PDFNetException) {
            onErrorAction.invoke(e.message ?: " error not found")


        }
    }

    fun simplePdfToImageConvert(inputFilePath: String , outputFilePath: String? , onFinishAction:()->Unit , onErrorAction:(message : String)->Unit){
        val draw = PDFDraw()
        try {
            val doc = PDFDoc(inputFilePath)
            doc.initSecurityHandler()
            draw.setDPI(92.0)
            val itr = doc.pageIterator
            while (itr.hasNext()) {
                val current = itr.next()!!
                draw.export(current, outputFilePath)
            }
            doc.close()
            onFinishAction.invoke()
        } catch (e: Exception) {
            onErrorAction.invoke(e.message ?: "unknown error")
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
            Log.e(ContentValues.TAG, "getRealPathFromURI Exception : $e")
            ""
        } finally {
            cursor?.close()
        }
    }







}

