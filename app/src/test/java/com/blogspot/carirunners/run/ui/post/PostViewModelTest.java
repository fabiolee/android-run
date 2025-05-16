package com.blogspot.carirunners.run.ui.post;

import com.blogspot.carirunners.run.repository.PostRepository;
import com.blogspot.carirunners.run.vo.Post;
import com.blogspot.carirunners.run.vo.Resource;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(JUnit4.class)
public class PostViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private PostRepository repository;
    private PostViewModel postViewModel;

    @Before
    public void setup() {
        repository = mock(PostRepository.class);
        postViewModel = new PostViewModel(repository);
    }


    @Test
    public void testNull() {
        assertThat(postViewModel.getPost(), notNullValue());
        verify(repository, never()).loadByPath(anyString());
        verify(repository, never()).load(anyString());
    }

    @Test
    public void dontFetchWithoutObservers() {
        postViewModel.setId(null, "b");
        verify(repository, never()).loadByPath(anyString());
        postViewModel.setId("a", null);
        verify(repository, never()).load(anyString());
    }

    @Test
    public void fetchWhenObserved() {
        ArgumentCaptor<String> id = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> path = ArgumentCaptor.forClass(String.class);
        postViewModel.getPost().observeForever(mock(Observer.class));

        postViewModel.setId(null, "b");
        verify(repository, times(1)).loadByPath(path.capture());
        assertThat(path.getValue(), is("b"));
        postViewModel.setId("a", null);
        verify(repository, times(1)).load(id.capture());
        assertThat(id.getValue(), is("a"));
    }

    @Test
    public void changeWhileObserved() {
        ArgumentCaptor<String> id = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> path = ArgumentCaptor.forClass(String.class);
        postViewModel.getPost().observeForever(mock(Observer.class));

        postViewModel.setId(null, "b");
        postViewModel.setId(null, "d");
        verify(repository, times(2)).loadByPath(path.capture());
        assertThat(path.getAllValues(), is(Arrays.asList("b", "d")));
        postViewModel.setId("a", null);
        postViewModel.setId("c", null);
        verify(repository, times(2)).load(id.capture());
        assertThat(id.getAllValues(), is(Arrays.asList("a", "c")));
    }

    @Test
    public void resetId() {
        Observer<PostViewModel.PostId> observer = mock(Observer.class);
        postViewModel.postId.observeForever(observer);
        verifyNoMoreInteractions(observer);
        postViewModel.setId("foo", "bar");
        verify(observer).onChanged(new PostViewModel.PostId("foo", "bar"));
        reset(observer);
        postViewModel.setId("foo", "bar");
        verifyNoMoreInteractions(observer);
        postViewModel.setId("a", "b");
        verify(observer).onChanged(new PostViewModel.PostId("a", "b"));
    }

    @Test
    public void retry() {
        postViewModel.retry();
        verifyNoMoreInteractions(repository);
        postViewModel.setId(null, "bar");
        verifyNoMoreInteractions(repository);
        Observer<Resource<Post>> observer = mock(Observer.class);
        postViewModel.getPost().observeForever(observer);
        verify(repository).loadByPath("bar");
        reset(repository);
        postViewModel.retry();
        verify(repository).loadByPath("bar");
    }

    @Test
    public void nullPostId() {
        postViewModel.setId(null, null);
        Observer<Resource<Post>> observer = mock(Observer.class);
        postViewModel.getPost().observeForever(observer);
        verify(observer).onChanged(null);
    }
}