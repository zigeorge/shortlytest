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
    ListAdapter<ShortLink, LinkHistoryAdapter.LinkHistoryViewHolder>(DiffCallback())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkHistoryViewHolder {
        return LinkHistoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_link_history, parent, false),
            parent.context
        )
    }

    private var copiedLinkId: Int? = null

    override fun onBindViewHolder(holder: LinkHistoryViewHolder, position: Int) {
        val shortLink = getItem(position)
        holder.itemView.apply {
            tvOriginLink.text = shortLink.origin
            tvShortenLink.text = shortLink.shortLink

            if(copiedLinkId != null && shortLink.id == copiedLinkId) {
                tvCopyBtn.setBackgroundResource(R.drawable.btn_bg_copied)
                tvCopyBtn.text = holder.context.getString(R.string.btn_text_copied)
            } else {
                tvCopyBtn.setBackgroundResource(R.drawable.bg_btn_url_short)
                tvCopyBtn.text = holder.context.getString(R.string.btn_text_copy)
            }

            ivRemoveHistory.setOnClickListener {
                onRemoveIconClickListener?.let { click ->
                    click(shortLink)
                }
            }
            tvCopyBtn.setOnClickListener {
                copiedLinkId = shortLink.id
                notifyDataSetChanged()
            }
        }
    }

    private var onRemoveIconClickListener: ((ShortLink) -> Unit)? = null

    fun setOnRemoveIconClickListener(listener: (ShortLink) -> Unit) {
        onRemoveIconClickListener = listener
    }

    class LinkHistoryViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView)
}

class DiffCallback : DiffUtil.ItemCallback<ShortLink>() {
    override fun areItemsTheSame(oldItem: ShortLink, newItem: ShortLink): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShortLink, newItem: ShortLink): Boolean {
        return newItem.id == oldItem.id
    }
}