package com.gabyperez.divisaclient

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SimpleCursorAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader

class MainActivity : AppCompatActivity() {
    private val myLoaderCallbacks = object : LoaderManager.LoaderCallbacks<Cursor> {
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
            //TODO("Not yet implemented")
            return CursorLoader(
                applicationContext,
                Uri.parse("content://com.gabyperez.divisaserver/monedas"),
                arrayOf("_ID","base_code","update","code","value"),
                null, null, null)
        }

        override fun onLoaderReset(loader: Loader<Cursor>) {
            //TODO("Not yet implemented")
            Log.i("MONEDAXCliente", " Nada de CP")
        }

        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
            //TODO("Not yet implemented")

            data?.apply {
                val adapter = SimpleCursorAdapter(applicationContext,
                    android.R.layout.simple_list_item_2,
                    this,
                    arrayOf("_ID","base_code","update","code","value"),
                    IntArray(4).apply {
                        set(3, android.R.id.text1)
                    } ,
                    SimpleCursorAdapter.IGNORE_ITEM_VIEW_TYPE
                )
                spn.adapter = adapter

                //Show general information
                var items = adapter.count
                var info = ""
                items --
                var uno = false


                for(i in 0..items) {
                    var cursor = adapter.getItem(i) as Cursor
                    //Log.d("thisI", i.toString() + " " + cursor.getString(cursor.getColumnIndexOrThrow("code")))

                    if (cursor.getString(cursor.getColumnIndexOrThrow("code")) == "USD") {
                        if(uno) info += "\n\n"
                        uno = true
                        val cursor = adapter.getItem(i) as Cursor
                        info += "Última actualización: " +
                                cursor.getString(cursor.getColumnIndexOrThrow("update"))

                        info += "\n"

                        info += cursor.getString(cursor.getColumnIndexOrThrow("code")) + " \t " +
                                cursor.getString(cursor.getColumnIndexOrThrow("value")) + "\n"
                    } else {
                        info += cursor.getString(cursor.getColumnIndexOrThrow("code")) + " \t " +
                                cursor.getString(cursor.getColumnIndexOrThrow("value")) + "\n"
                    }
                }
                txtJson.append(info)



                //Click on spinner
                /*spn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        val cursor = adapter.getItem(position) as Cursor
                        val code = cursor.getString(cursor.getColumnIndexOrThrow("code"))
                        //txt.text = code

                        //val uri = Uri.parse("content://com.gabyperez.divisaserver/monedas/"+code)
                        //contentResolver.query(uri, )

                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {

                    }
                }*/

                while (moveToNext()){
                    Log.i("MONEDAXClienteLC", " id: ${getInt(0)} , code: ${getString(1)}, moneda ${getString(2)}")
                }
            }



        }

    }

    lateinit var spn : Spinner
    lateinit var txtJson : TextView

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spn = findViewById(R.id.spinner)
        txtJson = findViewById(R.id.jsonText)

        LoaderManager.getInstance(this)
            .initLoader(1001, null, myLoaderCallbacks)

        val micursor   = contentResolver.query(
            Uri.parse("content://com.gabyperez.divisaserver/monedas"),
            arrayOf("_ID","base_code","update","code","value"),
            null, null,null
        )
        micursor?.apply {
            while (moveToNext()){
                Log.i("MONEDAXCliente", " id: ${getInt(0)} , code: ${getString(1)}, moneda ${getString(2)}")
            }
        }

    }
}