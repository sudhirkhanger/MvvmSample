package com.sudhirkhanger.networksample.ui.fourth

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
import com.sudhirkhanger.networksample.network.model.Status
import com.sudhirkhanger.networksample.utils.DefaultItemDecoration

class CountriesFragment : Fragment() {

    private var fragmentExpoBinding: FragmentCountriesBinding? = null

    companion object {
        @JvmStatic
        fun newInstance() = CountriesFragment()
    }

    private val viewModel: FourthActivityViewModel by activityViewModels {
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
        fragmentExpoBinding = binding
        return binding.root
    }

    override fun onDestroyView() {
        fragmentExpoBinding = null
        super.onDestroyView()
    }

    private fun setupUi() {
        viewModel.countriesLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> setUiData(it.data)
                Status.LOADING -> loading()
                Status.ERROR -> error(it.message ?: getString(R.string.unknown_error))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_expo, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh -> {
                if (isRefreshEnabled)
                    viewModel.getCountries()
                else
                    snackBar(getString(R.string.please_wait))?.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loading() {
        fragmentExpoBinding?.blockView?.visibility = View.VISIBLE
        fragmentExpoBinding?.progressBar?.visibility = View.VISIBLE
        fragmentExpoBinding?.statusTv?.visibility = View.VISIBLE
        fragmentExpoBinding?.statusTv?.text = getString(R.string.please_wait)
        isRefreshEnabled = false
    }

    private fun error(message: String) {
        fragmentExpoBinding?.blockView?.visibility = View.GONE
        fragmentExpoBinding?.progressBar?.visibility = View.GONE
        fragmentExpoBinding?.statusTv?.visibility = View.GONE
        isRefreshEnabled = true
        snackBar(message)?.show()
    }

    private fun success() {
        fragmentExpoBinding?.blockView?.visibility = View.GONE
        fragmentExpoBinding?.progressBar?.visibility = View.GONE
        fragmentExpoBinding?.statusTv?.visibility = View.GONE
        isRefreshEnabled = true
        fragmentExpoBinding?.countriesRv?.visibility = View.VISIBLE
        fragmentExpoBinding?.emptyView?.visibility = View.GONE
    }

    private fun noDataView() {
        fragmentExpoBinding?.blockView?.visibility = View.GONE
        fragmentExpoBinding?.progressBar?.visibility = View.GONE
        fragmentExpoBinding?.statusTv?.visibility = View.GONE
        isRefreshEnabled = true
        fragmentExpoBinding?.countriesRv?.visibility = View.GONE
        fragmentExpoBinding?.emptyView?.visibility = View.VISIBLE
    }

    private fun blockViewClickListener() {
        fragmentExpoBinding?.blockView?.setOnClickListener {
            snackBar(getString(R.string.please_wait))?.show()
        }
    }

    private fun snackBar(message: String): Snackbar? {
        fragmentExpoBinding?.mainLayout?.let {
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
        setupUi()
        blockViewClickListener()
        setUpSessionsRv()
        initSearchExpo()
        clearSearch()
    }

    private fun clearSearch() {
        fragmentExpoBinding?.cancelBtn?.setOnClickListener {
            fragmentExpoBinding?.searchView?.text = null
            viewModel.queryCountries("")
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
        fragmentExpoBinding?.countriesRv?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
            adapter = countriesAdapter
        }
        fragmentExpoBinding?.countriesRv?.addItemDecoration(
            DefaultItemDecoration(
                resources.getDimensionPixelSize(R.dimen.dimen_8dp),
                resources.getDimensionPixelSize(R.dimen.dimen_8dp)
            )
        )
    }

    private fun setUiData(response: CountriesResponse?) {
        if (response?.data.isNullOrEmpty()) {
            noDataView()
        } else {
            countriesAdapter.submitList(response?.data)
            success()
        }
    }

    private fun initSearchExpo() {
        fragmentExpoBinding?.searchView?.addTextChangedListener(
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
                        s?.let { viewModel.queryCountries(s.toString()) }
                    }, 300)
                }
            }
        )
    }
}