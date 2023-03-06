package com.o7services.recyclercrud

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.o7services.recyclercrud.databinding.ActivityMainBinding
import com.o7services.recyclercrud.databinding.AddLayoutBinding
import com.o7services.recyclercrud.databinding.EditLayoutBinding

class MainActivity : AppCompatActivity(), ClickList {
    lateinit var binding:ActivityMainBinding
    val db = Firebase.firestore
     var userList=ArrayList<UserModel>()
    lateinit var adapter: UserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db.collection("User").get().addOnSuccessListener {
       for (snapshot in it){
           val userModel = snapshot.toObject(UserModel::class.java)
           userList.add(userModel)

       }
        }

        adapter= UserAdapter(userList,this)
        binding.recycleview.adapter=adapter
        binding.recycleview.layoutManager=LinearLayoutManager(this)
        binding.btnok.setOnClickListener {
            val dialogBinding=AddLayoutBinding.inflate(layoutInflater)
            val customDialog=Dialog(this)
            customDialog.setContentView(dialogBinding.root)
            dialogBinding.btnAdd.setOnClickListener {
                if (dialogBinding.etName.text.toString().isEmpty()){
                    dialogBinding.etName.error="enter name"
                }
                else if (dialogBinding.etRollNo.text.toString().isEmpty()){
                    dialogBinding.etRollNo.error="enter rollno"
                }
                else{
                   val userModel=UserModel(dialogBinding.etName.text.toString(),dialogBinding.etRollNo.text.toString())
                    db.collection("User")
                        .add(userModel)
                        .addOnSuccessListener {

                        }


                    customDialog.dismiss()
                    adapter.notifyDataSetChanged()
                }
            }
            customDialog.show()
        }
    }

    override fun listClicked(position: Int) {
        val dialogBinding1=EditLayoutBinding.inflate(layoutInflater)
        val customDialog1=Dialog(this)
        customDialog1.setContentView(dialogBinding1.root)
        dialogBinding1.btnEdit.setOnClickListener {
            if (dialogBinding1.etName.text.toString().isEmpty()){
                dialogBinding1.etName.error="enter name"
            }
            else if (dialogBinding1.etRollNo.text.toString().isEmpty()){
                dialogBinding1.etRollNo.error="enter rollno"
            }
            else{
                userList.set(position,UserModel(dialogBinding1.etName.text.toString(),dialogBinding1.etRollNo.text.toString()))
                adapter.notifyDataSetChanged()
                customDialog1.dismiss()
            }
        }
        dialogBinding1.btnDelete.setOnClickListener {
            if (dialogBinding1.etName.text.toString().isEmpty()){
                dialogBinding1.etName.error="enter name"
            }
            else if (dialogBinding1.etRollNo.text.toString().isEmpty()){
                dialogBinding1.etRollNo.error="enter rollno"
            }
            else{
                userList.remove(UserModel(dialogBinding1.etName.text.toString(),dialogBinding1.etRollNo.text.toString()))
                adapter.notifyDataSetChanged()
                customDialog1.dismiss()
            }
        }
       customDialog1.show()

    }
}