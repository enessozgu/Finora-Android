
package com.example.finora.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finora.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding bağlantısı
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // İşlem listesini dikey sırala
        binding.rvTransactions.layoutManager =
            LinearLayoutManager(this)

        // PARA GÖNDER
        binding.btnSendMoney.setOnClickListener {
            val intent = Intent(this, SendMoneyActivity::class.java)
            startActivity(intent)
        }

        // FATURA ÖDE
        binding.btnPayBills.setOnClickListener {
            Toast.makeText(
                this,
                "Fatura ödeme ekranı yakında!",
                Toast.LENGTH_SHORT
            ).show()
        }

        // KARTLARIM
        binding.btnMyCards.setOnClickListener {
            Toast.makeText(
                this,
                "Kartlarım ekranı yakında!",
                Toast.LENGTH_SHORT
            ).show()
        }

        // PARA İSTE
        binding.btnRequestMoney.setOnClickListener {
            Toast.makeText(
                this,
                "Para isteme ekranı yakında!",
                Toast.LENGTH_SHORT
            ).show()
        }

        // QR İŞLEMLERİ
        binding.btnQr.setOnClickListener {
            Toast.makeText(
                this,
                "QR işlemleri yakında!",
                Toast.LENGTH_SHORT
            ).show()
        }

        // İŞLEM GEÇMİŞİ
        binding.btnHistory.setOnClickListener {
            Toast.makeText(
                this,
                "İşlem geçmişi ekranı yakında!",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Dashboard verilerini sunucudan getir
        getDashboard()
    }

    // DASHBOARD API BAĞLANTISI
    private fun getDashboard() {

        RetrofitClient.api.getDashboard().enqueue(
            object : retrofit2.Callback<DashboardDto> {

                override fun onResponse(
                    call: retrofit2.Call<DashboardDto>,
                    response: retrofit2.Response<DashboardDto>
                ) {

                    if (response.isSuccessful) {

                        val dashboard = response.body()

                        if (dashboard != null) {

                            // Hesap bakiyesini güncelle
                            binding.tvBalance.text =
                                "${dashboard.balance} ${dashboard.currency}"

                            // API'den gelen işlemleri listele
                            binding.rvTransactions.adapter =
                                TransactionAdapter(dashboard.transactions)
                        }

                    } else {

                        Toast.makeText(
                            this@MainActivity,
                            "Sunucu hatası: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<DashboardDto>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        this@MainActivity,
                        "Bağlantı hatası: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }
}
