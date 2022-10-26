package it.pdm.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import it.pdm.app.databinding.FragmentBinding
import it.pdm.app.databinding.FragmentHomeBinding

class BlankFragment : Fragment() {

    private lateinit var binding: FragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

   fun onBackPressed(){

   }

}