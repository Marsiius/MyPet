package it.pdm.app

import android.os.Binder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import it.pdm.app.databinding.FragmentBinding
import it.pdm.app.databinding.FragmentHomeBinding
import it.pdm.app.databinding.FragmentNotesBinding
import kotlinx.android.synthetic.main.fragment_add_notes.*
import kotlinx.android.synthetic.main.fragment_notes.*


class NotesFragment : Fragment() {

    private lateinit var uId: String
    private lateinit var binding: FragmentNotesBinding
    private lateinit var noteRecyclerView: RecyclerView
    //private lateinit var noteArray: ArrayList<Note>
    private lateinit var adapter : MyAdapter

    private lateinit var arrayList: ArrayList<Note>

    /*variabili primo codice*/
    private lateinit var auth: FirebaseAuth
    private lateinit var authId: String
    private lateinit var database: DatabaseReference
    private lateinit var taskAdapter: toDoAdapter
    private lateinit var toDoItemList: MutableList<Note>
    /*--------------VARIABILI-----------------*/


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
        fab.setOnClickListener{
            findNavController().navigate(R.id.action_notesFragment_to_fragmentAddNotes)
        }

        init()
        getNoteFromFirebase()
    }

    private fun getNoteFromFirebase(){
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                toDoItemList.clear()
                for (noteSnapshot in snapshot.children){
                    val toDoNote = noteSnapshot.key?.let { Note(noteSnapshot.value.toString()) }
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