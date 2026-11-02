package com.example.finora.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finora.databinding.ItemTransactionBinding
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class TransferAdapter(
    private val transfers: List<TransferResponseDto>
) : RecyclerView.Adapter<TransferAdapter.TransferViewHolder>() {

    class TransferViewHolder(
        val binding: ItemTransactionBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransferViewHolder {

        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return TransferViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TransferViewHolder,
        position: Int
    ) {

        val transfer = transfers[position]
        val binding = holder.binding

        binding.tvTransactionTitle.text =
            transfer.recipientName ?: "Para transferi"

        val description = transfer.description
            ?.takeIf { it.isNotBlank() }
            ?: "Demo transfer"

        binding.tvTransactionDescription.text = description

        val symbols = DecimalFormatSymbols(Locale("tr", "TR"))
        val formatter = DecimalFormat("#,##0.00", symbols)

        val amount = transfer.amount ?: BigDecimal.ZERO
        val amountText = formatter.format(amount)

        // Giden transfer
        binding.tvTransactionAmount.text = "-$amountText ₺"
        binding.tvTransactionAmount.setTextColor(
            Color.parseColor("#D9534F")
        )

        binding.tvTransactionIcon.text = "↑"
        binding.tvTransactionIcon.setTextColor(
            Color.parseColor("#D9534F")
        )

        binding.tvTransactionIcon.backgroundTintList =
            ColorStateList.valueOf(
                Color.parseColor("#FCEDEC")
            )
    }

    override fun getItemCount(): Int = transfers.size
}