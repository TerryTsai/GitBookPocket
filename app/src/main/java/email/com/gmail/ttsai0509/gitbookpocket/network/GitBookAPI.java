package email.com.gmail.ttsai0509.gitbookpocket.network;

import email.com.gmail.ttsai0509.gitbookpocket.model.author.Author;
import email.com.gmail.ttsai0509.gitbookpocket.model.book.Book;
import email.com.gmail.ttsai0509.gitbookpocket.model.common.Page;
import email.com.gmail.ttsai0509.gitbookpocket.service.Const;
import retrofit2.Call;
import retrofit2.http.*;
import email.com.gmail.ttsai0509.gitbookpocket.model.content.Content;

public interface GitBookAPI {

    @FormUrlEncoded
    @POST("/oauth/access_token")
    Call<GitBookToken> getAccessToken(
            @Field("code") String code,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("grant_type") String grantType);

    @GET("/books")
    Call<Page<Book>> getMyBooks(
            @Query("page") int page);

    @GET("/books/all")
    Call<Page<Book>> getAllBooks(
            @Query("page") int page);

    @GET("/book/{username}/{name}")
    Call<Book> getOneBook(
            @Path("username") String username,
            @Path("name") String name);

    @GET("/book/{username}/{name}/contents/{file}")
    Call<Content> getBookContentsFile(
            @Path("username") String username,
            @Path("name") String name,
            @Path("file") String file);

    @GET("/book/{username}/{name}/contents/{language}/{file}")
    Call<Content> getBookContentsFileLanguage(
            @Path("username") String username,
            @Path("name") String name,
            @Path("language") String language,
            @Path("file") String file);

    @GET("/author/{username}")
    Call<Author> getAuthor(
            @Path("username") String username);

    @GET("/author/{username}/books")
    Call<Page<Book>> getAuthorBooks(
            @Path("username") String username,
            @Query("page") int page);

}
