package binar.ganda.notesapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import binar.ganda.notesapp.R
import binar.ganda.notesapp.local.notes.Notes
import binar.ganda.notesapp.local.notes.NotesDatabase
import kotlinx.android.synthetic.main.item_notes.view.*

class NotesAdapter(private val listNotes : List<Notes>): RecyclerView.Adapter<NotesAdapter.ViewHolder>(){

    private var notesDB : NotesDatabase? = null

    class ViewHolder(layout : View) : RecyclerView.ViewHolder(layout){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesAdapter.ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_notes, parent, false)
        return   ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: NotesAdapter.ViewHolder, position: Int) {
        holder.itemView.title_tv.text = listNotes[position].title
        holder.itemView.desc_tv.text = listNotes[position].desc
        holder.itemView.date_tv.text = listNotes[position].date.toString()
    }

    override fun getItemCount(): Int {
        return listNotes.size
    }
}