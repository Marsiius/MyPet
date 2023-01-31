package it.pdm.app

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

        card112.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Are you sure?")
            builder.setPositiveButton("Yes"){
                _,_-> callNumber("112")
            }
            builder.setNegativeButton("Cancel"){_,_->}
            val dialog = builder.create()
            dialog.show()
        }
        enpaCard.setOnClickListener{
            callNumber("02.97064220")
        }
        websiteCard.setOnClickListener{
            val url = "https://www.salute.gov.it/portale/caniGatti/homeCaniGatti.jsp"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        emergencyCard4.setOnClickListener{
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