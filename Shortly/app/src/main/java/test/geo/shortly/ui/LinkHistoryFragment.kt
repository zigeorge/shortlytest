package test.geo.shortly.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_link_history.*
import test.geo.shortly.R
import test.geo.shortly.ui.adapters.LinkHistoryAdapter
import test.geo.shortly.ui.viewmodel.ShortlyViewModel

class LinkHistoryFragment : Fragment(R.layout.fragment_link_history) {

    private lateinit var viewModel: ShortlyViewModel

    private val linkHistoryAdapter = LinkHistoryAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ShortlyViewModel::class.java]

        viewModel.linkHistoryUpdated()
        setRecyclerView()
        viewModel.linkHistory.observe(requireActivity()) {
            linkHistoryAdapter.submitList(it)
        }

    }

    private fun setRecyclerView() {
        rvLinkHistory.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
            )
            adapter = linkHistoryAdapter
        }
        linkHistoryAdapter.setOnRemoveIconClickListener {
            viewModel.deleteLnk(it)
        }

        linkHistoryAdapter.setOnCopyButtonClickListener {
            copyLink(it.shortLink.shortLink)
            viewModel.linkHistoryUpdated(it)
        }
    }

    private fun copyLink(link: String) {
        val clipboard: ClipboardManager? =
            ContextCompat.getSystemService(
                requireContext(),
                ClipboardManager::class.java
            )
        val clip = ClipData.newPlainText(link, link)
        clipboard?.setPrimaryClip(clip)
    }

}