package socialmedia;

import socialmedia.exceptions.*;

/**
 * Used to handle all operations related to comment Posts
 * Inherits the attributes / methods of Post class
 */
public class CommentPost extends Post {
    // instance attributes
    // parentPost: link to the Post (original, comment or endorsed) replied to
    private Post parentPost;

    @Override
    public String toString() {
        return super.toString().substring(0, super.toString().length() - 1)
                + ", parentPost = " + parentPost + "]";
    }

    // Constructor
    public CommentPost(String handle, int id, String message)
            throws HandleNotRecognisedException, PostIDNotRecognisedException {
        // assigning handle and message attributes of the CommentPost
        super(handle, message);

        // assigning the parentPost commented on
        parentPost = Post.findPost(id);

    }

    // getter method
    public Post getParentPost() { return parentPost; }
}
