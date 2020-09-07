package com.sudhirkhanger.networksample.ui.third

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.sudhirkhanger.networksample.NetworkSampleComponent
import com.sudhirkhanger.networksample.databinding.ActivityThirdBinding
import com.sudhirkhanger.networksample.ui.first.FirstViewModel
import com.sudhirkhanger.networksample.ui.fourth.FourthActivity
import com.sudhirkhanger.networksample.ui.second.SecondActivity

class ThirdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThirdBinding

    private val viewModel: ThirdViewModel by viewModels {
        NetworkSampleComponent.provideMainViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Third Activity"

        binding.nextBtn.setOnClickListener {
            startActivity(Intent(this, FourthActivity::class.java))
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