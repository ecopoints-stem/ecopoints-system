package br.edu.uea.ecopoints.screen.home.employee.fragment.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.edu.uea.ecopoints.R
import br.edu.uea.ecopoints.databinding.ItemSpMaterialCardBinding
import br.edu.uea.ecopoints.domain.entity.SeparatedMaterial
import br.edu.uea.ecopoints.domain.entity.enums.MaterialType.EXPANDED_POLYSTYRENE
import br.edu.uea.ecopoints.domain.entity.enums.MaterialType.GLASS
import br.edu.uea.ecopoints.domain.entity.enums.MaterialType.METALS
import br.edu.uea.ecopoints.domain.entity.enums.MaterialType.PAPER
import br.edu.uea.ecopoints.domain.entity.enums.MaterialType.PLASTICS
import br.edu.uea.ecopoints.domain.entity.enums.MaterialType.WASTE
import br.edu.uea.ecopoints.util.formatedPersonDate
import br.edu.uea.ecopoints.util.formatedPersonDateTime

class SeparatedMaterialAdapter : PagingDataAdapter<SeparatedMaterial, SeparatedMaterialAdapter.SeparatedMaterialViewHolder>(SeparatedMaterialDiffCallback()) {
    override fun onBindViewHolder(holder: SeparatedMaterialViewHolder, position: Int) {
        val spMaterial = getItem(position)
        holder.materialName.text = spMaterial?.materialName
        holder.materialIcon.setImageResource(
            when(spMaterial?.materialType){
                PLASTICS -> R.drawable.plastics
                PAPER -> R.drawable.paper
                METALS -> R.drawable.metals
                GLASS -> R.drawable.glass
                EXPANDED_POLYSTYRENE -> R.drawable.expanded_polystyrene
                WASTE -> R.drawable.waste
                else -> R.drawable.waste
            }
        )
        holder.kg.text = spMaterial?.quantity.toString()
        holder.personDate.text = spMaterial?.separatedDate?.formatedPersonDateTime()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeparatedMaterialViewHolder {
        val binding = ItemSpMaterialCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeparatedMaterialViewHolder(binding)
    }

    inner class SeparatedMaterialViewHolder(binding: ItemSpMaterialCardBinding) : RecyclerView.ViewHolder(binding.root) {
        var cardContent = binding.clCardContent
        var materialName = binding.tvMaterialName
        var materialIcon = binding.ivMaterialIcon
        var kg = binding.tvKg
        var personDate = binding.tvDate
    }
}

class SeparatedMaterialDiffCallback : DiffUtil.ItemCallback<SeparatedMaterial>() {
    override fun areItemsTheSame(oldItem: SeparatedMaterial, newItem: SeparatedMaterial): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: SeparatedMaterial,
        newItem: SeparatedMaterial
    ): Boolean = oldItem == newItem
}