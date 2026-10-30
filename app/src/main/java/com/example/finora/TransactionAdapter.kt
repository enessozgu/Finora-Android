
package com.example.finora.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finora.databinding.ItemTransactionBinding
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class TransactionAdapter(
    private val transactions: List<TransactionDto>
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(
        val binding: ItemTransactionBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionViewHolder {

        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TransactionViewHolder,
        position: Int
    ) {

        val transaction = transactions[position]
        val binding = holder.binding

        binding.tvTransactionTitle.text = transaction.title
        binding.tvTransactionDescription.text = transaction.description

        val symbols = DecimalFormatSymbols(Locale("tr", "TR"))
        val formatter = DecimalFormat("#,##0.00", symbols)

        val amountText = formatter.format(
            kotlin.math.abs(transaction.amount)
        )

        if (transaction.amount < 0) {

            // Gider işlemi
            binding.tvTransactionAmount.text = "-$amountText ₺"
            binding.tvTransactionAmount.setTextColor(
                Color.parseColor("#D9534F")
            )

            binding.tvTransactionIcon.text = "↑"
            binding.tvTransactionIcon.setTextColor(
                Color.parseColor("#D9534F")
            )

            binding.tvTransactionIcon.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FCEDEC"))

        } else {

            // Gelir işlemi
            binding.tvTransactionAmount.text = "+$amountText ₺"
            binding.tvTransactionAmount.setTextColor(
                Color.parseColor("#239B72")
            )

            binding.tvTransactionIcon.text = "↓"
            binding.tvTransactionIcon.setTextColor(
                Color.parseColor("#239B72")
            )

            binding.tvTransactionIcon.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#EAF7F1"))
        }
    }

    override fun getItemCount(): Int {
        return transactions.size
    }
}
