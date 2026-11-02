package com.example.finora.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finora.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        // Bakiye bilgisini getir
        getDashboard()
    }

    // Ekrana her dönüşte işlem listesini yenile
    override fun onResume() {
        super.onResume()

        if (::binding.isInitialized) {
            getTransfers()
        }
    }

    // DASHBOARD API
    private fun getDashboard() {

        RetrofitClient.api.getDashboard().enqueue(
            object : Callback<DashboardDto> {

                override fun onResponse(
                    call: Call<DashboardDto>,
                    response: Response<DashboardDto>
                ) {

                    if (response.isSuccessful) {

                        val dashboard = response.body()

                        if (dashboard != null) {

                            binding.tvBalance.text =
                                "${dashboard.balance} ${dashboard.currency}"
                        }

                    } else {

                        Toast.makeText(
                            this@MainActivity,
                            "Bakiye sunucu hatası: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<DashboardDto>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        this@MainActivity,
                        "Bakiye bağlantı hatası: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }

    // POSTGRESQL TRANSFER LİSTESİ
    private fun getTransfers() {

        RetrofitClient.api.getTransfers().enqueue(
            object : Callback<List<TransferResponseDto>> {

                override fun onResponse(
                    call: Call<List<TransferResponseDto>>,
                    response: Response<List<TransferResponseDto>>
                ) {

                    if (response.isSuccessful) {

                        val transfers = response.body() ?: emptyList()

                        // Veritabanındaki transferleri göster
                        binding.rvTransactions.adapter =
                            TransferAdapter(transfers)

                    } else {

                        Toast.makeText(
                            this@MainActivity,
                            "İşlem geçmişi hatası: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<List<TransferResponseDto>>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        this@MainActivity,
                        "İşlem geçmişi bağlantı hatası: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }
}