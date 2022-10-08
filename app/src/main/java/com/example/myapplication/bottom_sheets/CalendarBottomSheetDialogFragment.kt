package com.example.myapplication.bottom_sheets

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.databinding.CalendarBottomSheetDialogFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CalendarBottomSheetDialogFragment : BottomSheetDialogFragment() {

    interface OnClickListener {
        fun onViewClick(title: String)
    }

    private var clickListener: OnClickListener? = null

    private lateinit var binding: CalendarBottomSheetDialogFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CalendarBottomSheetDialogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textView2.setOnClickListener {
            clickListener?.onViewClick(binding.textView2.text.toString())
        }

        binding.textView3.setOnClickListener {
            clickListener?.onViewClick(binding.textView3.text.toString())
        }
        binding.textView4.setOnClickListener {
            clickListener?.onViewClick(binding.textView4.text.toString())
        }
        binding.textView5.setOnClickListener {
            clickListener?.onViewClick(binding.textView5.text.toString())
        }
        binding.textView6.setOnClickListener {
            clickListener?.onViewClick(binding.textView6.text.toString())
        }

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            if (context is BottomSheetExampleActivity)
                clickListener = context
        } catch (e: Exception) {
            throw ClassCastException("")
        }
    }
}