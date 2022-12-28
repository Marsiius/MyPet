package it.pdm.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.pdm.app.databinding.ItemRecycleviewBinding

class MyAdapter(private val data: ArrayList<Note>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
//class MyAdapter(private val noteList: ArrayList<Note>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        /*val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recycleview, parent, false)
        return MyViewHolder(itemView)*/
        val binding = ItemRecycleviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = data[position]
        //holder.initialize(currentitem)
        holder.noteItem.text = currentitem.textBody
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(binding: ItemRecycleviewBinding) : RecyclerView.ViewHolder(binding.root){
        var noteItem = binding.tvNoteItem

        /*fun initialize(item : Note){
            noteItem.text = item.textBody
        }*/
    }

    /*override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_recycleview, parent, false)
        return MyViewHolder(itemView)


    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = noteList[position]
        holder.noteItem.text = currentItem.textBody
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var noteItem : TextView = itemView.findViewById(R.id.tvNoteItem)

    }*/

}