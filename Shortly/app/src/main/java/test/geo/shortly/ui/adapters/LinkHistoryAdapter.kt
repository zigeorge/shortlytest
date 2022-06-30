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
import test.geo.shortly.ui.model.LinkHistoryListItem

class LinkHistoryAdapter :
    ListAdapter<LinkHistoryListItem, LinkHistoryAdapter.LinkHistoryViewHolder>(DiffCallback()) {

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
            tvOriginLink.text = item.shortLink.origin
            tvShortenLink.text = item.shortLink.shortLink

            if (item.isCopied) {
                tvCopyBtn.setBackgroundResource(R.drawable.btn_bg_copied)
                tvCopyBtn.text = holder.context.getString(R.string.btn_text_copied)
            } else {
                tvCopyBtn.setBackgroundResource(R.drawable.btn_bg_copy)
                tvCopyBtn.text = holder.context.getString(R.string.btn_text_copy)
            }

            ivRemoveHistory.setOnClickListener {
                onRemoveIconClickListener?.let { click ->
                    click(item.shortLink)
                }
            }
            tvCopyBtn.setOnClickListener {
                onCopyButtonClickListener?.let { click ->
                    click(item)
                }
            }
        }
    }

    private var onRemoveIconClickListener: ((ShortLink) -> Unit)? = null

    fun setOnRemoveIconClickListener(listener: (ShortLink) -> Unit) {
        onRemoveIconClickListener = listener
    }

    private var onCopyButtonClickListener: ((LinkHistoryListItem) -> Unit)? = null

    fun setOnCopyButtonClickListener(listener: (LinkHistoryListItem) -> Unit) {
        onCopyButtonClickListener = listener
    }

    class LinkHistoryViewHolder(itemView: View, val context: Context) :
        RecyclerView.ViewHolder(itemView)
}

class DiffCallback : DiffUtil.ItemCallback<LinkHistoryListItem>() {
    override fun areItemsTheSame(
        oldItem: LinkHistoryListItem,
        newItem: LinkHistoryListItem
    ): Boolean {
        return oldItem.shortLink == newItem.shortLink
    }

    override fun areContentsTheSame(
        oldItem: LinkHistoryListItem,
        newItem: LinkHistoryListItem
    ): Boolean {
        return oldItem == newItem
    }
}