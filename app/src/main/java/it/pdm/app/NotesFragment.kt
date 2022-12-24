package it.pdm.app

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.pdm.app.databinding.FragmentBinding
import it.pdm.app.databinding.FragmentHomeBinding
import it.pdm.app.databinding.FragmentNotesBinding
import kotlinx.android.synthetic.main.fragment_notes.*


class NotesFragment : Fragment() {

    private lateinit var binding: FragmentNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = FragmentBinding.inflate(inflater, container, false)
    }

    override fun onCreateView(
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
    }

}