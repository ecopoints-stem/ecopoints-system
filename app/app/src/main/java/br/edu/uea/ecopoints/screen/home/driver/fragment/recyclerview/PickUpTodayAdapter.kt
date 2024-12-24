package br.edu.uea.ecopoints.screen.home.driver.fragment.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.paging.PagingSource
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.edu.uea.ecopoints.R
import br.edu.uea.ecopoints.databinding.PickupItemBinding
import br.edu.uea.ecopoints.domain.entity.PickUpRequest
import br.edu.uea.ecopoints.domain.entity.enums.MaterialType.PLASTICS
import br.edu.uea.ecopoints.domain.entity.enums.MaterialType.PAPER
import br.edu.uea.ecopoints.domain.entity.enums.MaterialType.METALS
import br.edu.uea.ecopoints.domain.entity.enums.MaterialType.GLASS
import br.edu.uea.ecopoints.domain.entity.enums.MaterialType.EXPANDED_POLYSTYRENE
import br.edu.uea.ecopoints.domain.entity.enums.MaterialType.WASTE
import br.edu.uea.ecopoints.util.formatedPersonDate
import br.edu.uea.ecopoints.util.toMaterialString
import br.edu.uea.ecopoints.util.toStatusString

class PickUpTodayAdapter (
    private val onItemClicked: (PickUpRequest) -> Unit
) : PagingDataAdapter<PickUpRequest, PickUpTodayAdapter.PickUpViewHolder>(PickUpDiffCallback()) {

    override fun onBindViewHolder(holder: PickUpViewHolder, position: Int) {
        val pickup = getItem(position)
        holder.cardContent.setOnClickListener {
            pickup?.let{
                onItemClicked(it)
            }
        }
        holder.materialType.text = pickup?.materialType?.toMaterialString()
        holder.materialIcon.setImageResource(
            when(pickup?.materialType){
                PLASTICS -> R.drawable.plastics
                PAPER -> R.drawable.paper
                METALS -> R.drawable.metals
                GLASS -> R.drawable.glass
                EXPANDED_POLYSTYRENE -> R.drawable.expanded_polystyrene
                WASTE -> R.drawable.waste
                else -> R.drawable.waste
            }
        )
        holder.kg.text = pickup?.quantity.toString()
        holder.personDate.text = pickup?.requestDate?.formatedPersonDate()
        holder.address.text = pickup?.address
        holder.status.text = pickup?.status?.toStatusString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickUpViewHolder {
        val binding = PickupItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PickUpViewHolder(binding)
    }

    inner class PickUpViewHolder(binding: PickupItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var cardContent = binding.clCardContent
        var materialType = binding.tvMaterialType
        var materialIcon = binding.ivMaterialIcon
        var kg = binding.tvKg
        var personDate = binding.tvDate
        var address = binding.tvAddress
        var status = binding.tvStatus
    }
}

class PickUpDiffCallback : DiffUtil.ItemCallback<PickUpRequest>(){
    override fun areItemsTheSame(oldItem: PickUpRequest, newItem: PickUpRequest): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: PickUpRequest, newItem: PickUpRequest): Boolean = oldItem == newItem
}