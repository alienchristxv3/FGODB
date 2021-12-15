package com.steventran.fgodb

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

private const val TAG = "FgoFragment"

class ServantListFragment : Fragment(), FilterDialogFragment.FilterDialogListener {
    private lateinit var servantListViewModel: ServantListViewModel
    private lateinit var servantRecyclerView: RecyclerView
    private lateinit var classFilters: List<String>
    private lateinit var rarityFilter: List<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        servantListViewModel =
            ViewModelProvider(this).get(ServantListViewModel::class.java)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fgodb, container, false)
        servantRecyclerView = view.findViewById(R.id.servant_recycler_view)
        servantRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        servantListViewModel.servantLiveData.observe(
            viewLifecycleOwner,
            Observer { servants ->
                servantRecyclerView.adapter = ServantAdapter(servants)
            })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.servant_search_menu, menu)
        val searchView = menu.findItem(R.id.action_search_servant).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val servantAdapter = servantRecyclerView.adapter as ServantAdapter
                servantAdapter.filter.filter(query)

                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                val servantAdapter = servantRecyclerView.adapter as ServantAdapter
                servantAdapter.filter.filter(query)

                return false
            }

        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.action_filter -> {
                createFilterDialog()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    override fun onFilterClick(dialog: FilterDialogFragment) {
        classFilters = dialog.classFilters
        rarityFilter = dialog.rarityFilters
        val servantAdapter = servantRecyclerView.adapter as ServantAdapter
        servantAdapter.filter.filter("")
        servantAdapter.notifyDataSetChanged()

    }

    override fun onFilterCancel(dialog: FilterDialogFragment) {
        classFilters = emptyList()
        rarityFilter = emptyList()
    }

    private fun createFilterDialog() {
        val dialog = FilterDialogFragment()
        dialog.show(childFragmentManager, "FilterDialogFragment")
    }

    private inner class ServantHolder(view: View): RecyclerView.ViewHolder(view) {


        private val servantNameTextView: TextView = view.findViewById(R.id.servant_name)
        private val servantRarityImageView: ImageView = view.findViewById(R.id.servant_rarity)
        private val servantClassImageView: ImageView = view.findViewById(R.id.servant_class)
        private val servantFaceImageView: ImageView = view.findViewById(R.id.servant_face)


        fun bindServant(servant: Servant) {
            servantNameTextView.text = servant.servantName
            servantRarityImageView.setImageDrawable(
                ResourcesCompat.getDrawable(resources, selectRarity(servant), null)
            )
            servantClassImageView.setImageDrawable(
                ResourcesCompat.getDrawable(resources, selectClass(servant), null)
            )

            Picasso.get()
                .load(servant.faceUrl)
                .placeholder(R.drawable.unknown_servant)
                .into(servantFaceImageView)



        }

        private fun selectClass(servant: Servant): Int {
            if (servant.className == "shielder") {
                return R.drawable.class_shielder
            }
            return when {
                servant.rarity == 0 -> {
                    R.drawable.class_avenger_0
                }
                servant.rarity < 3 -> {
                    when (servant.className) {
                        "saber" -> R.drawable.class_saber_bronze
                        "archer" -> R.drawable.class_archer_bronze
                        "lancer" -> R.drawable.class_lancer_bronze
                        "rider" -> R.drawable.class_rider_bronze
                        "assassin" -> R.drawable.class_assassin_bronze
                        "caster" -> R.drawable.class_caster_bronze
                        "berserker" -> R.drawable.class_beserker_bronze
                        "ruler" -> R.drawable.class_ruler_bronze
                        "moonCancer" -> R.drawable.class_mooncancer_bronze
                        "alterEgo" -> R.drawable.class_alterego_bronze
                        else -> R.drawable.class_unknown
                    }
                }
                servant.rarity == 3 -> {
                    when (servant.className) {
                        "saber" -> R.drawable.class_saber_silver
                        "archer" -> R.drawable.class_archer_silver
                        "lancer" -> R.drawable.class_lancer_silver
                        "rider" -> R.drawable.class_rider_silver
                        "assassin" -> R.drawable.class_assassin_silver
                        "caster" -> R.drawable.class_caster_silver
                        "berserker" -> R.drawable.class_beserker_silver
                        "ruler" -> R.drawable.class_ruler_silver
                        "avenger" -> R.drawable.class_avenger_silver
                        "moonCancer" -> R.drawable.class_mooncancer_silver
                        "alterEgo" -> R.drawable.class_alterego_silver
                        else -> R.drawable.class_unknown
                    }
                }
                servant.rarity > 3 -> {
                    when (servant.className) {
                        "saber" -> R.drawable.class_saber_gold
                        "archer" -> R.drawable.class_archer_gold
                        "lancer" -> R.drawable.class_lancer_gold
                        "rider" -> R.drawable.class_rider_gold
                        "assassin" -> R.drawable.class_assassin_gold
                        "caster" -> R.drawable.class_caster_gold
                        "berserker" -> R.drawable.class_beserker_gold
                        "ruler" -> R.drawable.class_ruler_gold
                        "avenger" -> R.drawable.class_avenger_gold
                        "moonCancer" -> R.drawable.class_mooncancer_gold
                        "alterEgo" -> R.drawable.class_alterego_gold
                        "foreigner" -> R.drawable.class_foreigner_gold
                        else -> R.drawable.class_unknown
                    }
                }
                else -> {
                    R.drawable.class_unknown
                }
            }

        }

        private fun selectRarity(servant: Servant): Int {

            return when (servant.rarity) {
                    1 -> R.drawable.star1
                    2 -> R.drawable.star2
                    3 -> R.drawable.star3
                    4 -> R.drawable.star4
                    5 -> R.drawable.star5
                    else -> R.drawable.star1
                }
        }
    }
    private inner class ServantAdapter(private val servants: List<Servant>) : RecyclerView.Adapter<ServantHolder>(), Filterable {
        private var filteredServants: List<Servant> = servants

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServantHolder {
            val view = layoutInflater.inflate(
                R.layout.list_item_servants,
                parent,
                false)
            return ServantHolder(view)
        }

        override fun getItemCount(): Int = filteredServants.size


        override fun onBindViewHolder(holder: ServantHolder, position: Int) {
            val servant = filteredServants[position]
            holder.bindServant(servant)
        }

        override fun getFilter() : Filter {
            return object: Filter() {
                override fun performFiltering(charSequence: CharSequence?): FilterResults {

                    filteredServants = if(charSequence.isNullOrBlank()) {
                        servants
                    } else {
                        var filteredList = mutableListOf<Servant>()
                        servants.forEach {servant: Servant ->
                            if(servant.servantName.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                if (rarityFilter.isEmpty() && classFilters.isEmpty()) {
                                    filteredList.add(servant)
                                }
                                else {
                                    if (rarityFilter.isEmpty() && classFilters.isNotEmpty()) {
                                        if (classFilters.contains(servant.className)) {
                                            filteredList.add(servant)
                                        }
                                    }
                                    else if (classFilters.isEmpty() && rarityFilter.isNotEmpty()) {
                                        if (rarityFilter.contains(servant.rarity)) {
                                            filteredList.add(servant)
                                        }
                                    }
                                    else {
                                        if (rarityFilter.contains(servant.rarity) &&
                                            classFilters.contains(servant.className)) {
                                            filteredList.add(servant)
                                        }
                                    }
                                }
                            }
                        }
                        filteredList
                    }



                    val filteredResults = FilterResults()
                    filteredResults.values = filteredServants

                    return filteredResults
                }

                override fun publishResults(charSeq: CharSequence?, filteredResults: FilterResults?) {
                    if (filteredResults != null) {
                        filteredServants = filteredResults.values as List<Servant>
                    }
                    notifyDataSetChanged()
                }

            }
        }

    }


    companion object {
        fun newInstance() = ServantListFragment()
    }
}