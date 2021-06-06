package socialmedia;

import java.io.*;
import java.util.ArrayList;


/**
 * SocialMedia  is an implementor class for SocialMediaPlatform interface
 */
public class SocialMedia implements SocialMediaPlatform {
    @Override
    public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
        Account account = new Account(handle);
        return account.getId();
    }

    @Override
    public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
        Account account = new Account(handle, description);
        return account.getId();
    }

    @Override
    public void removeAccount(int id) throws AccountIDNotRecognisedException {
        Account accountToRemove = Account.findAccount(id);
        accountToRemove.removeAccount();
    }

    @Override
    public void removeAccount(String handle) throws HandleNotRecognisedException {
        Account accountToRemove = Account.findAccount(handle);
        accountToRemove.removeAccount();
    }

    @Override
    public void changeAccountHandle(String oldHandle, String newHandle)
            throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
        Account.changeHandle(oldHandle, newHandle);
    }

    @Override
    public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException {
        Account.updateDescription(handle, description);
        assert (Account.findAccount(handle).getDescription().equals(description));
    }

    @Override
    public String showAccount(String handle) throws HandleNotRecognisedException {
        return Account.findAccount(handle).showAccount();
    }

    @Override
    public int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
        Post post = new Post(handle, message);
        return post.getId();
    }

    @Override
    public int endorsePost(String handle, int id)
            throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
        EndorsedPost endorsedPost = new EndorsedPost(handle, id);
        return endorsedPost.getId();
    }

    @Override
    public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException,
            PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {
        CommentPost commentPost = new CommentPost(handle, id, message);
        return commentPost.getId();
    }

    @Override
    public void deletePost(int id) throws PostIDNotRecognisedException {
        Post postToDelete = Post.findPost(id);
        postToDelete.removePost();
    }

    @Override
    public String showIndividualPost(int id) throws PostIDNotRecognisedException {
        // finding the post to show using post ID
        Post postToShow = Post.findPost(id);
        // finding account of the post to show using above found post
        Account authorOfPostToShow = Account.findAccount(postToShow);
        // calling showPost using above assigned data
        return postToShow.showPost(authorOfPostToShow, "");
    }

    @Override
    public StringBuilder showPostChildrenDetails(int id)
            throws PostIDNotRecognisedException, NotActionablePostException {
        // finding the post to show using post ID
        Post postToShow = Post.findPost(id);
        // ensuring post to show is not an endorsed post
        if (postToShow instanceof EndorsedPost)
            throw new NotActionablePostException("Can not show post children details of an Endorsed Post");

        // finding account of the post to show
        Account authorOFPostToShow = Account.findAccount(postToShow);
        // calling showPost using above assigned data
        return postToShow.showPostChildren(authorOFPostToShow, "");
    }

    @Override
    public int getNumberOfAccounts() {
        return Account.getAllAccounts().size();
    }

    @Override
    public int getTotalOriginalPosts() {
        // searching through all accounts
        int totalNumOriginalPosts = 0;
        for (Account account : Account.getAllAccounts()) {
            // getting number of original posts per account
            totalNumOriginalPosts += account.getNumOriginalPosts();
        } return totalNumOriginalPosts;
    }

    @Override
    public int getTotalEndorsmentPosts() {
        // searching through all accounts
        int totalNumEndorsedPosts = 0;
        for (Account account : Account.getAllAccounts()) {
            // adding endorsements of each account
            totalNumEndorsedPosts += account.getNumEndorsedPosts();
        } return totalNumEndorsedPosts;
    }

    @Override
    public int getTotalCommentPosts() {
        // searching through all accounts
        int totalNumCommentPosts = 0;
        for (Account account : Account.getAllAccounts()) {
            // adding comments of each account
            totalNumCommentPosts += account.getNumCommentPosts();
        } return totalNumCommentPosts;
    }

    @Override
    public int getMostEndorsedPost() {
        Post mostEndorsedPost = null;
        int mostEndorsements = 0;

        // searching through posts of all accounts
        for (Account account : Account.getAllAccounts()) {
            for (Post post : account.getPosts()) {
                // checking if currently checked post has more endorsements
                // than mostEndorsedPost
                if (post.getEndorsementsReceived() > mostEndorsements) {
                    mostEndorsedPost = post;
                    mostEndorsements = post.getEndorsementsReceived();
                }
            }
        }
        // returning -1 if there are not any posts in the system
        if (mostEndorsedPost == null)
            return -1;

        assert (mostEndorsements >= 0);
        return mostEndorsedPost.getId();
    }

    @Override
    public int getMostEndorsedAccount() {
        Account mostEndorsedAccount = null;
        int mostEndorsements = 0;

        // searching through all accounts
        for (Account account : Account.getAllAccounts()) {
            // checking if currently account has more endorsements
            // than mostEndorsedAccount
            if (account.getEndorsementsReceived() > mostEndorsements) {
                mostEndorsedAccount = account;
                mostEndorsements = account.getEndorsementsReceived();
            }
        }
        // returning -1 if there are not any accounts in the system
        if (mostEndorsedAccount == null)
            return -1;

        assert (mostEndorsements >= 0);
        return mostEndorsedAccount.getId();
    }

    @Override
    public void erasePlatform() {
        // erasing the platform by clearing ALL_ACCOUNTS ArrayList
        Account.getAllAccounts().clear();
    }

    @Override
    public void savePlatform(String filename) throws IOException {
        // file to save at
        filename = "./" + filename + ".ser";

        // serializing ALL_ACCOUNTS
        try (ObjectOutputStream out = new ObjectOutputStream
                (new FileOutputStream(filename))) {
            out.writeObject(Account.getAllAccounts());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
        // file to save at
        filename = "./" + filename + ".ser";

        // deserializing contents of 'filename'
        try (ObjectInputStream in = new ObjectInputStream
                (new FileInputStream(filename))) {
            Account.setAllAccounts((ArrayList<Account>) in.readObject());
        }
    }

    /**
     * Constructor erases the existing platform upon object instantiation
     */
    public SocialMedia() {
        erasePlatform();
    }
}
