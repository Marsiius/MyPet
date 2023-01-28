package it.pdm.app

import Notes.Note
import Notes.toDoAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.pdm.app.databinding.ItemRecycleviewBinding
import medpet.Vaccine

class VaccineAdapter(private val list: MutableList<Vaccine>) : RecyclerView.Adapter<VaccineAdapter.VaccineViewHolder>(){
    private var listener: adapterClickInterface?= null
    fun setListener(listener: adapterClickInterface){
        this.listener = listener
    }

    inner class VaccineViewHolder(val binding: ItemRecycleviewBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccineViewHolder {
        val binding = ItemRecycleviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VaccineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VaccineViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.tvNoteItem.text = this.nameVaccine //questo prende il tvNoteItem dal binding del itemRecycleView
                binding.tvDateItem.text = this.dateVaccine
                binding.tvBodyItem.text = this.recallVaccine

                binding.deleteNote.setOnClickListener{
                    listener?.onDeleteNoteBtnClicked(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface adapterClickInterface{
        fun onDeleteNoteBtnClicked(vaccine: Vaccine)
    }

}