package it.pdm.app

import Notes.noteAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class Activity_fetching : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var noteList: ArrayList<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        recyclerView = findViewById(R.id.noteRecycle)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        noteList = arrayListOf<Note>()

        getNoteData()
    }

    private fun getNoteData(){
        recyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        val db = FirebaseRealtimeDBHelper.dbRefRT.child("Notes")

        db.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                noteList.clear()
                if (snapshot.exists()){
                    for (noteSnap in snapshot.children){
                        val note = noteSnap.getValue(Note::class.java)
                        noteList.add(note!!)
                    }
                    val mAdapter = noteAdapter(noteList)
                    recyclerView.adapter = mAdapter

                    recyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }
}