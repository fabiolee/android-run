package com.blogspot.carirunners.run.db;

import android.support.test.runner.AndroidJUnit4;

import com.blogspot.carirunners.run.util.LiveDataTestUtil;
import com.blogspot.carirunners.run.util.TestUtil;
import com.blogspot.carirunners.run.vo.Post;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class PostDaoTest extends DbTest {
    @Test
    public void insertAndRead() throws InterruptedException {
        Post post = TestUtil.createPost("2013-05-27T11:16:00+08:00",
                "2013-05-27T11:16:11+08:00",
                "http://carirunners.blogspot.com/2013/05/kul-imu-charity-run-2013.html",
                "[KUL] IMU Charity Run 2013", "HTML content",
                Arrays.asList("kuala lumpur", "race", "road"));
        db.postDao().insert(post);
        List<Post> loadedAll = LiveDataTestUtil.getValue(db.postDao().load());
        assertThat(loadedAll, notNullValue());
        Post loadedPost = loadedAll.get(0);
        assertThat(loadedPost.title, is("[KUL] IMU Charity Run 2013"));
        assertThat(loadedPost.content, is("HTML content"));
        assertThat(loadedPost.labels, notNullValue());
        assertThat(loadedPost.labels.size(), is(3));
    }

    @Test
    public void deleteAndRead() throws InterruptedException {
        List<Post> posts = TestUtil.createPost(3, "2013-05-27T11:16:00+08:00",
                "2013-05-27T11:16:11+08:00",
                "http://carirunners.blogspot.com/2013/05/kul-imu-charity-run-2013.html",
                "[KUL] IMU Charity Run 2013", "HTML content",
                Arrays.asList("kuala lumpur", "race", "road"));
        db.postDao().insert(posts);
        db.postDao().delete();
        List<Post> loadedAll = LiveDataTestUtil.getValue(db.postDao().load());
        assertThat(loadedAll, notNullValue());
        assertThat(loadedAll.size(), is(0));
    }
}
