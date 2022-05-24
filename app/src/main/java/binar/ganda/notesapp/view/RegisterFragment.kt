package binar.ganda.notesapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import binar.ganda.notesapp.R
import binar.ganda.notesapp.local.user.User
import binar.ganda.notesapp.local.user.UserDatabase
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


class RegisterFragment : Fragment() {

    private var userDatabase : UserDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register_btn.setOnClickListener {

            //check if any of field are empty
            if (et_username_register.text.isEmpty() && et_name_register.text.isEmpty() &&
                et_password.text.isEmpty() && et_confirm_password_register.text.isEmpty()){
                //Toast
                Toast.makeText(requireContext(), "Ada Field Yang Kosong, Harus isi semua!!", Toast.LENGTH_LONG).show()
            } else {
                //check similarity of password and confirm password must be same
                if (et_password_register.text.toString() == et_confirm_password_register.text.toString()){
                    registerUser()
                    Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
                } else {
                    Toast.makeText(requireContext(),"Password dan Confirm Password harus sama" ,Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun registerUser() {
        userDatabase = UserDatabase.getInstance(requireContext())

        GlobalScope.async {
            val usernameRegister = et_username_register.text.toString()
            val nameRegister = et_name_register.text.toString()
            val passwordRegister = et_password_register.text.toString()
            val dataRegistUser = User(null, usernameRegister, nameRegister, passwordRegister)

            val insertDataUser = userDatabase?.userDao()?.registerUser(dataRegistUser)

            activity?.runOnUiThread {
                if (insertDataUser != 0.toLong()){
                    Toast.makeText(requireContext(), "Berhasil Melakukan Register User", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Gagal Melakukakn Register User", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}