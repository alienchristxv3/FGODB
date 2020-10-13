package com.steventran.fgodb

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.steventran.fgodb.api.DetailedSkill
import kotlin.properties.Delegates

private const val TAG = "DetailedServantFragment"
private const val ARG_SERVANT_COLLECTION_NO = "servant"
class DetailedServantFragment: Fragment() {

    private lateinit var detailedServant: DetailedServant
    private lateinit var detailedServantViewModel: DetailedServantViewModel
    private lateinit var servantNameTextView: TextView
    private lateinit var servantPortraitImageView: ImageView
    private lateinit var ascension1Button: Button
    private lateinit var ascension2Button: Button
    private lateinit var ascension3Button: Button
    private lateinit var ascension4Button: Button
    private lateinit var ascensionButtonList: List<Button>
    private lateinit var skillRecyclerView: RecyclerView

    private var servantCollectionNo by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailedServantViewModel = ViewModelProvider(this)
            .get(DetailedServantViewModel::class.java)
        servantCollectionNo = arguments?.getInt(ARG_SERVANT_COLLECTION_NO) as Int
        detailedServantViewModel.getServant(servantCollectionNo)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detailed_servant, container, false)
        servantNameTextView = view.findViewById(R.id.servant_name)
        servantPortraitImageView = view.findViewById(R.id.servant_portrait)
        ascension1Button = view.findViewById(R.id.ascension_1_button)
        ascension2Button = view.findViewById(R.id.ascension_2_button)
        ascension3Button = view.findViewById(R.id.ascension_3_button)
        ascension4Button = view.findViewById(R.id.ascension_4_button)
        ascensionButtonList = listOf(
            ascension1Button,
            ascension2Button,
            ascension3Button,
            ascension4Button
        )


        ascensionButtonList.forEach { button ->
            button.setOnClickListener(onAscensionButtonClicked(button))
        }
        skillRecyclerView = view.findViewById(R.id.skill_recycler_view)
        skillRecyclerView.layoutManager = LinearLayoutManager(context)
        skillRecyclerView.addItemDecoration(
            VerticalSkillPaddingDecorationItem(
            24 * (context?.resources?.displayMetrics?.density?.toInt() ?: 1)
        )
        )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailedServantViewModel.detailedServantLiveData.observe(
            viewLifecycleOwner,
            Observer {servant ->
                this.detailedServant = servant
                servantNameTextView.text = servant.servantName
                insertImageIntoView(
                    servantPortraitImageView,
                    servant.characterAscensionUrls[0]
                    )

                skillRecyclerView.adapter = SkillAdapter(servant.skills)


            }
        )
    }

    fun onAscensionButtonClicked(view: View): View.OnClickListener? {


        return View.OnClickListener {
            ascensionButtonList.forEach { button ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    button.backgroundTintList = ColorStateList.valueOf(
                        resources.getColor(R.color.colorUnselectedAscensionButton,
                            context?.theme
                        ))
                }
            }
            when (view.id) {
                R.id.ascension_1_button -> insertImageIntoView(servantPortraitImageView,
                        detailedServant.characterAscensionUrls[0])
                R.id.ascension_2_button -> insertImageIntoView(servantPortraitImageView,
                    detailedServant.characterAscensionUrls[1])
                R.id.ascension_3_button -> insertImageIntoView(servantPortraitImageView,
                    detailedServant.characterAscensionUrls[2])
                R.id.ascension_4_button -> insertImageIntoView(servantPortraitImageView,
                    detailedServant.characterAscensionUrls[3])
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorAscensionButton, context?.theme
                ))
            }
        }
    }
    private fun insertImageIntoView(view: ImageView, url: String) {
        Picasso.get()
            .load(url)
            .into(view)
    }
    private inner class SkillHolder(view: View): RecyclerView.ViewHolder(view) {

        private val skillIconView: ImageView = view.findViewById(R.id.skill_icon)
        private val skillNameTextView: TextView = view.findViewById(R.id.skill_name)
        private val skillDescriptionTextView: TextView = view.findViewById(R.id.skill_description)
        private val skillBuffTableLayout: TableLayout =
            view.findViewById(R.id.skill_buff_table_layout)

        fun bindSkill(detailedSkill: DetailedSkill) {
            insertImageIntoView(skillIconView, detailedSkill.iconUrl)
            skillNameTextView.text = detailedSkill.name
            skillDescriptionTextView.text = detailedSkill.description
            createBuffValueTable(detailedSkill, skillBuffTableLayout)



        }

    }

    private inner class SkillAdapter(val skills: List<DetailedSkill>): RecyclerView.Adapter<SkillHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillHolder {
            val view = layoutInflater.inflate(R.layout.list_item_skill, parent, false)
            return SkillHolder(view)
        }

        override fun onBindViewHolder(holder: SkillHolder, position: Int) {
            val skill: DetailedSkill = skills[position]
            holder.bindSkill(skill)


        }

        override fun getItemCount(): Int {
            return skills.size
        }

    }

    private inner class VerticalSkillPaddingDecorationItem(val verticalSpaceHeight: Int): RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.bottom = verticalSpaceHeight
        }
    }

    private fun createBuffValueTable(detailedSkill: DetailedSkill, skillBuffTableLayout: TableLayout)
    {
        val layoutParams: TableRow.LayoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        val cooldownsTableRow = TableRow(context).apply {
            this.layoutParams = layoutParams
            addView(TextView(context))
        }
        for(i in 0..9) {
            val coolDown = detailedSkill.coolDowns.get(i)
            val cooldownTextView = TextView(context)
            cooldownTextView.text = coolDown.toString()
            cooldownTextView.background = ResourcesCompat.getDrawable(
                resources, R.drawable.skill_border, context?.theme
            )
            cooldownTextView.layoutParams = TableRow.LayoutParams(i+1)
            cooldownsTableRow.addView(cooldownTextView)
        }
        skillBuffTableLayout.addView(cooldownsTableRow)
        detailedSkill.skillFunctions.forEach{skillFunction ->
            val skillTableRow = TableRow(context)

            val funcDescriptionTextView = TextView(context)
            funcDescriptionTextView.apply {
                background = ResourcesCompat.getDrawable(
                    resources, R.drawable.skill_border, context?.theme
                )
            }

            skillTableRow.layoutParams = layoutParams
            funcDescriptionTextView.text = skillFunction.functDescrip
            skillTableRow.addView(funcDescriptionTextView)
            skillFunction.skillValues.forEach { skillValue ->
                val skillValueTextView = TextView(context)
                skillValueTextView.text = if (skillValue.value  == 0) {
                    "-"
                } else {
                    skillValue.value.toString()
                }
                skillValueTextView.background = ResourcesCompat.getDrawable(
                    resources, R.drawable.skill_border, context?.theme
                )
                skillTableRow.addView(skillValueTextView)
            }

            skillBuffTableLayout.addView(skillTableRow)

        }
    }

    companion object {
        fun newInstance(servantId: Int): DetailedServantFragment {
            val args = Bundle().apply {
                putInt(ARG_SERVANT_COLLECTION_NO, servantId)
                Log.d(TAG, "Transferring over $servantId")
            }
            return DetailedServantFragment().apply {
                arguments = args
            }
        }
    }
}