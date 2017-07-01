package com.blogspot.carirunners.run.ui.search;

import com.blogspot.carirunners.run.R;
import com.blogspot.carirunners.run.binding.FragmentBindingAdapters;
import com.blogspot.carirunners.run.testing.SingleFragmentActivity;
import com.blogspot.carirunners.run.ui.common.NavigationController;
import com.blogspot.carirunners.run.util.RecyclerViewMatcher;
import com.blogspot.carirunners.run.util.TaskExecutorWithIdlingResourceRule;
import com.blogspot.carirunners.run.util.TestUtil;
import com.blogspot.carirunners.run.util.ViewModelUtil;
import com.blogspot.carirunners.run.vo.Post;
import com.blogspot.carirunners.run.vo.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;

import java.util.Arrays;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class SearchFragmentTest {
    @Rule
    public ActivityTestRule<SingleFragmentActivity> activityRule =
            new ActivityTestRule<>(SingleFragmentActivity.class, true, true);
    @Rule
    public TaskExecutorWithIdlingResourceRule executorRule =
            new TaskExecutorWithIdlingResourceRule();

    private FragmentBindingAdapters fragmentBindingAdapters;
    private NavigationController navigationController;

    private SearchViewModel viewModel;

    private MutableLiveData<Resource<List<Post>>> results = new MutableLiveData<>();
    private MutableLiveData<SearchViewModel.LoadMoreState> loadMoreStatus = new MutableLiveData<>();

    @Before
    public void init() {
        SearchFragment searchFragment = new SearchFragment();
        viewModel = mock(SearchViewModel.class);
        when(viewModel.getLoadMoreStatus()).thenReturn(loadMoreStatus);
        when(viewModel.getResults()).thenReturn(results);

        fragmentBindingAdapters = mock(FragmentBindingAdapters.class);
        navigationController = mock(NavigationController.class);
        searchFragment.viewModelFactory = ViewModelUtil.createFor(viewModel);
        searchFragment.dataBindingComponent = () -> fragmentBindingAdapters;
        searchFragment.navigationController = navigationController;
        activityRule.getActivity().setFragment(searchFragment);
    }

    @Test
    public void search() {
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.input)).perform(typeText("foo"),
                pressKey(KeyEvent.KEYCODE_ENTER));
        verify(viewModel).setQuery("foo");
        results.postValue(Resource.loading(null));
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()));
    }

    @Test
    public void loadResults() {
        Post post = TestUtil.createPost("2013-05-27T11:16:00+08:00",
                "2013-05-27T11:16:11+08:00",
                "http://carirunners.blogspot.com/2013/05/kul-imu-charity-run-2013.html",
                "[KUL] IMU Charity Run 2013", "HTML content",
                Arrays.asList("kuala lumpur", "race", "road"));
        results.postValue(Resource.success(Arrays.asList(post)));
        onView(listMatcher().atPosition(0))
                .check(matches(hasDescendant(withText("[KUL] IMU Charity Run 2013"))));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
    }

    @Test
    public void dataWithLoading() {
        Post post = TestUtil.createPost("2013-05-27T11:16:00+08:00",
                "2013-05-27T11:16:11+08:00",
                "http://carirunners.blogspot.com/2013/05/kul-imu-charity-run-2013.html",
                "[KUL] IMU Charity Run 2013", "HTML content",
                Arrays.asList("kuala lumpur", "race", "road"));
        results.postValue(Resource.loading(Arrays.asList(post)));
        onView(listMatcher().atPosition(0))
                .check(matches(hasDescendant(withText("[KUL] IMU Charity Run 2013"))));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
    }

    @Test
    public void error() {
        results.postValue(Resource.error("failed to load", null));
        onView(withId(R.id.error_msg)).check(matches(isDisplayed()));
    }

    @Test
    public void loadMore() throws Throwable {
        List<Post> posts = TestUtil.createPost(50, "2013-05-27T11:16:00+08:00",
                "2013-05-27T11:16:11+08:00",
                "http://carirunners.blogspot.com/2013/05/kul-imu-charity-run-2013.html",
                "[KUL] IMU Charity Run 2013", "HTML content",
                Arrays.asList("kuala lumpur", "race", "road"));
        results.postValue(Resource.success(posts));
        onView(withId(R.id.repo_list)).perform(RecyclerViewActions.scrollToPosition(49));
        onView(listMatcher().atPosition(49)).check(matches(isDisplayed()));
        verify(viewModel).loadNextPage();
    }

    @Test
    public void navigateToRepo() throws Throwable {
        Post post = TestUtil.createPost("2013-05-27T11:16:00+08:00",
                "2013-05-27T11:16:11+08:00",
                "http://carirunners.blogspot.com/2013/05/kul-imu-charity-run-2013.html",
                "[KUL] IMU Charity Run 2013", "HTML content",
                Arrays.asList("kuala lumpur", "race", "road"));
        results.postValue(Resource.success(Arrays.asList(post)));
        onView(withText("desc")).perform(click());
        verify(navigationController).navigateToRepo("foo", "bar");
    }

    @Test
    public void loadMoreProgress() {
        loadMoreStatus.postValue(new SearchViewModel.LoadMoreState(true, null));
        onView(withId(R.id.load_more_bar)).check(matches(isDisplayed()));
        loadMoreStatus.postValue(new SearchViewModel.LoadMoreState(false, null));
        onView(withId(R.id.load_more_bar)).check(matches(not(isDisplayed())));
    }

    @Test
    public void loadMoreProgressError() {
        loadMoreStatus.postValue(new SearchViewModel.LoadMoreState(true, "QQ"));
        onView(withText("QQ")).check(matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @NonNull
    private RecyclerViewMatcher listMatcher() {
        return new RecyclerViewMatcher(R.id.repo_list);
    }
}