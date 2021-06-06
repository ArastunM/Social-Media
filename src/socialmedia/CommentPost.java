package socialmedia;


/**
 * Used to handle all operations related to comment Posts
 * Inherits the attributes / methods of Post class
 */
public class CommentPost extends Post {
    // instance attributes
    // parentPost: link to the Post (original) replied to
    private Post parentPost;
    // commentPost: link to the Comment Post replied to
    private CommentPost parentComment;
    // empty: commentPost set as empty when its parenPost is removed
    private boolean empty = false;

    /**
     * Used to set commentPosts as empty when their parentPost is removed
     */
    // method: used to set commentPosts as empty when their parentPost is removed
    public void setAsEmpty() {
        setParentPost(null); // link to parentPost is removed
        setEmpty(true); // commentPost is considered empty
        setMessage("The original content was removed from the system and is no longer available.");
    }

    /**
     * String representation of the Comment Post
     * @return formatted String representation of the Comment Post
     */
    @Override
    public String toString() {
        return super.toString().substring(0, super.toString().length() - 1)
                + ", parentPost = " + parentPost
                + ", commentPost = " + parentComment + "]";
    }

    /**
     * Used to construct Comment Posts given author's handle, ID of
     * the Post to endorse and comment's String message
     *
     * @param handle author's handle
     * @param id ID of the Post to comment on
     * @param message string content of the comment
     *
     * @throws HandleNotRecognisedException if Account (author) with given handle does not exist
     * @throws PostIDNotRecognisedException if Post with given ID does not exist
     * @throws InvalidPostException if comment's message is not in appropriate form
     * @throws NotActionablePostException if Post to comment on is an Endorsed Post
     */
    public CommentPost(String handle, int id, String message)
            throws HandleNotRecognisedException, PostIDNotRecognisedException,
            InvalidPostException, NotActionablePostException {
        // assigning handle and message attributes of the CommentPost
        super(handle, message);

        parentPost = Post.findPost(id);
        Account authorOfEndorsedPost = Account.findAccount(parentPost);

        // assigning the parentPost / commentPost commented on
        if (parentPost instanceof CommentPost) {
            parentComment = (CommentPost) parentPost;
            parentPost = ((CommentPost) parentPost).getParentPost();
        }
        // checking if the post can be endorsed (is not an already endorsed post)
        else if (parentPost instanceof EndorsedPost) {
            authorOfEndorsedPost.getPosts().remove(this);
            throw new NotActionablePostException("Endorsed Posts can not be endorsed");
        }

        assert (! (parentPost instanceof EndorsedPost || parentPost instanceof CommentPost));

        // increasing numCommentPosts of the author
        Account author = Account.findAccount(handle);
        author.incrementNumCommentPosts(1);
    }

    /*
     * Getter method for the parentPost, Post commented on by Comment Post
     */
    public Post getParentPost() { return parentPost; }
    /*
     * Getter method for the "empty" status of Comment Post
     */
    public boolean getEmpty() { return empty; }
    public Post getParentComment() { return parentComment; }

    /*
     * Setter method for the parentPost of Comment Post
     */
    public void setParentPost(Post parentPost) { this.parentPost = parentPost; }
    /*
     * Setter method for the "empty" status of Comment Post
     */
    public void setEmpty(boolean status) { empty = status; }
}
