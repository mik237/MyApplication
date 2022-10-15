package com.example.myapplication.stick_header_recycler_view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnLayout
import androidx.core.view.updateLayoutParams
import com.example.myapplication.databinding.ActivityStickyHeaderBinding
import com.example.myapplication.utils.Extensions.dpToPx

class StickyHeaderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStickyHeaderBinding

    private val itemsAdapter: StickyAdapter by lazy { StickyAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStickyHeaderBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnAdd.doOnLayout {


            val screenWidth = resources.displayMetrics.widthPixels
            val addBtnMargin = 20.dpToPx
            val addBtnWidth = it.width

            Log.d("TAG__", "ScreenWidth: $screenWidth")
            Log.d("TAG__", "addBtnMargin: $addBtnMargin")
            Log.d("TAG__", "addBtnWidth: $addBtnWidth")

            binding.rvItems.updateLayoutParams<ConstraintLayout.LayoutParams> {
                matchConstraintMaxWidth = screenWidth - addBtnWidth - addBtnMargin
            }
        }

        binding.rvItems.apply {
            adapter = itemsAdapter
        }

    }



}