package com.projeto.controlefinanceiro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ConfirmarExclusaoActivity : AppCompatActivity() {

    private var itemIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_confirmar_exclusao)

        val itemTexto = intent.getStringExtra("itemTexto") ?: "este item"

        // Recupera o índice do item a ser excluído
        val itemIndex = intent.getIntExtra("itemIndex", -1)

        val textoConfirmacao = findViewById<TextView>(R.id.textPergunta)
        textoConfirmacao.text = "Tem certeza que deseja excluir \"$itemTexto\"?"

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.confirmarExclusaoLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnConfirmar = findViewById<Button>(R.id.btnConfirmar)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        // Botão "Sim" – confirma exclusão
        btnConfirmar.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("itemIndex", itemIndex)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        // Botão "Não" – cancela e retorna
        btnCancelar.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}
