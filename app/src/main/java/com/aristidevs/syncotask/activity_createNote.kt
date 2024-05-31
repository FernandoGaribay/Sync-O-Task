package com.aristidevs.syncotask

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aristidevs.syncotask.objects.Note
import com.aristidevs.syncotask.objects.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class activity_createNote : AppCompatActivity() {

    private lateinit var dbRef : DatabaseReference

    lateinit var btnBack: ImageView
    lateinit var btnCreateNote: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        btnCreateNote = findViewById<Button>(R.id.btnCreateNote)
        btnCreateNote.setOnClickListener {
            val note = Note(
                title = findViewById<EditText>(R.id.textTitle).text.toString(),
                description = findViewById<EditText>(R.id.textDescription).text.toString(),
                date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            )

            dbRef = FirebaseDatabase.getInstance().getReference("Notes")
            val empId = dbRef.push().key!!
            dbRef.child(empId).setValue(note)
                .addOnCompleteListener{
                    Toast.makeText(this,"Datos subidos", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{ err ->
                    showToast("eror")
                    Toast.makeText(this,"Error ${err.message}", Toast.LENGTH_SHORT).show()
                }
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}