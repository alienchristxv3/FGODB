package com.steventran.fgodb

import android.os.Bundle
import androidx.fragment.app.Fragment

private const val TAG = "DetailedServantFragment"
private const val ARG_SERVANT_COLLECTION_NO = "servant"
class DetailedServantFragment: Fragment() {
    companion object {
        fun newInstance(servant: Servant): DetailedServantFragment {
            val args = Bundle().apply {
                putInt(ARG_SERVANT_COLLECTION_NO, servant.collectionNo)
            }
            return DetailedServantFragment().apply {
                arguments = args
            }
        }
    }
}