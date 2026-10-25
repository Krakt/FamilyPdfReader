package com.krakt.familypdfreader

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Launcher to open the file picker for PDF
    private val pdfPickerLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let { selectedUri ->
            // Ask for file permission to read the selected PDF
            val contentResolver = applicationContext.contentResolver
            contentResolver.takePersistableUriPermission(
                selectedUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            Toast.makeText(this, "PDF sélectionné avec succès !", Toast.LENGTH_LONG).show()

            // Launch the native PDF viewer with the selected PDF
            openPdfWithNativeApp(selectedUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSelectPdf = findViewById<Button>(R.id.btnSelectPdf)
        btnSelectPdf.setOnClickListener {
            // Trigger the explorer to select a PDF
            pdfPickerLauncher.launch(arrayOf("application/pdf"))
        }
    }

    private fun openPdfWithNativeApp(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            // Give the app permission to read the PDF
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Aucune application trouvée pour ouvrir ce PDF", Toast.LENGTH_LONG).show()
        }
    }
}