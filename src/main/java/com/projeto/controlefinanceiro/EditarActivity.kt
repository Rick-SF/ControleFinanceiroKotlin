package com.projeto.controlefinanceiro

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class EditarActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    val listaGlobal = DadosFinanceiros.listaGlobal

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lista_telaeditar)

        listView = findViewById(R.id.ListaEditar)

        val BtnVoltar = findViewById<Button>(R.id.BtnVoltar)
        BtnVoltar.setOnClickListener{
            finish()
        }

        adapter = object : ArrayAdapter<String>(
            this, 0, listaGlobal
        ) {
            override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View{
                val view = layoutInflater.inflate(R.layout.item_lista_editar, parent, false)
                val textItem = view.findViewById<TextView>(R.id.textItem)
                val BtnApagar = view.findViewById<Button>(R.id.BtnApagar)

                textItem.text = listaGlobal[position]
                BtnApagar.setOnClickListener {
                    listaGlobal.removeAt(position)
                    notifyDataSetChanged()
                    Toast.makeText(this@EditarActivity,"Item removido!", Toast.LENGTH_SHORT).show()
                }
                return view
            }
        }
        listView.adapter = adapter
    }
}