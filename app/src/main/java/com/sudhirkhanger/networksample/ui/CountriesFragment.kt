package com.sudhirkhanger.networksample.ui

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sudhirkhanger.networksample.NetworkSampleComponent
import com.sudhirkhanger.networksample.R
import com.sudhirkhanger.networksample.databinding.FragmentCountriesBinding
import com.sudhirkhanger.networksample.network.model.NetworkStatus
import com.sudhirkhanger.networksample.utils.DefaultItemDecoration
import com.sudhirkhanger.networksample.utils.EventObserver

class CountriesFragment : Fragment() {

    private var fragmentCountriesBinding: FragmentCountriesBinding? = null

    companion object {
        @JvmStatic
        fun newInstance() = CountriesFragment()
    }

    private val viewModel: MainActivityViewModel by activityViewModels {
        NetworkSampleComponent.provideMainViewModelFactory()
    }

    private var isRefreshEnabled = true
    private lateinit var countriesAdapter: CountriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCountriesBinding.inflate(inflater, container, false)
        fragmentCountriesBinding = binding
        return binding.root
    }

    override fun onDestroyView() {
        fragmentCountriesBinding = null
        super.onDestroyView()
    }

    private fun setUpUi() {
        viewModel.countries.observe(viewLifecycleOwner) {
            if (it.isEmpty())
                noDataView()
            else
                loadData(it)
        }

        viewModel.networkState.observe(viewLifecycleOwner, EventObserver {
            when (it.status) {
                NetworkStatus.SUCCESS -> success()
                NetworkStatus.RUNNING -> loading()
                NetworkStatus.FAILED -> error(it.msg ?: "null")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh -> {
                if (isRefreshEnabled)
                    viewModel.refresh()
                else
                    snackBar(getString(R.string.please_wait))?.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loading() {
        isRefreshEnabled = false
        fragmentCountriesBinding?.apply {
            blockView.isVisible = true
            progressBar.isVisible = true
            statusTv.isVisible = true
            statusTv.text = getText(R.string.please_wait)
        }
    }

    private fun error(message: String) {
        isRefreshEnabled = true

        fragmentCountriesBinding?.apply {
            blockView.isGone = true
            progressBar.isGone = true
            statusTv.isGone = true
        }

        snackBar(message)?.show()
    }

    private fun success() {
        isRefreshEnabled = true

        fragmentCountriesBinding?.apply {
            blockView.isGone = true
            progressBar.isGone = true
            statusTv.isGone = true
        }
    }

    private fun noDataView() {
        fragmentCountriesBinding?.apply {
            countriesRv.isGone = true
            emptyView.isVisible = true
        }
    }

    private fun loadData(countries: List<Country?>) {
        fragmentCountriesBinding?.apply {
            countriesRv.isVisible = true
            emptyView.isGone = true
            countriesAdapter.submitList(countries)
        }
    }

    private fun blockViewClickListener() {
        fragmentCountriesBinding?.blockView?.setOnClickListener {
            snackBar(getString(R.string.please_wait))?.show()
        }
    }

    private fun snackBar(message: String): Snackbar? {
        fragmentCountriesBinding?.mainLayout?.let {
            return Snackbar.make(
                it,
                message,
                Snackbar.LENGTH_SHORT
            )
        }
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSessionsRv()
        setUpUi()
        blockViewClickListener()
        initSearchExpo()
        clearSearch()
    }

    private fun clearSearch() {
        fragmentCountriesBinding?.cancelBtn?.setOnClickListener {
            fragmentCountriesBinding?.searchView?.text = null
            hideKeyboard(it)
            viewModel.clearSearch()
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setUpSessionsRv() {
        countriesAdapter = CountriesAdapter {
            snackBar("Open Item")?.show()
        }
        fragmentCountriesBinding?.countriesRv?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
            adapter = countriesAdapter
        }
        fragmentCountriesBinding?.countriesRv?.addItemDecoration(
            DefaultItemDecoration(
                resources.getDimensionPixelSize(R.dimen.dimen_8dp),
                resources.getDimensionPixelSize(R.dimen.dimen_8dp)
            )
        )
    }

    private fun initSearchExpo() {
        if (fragmentCountriesBinding?.searchView?.text.isNullOrEmpty())
            fragmentCountriesBinding?.cancelBtn?.isGone = true
        fragmentCountriesBinding?.searchView?.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        s?.let {
                            if (it.isBlank()) {
                                fragmentCountriesBinding?.cancelBtn?.isGone = true
                                viewModel.clearSearch()
                            } else {
                                fragmentCountriesBinding?.cancelBtn?.isVisible = true
                                viewModel.search(s.toString().trim())
                            }
                        }
                    }, 300)
                }
            }
        )
    }
}