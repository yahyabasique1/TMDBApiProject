package com.yahya.tmdbapiproject.view.moviesfeed

//import kotlinx.android.synthetic.main.fragment_movies_feed.*
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import com.yahya.tmdbapiproject.R
import com.yahya.tmdbapiproject.databinding.FragmentMoviesFeedBinding
import com.yahya.tmdbapiproject.di.injectViewModel
import com.yahya.tmdbapiproject.util.LOW_RATED
import com.yahya.tmdbapiproject.util.SORT_POPULAR
import com.yahya.tmdbapiproject.util.TOP_RATED
import com.yahya.tmdbapiproject.util.isNetworkAvailable
import com.yahya.tmdbapiproject.view.moviesfeed.adapter.LoaderStateAdapter
import com.yahya.tmdbapiproject.view.moviesfeed.adapter.MoviesFeedAdapter
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.launch
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MoviesFeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@ExperimentalPagingApi
class MoviesFeedFragment : DaggerFragment(), AdapterView.OnItemSelectedListener {
    lateinit var loaderStateAdapter: LoaderStateAdapter

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var moviesFeedVM: MoviesFeedVM

    val moviesFeedAdapter = MoviesFeedAdapter()

    private var SORT_BY = SORT_POPULAR

    var _moviesFeedBinding: FragmentMoviesFeedBinding? = null

    val moviesFeedBinding get() = _moviesFeedBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _moviesFeedBinding = FragmentMoviesFeedBinding.inflate(inflater, container, false)

        return moviesFeedBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            activity?.getColor(R.color.colorPrimaryDark)
                ?.let { activity?.window?.setStatusBarColor(it) }
        }
        if (isNetworkAvailable(requireContext()!!).not()) {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show()
        }

        setHasOptionsMenu(true)
        initMembers()
        setUpViews()
        setUpSortSpinner()
        fetchMovies(SORT_BY)
    }


    private fun initMembers() {
        moviesFeedVM = injectViewModel(viewModelFactory)

        loaderStateAdapter = LoaderStateAdapter {
            moviesFeedAdapter.retry()
        }

    }

    private fun setUpViews() {
        moviesFeedBinding.rvMoviesFeedLoader.layoutManager = GridLayoutManager(context, 2)
        moviesFeedBinding.rvMoviesFeedLoader.adapter =
            moviesFeedAdapter.withLoadStateFooter(loaderStateAdapter)
    }


    private fun setUpSortSpinner() {
        moviesFeedBinding.sortSpinner.setOnItemSelectedListener(this)

        val mutableList = listOf<String>("Popular", "Low Rated", "Top rated")

        val aa: ArrayAdapter<String> = object : ArrayAdapter<String>(
            requireContext()!!,
            R.layout.spinner_course_frag_item,
            mutableList
        ) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                return super.getDropDownView(position, convertView, parent).also { view ->
                    if (position == moviesFeedBinding.sortSpinner.selectedItemPosition)
                        view.setBackgroundColor(Color.parseColor("#ededed"))
                }
            }
        }
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moviesFeedBinding.sortSpinner.setAdapter(aa);


    }

    fun fetchMovies(SORT_BY: String) {
        lifecycleScope.launch {
            moviesFeedVM.fetchMoviesList(isNetworkAvailable(requireContext()), SORT_BY)
                .observe(viewLifecycleOwner, Observer {
                    moviesFeedAdapter.submitData(lifecycle = lifecycle, pagingData = it)
                })

        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        SORT_BY = when (p2) {
            0 -> SORT_POPULAR
            1 -> TOP_RATED
            2 -> LOW_RATED
            else -> SORT_POPULAR
        }
        fetchMovies(SORT_BY)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        _moviesFeedBinding = null
        super.onDestroyView()
    }
}
