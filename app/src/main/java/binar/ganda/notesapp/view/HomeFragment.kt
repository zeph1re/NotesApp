package binar.ganda.notesapp.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import binar.ganda.notesapp.R
import binar.ganda.notesapp.datastore.UserManager
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
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {

    //GLOBAL VARIABLE
    private var notesDB : NotesDatabase? = null
    private var userManager : UserManager? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        notesDB = NotesDatabase.getInstance(requireContext())
        userManager = UserManager(requireContext())
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Greeting
        getGreeting()

        //TAMPILKAN NOTES
        getDataNotes()

        //TAMBAH NOTES
        add_notes.setOnClickListener {
            val view = LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialog_add, null, false)
            val customDialog = AlertDialog.Builder(requireContext())
            customDialog.setView(view)
            customDialog.create()

            view.add_dialog_btn.setOnClickListener {
                GlobalScope.async {

                    //Get Title and Desc from Input
                    val title = et_add_title.text.toString()
                    val desc = et_add_desc.text.toString()

                    //Get Date Now
                    val current = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                    val formatted = current.format(formatter)
                    val date = formatted.toString()

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

        //SHARE Button
        shareNotes()

        //to Profile
        goToProfile()

    }

    fun goToProfile() {
        profile_btn.setOnClickListener{
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_profileFragment)
        }
    }

    fun shareNotes() {
        share_btn.setOnClickListener {
            val intent= Intent()
            intent.action= Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,"Hey Check out this Great app:")
            intent.type="text/plain"
            startActivity(Intent.createChooser(intent,"Share To:"))
        }
    }

    @SuppressLint("SetTextI18n")
    fun getGreeting() {
        var username = ""

            userManager?.userUsername?.asLiveData()?.observe(viewLifecycleOwner){
                username = it.toString()
        }

        welcome_tv.text = "Hello, $username"
    }

    fun getDataNotes() {
        rv_notes.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)

        GlobalScope.launch {
            val listNotes = notesDB?.notesDao()?.getAllNotes()

            activity?.runOnUiThread {
                listNotes.let {
                    val adapt = NotesAdapter(it!! as ArrayList<Notes>){
                        val clickedNotes = bundleOf("DATA_NOTES" to it)
                        Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_detailFragment, clickedNotes)
                    }
                    rv_notes.adapter = adapt
                }
            }
        }
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun addDataNotes() {
//        //Floating Add Button
//        add_notes.setOnClickListener {
//            val view = LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialog_add, null, false)
//            val customDialog = AlertDialog.Builder(requireContext())
//            customDialog.setView(view)
//            customDialog.create()
//
//            view.add_dialog_btn.setOnClickListener {
//                GlobalScope.async {
//
//                    //Get Title and Desc from Input
//                    val title = et_add_title.text.toString()
//                    val desc = et_add_desc.text.toString()
//
//                    //Get Date Now
//                    val current = LocalDateTime.now()
//                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
//                    val formatted = current.format(formatter)
//                    val date = formatted.toString()
//
//                    val notes = Notes(null, title, desc, date)
//
//                    val result = notesDB?.notesDao()?.insertNotes(notes)
//
//                    activity?.runOnUiThread {
//                        if (result != 0.toLong()){
//                            Toast.makeText(requireContext(), "Berhasil", Toast.LENGTH_LONG).show()
//                        } else {
//                            Toast.makeText(requireContext(), "Gagal", Toast.LENGTH_LONG).show()
//                        }
//                    }
//                }
//            }
//        }
//    }
}