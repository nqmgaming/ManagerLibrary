package com.example.managerlibrary.ui.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.example.managerlibrary.R
import java.util.ArrayList

class AddLoanActivity : AppCompatActivity() {
    private var spProvince: SmartMaterialSpinner<String>? = null
    private var spEmptyItem: SmartMaterialSpinner<String>? = null
    private var provinceList: MutableList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_loan)
        initSpinner()
    }
    private fun initSpinner() {
        spProvince = findViewById(R.id.spinner1)
//        spEmptyItem = findViewById(R.id.sp_empty_item)
        provinceList = ArrayList()

        provinceList?.add("Kampong Thom")
        provinceList?.add("Kampong Cham")
        provinceList?.add("Kampong Chhnang")
        provinceList?.add("Phnom Penh")
        provinceList?.add("Kandal")
        provinceList?.add("Kampot")

        spProvince?.item = provinceList

        spProvince?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@AddLoanActivity, provinceList!![position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
    }
}