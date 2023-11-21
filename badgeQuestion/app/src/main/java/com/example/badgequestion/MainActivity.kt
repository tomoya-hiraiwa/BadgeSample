package com.example.badgequestion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.badgequestion.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[ListViewModel::class.java]
        val fileName = "events_data.json"
        val inputStream = assets.open(fileName)
        val file = File(filesDir,fileName)
        if (!file.exists()){
            file.outputStream().use {inputStream.copyTo(it) }
        }
        val rail = binding.rail
        val listBadge = rail.getOrCreateBadge(R.id.menu_list)
        listBadge.isVisible = true
        rail.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_list ->{
                    changeFragment(ListFragment())
                    true
                }
                R.id.menu_other ->{
                    changeFragment(OtherFragment())
                    true
                }
                else -> false
            }
        }
        lifecycleScope.launch {
            viewModel.getData(file)

            listBadge.number = viewModel.badgeCount.value ?: 0
        }
        viewModel.badgeCount.observe(this){count ->
            listBadge.number = count
            if(count == 0) listBadge.isVisible = false
        }
    }
    private fun changeFragment(fm: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView,fm)
            .commit()
    }
}