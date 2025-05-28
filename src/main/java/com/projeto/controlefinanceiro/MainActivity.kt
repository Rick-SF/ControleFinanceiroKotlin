package com.projeto.controlefinanceiro

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var TextDescricao: EditText
    private lateinit var TextValor: EditText
    private lateinit var ListGastos: ListView
    private lateinit var ViewTotal: TextView
    private lateinit var BtnAdicionarReceita: Button
    private lateinit var BtnAdicionarDespesa: Button
    private lateinit var BtnEditar: Button

    private val listaGastos = DadosFinanceiros.listaGlobal
    private lateinit var adapter: ArrayAdapter<String>
    private var somaTotal = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        TextDescricao = findViewById(R.id.Descricao)
        ViewTotal = findViewById(R.id.TotalGasto)
        TextValor = findViewById(R.id.Valor)
        ListGastos = findViewById(R.id.ListaGastos)
        BtnAdicionarReceita = findViewById(R.id.BtnAdicionarReceita)
        BtnAdicionarDespesa = findViewById(R.id.BtnAdicionarDespesa)
        BtnEditar = findViewById(R.id.BtnEditar)

        adapter = CustomAdapter()
        ListGastos.adapter = adapter

        BtnAdicionarReceita.setOnClickListener {
            adicionarItem(isGasto = false)
        }
        BtnAdicionarDespesa.setOnClickListener {
            adicionarItem(isGasto = true)
        }

        BtnEditar.setOnClickListener {
            val intent = Intent(this, EditarActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        recalculaTotal()
        adapter.notifyDataSetChanged()
    }

    private fun recalculaTotal() {
        somaTotal = 0.0
        for (item in listaGastos) {
            if (item.startsWith("Receita")) {
                val valor = extraiValor(item)
                somaTotal += valor
            } else if (item.startsWith("Despesa")) {
                val valor = extraiValor(item)
                somaTotal -= valor
            }
        }
        ViewTotal.text = "Soma Total: R$%.2f".format(somaTotal)
    }

    private fun extraiValor(item: String): Double {
        val regex = Regex("R\\$(\\d+(?:\\.\\d{1,2})?)")
        val match = regex.find(item)
        return match?.groupValues?.get(1)?.toDoubleOrNull() ?: 0.0
    }

    fun adicionarItem(isGasto: Boolean) {
        val descricao = TextDescricao.text.toString().trim()
        val valorTexto = TextValor.text.toString().trim()

        if (descricao.isNotEmpty() && valorTexto.isNotEmpty()) {
            val valor = valorTexto.replace(",", ".").toDoubleOrNull()
            if (valor != null) {
                val tipo = if (isGasto) "Despesa" else "Receita"
                val item = "$tipo: $descricao - R$%.2f".format(valor)

                listaGastos.add(item)
                adapter.notifyDataSetChanged()
                recalculaTotal()

                TextDescricao.text.clear()
                TextValor.text.clear()
            } else {
                Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Preencha os campos.", Toast.LENGTH_SHORT).show()
        }
    }

    inner class CustomAdapter : ArrayAdapter<String>(this, R.layout.item_lista_main, R.id.textItem, listaGastos) {
        override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
            val view = super.getView(position, convertView, parent)
            val textView = view.findViewById<TextView>(R.id.textItem)
            val item = listaGastos[position]
            if (item.startsWith("Receita")) {
                textView.setTextColor(android.graphics.Color.parseColor("#388E3C"))
            } else if (item.startsWith("Despesa")) {
                textView.setTextColor(android.graphics.Color.parseColor("#E71D41"))
            } else {
                textView.setTextColor(android.graphics.Color.WHITE)
            }
            return view
        }
    }
}
