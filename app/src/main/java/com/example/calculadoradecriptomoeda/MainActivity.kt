package com.example.calculadoradecriptomoeda

import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.bloco_cotacao.*
import android.os.Bundle
import kotlinx.android.synthetic.main.bloco_entrada.*
import kotlinx.android.synthetic.main.bloco_saida.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.net.URL
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    val API_URL = "https://www.mercadobitcoin.net/api/BTC/ticker/"
    val formatar = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
    var cotacaoBitcoin: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buscarCotacao()

        btn_calcular.setOnClickListener {
            calcular()
        }
    }

    fun buscarCotacao() {
        doAsync {
            val resposta_api = URL(API_URL).readText()

            cotacaoBitcoin = JSONObject(resposta_api).getJSONObject("ticker").getDouble("last")

            val cotacaoFormatada = formatar.format(cotacaoBitcoin)
            uiThread {
                txt_cotacao.setText("$cotacaoFormatada")
            }
        }
    }

    fun calcular() {
        if (txt_valor.text.isEmpty()){
            txt_valor.error = "Preencha um valor"
            return
        }
        val valorDigitado = txt_valor.text.toString().replace(",",".").toDouble()
        val resultado = if(cotacaoBitcoin > 0) valorDigitado / cotacaoBitcoin else 0.00
        txt_qtd_bitcoins.text = "%.8f".format(resultado)
    }
}