package com.blogspot.carirunners.run.ui.post;

import com.blogspot.carirunners.run.R;
import com.blogspot.carirunners.run.binding.FragmentBindingAdapters;
import com.blogspot.carirunners.run.testing.SingleFragmentActivity;
import com.blogspot.carirunners.run.ui.common.NavigationController;
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
import android.support.annotation.StringRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import java.util.Arrays;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class PostFragmentTest {
    @Rule
    public ActivityTestRule<SingleFragmentActivity> activityRule =
            new ActivityTestRule<>(SingleFragmentActivity.class, true, true);
    @Rule
    public TaskExecutorWithIdlingResourceRule executorRule =
            new TaskExecutorWithIdlingResourceRule();
    private MutableLiveData<Resource<Post>> post = new MutableLiveData<>();
    private PostFragment postFragment;
    private PostViewModel viewModel;

    private FragmentBindingAdapters fragmentBindingAdapters;
    private NavigationController navigationController;


    @Before
    public void init() {
        postFragment = PostFragment.newInstance("a", "b");
        viewModel = mock(PostViewModel.class);
        fragmentBindingAdapters = mock(FragmentBindingAdapters.class);
        navigationController = mock(NavigationController.class);

        when(viewModel.getPost()).thenReturn(post);

        postFragment.viewModelFactory = ViewModelUtil.createFor(viewModel);
        postFragment.dataBindingComponent = () -> fragmentBindingAdapters;
        postFragment.navigationController = navigationController;

        activityRule.getActivity().setFragment(postFragment);
    }

    @Test
    public void testLoading() {
        post.postValue(Resource.loading(null));
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testValueWhileLoading() {
        Post post = TestUtil.createPost("2013-05-27T11:16:00+08:00",
                "2013-05-27T11:16:11+08:00",
                "http://carirunners.blogspot.com/2013/05/kul-imu-charity-run-2013.html",
                "[KUL] IMU Charity Run 2013", "HTML content",
                Arrays.asList("kuala lumpur", "race", "road"));
        this.post.postValue(Resource.loading(post));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.title)).check(matches(withText("[KUL] IMU Charity Run 2013")));
        onView(withId(R.id.content)).check(matches(withText("HTML content")));
    }

    @Test
    public void testLoaded() throws InterruptedException {
        Post post = TestUtil.createPost("2013-05-27T11:16:00+08:00",
                "2013-05-27T11:16:11+08:00",
                "http://carirunners.blogspot.com/2013/05/kul-imu-charity-run-2013.html",
                "[KUL] IMU Charity Run 2013", "HTML content",
                Arrays.asList("kuala lumpur", "race", "road"));
        this.post.postValue(Resource.success(post));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.title)).check(matches(withText("[KUL] IMU Charity Run 2013")));
        onView(withId(R.id.content)).check(matches(withText("HTML content")));
    }

    @Test
    public void testError() throws InterruptedException {
        post.postValue(Resource.error("foo", null));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.retry)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).perform(click());
        verify(viewModel).retry();
        post.postValue(Resource.loading(null));

        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())));
        Post post = TestUtil.createPost("2013-05-27T11:16:00+08:00",
                "2013-05-27T11:16:11+08:00",
                "http://carirunners.blogspot.com/2013/05/kul-imu-charity-run-2013.html",
                "[KUL] IMU Charity Run 2013", "HTML content",
                Arrays.asList("kuala lumpur", "race", "road"));
        this.post.postValue(Resource.success(post));

        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())));
        onView(withId(R.id.title)).check(matches(withText("[KUL] IMU Charity Run 2013")));
        onView(withId(R.id.content)).check(matches(withText("HTML content")));
    }

    @Test
    public void nullRepo() {
        this.post.postValue(null);
        onView(withId(R.id.title)).check(matches(not(isDisplayed())));
    }

    private String getString(@StringRes int id, Object... args) {
        return InstrumentationRegistry.getTargetContext().getString(id, args);
    }
}