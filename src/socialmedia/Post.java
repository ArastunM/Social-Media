package socialmedia;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Used to handle all Post related operations.
 * Used as an attribute of the Account class
 */
public class Post implements Serializable {
    // instance attributes
    // id: unique numerical identifier reflecting post's chronology
    private int id;
    // message: string message of the Post
    private String message;
    // STATISTICS
    // endorsementsReceived: total endorsements received by the Post
    private int endorsementsReceived;

    /**
     * Used to generate a numerical identifier (ID) based on ID of author
     * of the Post and Post chronology of the author
     *
     * @param authorOfPost author of the Post
     * @return generated int numerical identifier (ID)
     */
    public int generatePostID(Account authorOfPost) {
        String id = authorOfPost.getId() + "" + authorOfPost.getPosts().size();
        return Integer.parseInt(id);
    }
    /**
     * Used to check if given String Post message is in appropriate form
     * @param message message to check
     * @return true if String message is appropriate, false otherwise
     */
    private static boolean notAppropriateMessage(String message) {
        // message can not be empty
        boolean case1 = message.strip().equals("");
        // message can not be more than 100 characters
        boolean case2 = (message.length() > 100);
        return case1 || case2;
    }

    // METHODS of FINDING a Post
    /**
     * Used to find a Post given its ID
     *
     * @param id ID to search for
     * @return Post with given ID
     * @throws PostIDNotRecognisedException if Post with given ID does not exist
     */
    public static Post findPost(int id) throws PostIDNotRecognisedException {
        String stringID = String.valueOf(id);
        try {
            int accountID = Integer.parseInt(stringID.substring(0, 4));
            // finding the author of the post to find
            Account authorOfPost = Account.findAccount(accountID);
            // finding the post within account
            return findPost(authorOfPost, id);

        } catch (AccountIDNotRecognisedException | PostIDNotRecognisedException | StringIndexOutOfBoundsException e) {
            // as Post ID is linked with its author Account's ID,
            // either of above 3 exceptions would hint to PostIDNotRecognizedException
            throw new PostIDNotRecognisedException("Post with given id: " + id + " does not exit in the system");
        }
    }
    /**
     * Used to find a Post given its author and ID
     *
     * @param authorOfPost author of the Post to search for
     * @param id id to search for
     * @return Post with given author and ID
     * @throws PostIDNotRecognisedException if Post with given author / ID does not exist
     */
    public static Post findPost(Account authorOfPost, int id) throws PostIDNotRecognisedException {
        for (Post post : authorOfPost.getPosts()) {
            if (post.getId() == id)
                return post;
        } throw new PostIDNotRecognisedException();
    }

    // METHODS of FINDING Endorsements / Comments received by a Post
    /**
     * Used to find all endorsements received by the Post
     *
     * @param postToSearch post to search
     * @return ArrayList of all endorsements received by the Post
     */
    private static ArrayList<EndorsedPost> findEndorsementsOf(Post postToSearch) {
        ArrayList<EndorsedPost> endorsedPostsOfGivenPost = new ArrayList<>();
        // searching through posts of all accounts
        for (Account account : Account.getAllAccounts()) {
            for (Post post : account.getPosts()) {
                // if a post is an EndorsedPost
                if (post instanceof EndorsedPost) {
                    EndorsedPost endorsedPost = (EndorsedPost) post;
                    // and its parentPost is postToSearch
                    if (endorsedPost.getParentPost().equals(postToSearch))
                        // post is added to endorsedPostsOfGivenPost ArrayList
                        endorsedPostsOfGivenPost.add(endorsedPost);
                }
            }
        } return endorsedPostsOfGivenPost;
    }
    /**
     * Used to find all or direct comments received by a Post or a CommentPost.
     *
     * @param postToSearch Post (original or comment) to search
     * @param directComment if true, search for only direct comments to the post, search for
     *                      all of the comments received otherwise
     * @return ArrayList of all / direct comments received by the Post
     */
    private static ArrayList<CommentPost> findCommentsOf(Post postToSearch, boolean directComment) {
        ArrayList<CommentPost> commentPostsOfGivenPost = new ArrayList<>();
        // searching through posts of all accounts
        for (Account account : Account.getAllAccounts()) {
            for (Post post : account.getPosts()) {
                if (post instanceof CommentPost) {
                    CommentPost commentPost = (CommentPost) post;

                    // if postToSearch is a CommentPost
                    if (postToSearch instanceof CommentPost) {
                        if (commentPost.getParentComment() != null
                                && commentPost.getParentComment().equals(postToSearch))
                            commentPostsOfGivenPost.add(commentPost);
                    } // if postToSearch is a Post (original)
                    // and only direct comments to the Post are required
                    else if (directComment) {
                        if (commentPost.getParentPost() != null
                                && commentPost.getParentComment() == null
                                && commentPost.getParentPost().equals(postToSearch))
                            commentPostsOfGivenPost.add(commentPost);
                    } // if postToSearch is a Post (original) and all comments are required
                    else {
                        if (commentPost.getParentPost() != null
                                && commentPost.getParentPost().equals(postToSearch))
                            commentPostsOfGivenPost.add(commentPost);
                    }
                }
            }
        } return commentPostsOfGivenPost;
    }

    /**
     * Used to remove the Post from the system.
     *
     * Before removing a post all of its endorsements
     * are removed and comment replies are set as empty
     */
    public void removePost() {
        // finding all endorsements and comments received by the Post to remove
        ArrayList<EndorsedPost> endorsedPostsOfGivenPost = findEndorsementsOf(this);
        ArrayList<CommentPost> commentPostOfGivenPost = findCommentsOf(this, false);

        // removing all endorsements of the postToRemove
        // decreasing endorsementsReceived of postToRemove
        for (EndorsedPost endorsedPost : endorsedPostsOfGivenPost) {
            endorsedPost.removePost();
            this.incrementEndorsementsReceived(-1);
        }

        // updating all comments on the postToRemove
        for (CommentPost commentPost : commentPostOfGivenPost) { commentPost.setAsEmpty(); }

        assert (this.getEndorsementsReceived() == 0);
        assert (findCommentsOf(this, false).size() == 0);

        // finding author of the Post to remove and removing the Post itself
        try {
            Account authorOfThePostToRemove = Account.findAccount(this);
            authorOfThePostToRemove.getPosts().remove(this);

            if (this instanceof EndorsedPost)
                authorOfThePostToRemove.incrementNumEndorsedPosts(-1);
            else if (this instanceof CommentPost)
                authorOfThePostToRemove.incrementNumCommentPosts(-1);

        } catch (PostIDNotRecognisedException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * String representation of the Post
     * @return formatted String representation of the Post
     */
    @Override
    public String toString() {
        return "[id = " + id + ", message = " + message + "]";
    }
    /**
     * Used to show the Post given its author and tab value
     *
     * @param author author of the Post to show
     * @param tab used by showPostChildren, default value: empty string
     * @return formatted String representation of the Post
     */
    public String showPost(Account author, String tab) {
        return "ID: " + this.id + "\n" + tab + "Account: " + author.getHandle() + "\n" + tab + "No. endorsements: "
                + findEndorsementsOf(this).size() + " | No. comments: "
                + findCommentsOf(this, false).size() + "\n" + tab + message;
    }
    /**
     * Used to show the Post all its Children given its author and tab value
     *
     * @param author author of the Post to show
     * @param tab tab position of the current Post in StringBuilder
     * @return formatted StringBuilder representation of a Post and all its Children
     * @throws PostIDNotRecognisedException used when finding the author of Children Posts
     *                                      (not thrown unless there is an error)
     */
    public StringBuilder showPostChildren(Account author, String tab) throws PostIDNotRecognisedException {
        StringBuilder stringbuilder = new StringBuilder();
        String tabShift = "\n" + tab + "|\n" + tab + "| > ";
        ArrayList<EndorsedPost> endorsedPostsOfGivenPost = findEndorsementsOf(this);
        ArrayList<CommentPost> commentPostOfGivenPost;

        if (this instanceof CommentPost)
            commentPostOfGivenPost = findCommentsOf(this, false);
        else
            commentPostOfGivenPost = findCommentsOf(this, true);

        // appending current Post's template
        stringbuilder.append(this.showPost(author, tab));
        // appending template of children of current Post
        for (EndorsedPost endorsedPost : endorsedPostsOfGivenPost) {
            stringbuilder.append(tabShift);
            stringbuilder.append(endorsedPost.showPostChildren
                    (Account.findAccount(endorsedPost), tab + "\t"));
        }
        for (CommentPost commentPost : commentPostOfGivenPost) {
            stringbuilder.append(tabShift);
            stringbuilder.append(commentPost.showPostChildren
                    (Account.findAccount(commentPost), tab + "\t"));
        }
        return stringbuilder;
    }

    /**
     * Default empty constructor of Post
     */
    public Post() {}
    /**
     * Used to construct a new Post given its handle and message
     *
     * @param handle handle of the author of the Post
     * @param message Post's message
     * @throws HandleNotRecognisedException if Account with given handle does not exist
     * @throws InvalidPostException if Post message is not in appropriate form
     */
    public Post(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
        Account authorOfPost = Account.findAccount(handle);

        // checking if String message is in appropriate form
        if (notAppropriateMessage(message))
            throw new InvalidPostException("Given post message: " + message
                    + ", can not be empty or have more than 100 characters");

        // assigning values
        id = generatePostID(authorOfPost);
        this.message = message;
        authorOfPost.getPosts().add(this);

        assert (String.valueOf(id).length() > String.valueOf(authorOfPost.getId()).length());
        assert (!notAppropriateMessage(this.message));
    }

    /*
     * Getter method for ID of the Post
     */
    public int getId() { return id; }
    /*
     * Getter method for message of the Post
     */
    public String getMessage() { return message; }
    /*
     * Getter method for number of endorsements received by the Post
     */
    public int getEndorsementsReceived() { return endorsementsReceived; }

    /*
     * Setter method for ID of the Post
     */
    public void setId(int id) { this.id = id; }
    /*
     * Setter method for message of the Post
     */
    public void setMessage(String message) { this.message = message; }
    /*
     * Used to increment number of endorsements received by the Post
     * @param incrementBy int amount to increment by
     */
    public void incrementEndorsementsReceived(int incrementBy) { this.endorsementsReceived += incrementBy; }
}
