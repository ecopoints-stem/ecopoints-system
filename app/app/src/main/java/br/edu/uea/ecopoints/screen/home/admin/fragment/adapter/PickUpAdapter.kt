package br.edu.uea.ecopoints.screen.home.admin.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.edu.uea.ecopoints.R
import br.edu.uea.ecopoints.databinding.PickupItemBinding
import br.edu.uea.ecopoints.domain.entity.PickUpRequest
import br.edu.uea.ecopoints.domain.entity.enums.MaterialType.*

class PickUpAdapter : PagingDataAdapter<PickUpRequest,PickUpAdapter.PickUpViewHolder>(PickUpDiffCallback()) {

    override fun onBindViewHolder(holder: PickUpViewHolder, position: Int) {
        val pickup = getItem(position)
        holder.materialType.text = pickup?.materialType.toString()
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
        holder.personDate.text = pickup?.requestDate.toString()
        holder.address.text = pickup?.address
        holder.status.text = pickup?.status.toString()
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