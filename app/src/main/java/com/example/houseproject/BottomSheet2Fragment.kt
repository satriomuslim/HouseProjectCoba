package com.example.houseproject

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.houseproject.databinding.FragmentBottomSheet2Binding
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheet2Fragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheet2Binding? = null
    private val binding get() = _binding!!

    private lateinit var buttonLayoutParams: ConstraintLayout.LayoutParams
    private lateinit var adapter: Adapter

    private var collapsedMargin: Int = 0
    private var buttonHeight: Int = 0
    private var expandedHeight: Int = 0

    companion object {
        fun newInstance(): BottomSheet2Fragment {
            return BottomSheet2Fragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBottomSheet2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = Adapter(initString())
        binding.sheetRecyclerview.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            adapter = this@BottomSheet2Fragment.adapter
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener {
            setupRatio(dialog as BottomSheetDialog)
        }

        (dialog as BottomSheetDialog).behavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset > 0 && slideOffset < 1) { // Sliding happens from 0 (Collapsed) to 1 (Expanded) - if so, calculate margins
                    buttonLayoutParams.topMargin =
                        (((expandedHeight - buttonHeight) - collapsedMargin) * slideOffset + collapsedMargin).toInt()
                } else if (slideOffset == 0f) { // If not sliding above expanded, set initial margin
                    buttonLayoutParams.topMargin = collapsedMargin
                }
                binding.sheetButton.layoutParams = buttonLayoutParams // Set layout params to button (margin from top)
            }
        })

        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<FrameLayout>(R.id.design_bottom_sheet) ?: return

        //Retrieve button parameters
        buttonLayoutParams = binding.sheetButton.layoutParams as ConstraintLayout.LayoutParams

        //Retrieve bottom sheet parameters
        BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED
        val bottomSheetLayoutParams = bottomSheet.layoutParams
        bottomSheetLayoutParams.height = getBottomSheetDialogDefaultHeight()

        expandedHeight = bottomSheetLayoutParams.height
        val peekHeight = (expandedHeight / 1.3).toInt() //Peek height to 70% of expanded height (Change based on your view)

        //Setup bottom sheet
        bottomSheet.layoutParams = bottomSheetLayoutParams
        BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED
        BottomSheetBehavior.from(bottomSheet).peekHeight = peekHeight
        BottomSheetBehavior.from(bottomSheet).isHideable = true

        //Calculate button margin from top
        buttonHeight = binding.sheetButton.height + 40 //How tall is the button + experimental distance from bottom (Change based on your view)
        collapsedMargin = peekHeight - buttonHeight //Button margin in bottom sheet collapsed state
        buttonLayoutParams.topMargin = collapsedMargin
        binding.sheetButton.layoutParams = buttonLayoutParams

        //OPTIONAL - Setting up margins
        val recyclerLayoutParams =
            binding.sheetRecyclerview.layoutParams as ConstraintLayout.LayoutParams
        val k = (buttonHeight - 60) / buttonHeight.toFloat() //60 is amount that you want to be hidden behind button
        recyclerLayoutParams.bottomMargin = (k * buttonHeight).toInt() //Recyclerview bottom margin (from button)
        binding.sheetRecyclerview.layoutParams = recyclerLayoutParams
    }

    //Calculates height for 90% of fullscreen
    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight() * 90 / 100
    }

    //Calculates window height for fullscreen use
    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        (requireContext() as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    private fun initString(): List<String> {
        val list: MutableList<String> = ArrayList()
        for (i in 0..34) list.add("Item $i")
        return list
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.sheetRecyclerview.adapter = null
    }

}