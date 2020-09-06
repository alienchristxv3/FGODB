package com.steventran.fgodb

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
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


        ascension1Button.setOnClickListener(onAscensionButtonClicked(ascension1Button))
        ascension2Button.setOnClickListener(onAscensionButtonClicked(ascension2Button))
        ascension3Button.setOnClickListener(onAscensionButtonClicked(ascension3Button))
        ascension4Button.setOnClickListener(onAscensionButtonClicked(ascension4Button))
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

            }
        )
    }

    fun onAscensionButtonClicked(view: View): View.OnClickListener? {
        return View.OnClickListener {
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
        }
    }
    private fun insertImageIntoView(view: ImageView, url: String) {
        Picasso.get()
            .load(url)
            .into(view)
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