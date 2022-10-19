package it.pdm.app

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Color.*
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pet, container, false)
    }

    //implementazione FAB botton con override di onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        val fabAdd = view.findViewById<FloatingActionButton>(R.id.fab_add)
        val fabEdit = view.findViewById<FloatingActionButton>(R.id.fab_edit)
        val fabDel = view.findViewById<FloatingActionButton>(R.id.fab_del)
        val open = AnimationUtils.loadAnimation(context, R.anim.fab_open)
        val forward = AnimationUtils.loadAnimation(context, R.anim.rotate_forward)
        val backward = AnimationUtils.loadAnimation(context,R.anim.rotate_backward)
        val colorOpen = Color.rgb(255,0,0)
        val colorClose = Color.rgb(76, 175, 80)

        fab.setOnClickListener{
            if(!clicked){
                //fab.startAnimation(open)
                fab.startAnimation(forward)
                //fab.setRippleColor(WHITE) //Cambia il colore SOLO QUANDO viene premuto
                fab.backgroundTintList = ColorStateList.valueOf(colorOpen)
                fabAdd.visibility = View.VISIBLE
                fabEdit.visibility = View.VISIBLE
                fabDel.visibility = View.VISIBLE
            }else{
                //fab.startAnimation(close)
                fab.startAnimation(backward)
                fab.backgroundTintList = ColorStateList.valueOf(colorClose)
                fabAdd.visibility = View.INVISIBLE
                fabEdit.visibility = View.INVISIBLE
                fabDel.visibility = View.INVISIBLE
            }
            clicked = !clicked
        }
    }

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






