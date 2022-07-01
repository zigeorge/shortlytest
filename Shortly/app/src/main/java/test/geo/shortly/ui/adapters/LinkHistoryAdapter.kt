package test.geo.shortly.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_link_history.view.*
import test.geo.shortly.R
import test.geo.shortly.data.local.ShortLink

class LinkHistoryAdapter :
    ListAdapter<ShortLink, LinkHistoryAdapter.LinkHistoryViewHolder>(ListHistoryDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkHistoryViewHolder {
        return LinkHistoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_link_history, parent, false),
            parent.context
        )
    }

    override fun onBindViewHolder(holder: LinkHistoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.apply {
            tvOriginLink.text = item.origin
            tvShortenLink.text = item.shortLink

            if (position == latestCopiedPosition) {
                tvCopyBtn.setBackgroundResource(R.drawable.btn_bg_copied)
                tvCopyBtn.text = holder.context.getString(R.string.btn_text_copied)
            } else {
                tvCopyBtn.setBackgroundResource(R.drawable.btn_bg_copy)
                tvCopyBtn.text = holder.context.getString(R.string.btn_text_copy)
            }

            ivRemoveHistory.setOnClickListener {
                onRemoveIconClickListener?.let { click ->
                    click(item)
                }
            }
            tvCopyBtn.setOnClickListener {
                onCopyButtonClickListener?.let { click ->
                    click(item)
                }
                latestCopiedPosition = if(latestCopiedPosition == -1) {
                    holder.adapterPosition
                } else {
                    notifyItemChanged(latestCopiedPosition)
                    holder.adapterPosition
                }
                notifyItemChanged(holder.adapterPosition)
            }
        }
    }

    private var latestCopiedPosition = -1

    fun updateCopiedPosition() {
        if (latestCopiedPosition == -1) {
            return
        }
        latestCopiedPosition++
    }

    private var onRemoveIconClickListener: ((ShortLink) -> Unit)? = null

    fun setOnRemoveIconClickListener(listener: (ShortLink) -> Unit) {
        onRemoveIconClickListener = listener
    }

    private var onCopyButtonClickListener: ((ShortLink) -> Unit)? = null

    fun setOnCopyButtonClickListener(listener: (ShortLink) -> Unit) {
        onCopyButtonClickListener = listener
    }

    class LinkHistoryViewHolder(itemView: View, val context: Context) :
        RecyclerView.ViewHolder(itemView)
}

class ListHistoryDiff : DiffUtil.ItemCallback<ShortLink>() {
    override fun areItemsTheSame(
        oldItem: ShortLink,
        newItem: ShortLink
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ShortLink,
        newItem: ShortLink
    ): Boolean {
        return oldItem == newItem
    }
}