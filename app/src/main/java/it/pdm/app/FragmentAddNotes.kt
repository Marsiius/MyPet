package it.pdm.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import it.pdm.app.databinding.FragmentAddNotesBinding
import it.pdm.app.databinding.FragmentNotesBinding
import kotlinx.android.synthetic.main.fragment_add_notes.*
import kotlinx.android.synthetic.main.fragment_signup_pet.*
import pets.MyPet

class FragmentAddNotes : Fragment() {

    private lateinit var binding: FragmentAddNotesBinding
    private lateinit var noteDao: NoteDao

    private lateinit var database: FirebaseDatabase
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var email: String
    private lateinit var uId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentAddNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeUI()

        addNoteButton.setOnClickListener{

            val note = noteEditText.text.toString()
            if (note.isNotEmpty()){
                writeUser()
                writeNote()
                Toast.makeText(context,"Registrato!", Toast.LENGTH_LONG)
                    .show()
                findNavController().navigate(R.id.action_fragmentAddNotes_to_notesFragment)
            }else
                Toast.makeText(context, "Impossibile lasciare vuoto", Toast.LENGTH_SHORT).show()
        }
    }

    private fun writeUser(){
        val ref = database.getReference("users")
        ref.child(uId)
    }

    private fun writeNote() {
        val note = noteEditText.text.toString()
        FirebaseRealtimeDBHelper.dbRef.child(uId).child("Notes").push().setValue(note) //con push() si ottiene una chiave univoca del figlio, così da poter creare più note, eliminando l'overwriting.
    }

    private fun initializeUI(){
        database = Firebase.database
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        email = firebaseUser.email.toString()
        uId = firebaseUser.uid
    }

}