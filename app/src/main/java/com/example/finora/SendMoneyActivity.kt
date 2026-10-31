
package com.example.finora.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.finora.databinding.ActivitySendMoneyBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

class SendMoneyActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendMoneyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySendMoneyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Geri dön
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Devam et
        binding.btnContinue.setOnClickListener {
            validateAndConfirm()
        }
    }

    // FORM KONTROLLERİ
    private fun validateAndConfirm() {

        val iban = binding.etIban.text.toString()
            .filterNot { it.isWhitespace() }
            .uppercase(Locale.ROOT)

        val recipient = binding.etRecipientName.text.toString().trim()

        val amountText = binding.etAmount.text.toString()
            .trim()
            .replace(",", ".")

        val description = binding.etDescription.text.toString().trim()

        // IBAN kontrolü
        if (!iban.matches(Regex("^TR\\d{24}$"))) {
            binding.etIban.error = "Geçerli bir TR IBAN gir"
            binding.etIban.requestFocus()
            return
        }

        // Alıcı kontrolü
        if (recipient.length < 3) {
            binding.etRecipientName.error = "Alıcı adını kontrol et"
            binding.etRecipientName.requestFocus()
            return
        }

        // Tutar kontrolü
        val amount = amountText.toBigDecimalOrNull()

        if (amount == null || amount <= BigDecimal.ZERO) {
            binding.etAmount.error = "Geçerli bir tutar gir"
            binding.etAmount.requestFocus()
            return
        }

        // Transfer bilgilerini onaya gönder
        showConfirmation(
            iban,
            recipient,
            amount,
            description
        )
    }

    // TRANSFER ONAY EKRANI
    private fun showConfirmation(
        iban: String,
        recipient: String,
        amount: BigDecimal,
        description: String
    ) {

        val formatter = NumberFormat.getNumberInstance(
            Locale("tr", "TR")
        )

        formatter.minimumFractionDigits = 2
        formatter.maximumFractionDigits = 2

        val message = """
            Alıcı: $recipient

            IBAN: $iban

            Tutar: ${formatter.format(amount)} ₺

            Açıklama: ${description.ifBlank { "Belirtilmedi" }}
        """.trimIndent()

        AlertDialog.Builder(this)
            .setTitle("Transfer bilgilerini kontrol et")
            .setMessage(message)
            .setNegativeButton("Düzenle", null)
            .setPositiveButton("Onayla") { _, _ ->

                sendTransfer(
                    iban,
                    recipient,
                    amount,
                    description
                )
            }
            .show()
    }

    // BACKEND'E TRANSFER İSTEĞİ GÖNDER
    private fun sendTransfer(
        iban: String,
        recipient: String,
        amount: BigDecimal,
        description: String
    ) {

        binding.btnContinue.isEnabled = false

        val request = TransferRequestDto(
            iban = iban,
            recipientName = recipient,
            amount = amount,
            description = description.ifBlank { null }
        )

        RetrofitClient.api.createTransfer(request).enqueue(
            object : Callback<TransferResponseDto> {

                override fun onResponse(
                    call: Call<TransferResponseDto>,
                    response: Response<TransferResponseDto>
                ) {

                    binding.btnContinue.isEnabled = true

                    val result = response.body()

                    if (response.isSuccessful && result != null) {

                        val transferInfo = """
                            ${result.message}

                            Alıcı: ${result.recipientName}
                            Tutar: ${result.amount} ₺
                            İşlem No: ${result.transferId}
                        """.trimIndent()

                        AlertDialog.Builder(this@SendMoneyActivity)
                            .setTitle("Sunucu yanıtı")
                            .setMessage(transferInfo)
                            .setPositiveButton("Tamam", null)
                            .show()

                    } else {

                        Toast.makeText(
                            this@SendMoneyActivity,
                            "Sunucu hatası: HTTP ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<TransferResponseDto>,
                    t: Throwable
                ) {

                    binding.btnContinue.isEnabled = true

                    Toast.makeText(
                        this@SendMoneyActivity,
                        "Bağlantı hatası: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }
}
