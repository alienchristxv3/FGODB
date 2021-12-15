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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_fgo_main.*
import java.lang.ClassCastException
import java.lang.IllegalStateException
private val rarityIDs: List<Int> = listOf(
    R.id.checkbox_one_star,
    R.id.checkbox_two_star,
    R.id.checkbox_three_star,
    R.id.checkbox_four_star,
    R.id.checkbox_five_star)
private val classNames: List<String> = listOf(
    "saber",
    "archer",
    "lancer",
    "rider",
    "assassin",
    "caster",
    "beserker",
    "ruler",
    "avenger",
    "moonCancer",
    "alterEgo",
    "foreigner",
    "shielder"
)
class FilterDialogFragment: DialogFragment() {

    private lateinit var listener: FilterDialogListener
    private var rarityButtons: MutableList<CheckBox> = mutableListOf()
    private var classBoxes: MutableList<CheckBox> = mutableListOf()


    var classFilters = mutableListOf<String>()
    var rarityFilters = mutableListOf<Int>()

    interface FilterDialogListener {
        fun onFilterClick(dialog: FilterDialogFragment)
        fun onFilterCancel(dialog: FilterDialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)


        listener = parentFragment as FilterDialogListener

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

    override fun onStart() {
        super.onStart()
        // Creating references to rarity checkboxes
        rarityIDs.forEach {
            rarityButtons.add(dialog?.findViewById(it) as CheckBox)
        }
        // Making it so that onClickRarity works with these
        rarityButtons.forEach {
            it.apply {
                setOnClickListener { onClickRarity(this) }
            }
        }
        val layout = dialog?.findViewById(R.id.filter_layout) as ConstraintLayout
        classNames.forEach {
            classBoxes.add(layout.findViewWithTag(it) as CheckBox)
        }
        classBoxes.forEach {
            it.apply {
                setOnClickListener {
                    onClickClass(this)
                }
            }
        }
    }

    // Refactor to use similar functionality to onClickClass
    // Maybe combine into one function w/ another parameter for the list that it goes into?
    private fun onClickRarity(view: View) {
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
                    // Does nothing
                }
            }
        }
    }
    // Adds or removes the checkboxes' class from the list using tag when clicked
    private fun onClickClass(view: View) {
        if (view is CheckBox) {
            if (view.isChecked) {
                classFilters.add(view.tag.toString())
            }
            else {
                classFilters.remove(view.tag.toString())
            }
        }
    }

}