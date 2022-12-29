package Notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import it.pdm.app.R
import it.pdm.app.databinding.FragmentPopUpNotesBinding


class FragmentPopUpNotes : DialogFragment() {

    private lateinit var binding: FragmentPopUpNotesBinding
    private var listener: OnDialogNextBtnClickListener? = null

    fun setListener(listener: OnDialogNextBtnClickListener){
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPopUpNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvents()
    }

    private fun registerEvents(){
        binding.popUpNext.setOnClickListener{
            val noteTask = binding.tvNote.text.toString()
            if (noteTask.isNotEmpty()){
                listener?.onSaveTask(noteTask, binding.tvNote)
            }else{
                Toast.makeText(context, "Non puoi lasciare vuoto", Toast.LENGTH_SHORT).show()
            }

            binding.popUpClose.setOnClickListener{
                 dismiss()
            }
        }
    }

    interface OnDialogNextBtnClickListener{
        fun onSaveTask(note : String, tvNote: TextInputEditText)
    }

}