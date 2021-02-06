package com.yahya.tmdbapiproject.repository.network
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class PopularMoviesResponse(
        @SerializedName("page")
    var page: Int,
        @SerializedName("results")
    var moviesData: List<MoviesData>,
        @SerializedName("total_pages")
    var totalPages: Int,
        @SerializedName("total_results")
    var totalResults: Int
):Serializable
@Entity
data class MoviesData(
    @PrimaryKey
    @SerializedName("id")
    var id: Long,
    @SerializedName("adult")
    var adult: Boolean=false,
    @SerializedName("backdrop_path")
    var backdropPath: String?="",
    @SerializedName("original_language")
    var originalLanguage: String?="",
    @SerializedName("original_title")
    var originalTitle: String?="",
    @SerializedName("overview")
    var overview: String?="",
    @SerializedName("popularity")
    var popularity: Double=0.0,
    @SerializedName("poster_path")
    var posterPath: String?="",
    @SerializedName("release_date")
    var releaseDate: String?="",
    @SerializedName("title")
    var title: String?="",
    @SerializedName("video")
    var video: Boolean=false,
    @SerializedName("vote_count")
    var voteCount: Int=0 ,
    @SerializedName("vote_average")
    var vote_average: Double=0.0
):Serializable

data class MovieImagesResponse(
    @SerializedName("backdrops")
    var backdrops: List<Backdrop>,
    @SerializedName("id")
    var id: Int,
    @SerializedName("posters")
    var posters: List<Poster>
)

data class Backdrop(
    @SerializedName("aspect_ratio")
    var aspectRatio: Double,
    @SerializedName("file_path")
    var filePath: String,
    @SerializedName("height")
    var height: Int,
    @SerializedName("iso_639_1")
    var iso6391: Any,
//    @SerializedName("vote_average")
//    var voteAverage: Int,
    @SerializedName("vote_count")
    var voteCount: Int,
    @SerializedName("width")
    var width: Int
)

data class Poster(
    @SerializedName("aspect_ratio")
    var aspectRatio: Double,
    @SerializedName("file_path")
    var filePath: String,
    @SerializedName("height")
    var height: Int,
    @SerializedName("iso_639_1")
    var iso6391: String,
//    @SerializedName("vote_average")
//    var voteAverage: Int,
    @SerializedName("vote_count")
    var voteCount: Int,
    @SerializedName("width")
    var width: Int
)