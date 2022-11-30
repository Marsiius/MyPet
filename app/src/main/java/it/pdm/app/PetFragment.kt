package it.pdm.app

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_pet.*
import kotlinx.android.synthetic.main.toolbar.*

var clicked = false

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Pet.newInstance] factory method to
 * create an instance of this fragment.
 */
class Pet : Fragment() {

    private val SHARED_PREFS = "sharedPrefs"

    //override per la definizione della parte grafica/layout del fragment
    //questo metodo deve ritornare una vista oppure null (default)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pet, container, false)
    }

    //implementazione FAB botton con override di onViewCreated
    //Viene chiamato dopo onCreateView() e viene utilizzato principalmente per le inizializzazioni-
    //-finali (ad esempio, la modifica degli elementi dell'interfaccia utente);-
    //-questo è deprecato dal livello API 28.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab.setOnClickListener{
            setFabAnimation()
        }

        fab_add.setOnClickListener {
            findNavController().navigate(R.id.action_petFragment_to_signupPetFragment)
        }

        ic_logout.setOnClickListener{
            logout()
        }
    }

    private fun logout() {
        Toast.makeText(context, "LOG OUT", Toast.LENGTH_LONG).show()

        val sharedPreferences: SharedPreferences? = context?.getSharedPreferences(SHARED_PREFS,
            AppCompatActivity.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString("name", "")
        editor?.apply()

        val intent: Intent = Intent(context, RegisterActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun setFabAnimation(){
        if(!clicked){
            fab.startAnimation(AnimationUtils.loadAnimation(context,R.anim.rotate_forward))
            fab.backgroundTintList = ColorStateList.valueOf(rgb(255,0,0))
            fab_add.visibility = View.VISIBLE
            fab_edit.visibility = View.VISIBLE
            fab_del.visibility = View.VISIBLE
        }else{
            fab.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_backward))
            fab.backgroundTintList = ColorStateList.valueOf(rgb(76,175,80))
            fab_add.visibility = View.INVISIBLE
            fab_edit.visibility = View.INVISIBLE
            fab_del.visibility = View.INVISIBLE
        }
        clicked = !clicked
    }

    //companion object da implementare per il salvataggio di contesto
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Pet.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Pet().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}





