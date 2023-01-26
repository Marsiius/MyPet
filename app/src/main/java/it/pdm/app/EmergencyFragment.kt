package it.pdm.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_emergency.*

class EmergencyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_emergency, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button.setOnClickListener{
            callNumber("1223334444")
        }

        //implementare fragment con numeri di emergenza (112, numeri verde per gli animali,
        //numero per animali selvatici e domestici...)

        //FACOLTATIVO: piccolo pdf con all'interno informazioni su come comportarsi in caso
        //di emergenza con animale domestico e/o selvatico

    }

    private fun callNumber(phoneNumber: String){
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }
}