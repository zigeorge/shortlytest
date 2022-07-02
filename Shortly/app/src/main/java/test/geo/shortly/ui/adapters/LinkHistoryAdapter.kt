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


/*
* Represents a ListAdapter class that inflates list_item_link_history and sets ShortLink instances
* in a recyclerView
* */
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
            tvShortLink.text = item.shortLink

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
                setLatestCopiedPosition(holder.adapterPosition)
            }
        }
    }

    /*
    * Sets the newly copied position in @param latestCopiedPositions and notify the recyclerView
    * adapter about the changed position.
    * */
    private fun setLatestCopiedPosition(adapterPosition: Int) {
        latestCopiedPosition = if(latestCopiedPosition == -1) {
            adapterPosition
        } else {
            notifyItemChanged(latestCopiedPosition)
            adapterPosition
        }
        notifyItemChanged(adapterPosition)
    }

    /* Represents the latest copied position */
    private var latestCopiedPosition = -1

    /*
    * increment the latestCopiedPosition by one when a new item is added. All new item is pushed at
    * the top of the list. Hence, if the latest copied item position was at 0th position, it'll be
    * pushed to the 1st position. This function helps the recyclerView to incorporate this behaviour
    * */
    fun updateCopiedPosition() {
        if (latestCopiedPosition == -1) {
            return
        }
        latestCopiedPosition++
    }

    /* Represents the listener for when user taps on the bin icon to delete an item from the list */
    private var onRemoveIconClickListener: ((ShortLink) -> Unit)? = null

    /* Sets onRemoveIconClickListener */
    fun setOnRemoveIconClickListener(listener: (ShortLink) -> Unit) {
        onRemoveIconClickListener = listener
    }

    /* Represents the listener for when user taps on copy button to copy a shorten link to clipboard */
    private var onCopyButtonClickListener: ((ShortLink) -> Unit)? = null

    /* Sets onCopuButtonClickListener */
    fun setOnCopyButtonClickListener(listener: (ShortLink) -> Unit) {
        onCopyButtonClickListener = listener
    }

    class LinkHistoryViewHolder(itemView: View, val context: Context) :
        RecyclerView.ViewHolder(itemView)
}


/*
* Represents a DiffUtil.ItemCallback of type ShortLink
* ** DiffUtil.ItemCallback helps ListAdapter to add new item in recyclerView and identifies if there
* ** is any change in the submitted list
* The ListHistoryDiff is type casted with ShortLink so that the DiffUtil can identify changes in a
* given List of shortLinks
* */
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