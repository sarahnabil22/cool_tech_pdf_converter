package id.ac.umn.cool_tech_pdf_converter.utils

import android.content.Context
import android.widget.Toast
import com.pdftron.common.PDFNetException
import com.pdftron.pdf.Convert
import com.pdftron.pdf.PDFDoc
import com.pdftron.pdf.utils.Utils
import com.pdftron.sdf.SDFDoc

class PdftronHelper (val context: Context?)  {

    fun simpleDocxConvert(inputFilePath: String, outputFilePath: String?) {
            try {

                // perform the conversion with no optional parameters
                val pdfdoc = PDFDoc()
                Convert.officeToPdf(pdfdoc, inputFilePath , null)

                // save the result
                pdfdoc.save(outputFilePath , SDFDoc.SaveMode.INCREMENTAL, null)

                // And we're done!
                Toast.makeText(context , "file converted" , Toast.LENGTH_SHORT).show()
            } catch (e: PDFNetException) {
                Toast.makeText(context , "unable to convert this file $e.message" , Toast.LENGTH_SHORT).show()


            }
        }





}

