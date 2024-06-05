package com.aristidevs.syncotask.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.aristidevs.syncotask.R
import com.aristidevs.syncotask.objects.Note
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditNoteDialog(private val context: Context, private val fragmentManager: FragmentManager) {

    private lateinit var mAlertDialog: AlertDialog
    private lateinit var noteId: String
    private lateinit var title: EditText
    private lateinit var description: EditText
    private lateinit var btnEditNote: Button
    private lateinit var btnDeleteNote: Button
    private lateinit var btnSelectImage: Button
    private var imageUri: Uri? = null
    private lateinit var imageNote: ImageView

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    fun showEditNoteDialog(note: Note) {
        initViews(note)
        initListeners(note)
    }

    private fun initViews(note: Note) {
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_note, null)
        val mBuilder = AlertDialog.Builder(context).setView(mDialogView)
        mAlertDialog = mBuilder.show()

        noteId = note.noteId!!
        title = mDialogView.findViewById(R.id.textTitle)
        description = mDialogView.findViewById(R.id.textDescription)
        btnEditNote = mDialogView.findViewById(R.id.btnEditNote)
        btnDeleteNote = mDialogView.findViewById(R.id.btnDeleteNote)
        imageNote = mDialogView.findViewById(R.id.imageNote)
        btnSelectImage = mDialogView.findViewById(R.id.btnEditImage)

        title.setText(note.title)
        description.setText(note.description)
    }

    private fun initListeners(note: Note) {
        btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            (context as Activity).startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnEditNote.setOnClickListener {
            if (title.text.isEmpty()) {
                showToast("Por favor, ingrese un título.")
            } else {
                val newNote = Note(
                    noteId,
                    title.text.toString(),
                    description.text.toString(),
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                )

                val dbRef = FirebaseDatabase.getInstance().getReference("Notes").child(noteId)
                dbRef.setValue(newNote).addOnCompleteListener { taskResult ->
                    if (taskResult.isSuccessful) {
                        imageUri?.let { uri ->
                            uploadImageToFirebaseStorage(uri, noteId) { imageUrl ->
                                dbRef.child("imageUrl").setValue(imageUrl)
                                showToast("Nota: " + newNote.title + " actualizada.")
                            }
                        } ?: showToast("Nota: " + newNote.title + " actualizada.")
                    } else {
                        showToast("Error al actualizar la nota: " + taskResult.exception?.message)
                    }
                }
                mAlertDialog.dismiss()
            }
        }

        btnDeleteNote.setOnClickListener {
            val dbRef = FirebaseDatabase.getInstance().getReference("Notes").child(noteId)
            val noteDeleted = dbRef.removeValue()
            noteDeleted.addOnSuccessListener {
                showToast("Nota: \"" + note.title + "\" eliminada.")
            }.addOnFailureListener { error ->
                showToast("Hubo un error al eliminar la nota: \"" + note.title + "\"")
            }
            mAlertDialog.dismiss()
        }
    }

    private fun uploadImageToFirebaseStorage(imageUri: Uri, noteId: String, onSuccess: (String) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().getReference("NoteImages/$noteId.jpg")
        storageRef.putFile(imageUri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
        }.addOnFailureListener { exception ->
            showToast("Error al subir la imagen: ${exception.message}")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(mAlertDialog.context, message, Toast.LENGTH_SHORT).show()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            imageNote.setImageURI(imageUri)
        }
    }
}
