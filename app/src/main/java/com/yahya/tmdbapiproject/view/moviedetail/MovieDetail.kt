package com.yahya.tmdbapiproject.view.moviedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.yahya.tmdbapiproject.databinding.FragmentMovieDetailBinding
import com.yahya.tmdbapiproject.di.injectViewModel
import com.yahya.tmdbapiproject.extensions.LinePagerIndicatorDecoration
import com.yahya.tmdbapiproject.extensions.loadUrl
import com.yahya.tmdbapiproject.repository.network.MoviesData
import com.yahya.tmdbapiproject.view.moviedetail.adapter.MovieDetailAdapter
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@ExperimentalPagingApi
class MovieDetail : DaggerFragment() {

    private val args: MovieDetailArgs by navArgs()

    lateinit var moviesData: MoviesData

    lateinit var movoeDetailVM: MovoeDetailVM

    var _fragmentMovieDetailBinding: FragmentMovieDetailBinding? = null
    val fragmentMovieDetailBinding get() = _fragmentMovieDetailBinding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moviesData = args.MoviesDetail

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _fragmentMovieDetailBinding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return fragmentMovieDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDataMembers()
        setUpViews()
        fetchMovieImages()
    }

    private fun initDataMembers() {
        movoeDetailVM = injectViewModel(viewModelFactory)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

    }

    private fun setUpViews() {
        moviesData?.backdropPath?.let {
            fragmentMovieDetailBinding.ivBannerImage.loadUrl("https://image.tmdb.org/t/p/w342/${it}")

        }
        fragmentMovieDetailBinding.rvMovieImages.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        try {
            val date1 = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(
                moviesData?.releaseDate ?: ""
            )
            val format: DateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)
            fragmentMovieDetailBinding.tvReleaseDate.setText(format.format(date1!!))
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        fragmentMovieDetailBinding.tvDescription.text = moviesData.overview
        fragmentMovieDetailBinding.tvMovieTitle.text = moviesData.title
        fragmentMovieDetailBinding.rbMovieRating.rating = moviesData.vote_average.toFloat() / 2f
        fragmentMovieDetailBinding.ivBack.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }

    private fun fetchMovieImages() {
        CoroutineScope(Dispatchers.Main).launch {
            movoeDetailVM.getMovieImages(moviesData.id)
                .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    it?.let {
                        fragmentMovieDetailBinding.rvMovieImages.adapter =
                            MovieDetailAdapter(
                                context = requireContext()!!,
                                items = it.posters.map {
                                    it.filePath
                                })
                        fragmentMovieDetailBinding.rvMovieImages.addItemDecoration(
                            LinePagerIndicatorDecoration()
                        )

                    }
                })
        }

    }

    override fun onDestroyView() {
        _fragmentMovieDetailBinding=null
        super.onDestroyView()
    }
}