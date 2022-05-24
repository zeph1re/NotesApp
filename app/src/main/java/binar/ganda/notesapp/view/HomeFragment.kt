package binar.ganda.notesapp.view

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import binar.ganda.notesapp.R
import binar.ganda.notesapp.local.notes.Notes
import binar.ganda.notesapp.local.notes.NotesDatabase
import binar.ganda.notesapp.view.adapter.NotesAdapter
import kotlinx.android.synthetic.main.custom_dialog_add.*
import kotlinx.android.synthetic.main.custom_dialog_add.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class HomeFragment : Fragment() {

    //GLOBAL VARIABLE
    private var notesDB: NotesDatabase? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TAMPILKAN NOTES

        getDataNotes()

        //Floating Add Button
        add_notes.setOnClickListener {

            val view = LayoutInflater.from(requireContext())
                .inflate(R.layout.custom_dialog_add, null, false)
            val customDialog = AlertDialog.Builder(requireContext())
            customDialog.setView(view)
            customDialog.create()

            view.add_dialog_btn.setOnClickListener {
                GlobalScope.async {
                    val title = et_add_title.text.toString()
                    val desc = et_add_desc.text.toString()
                    val date = LocalDateTime.now().toString()
                    val notes = Notes(null, title, desc, date)

                    val result = notesDB?.notesDao()?.insertNotes(notes)

                    activity?.runOnUiThread {
                        if (result != 0.toLong()){
                            Toast.makeText(requireContext(), "Berhasil", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(requireContext(), "Gagal", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    fun getDataNotes() {
        rv_notes.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)

        GlobalScope.launch {
            val listNotes = notesDB?.notesDao()?.getAllNotes()

            activity?.runOnUiThread {
                listNotes.let {
                    val adapt = NotesAdapter(it!!)
                    rv_notes.adapter = adapt
                }
            }
        }
    }
}