package com.steventran.fgodb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class FgoMainActivity : AppCompatActivity() {
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


}