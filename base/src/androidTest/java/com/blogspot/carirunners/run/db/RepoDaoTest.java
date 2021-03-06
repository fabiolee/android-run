package com.blogspot.carirunners.run.db;

import com.blogspot.carirunners.run.util.LiveDataTestUtil;
import com.blogspot.carirunners.run.util.TestUtil;
import com.blogspot.carirunners.run.vo.Contributor;
import com.blogspot.carirunners.run.vo.Repo;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.arch.lifecycle.LiveData;
import android.database.sqlite.SQLiteException;
import android.support.test.runner.AndroidJUnit4;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class RepoDaoTest extends DbTest {
    @Test
    public void insertAndRead() throws InterruptedException {
        Repo repo = TestUtil.createRepo("foo", "bar", "desc");
        db.repoDao().insert(repo);
        Repo loaded = LiveDataTestUtil.getValue(db.repoDao().load("foo", "bar"));
        assertThat(loaded, notNullValue());
        assertThat(loaded.name, is("bar"));
        assertThat(loaded.description, is("desc"));
        assertThat(loaded.owner, notNullValue());
        assertThat(loaded.owner.login, is("foo"));
    }

    @Test
    public void insertContributorsWithoutRepo() {
        Repo repo = TestUtil.createRepo("foo", "bar", "desc");
        Contributor contributor = TestUtil.createContributor(repo, "c1", 3);
        try {
            db.repoDao().insertContributors(Collections.singletonList(contributor));
            throw new AssertionError("must fail because repo does not exist");
        } catch (SQLiteException ex) {
        }
    }

    @Test
    public void insertContributors() throws InterruptedException {
        Repo repo = TestUtil.createRepo("foo", "bar", "desc");
        Contributor c1 = TestUtil.createContributor(repo, "c1", 3);
        Contributor c2 = TestUtil.createContributor(repo, "c2", 7);
        db.beginTransaction();
        try {
            db.repoDao().insert(repo);
            db.repoDao().insertContributors(Arrays.asList(c1, c2));
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        List<Contributor> list = LiveDataTestUtil.getValue(db.repoDao()
                .loadContributors("foo", "bar"));
        assertThat(list.size(), is(2));
        Contributor first = list.get(0);

        assertThat(first.getLogin(), is("c2"));
        assertThat(first.getContributions(), is(7));

        Contributor second = list.get(1);
        assertThat(second.getLogin(), is("c1"));
        assertThat(second.getContributions(), is(3));
    }

    @Test
    public void createIfNotExists_exists() throws InterruptedException {
        Repo repo = TestUtil.createRepo("foo", "bar", "desc");
        db.repoDao().insert(repo);
        assertThat(db.repoDao().createRepoIfNotExists(repo), is(-1L));
    }

    @Test
    public void createIfNotExists_doesNotExist() {
        Repo repo = TestUtil.createRepo("foo", "bar", "desc");
        assertThat(db.repoDao().createRepoIfNotExists(repo), is(1L));
    }

    @Test
    public void insertContributorsThenUpdateRepo() throws InterruptedException {
        Repo repo = TestUtil.createRepo("foo", "bar", "desc");
        db.repoDao().insert(repo);
        Contributor contributor = TestUtil.createContributor(repo, "aa", 3);
        db.repoDao().insertContributors(Collections.singletonList(contributor));
        LiveData<List<Contributor>> data = db.repoDao().loadContributors("foo", "bar");
        assertThat(LiveDataTestUtil.getValue(data).size(), is(1));

        Repo update = TestUtil.createRepo("foo", "bar", "desc");
        db.repoDao().insert(update);
        data = db.repoDao().loadContributors("foo", "bar");
        assertThat(LiveDataTestUtil.getValue(data).size(), is(1));
    }
}
