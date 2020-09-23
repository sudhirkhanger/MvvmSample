package com.sudhirkhanger.networksample.ui

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
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

    private fun setupUi() {
        viewModel.countries.observe(viewLifecycleOwner) {
            countriesAdapter.submitList(it)
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
        fragmentCountriesBinding?.blockView?.visibility = View.VISIBLE
        fragmentCountriesBinding?.progressBar?.visibility = View.VISIBLE
        fragmentCountriesBinding?.statusTv?.visibility = View.VISIBLE
        fragmentCountriesBinding?.statusTv?.text = getString(R.string.please_wait)
        isRefreshEnabled = false
    }

    private fun error(message: String) {
        fragmentCountriesBinding?.blockView?.visibility = View.GONE
        fragmentCountriesBinding?.progressBar?.visibility = View.GONE
        fragmentCountriesBinding?.statusTv?.visibility = View.GONE
        isRefreshEnabled = true
        snackBar(message)?.show()
    }

    private fun success() {
        fragmentCountriesBinding?.blockView?.visibility = View.GONE
        fragmentCountriesBinding?.progressBar?.visibility = View.GONE
        fragmentCountriesBinding?.statusTv?.visibility = View.GONE
        isRefreshEnabled = true
        fragmentCountriesBinding?.countriesRv?.visibility = View.VISIBLE
        fragmentCountriesBinding?.emptyView?.visibility = View.GONE
    }

    private fun noDataView() {
        fragmentCountriesBinding?.blockView?.visibility = View.GONE
        fragmentCountriesBinding?.progressBar?.visibility = View.GONE
        fragmentCountriesBinding?.statusTv?.visibility = View.GONE
        isRefreshEnabled = true
        fragmentCountriesBinding?.countriesRv?.visibility = View.GONE
        fragmentCountriesBinding?.emptyView?.visibility = View.VISIBLE
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
        setupUi()
        blockViewClickListener()
        initSearchExpo()
        clearSearch()
    }

    private fun clearSearch() {
        fragmentCountriesBinding?.cancelBtn?.setOnClickListener {
            fragmentCountriesBinding?.searchView?.text = null
            hideKeyboard(it)
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
                                viewModel.clearSearch()
                            } else {
                                viewModel.search(s.toString())
                            }
                        }
                    }, 300)
                }
            }
        )
    }
}