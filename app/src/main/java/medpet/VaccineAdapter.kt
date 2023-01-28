package medpet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.pdm.app.databinding.ItemRecycleviewBinding

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
                    listener?.onDeleteVaccineBtnClicked(this)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface adapterClickInterface{
        fun onDeleteVaccineBtnClicked(vaccine: Vaccine)
    }

}