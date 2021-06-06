package socialmedia;


/**
 * Used to handle all operations related to endorsed Posts.
 * Inherits the attributes / methods of Post class
 */
public class EndorsedPost extends Post {
    // instance attributes
    // parentPost: link to the endorsed Post (original or comment)
    private Post parentPost;

    /**
     * String representation of the Endorsed Post
     * @return formatted String representation of the Endorsed Post
     */
    @Override
    public String toString() {
        return super.toString().substring(0, super.toString().length() - 1)
                + ", parentPost = " + parentPost + "]";
    }

    /**
     * Used to construct Endorsed Posts given handle of the author
     * and ID of the Post to endorse
     *
     * @param handle author's handle
     * @param id ID of the Post to endorse
     * @throws HandleNotRecognisedException if Account (author) with given handle does not exist
     * @throws PostIDNotRecognisedException if Post with given ID does not exist
     * @throws NotActionablePostException if Post to endorse is an Endorsed Post itself
     */
    public EndorsedPost(String handle, int id)
            throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
        // assigning the link to the original Post
        parentPost = Post.findPost(id);

        // checking if the post can be endorsed (is not an already endorsed post)
        // endorsed posts can not be endorsed
        // comment posts with removed parentPost also can't be endorsed
        if (parentPost instanceof EndorsedPost ||
                (parentPost instanceof CommentPost && ((CommentPost) parentPost).getEmpty()))
            throw new NotActionablePostException("Endorsed Posts can not be endorsed");

        // replicating the message of the original Post
        Account author = Account.findAccount(handle);
        this.setId(this.generatePostID(author));
        this.setMessage(parentPost.getMessage());
        author.getPosts().add(this);

        // increasing numEndorsements of the parentPost
        parentPost.incrementEndorsementsReceived(1);
        // increasing numEndorsedPosts of the author
        author.incrementNumEndorsedPosts(1);
    }

    /*
     * Getter method for the parentPost of Endorsed Post
     */
    public Post getParentPost() { return parentPost; }
}
