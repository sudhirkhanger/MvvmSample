package com.sudhirkhanger.networksample.ui.first

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.sudhirkhanger.networksample.NetworkSampleComponent
import com.sudhirkhanger.networksample.databinding.ActivityFirstBinding
import com.sudhirkhanger.networksample.ui.second.SecondActivity

class FirstActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstBinding

    private val viewModel: FirstViewModel by viewModels {
        NetworkSampleComponent.provideMainViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "First Activity"

        binding.nextBtn.setOnClickListener {
            startActivity(Intent(this, SecondActivity::class.java))
        }

        binding.incrementBtn.setOnClickListener {
            viewModel.increment()
        }

        viewModel.counterLiveData.observe(this, Observer {
            binding.counterTv.text = it.toString()
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}