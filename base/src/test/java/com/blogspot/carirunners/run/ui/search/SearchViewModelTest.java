package com.blogspot.carirunners.run.ui.search;


import com.blogspot.carirunners.run.repository.PostRepository;
import com.blogspot.carirunners.run.repository.RepoRepository;
import com.blogspot.carirunners.run.vo.Post;
import com.blogspot.carirunners.run.vo.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class SearchViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantExecutor = new InstantTaskExecutorRule();
    private SearchViewModel viewModel;
    private PostRepository postRepository;
    private RepoRepository repoRepository;

    @Before
    public void init() {
        postRepository = mock(PostRepository.class);
        repoRepository = mock(RepoRepository.class);
        viewModel = new SearchViewModel(postRepository, repoRepository);
    }

    @Test
    public void empty() {
        Observer<Resource<List<Post>>> result = mock(Observer.class);
        viewModel.getResults().observeForever(result);
        viewModel.loadNextPage();
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    public void basic() {
        Observer<Resource<List<Post>>> result = mock(Observer.class);
        viewModel.getResults().observeForever(result);
        viewModel.setQuery("foo");
        verify(postRepository).search("foo");
        verify(postRepository, never()).searchNextPage("foo");
    }

    @Test
    public void noObserverNoQuery() {
        when(postRepository.searchNextPage("foo")).thenReturn(mock(LiveData.class));
        viewModel.setQuery("foo");
        verify(postRepository, never()).search("foo");
        // next page is user interaction and even if loading state is not observed, we query
        // would be better to avoid that if main search query is not observed
        viewModel.loadNextPage();
        verify(postRepository).searchNextPage("foo");
    }

    @Test
    public void swap() {
        LiveData<Resource<Boolean>> nextPage = new MutableLiveData<>();
        when(postRepository.searchNextPage("foo")).thenReturn(nextPage);

        Observer<Resource<List<Post>>> result = mock(Observer.class);
        viewModel.getResults().observeForever(result);
        verifyNoMoreInteractions(postRepository);
        viewModel.setQuery("foo");
        verify(postRepository).search("foo");
        viewModel.loadNextPage();

        viewModel.getLoadMoreStatus().observeForever(mock(Observer.class));
        verify(postRepository).searchNextPage("foo");
        assertThat(nextPage.hasActiveObservers(), is(true));
        viewModel.setQuery("bar");
        assertThat(nextPage.hasActiveObservers(), is(false));
        verify(postRepository).search("bar");
        verify(postRepository, never()).searchNextPage("bar");
    }

    @Test
    public void refresh() {
        viewModel.refresh();
        verifyNoMoreInteractions(postRepository);
        viewModel.setQuery("foo");
        viewModel.refresh();
        verifyNoMoreInteractions(postRepository);
        viewModel.getResults().observeForever(mock(Observer.class));
        verify(postRepository).search("foo");
        reset(postRepository);
        viewModel.refresh();
        verify(postRepository).search("foo");
    }

    @Test
    public void resetSameQuery() {
        viewModel.getResults().observeForever(mock(Observer.class));
        viewModel.setQuery("foo");
        verify(postRepository).search("foo");
        reset(postRepository);
        viewModel.setQuery("FOO");
        verifyNoMoreInteractions(postRepository);
        viewModel.setQuery("bar");
        verify(postRepository).search("bar");
    }
}