package com.example.myapplication.bottom_sheets

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityBottomSheetExampleBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class BottomSheetExampleActivity : AppCompatActivity(), CalendarBottomSheetDialogFragment.OnClickListener {

    lateinit var binding: ActivityBottomSheetExampleBinding

    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var calendarBottomSheetDialogFragment: CalendarBottomSheetDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomSheetExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inititializeBottomSheetDialog()
        initBottomsheetDialogFragment()

        binding.btnShowBottomSheet.setOnClickListener {
//            bottomSheetDialog.show()
            calendarBottomSheetDialogFragment.show(supportFragmentManager, "CalendarBottomSheet")
        }
    }

    private fun initBottomsheetDialogFragment() {
        calendarBottomSheetDialogFragment= CalendarBottomSheetDialogFragment()
    }

    private fun inititializeBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(this@BottomSheetExampleActivity, R.style.BottomSheetTheme)
        bottomSheetDialog.setCancelable(false)
        val view = layoutInflater.inflate(R.layout.dialog_bottom_sheet, null)
        val tv4=view.findViewById<TextView>(R.id.textView4)

        tv4.setOnClickListener {
            Toast.makeText(this@BottomSheetExampleActivity,"Email Link clicked", Toast.LENGTH_LONG).show()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(view)
    }

    override fun onViewClick(title: String) {
//        calendarBottomSheetDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme)
        calendarBottomSheetDialogFragment.dismiss()
        Toast.makeText(this@BottomSheetExampleActivity,"$title clicked", Toast.LENGTH_LONG).show()
    }
}