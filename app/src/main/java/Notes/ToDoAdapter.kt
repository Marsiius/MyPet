package Notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.pdm.app.databinding.ItemRecycleviewBinding

class toDoAdapter(private val list: MutableList<Note>) : RecyclerView.Adapter<toDoAdapter.toDoViewHolder>(){

    private var listener: adapterClickInterface?= null
    fun setListener(listener : adapterClickInterface){
        this.listener = listener
    }

    inner class toDoViewHolder(val binding: ItemRecycleviewBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): toDoViewHolder {
        val binding = ItemRecycleviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return toDoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: toDoViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.tvNoteItem.text = this.textNote

                binding.deleteNote.setOnClickListener{
                    listener?.onDeleteNoteBtnClicked(this)
                }
                binding.editNote.setOnClickListener{
                    listener?.onEditNoteBtnClicked(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface adapterClickInterface{
        fun onDeleteNoteBtnClicked(note: Note)
        fun onEditNoteBtnClicked(note: Note)
    }

}