package com.blogspot.carirunners.run.util;

import com.blogspot.carirunners.run.vo.Contributor;
import com.blogspot.carirunners.run.vo.Post;
import com.blogspot.carirunners.run.vo.Repo;
import com.blogspot.carirunners.run.vo.User;

import java.util.ArrayList;
import java.util.List;

public class TestUtil {
    public static Post createPost(String published, String updated, String url, String title,
                                  String content, List<String> labels) {
        return new Post(Post.UNKNOWN_ID, published, updated, url, title, content, labels);
    }

    public static List<Post> createPost(int count, String published, String updated, String url,
                                        String title, String content, List<String> labels) {
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            posts.add(createPost(published, updated, url, title + i, content + i,
                    labels));
        }
        return posts;
    }

    public static User createUser(String login) {
        return new User(login, null,
                login + " name", null, null, null);
    }

    public static List<Repo> createRepos(int count, String owner, String name,
                                         String description) {
        List<Repo> repos = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            repos.add(createRepo(owner + i, name + i, description + i));
        }
        return repos;
    }

    public static Repo createRepo(String owner, String name, String description) {
        return createRepo(Repo.UNKNOWN_ID, owner, name, description);
    }

    public static Repo createRepo(int id, String owner, String name, String description) {
        return new Repo(id, name, owner + "/" + name,
                description, new Repo.Owner(owner, null), 3);
    }

    public static Contributor createContributor(Repo repo, String login, int contributions) {
        Contributor contributor = new Contributor(login, contributions, null);
        contributor.setRepoName(repo.name);
        contributor.setRepoOwner(repo.owner.login);
        return contributor;
    }
}
