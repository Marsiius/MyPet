package Notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import it.pdm.app.R
import it.pdm.app.databinding.FragmentNotesBinding
import kotlinx.android.synthetic.main.fragment_notes.*
import kotlinx.android.synthetic.main.fragment_pop_up_notes.*


class NotesFragment : Fragment(), FragmentPopUpNotes.OnDialogNextBtnClickListener {

    private lateinit var binding: FragmentNotesBinding

    /*variabili primo codice*/
    private lateinit var auth: FirebaseAuth
    private lateinit var authId: String
    private lateinit var database: DatabaseReference
    private lateinit var taskAdapter: toDoAdapter
    private lateinit var toDoItemList: MutableList<Note>
    /*--------------VARIABILI-----------------*/
    private lateinit var popUpFragment: FragmentPopUpNotes
    /*--------VARIABILI PER IL POPUP-------*/


    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = FragmentBinding.inflate(inflater, container, false)


    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*fab.setOnClickListener{
            findNavController().navigate(R.id.action_notesFragment_to_fragmentAddNotes)
        }*/

        init()
        getNoteFromFirebase()
        registerEvents()
        /*binding.fab.setOnClickListener{
            if(popUpFragment != null){
                childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
                popUpFragment = FragmentPopUpNotes()
                popUpFragment!!.setListener(this)

                popUpFragment!!.show(childFragmentManager, "DialogFragment")
            }
        }*/
    }

    private fun getNoteFromFirebase(){
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                toDoItemList.clear()
                for (noteSnapshot in snapshot.children){
                    val toDoNote = noteSnapshot.key?.let { Note(noteSnapshot.value.toString()) }
                    //val toDoDate = noteSnapshot.key?.let { Note(noteSnapshot.value.toString()) }
                    if(toDoNote != null){
                        toDoItemList.add(toDoNote)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun init() {

        auth = FirebaseAuth.getInstance()
        authId = auth.currentUser!!.uid
        database = Firebase.database.reference.child("users").child(authId).child("Notes")

        binding.noteList.setHasFixedSize(true)
        binding.noteList.layoutManager = LinearLayoutManager(context)

        toDoItemList = mutableListOf()
        taskAdapter = toDoAdapter(toDoItemList)
        binding.noteList.adapter = taskAdapter
    }

    private fun registerEvents(){
        binding.fab.setOnClickListener{
            popUpFragment = FragmentPopUpNotes()
            popUpFragment.setListener(this)
            popUpFragment.show(childFragmentManager, "FragmentPopUpNotes")
        }
    }

    /*override fun onSaveTask(note: String, tvNote: TextInputEditText) {
        database.push().setValue(note).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(context, "Nota aggiunta", Toast.LENGTH_SHORT).show()
                tvNote.text = null
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            popUpFragment.dismiss()
        }
    }*/

    override fun onSaveTask(note: String, tvNote: TextInputEditText) {
        database.push().setValue(note).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(context, "Nota aggiunta", Toast.LENGTH_SHORT).show()
                tvNote.text = null
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            popUpFragment.dismiss()
        }
    }

/*---------------------------------------------------------------------------------------------------------*/
    /*override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setOnClickListener{
            findNavController().navigate(R.id.action_notesFragment_to_fragmentAddNotes)
        }
        /*noteList.layoutManager = LinearLayoutManager(requireContext())
        noteList.setHasFixedSize(true)*/
        init()

        noteRecyclerView = view.findViewById(R.id.noteList)
        noteRecyclerView.layoutManager = LinearLayoutManager(activity)
        noteRecyclerView.setHasFixedSize(true)

        arrayList = arrayListOf<Note>()

        getUserData()
    }

    private fun getUserData() {
        val db = FirebaseRealtimeDBHelper.dbRefRT.child("Notes")

        db.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                if (snapshot.exists()){
                    for (userSnapshot in snapshot.children){
                        val note = userSnapshot.child("NKJOLQ4kU-SfaVIX93m").getValue(Note::class.java)
                        if (note != null) {
                            arrayList.add(note)
                        }
                        Log.d("nota", note.toString())
                    }
                    val mAdapter = MyAdapter(arrayList)
                    noteRecyclerView.adapter = mAdapter

                    noteRecyclerView.visibility = View.VISIBLE
                    //noteList.adapter = MyAdapter(noteArray)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }*/

    /*------------------------------------------------------------------------------------------*/


    /*
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        
        val array = ArrayList<Note>()
        setNote(array)

        return binding.root
    }

    private fun setNote(array: ArrayList<Note>) {
        val db = FirebaseRealtimeDBHelper.dbRefRT.child("Notes")
        db.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (i in snapshot.children){
                        val note = i.child("NKJOLQ4kU-SfaVIX93m").getValue(Note::class.java)
                        //array.add(note!!)  //se metto .child("Notes") da errore all'array add
                        //arrayList.add(note!!)
                        Log.d("nota", note.toString())
                    }
                }

                val recyclerView = binding.noteList
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                val adapter = MyAdapter(array)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }*/

    /*private fun init() {
        uId = FirebaseAuth.getInstance().currentUser!!.uid
    }*/

}