package com.projeto.controlefinanceiro

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // inicialização atrasada das variáveis
    private lateinit var TextDescricao: EditText
    private lateinit var TextValor: EditText
    private lateinit var ListGastos: ListView
    private lateinit var ViewTotal: TextView
    private lateinit var BtnAdicionarReceita: Button
    private lateinit var BtnAdicionarDespesa: Button

    private val listaGastos = mutableListOf<String>()
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

        // define os componentes da tela de acordo com as variáveis
        TextDescricao = findViewById(R.id.Descricao)
        ViewTotal = findViewById(R.id.TotalGasto)
        TextValor = findViewById(R.id.Valor)
        ListGastos = findViewById(R.id.ListaGastos)
        BtnAdicionarReceita = findViewById(R.id.BtnAdicionarReceita)
        BtnAdicionarDespesa = findViewById(R.id.BtnAdicionarDespesa)

        // configura a lista
        adapter = CustomAdapter()
        ListGastos.adapter = adapter

        // Condicionais para se apertado o botão de Receita ou Despesa
        BtnAdicionarReceita.setOnClickListener {
            adicionarItem(isGasto = false)
        }
        BtnAdicionarDespesa.setOnClickListener {
            adicionarItem(isGasto = true)
        }
    }
    // Clique no botao que insere o valor e descrição na lista e no total gasto
    fun adicionarItem(isGasto: Boolean) {
        val descricao = TextDescricao.text.toString().trim()
        val valorTexto = TextValor.text.toString().trim()

        if (descricao.isNotEmpty() && valorTexto.isNotEmpty()) {
            val valor = valorTexto.replace(",", ".").toDoubleOrNull()
            if (valor != null) {
                val sinal = if (isGasto) -valor else valor
                val tipo = if (isGasto) "Despesa" else "Receita"
                val item = "$tipo: $descricao - R$%.2f".format(valor)

                Toast.makeText(this, "Adicionado: $sinal", Toast.LENGTH_SHORT).show()
                adapter.notifyDataSetChanged()

                listaGastos.add(item)
                adapter.notifyDataSetChanged()

                somaTotal += sinal
                ViewTotal.text = "Soma Total: R$%.2f".format(somaTotal)

                TextDescricao.text.clear()
                TextValor.text.clear()
            } else {
                Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Preencha os campos.", Toast.LENGTH_SHORT).show()
        }
    }

    inner class CustomAdapter : ArrayAdapter<String>(this, R.layout.item_gasto, R.id.textItem, listaGastos){
        override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
            val view = super.getView(position, convertView, parent)
            val textView = view.findViewById<TextView>(R.id.textItem)

            val item = listaGastos[position]
            if (item.startsWith("Receita")){
                textView.setTextColor(android.graphics.Color.parseColor("#388E3C"))
            } else if (item.startsWith("Despesa")){
                textView.setTextColor(android.graphics.Color.parseColor("#E71D41"))
            } else {
                textView.setTextColor(android.graphics.Color.WHITE)
            }

            return view
        }
    }
}