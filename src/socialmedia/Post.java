package socialmedia;

import socialmedia.exceptions.*;

/**
 * Used to handle all Post related operations
 * Used as an attribute of the Account class
 */
public class Post {
    // instance attributes
    // id: unique numerical identifier reflecting post's chronology
    private int id;
    // message: string message of the Post
    private String message;

    // method: used to generate a numerical identifier (id) based on post's chronology
    // post id = id of the account posting the post + id of the post within the account
    // TODO for now the numerical identifier represents the post chronology of the author
    private int generatePostID(Account authorOfPost) {
        String id = authorOfPost.getId() + "" + authorOfPost.getPosts().size();
        return Integer.parseInt(id);
    }
    // method: used to find the post with given post id
    public static Post findPost(int id) throws PostIDNotRecognisedException {
        String stringID = String.valueOf(id);
        try {
            int accountID = Integer.parseInt(stringID.substring(0, 4));
            int postID = Integer.parseInt(stringID.substring(4));
            Account authorOfPost = Account.findAccount(accountID);
            return authorOfPost.getPosts().get(postID);
        } catch (AccountIDNotRecognisedException | IndexOutOfBoundsException e) {
            throw new PostIDNotRecognisedException("Post with given id: " + id + " does not exit in the system");
        }
    }

    // method: String representation of the Post
    @Override
    public String toString() {
        return "[id = " + id + ", message = " + message + "]";
    }

    // getter methods
    public int getId() { return id; }
    public String getMessage() { return message; }

    // Constructor
    public Post(String handle, String message) throws HandleNotRecognisedException {
        Account authorOfPost = Account.findAccount(handle);

        // assigning values
        this.id = generatePostID(authorOfPost);
        this.message = message;
        authorOfPost.getPosts().add(this);
    }
}
