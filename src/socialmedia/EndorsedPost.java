package socialmedia;

import socialmedia.exceptions.*;

/**
 * Used to handle all operations related to endorsed Posts
 * Inherits the attributes / methods of Post class
 */
public class EndorsedPost extends Post {
    // instance attributes
    // parentPost: link to the endorsed Post (original or comment)
    private Post parentPost;

    // method: String representation of EndorsedPost
    @Override
    public String toString() {
        return super.toString().substring(0, super.toString().length() - 1)
                + ", parentPost = " + parentPost + "]";
    }

    // Constructor
    // TODO find a better solution for the constructor (specifically calling the super() part)
    public EndorsedPost(String handle, int id) throws HandleNotRecognisedException, PostIDNotRecognisedException {
        // replicating the message of the original Post
        super(handle, Post.findPost(id).getMessage());

        // assigning the link to the original Post
        parentPost = Post.findPost(id);
    }

    // getter method
    public Post getParentPost() { return parentPost; }
}
