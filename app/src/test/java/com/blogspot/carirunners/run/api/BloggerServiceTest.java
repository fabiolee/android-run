package com.blogspot.carirunners.run.api;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.blogspot.carirunners.run.util.LiveDataCallAdapterFactory;
import com.blogspot.carirunners.run.util.LiveDataTestUtil;
import com.blogspot.carirunners.run.vo.Post;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class BloggerServiceTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private BloggerService service;

    private MockWebServer mockWebServer;

    @Before
    public void createService() throws IOException {
        mockWebServer = new MockWebServer();
        service = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(BloggerService.class);
    }

    @After
    public void stopService() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void search() throws IOException, InterruptedException {
        enqueueResponse("search.json");
        ApiResponse<PostSearchResponse> response = LiveDataTestUtil.getValue(
                service.searchPosts("cari"));

        assertThat(response, notNullValue());
        assertThat(response.body, notNullValue());
        assertThat(response.body.getNextPageToken(), is("CgkIChjisu72ricQ-pesirL1iaR9"));
        assertThat(response.body.getItems().size(), is(10));

        Post post = response.body.getItems().get(0);
        assertThat(post.id.length(), not(0));
        assertThat(post.title.length(), not(0));
        assertThat(post.content.length(), not(0));
    }

    private void enqueueResponse(String fileName) throws IOException {
        enqueueResponse(fileName, Collections.emptyMap());
    }

    private void enqueueResponse(String fileName, Map<String, String> headers) throws IOException {
        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("api-response/" + fileName);
        BufferedSource source = Okio.buffer(Okio.source(inputStream));
        MockResponse mockResponse = new MockResponse();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            mockResponse.addHeader(header.getKey(), header.getValue());
        }
        mockWebServer.enqueue(mockResponse
                .setBody(source.readString(StandardCharsets.UTF_8)));
    }
}
