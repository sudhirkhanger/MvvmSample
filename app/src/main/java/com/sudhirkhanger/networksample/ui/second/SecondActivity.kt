package com.sudhirkhanger.networksample.ui.second

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.sudhirkhanger.networksample.NetworkSampleComponent
import com.sudhirkhanger.networksample.databinding.ActivitySecondBinding
import com.sudhirkhanger.networksample.ui.third.ThirdActivity

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    private val viewModel: SecondViewModel by viewModels {
        NetworkSampleComponent.provideMainViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Second Activity"

        binding.nextBtn.setOnClickListener {
            startActivity(Intent(this, ThirdActivity::class.java))
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