package email.com.gmail.ttsai0509.gitbookpocket.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import email.com.gmail.ttsai0509.gitbookpocket.model.explore.Explore;
import email.com.gmail.ttsai0509.gitbookpocket.model.member.Members;
import email.com.gmail.ttsai0509.gitbookpocket.model.search.Search;
import email.com.gmail.ttsai0509.gitbookpocket.model.starred.Starred;
import email.com.gmail.ttsai0509.gitbookpocket.model.common.Language;
import email.com.gmail.ttsai0509.gitbookpocket.model.common.ExploreSort;
import email.com.gmail.ttsai0509.gitbookpocket.model.common.SearchSort;
import email.com.gmail.ttsai0509.gitbookpocket.model.common.Type;

public interface GitBookSite {

    @GET("/search")
    Call<Search> searchBooks(
            @Query("q") String query,
            @Query("page") int page,
            @Query("type") Type type,
            @Query("sort") SearchSort sort);

    @GET("/explore")
    Call<Explore> exploreBooks(
            @Query("page") int page,
            @Query("lang") Language lang);

    @GET("/explore/topic/{topic}")
    Call<Explore> exploreTopic(
            @Path("topic") String topic,
            @Query("page") int page,
            @Query("lang") Language lang,
            @Query("sort") ExploreSort sort);

    @GET("/@{username}/starred")
    Call<Starred> getStarredBooks(
            @Path("username") String username,
            @Query("page") int page);

    @GET("/@{username}/members")
    Call<Members> getMembers(
            @Path("username") String username);

}
