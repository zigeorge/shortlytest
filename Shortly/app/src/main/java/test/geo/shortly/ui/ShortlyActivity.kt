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
import test.geo.shortly.R
import test.geo.shortly.data.local.ShortLink
import test.geo.shortly.data.remote.responses.ShortLinkResponse
import test.geo.shortly.databinding.ActivityShortlyBinding
import test.geo.shortly.other.Resource
import test.geo.shortly.other.Status
import test.geo.shortly.ui.adapters.LinkHistoryAdapter
import test.geo.shortly.ui.viewmodel.ShortlyViewModel
import javax.inject.Inject

/*
* Represents an Android activity
* The ShortlyActivity enables user to enter a link in an editText and a button to shorten the input
* It also shows a list of already shorten links from API and shows a splash to get start when no link
* had been shorten using this application
* User can also copy a shorten link from the list to their device's clipboard shown and delete it
* form the list
* */

@AndroidEntryPoint
class ShortlyActivity : AppCompatActivity() {

    lateinit var viewModel: ShortlyViewModel        /* made public to test activity */

    @Inject lateinit var linkHistoryAdapter: LinkHistoryAdapter     /* added dependency in singleton module */

    @Inject lateinit var loadingView: LoadingView   /* added dependency in singleton module */

    private lateinit var binding: ActivityShortlyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShortlyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ShortlyViewModel::class.java]

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

        binding.tvShortenLink.setOnClickListener {
            closeKeyboard()
            viewModel.addShortLink(binding.etUrlToShort.text.toString())
        }
        binding.etUrlToShort.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(
                textView: TextView?,
                id: Int,
                keyEvent: KeyEvent?
            ): Boolean {
                if (id == EditorInfo.IME_ACTION_DONE) {
                    closeKeyboard()
                    viewModel.addShortLink(binding.etUrlToShort.text.toString())
                    return true
                }
                return false
            }
        })
        binding.etUrlToShort.setOnClickListener {
            resetEditText()
        }

    }

    /*
    * Reset the et_url_to_short editText view
    * */
    private fun resetEditText() {
        binding.etUrlToShort.setBackgroundResource(R.drawable.bg_url_edit)
        binding.etUrlToShort.setHintTextColor(Color.parseColor("#9E9AA7"))
        binding.etUrlToShort.setHint(R.string.hint_shorten_a_link_here)
    }

    /*
    * Handle the status of the request to create a short link to interact with the user properly
    * */
    private fun handleStatus(resource: Resource<ShortLinkResponse>?) {
        when (resource?.status) {
            Status.ERROR -> handleError(resource)
            Status.LOADING -> showLoading()
            Status.SUCCESS -> {
                binding.etUrlToShort.setText("")
                linkHistoryAdapter.updateCopiedPosition()
            }
            else -> showSnackBar(getString(R.string.failed_to_shorten))
        }

        if(resource?.status != Status.LOADING) {
            hideLoading()
        }
    }

    /*
    * Show history of links user had shorten using the app so far
    * @param list: List<ShortLink> a list of ShortLink obtain from DB, that was previously stored there
    * */
    private fun showHistory(list: List<ShortLink>) {
        if(binding.includeGetStarted.clGetStarted.isVisible) {
            binding.includeGetStarted.clGetStarted.visibility = GONE
            binding.includeLinkHistory.clLinkHistory.visibility = VISIBLE
        }
        linkHistoryAdapter.submitList(list.reversed())

    }

    /*
    * Show a splash screen that shows a get start graphic
    * */
    private fun showGetStarted() {
        if(binding.includeGetStarted.clGetStarted.isVisible)
            return
        binding.includeGetStarted.clGetStarted.visibility = VISIBLE
        binding.includeLinkHistory.clLinkHistory.visibility = GONE
    }

    /*
    * handle any error occurs during the process of shortening a link and interact with user
    * accordingly
    * */
    private fun handleError(resource: Resource<ShortLinkResponse>?) {
        when (resource?.message) {
            LinkError.EMPTY_LINK.msg -> {
                binding.etUrlToShort.setBackgroundResource(R.drawable.bg_url_edit_error)
                binding.etUrlToShort.setHintTextColor(Color.parseColor("#F46262"))
                binding.etUrlToShort.setHint(R.string.empty_link_error)
            }
            LinkError.INVALID_LINK.msg -> {
                binding.etUrlToShort.setBackgroundResource(R.drawable.bg_url_edit_error)
                showSnackBar(LinkError.INVALID_LINK.msg)
            }
            LinkError.NO_INTERNET.msg -> showSnackBar(resource.message)
            else -> {
                showSnackBar(resource?.data?.error ?: getString(R.string.failed_to_shorten))
            }
        }
    }

    /*
    * shows a snackBar
    * @param text: String is the given text to show in the snackBar
    * */
    private fun showSnackBar(text: String) {
        Snackbar.make(
            findViewById(R.id.main),
            text,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    /*
    * closes the keyboard for currently focused view
    * */
    private fun closeKeyboard() {
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /*
    * set a recyclerView to show link history in a list
    * */
    private fun setRecyclerView() {
        binding.includeLinkHistory.rvLinkHistory.apply {
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

    /*
    * Copies a link
    * @param link: ShortLink is the shortLink instance user tries to copy
    * */
    private fun copyLink(link: ShortLink) {
        val clipboard: ClipboardManager? =
            ContextCompat.getSystemService(
                this,
                ClipboardManager::class.java
            )
        val clip = ClipData.newPlainText(link.origin, link.shortLink)
        clipboard?.setPrimaryClip(clip)
    }

    private fun showLoading() {
        loadingView.show()
    }

    private fun hideLoading() {
        loadingView.hide()
    }
}

/*
* Represents an enum class of different type of errors that may occur during the process of
* shortening a link
* */
enum class LinkError(val msg: String) {
    EMPTY_LINK("Empty Link"),
    INVALID_LINK("Invalid Link"),
    NO_INTERNET("No Internet Connection")
}