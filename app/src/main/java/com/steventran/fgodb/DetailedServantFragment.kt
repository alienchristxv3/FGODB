package com.steventran.fgodb

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private lateinit var detailedServantViewModel: DetailedServantViewModel
    private lateinit var servantNameTextView: TextView
    private lateinit var servantPortraitImageView: ImageView
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
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailedServantViewModel.detailedServantLiveData.observe(
            viewLifecycleOwner,
            Observer {servant ->
                servantNameTextView.text = servant.servantName
                insertImageIntoView(
                    servantPortraitImageView,
                    servant.characterAscensionUrls[0],
                    R.drawable.mash_portrait)
            }
        )
    }
    private fun insertImageIntoView(view: ImageView, url: String, @DrawableRes placeholderInt: Int) {
        Picasso.get()
            .load(url)
            .placeholder(placeholderInt)
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