package com.steventran.fgodb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class FgoMainActivity : AppCompatActivity(), ServantListFragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fgo_main)

        val isFragmentContainerEmpty = savedInstanceState == null
        if (isFragmentContainerEmpty) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, ServantListFragment.newInstance())
                .commit()
        }
    }

    override fun onServantSelected(servantId: Int) {

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, DetailedServantFragment.newInstance(servantId))
            .addToBackStack(null)
            .commit()
    }

}