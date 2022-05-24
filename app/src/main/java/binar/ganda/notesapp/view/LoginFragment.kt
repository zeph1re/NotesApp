package binar.ganda.notesapp.view

import android.content.Context
import android.os.Bundle
import android.service.autofill.UserData
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import binar.ganda.notesapp.R
import binar.ganda.notesapp.datastore.UserManager
import binar.ganda.notesapp.local.user.UserDatabase
import binar.ganda.notesapp.view.adapter.NotesAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private var userDatabase : UserDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)

        if (sharedPref.contains("USERNAME_LOGIN") && sharedPref.contains("PASSWORD_LOGIN")){
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment)
        } else {
            //Login with User
            login_btn.setOnClickListener {
                    if (et_username.text.isEmpty() && et_password.text.isEmpty()){
                        Toast.makeText(requireContext(), "Username dan Password Kosong, Silahkan isi terlebih dahulu", Toast.LENGTH_LONG).show()
                    } else {
                        login()
                        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment)
                    }
            }
            //Register
            register_tv.setOnClickListener {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
            }
        }
    }

    fun login() {

        userDatabase = UserDatabase.getInstance(requireContext())

        // Get Data From Fragment
        val usernameLogin = et_username.text.toString()
        val passwordLogin = et_password.text.toString()

        // check Info User from UserDatabase
        GlobalScope.launch {
            val userLogin = userDatabase?.userDao()?.getUserInfo(usernameLogin, passwordLogin)

            activity?.runOnUiThread {
                userLogin.let {
                    if (userLogin?.username.toString() != usernameLogin && userLogin?.password?.toString() != passwordLogin){
                        Toast.makeText(requireContext(),"Username dan Password Salah",Toast.LENGTH_LONG).show()
                    } else {
                        val sharedPref = requireContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
                        val sf = sharedPref.edit()
                        sf.putString("USERNAME_LOGIN", usernameLogin)
                        sf.putString("PASSWORD_LOGIN", passwordLogin)
                        sf.apply()
                    }
                }
            }
        }

    }

}