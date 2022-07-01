package test.geo.shortly.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_shortly.*
import kotlinx.android.synthetic.main.get_started.*
import kotlinx.android.synthetic.main.link_history.*
import kotlinx.android.synthetic.main.link_history.rvLinkHistory
import test.geo.shortly.R
import test.geo.shortly.data.remote.responses.ShortLinkResponse
import test.geo.shortly.other.Resource
import test.geo.shortly.other.Status
import test.geo.shortly.ui.adapters.LinkHistoryAdapter
import test.geo.shortly.ui.model.LinkHistoryListItem
import test.geo.shortly.ui.viewmodel.ShortlyViewModel

@AndroidEntryPoint
class ShortlyActivity : AppCompatActivity() {

    private lateinit var viewModel: ShortlyViewModel

    private val linkHistoryAdapter = LinkHistoryAdapter()

    private lateinit var loadingView: LoadingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shortly)

        viewModel = ViewModelProvider(this)[ShortlyViewModel::class.java]

        viewModel.getLinkHistory()

        loadingView = LoadingView.createLoading(this)

        viewModel.linkHistory.observe(this) {
            if (!it.isNullOrEmpty()) showHistory(it)
            else showGetStarted()
        }

        viewModel.addShortLinkStatus.observe(this) {
            val resource = it.getContentIfNotHandled()
            handleStatus(resource)
        }

        setRecyclerView()

        tvShortenLink.setOnClickListener {
            closeKeyboard()
            viewModel.addShortLink(et_url_to_short.text.toString())
        }
        et_url_to_short.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(
                textView: TextView?,
                id: Int,
                keyEvent: KeyEvent?
            ): Boolean {
                if (id == EditorInfo.IME_ACTION_DONE) {
                    closeKeyboard()
                    viewModel.addShortLink(et_url_to_short.text.toString())
                    return true
                }
                return false
            }
        })
        et_url_to_short.setOnClickListener {
            resetEditText()
        }

    }

    private fun resetEditText() {
        et_url_to_short.setBackgroundResource(R.drawable.bg_url_edit)
        et_url_to_short.setHintTextColor(Color.parseColor("#9E9AA7"))
        et_url_to_short.setHint(R.string.hint_shorten_a_link_here)
    }

    private fun handleStatus(resource: Resource<ShortLinkResponse>?) {
        when (resource?.status) {
            Status.ERROR -> handleError(resource)
            Status.LOADING -> showLoading()
            Status.SUCCESS -> et_url_to_short.setText("")
            else -> showSnackBar(getString(R.string.failed_to_shorten))
        }

        if(resource?.status != Status.LOADING) {
            hideLoading()
        }
    }

    private fun showHistory(list: List<LinkHistoryListItem>) {
        if(clGetStarted.isVisible) {
            clGetStarted.visibility = GONE
            clLinkHistory.visibility = VISIBLE
        }
        linkHistoryAdapter.submitList(list)

    }

    private fun showGetStarted() {
        if(clGetStarted.isVisible)
            return
        clGetStarted.visibility = VISIBLE
        clLinkHistory.visibility = GONE
    }

    private fun handleError(resource: Resource<ShortLinkResponse>?) {
        when (resource?.message) {
            LinkError.EMPTY_LINK.msg -> {
                et_url_to_short.setBackgroundResource(R.drawable.bg_url_edit_error)
                et_url_to_short.setHintTextColor(Color.parseColor("#F46262"))
                et_url_to_short.setHint(R.string.empty_link_error)
            }
            LinkError.INVALID_LINK.msg -> {
                et_url_to_short.setBackgroundResource(R.drawable.bg_url_edit_error)
                showSnackBar(LinkError.INVALID_LINK.msg)
            }
            LinkError.NO_INTERNET.msg -> showSnackBar(resource.message)
            else -> {
                showSnackBar(resource?.data?.error ?: getString(R.string.failed_to_shorten))
            }
        }
    }

    private fun showSnackBar(text: String) {
        Snackbar.make(
            findViewById(R.id.main),
            text,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun closeKeyboard() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun setRecyclerView() {
        rvLinkHistory.apply {
            layoutManager = LinearLayoutManager(
                this@ShortlyActivity,
            )
            adapter = linkHistoryAdapter
        }
        linkHistoryAdapter.setOnRemoveIconClickListener {
            viewModel.deleteLnk(it)
        }

        linkHistoryAdapter.setOnCopyButtonClickListener {
            copyLink(it)
        }
    }

    private fun copyLink(link: LinkHistoryListItem) {
        val clipboard: ClipboardManager? =
            ContextCompat.getSystemService(
                this,
                ClipboardManager::class.java
            )
        val clip = ClipData.newPlainText(link.shortLink.origin, link.shortLink.shortLink)
        clipboard?.setPrimaryClip(clip)
        viewModel.copyLink(link)
    }

    private fun showLoading() {
        loadingView.show()
    }

    private fun hideLoading() {
        loadingView.hide()
    }
}

enum class LinkError(val msg: String) {
    EMPTY_LINK("Empty Link"),
    INVALID_LINK("Invalid Link"),
    NO_INTERNET("No Internet Connection")
}