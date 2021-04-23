package com.steventran.fgodb

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_fgo_main.*
import java.lang.ClassCastException
import java.lang.IllegalStateException

class FilterDialogFragment: DialogFragment() {

    internal lateinit var listener: FilterDialogListener

    private var classFilters = mutableListOf<String>()
    private var rarityFilters = mutableListOf<Int>()

    interface FilterDialogListener {
        fun onFilterClick(dialog: DialogFragment)
        fun onFilterCancel(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as FilterDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "")
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            builder.apply {
                setView(inflater.inflate(R.layout.dialog_filter, fragment_container))
                setPositiveButton(R.string.filter_text, DialogInterface.OnClickListener{ dialog, id ->
                    listener.onFilterClick(this@FilterDialogFragment)
                })
                setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialogInterface, i ->
                    listener.onFilterCancel(this@FilterDialogFragment)
                })
            }
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun onClickRarity(view: View) {
        if(view is CheckBox) {
            val checked = view.isChecked
            when (view.id) {
                R.id.checkbox_one_star -> {
                    if (checked) {
                        rarityFilters.add(1)
                    }
                    else {
                        rarityFilters.remove(1)
                    }
                }
                R.id.checkbox_two_star -> {
                    if (checked) {
                        rarityFilters.add(2)
                    }
                    else {
                        rarityFilters.remove(2)
                    }
                }
                R.id.checkbox_three_star -> {
                    if (checked) {
                        rarityFilters.add(3)
                    }
                    else {
                        rarityFilters.remove(3)
                    }
                }
                R.id.checkbox_four_star -> {
                    if (checked) {
                        rarityFilters.add(4)
                    }
                    else {
                        rarityFilters.remove(4)
                    }
                }
                R.id.checkbox_five_star -> {
                    if (checked) {
                        rarityFilters.add(5)
                    }
                    else {
                        rarityFilters.remove(5)
                    }
                }
                else -> {

                }
            }


        }
    }

}