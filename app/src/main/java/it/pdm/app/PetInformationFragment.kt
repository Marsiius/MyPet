package it.pdm.app

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_pet.*
import kotlinx.android.synthetic.main.fragment_pet_information.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PetInformationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PetInformationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pet_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        card_information.setOnClickListener {
            findNavController().navigate(R.id.action_petFragment_to_petIdentityCardFragment)
        }

        card_medical.setOnClickListener {
            val fragment = MedFragment()
            val transaction: FragmentTransaction? = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.rl_pet_fragment, fragment)
            transaction?.commit()
        }

        card_camera.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            builder.setTitle("How?")
            builder.setMessage("Open camera or select picture from local storage?")
            builder.setPositiveButton("Camera"
            ) { _, _ -> takePicture()}
            builder.setNeutralButton("Gallery"
            ) { _, _ -> selectPicture()}
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        Card4.setOnClickListener {
            TODO()
        }
    }

    private fun selectPicture(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, Pet.GALLERY_REQUEST_CODE)
    }

    private fun takePicture(){
        if (context?.let { it1 ->
                ContextCompat.checkSelfPermission(
                    it1,
                    Manifest.permission.CAMERA
                )
            } == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, Pet.CAMERA_REQUEST_CODE)
        }
        else{
            ActivityCompat.requestPermissions(
                context as Activity, arrayOf(Manifest.permission.CAMERA),
                Pet.CAMERA_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== Pet.CAMERA_PERMISSION_CODE)
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, Pet.CAMERA_REQUEST_CODE)
            }else{
                Toast.makeText(context, "DENIED ACCESS, check device settings", Toast.LENGTH_LONG).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Pet.CAMERA_REQUEST_CODE){
                val thumbnail: Bitmap = data!!.extras!!.get("data") as Bitmap
                pet_picture.setImageBitmap(thumbnail)
            }else if(requestCode == Pet.GALLERY_REQUEST_CODE){
                val uri = data?.data
                pet_picture.setImageURI(uri)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PetInformationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PetInformationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}