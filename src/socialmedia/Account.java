package socialmedia;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Used to handle Account related operations
 */
public class Account implements Serializable {
    // instance attributes
    // id: unique numerical identifier
    private int id;
    // handle: unique string handle (username)
    private String handle;
    // description: field for personal information
    private String description;
    // posts: list of all posts (original, comment, endorsement) made by the Account
    private ArrayList<Post> posts = new ArrayList<>();
    // STATISTICS
    // numEndorsedPosts: number of endorsed Posts made by the Account
    private int numEndorsedPosts;
    // numCommentPosts: number of comment Posts made by the Account
    private int numCommentPosts;

    // static attribute
    // ALL_ACCOUNTS: used to store all registered accounts
    private static ArrayList<Account> ALL_ACCOUNTS = new ArrayList<>();


    // CREATING AN ACCOUNT
    /**
     * Checks if Account with given ID exists (is not Unique)
     *
     * @param id ID to check
     * @return true if Account with given ID exists (notUnique), false otherwise
     */
    private static boolean notUniqueID(int id) {
        for (Account account : ALL_ACCOUNTS) {
            if (id == account.id)
                return true;
        } return false;
    }
    /**
     * Checks if Account with handle exists (is not Unique)
     *
     * @param handle handle to check
     * @return true if Account with given handle exists (notUnique), false otherwise
     */
    private static boolean notUniqueHandle(String handle) {
        for (Account account : ALL_ACCOUNTS) {
            if (handle.equals(account.handle))
                return true;
        } return false;
    }
    /**
     * Generates a random numerical identifier for an Account
     * @return randomly generated int numerical identifier (ID)
     */
    private static int generateAccountID() {
        // generating a 4 digit code
        int randomIdentifier = (int) (Math.random() * 9000) + 1000;
        // re-generating the ID in case it is not unique
        if (notUniqueID(randomIdentifier))
            return generateAccountID();

        assert (!notUniqueID(randomIdentifier));
        return randomIdentifier;
    }
    /**
     * Checks if given handle is in appropriate format
     *
     * @param handle handle to check
     * @return true if handle is not appropriate, false otherwise
     */
    private static boolean notAppropriateHandle(String handle) {
        // handle cannot be empty
        boolean case1 = handle.strip().equals("");
        // contain more than 30 characters
        boolean case2 = (handle.length() > 30);
        // contain white space(s)
        boolean case3 = containsWhiteSpace(handle);
        return case1 || case2 || case3;
    }
    /**
     * Checks if given String contains empty space
     *
     * @param string string to check
     * @return true if the string contains empty space, false otherwise
     */
    private static boolean containsWhiteSpace(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (Character.isWhitespace(string.charAt(i)))
                return true;
        } return false;
    }

    // REMOVING AN ACCOUNT
    /**
     * Used to remove the Account (this) from the system
     */
    public void removeAccount() {
        // finding and assigning the account to remove
        while (getPosts().size() > 0) {
            // removing Posts related to the Account until there are not Posts left
            getPosts().get(0).removePost();
        }
        assert (getPosts().size() == 0);
        ALL_ACCOUNTS.remove(this);
    }

    // METHODS of FINDING an Account
    /**
     * Used to find Account with given ID
     *
     * @param id Account ID to search for
     * @return Account with given ID
     * @throws AccountIDNotRecognisedException if Account with given ID doest not exist
     */
    public static Account findAccount(int id) throws AccountIDNotRecognisedException {
        for (Account account : ALL_ACCOUNTS) {
            if (account.getId() == id)
                return account;
        } throw new AccountIDNotRecognisedException("Account with given id: " + id + ", does not exist");
    }
    /**
     * Used to find Account with given handle
     *
     * @param handle Account handle to search for
     * @return Account with given handle
     * @throws HandleNotRecognisedException if Account with given handle does not exist
     */
    public static Account findAccount(String handle) throws HandleNotRecognisedException {
        for (Account account : ALL_ACCOUNTS) {
            if (account.getHandle().equals(handle))
                return account;
        } throw new HandleNotRecognisedException("Account with given handle: " + handle + ", does not exist");
    }
    /**
     * Used to find Account with given Post (posted by the Account)
     *
     * @param post Post of the Account to search for
     * @return author Account of the Post
     * @throws PostIDNotRecognisedException if Post with given ID does not exist
     */
    public static Account findAccount(Post post) throws PostIDNotRecognisedException {
        try {
            String postID = String.valueOf(post.getId());
            int accountID = Integer.parseInt(postID.substring(0, 4));
            return findAccount(accountID);
        } catch (AccountIDNotRecognisedException | StringIndexOutOfBoundsException e) {
            throw new PostIDNotRecognisedException
                    ("Post with given id: " + post.getId() + " does not exit in the system");
        }
    }

    // CHANGING ACCOUNT DETAILS
    /**
     * Changes the handle of the Account
     *
     * @param oldHandle handle of the Account to change
     * @param newHandle Account's new handle
     * @throws HandleNotRecognisedException if Account with given oldHandle does not exist
     * @throws IllegalHandleException if given newHandle is not unique
     * @throws InvalidHandleException if given newHandle is not in appropriate form
     */
    public static void changeHandle(String oldHandle, String newHandle)
            throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
        // finding the Account with given oldHandle
        Account accountToChange = findAccount(oldHandle);

        // checking if given newHandle is valid and appropriate
        if (notUniqueHandle(newHandle))
            throw new InvalidHandleException("Given handle: " + newHandle + ", already exists in the system");
        if (notAppropriateHandle(newHandle))
            throw new IllegalHandleException("Given handle: " + newHandle + ", is not appropriate");

        accountToChange.setHandle(newHandle);
        assert accountToChange.getHandle().equals(newHandle);
    }
    /**
     * Updates the description of an Account
     *
     * @param handle handle of the Account to update
     * @param description new description of the Account
     * @throws HandleNotRecognisedException if Account with given handle does not exist
     */
    public static void updateDescription(String handle, String description) throws HandleNotRecognisedException {
        // finding the Account with given handle
        Account accountToChange = findAccount(handle);
        accountToChange.setDescription(description);
    }

    /**
     * String representation of the Account
     * @return formatted String representation of the Account
     */
    @Override
    public String toString() {
        return "[id = " + id + ", handle = " + handle
                + ", description = " + description + ", numEndorsements = "
                + getEndorsementsReceived() + ", posts = " + posts + "]";
    }
    /**
     * Used to show formatted String summary of an Account
     * @return formatted String summary of an Account
     */
    public String showAccount() {
        return "ID: " + id + "\nHandle: " + handle + "\nDescription: " + description +
                "\nPost count: " + posts.size() + "\nEndorse count: " + getEndorsementsReceived();
    }

    /**
     * Used to construct the Account object given its handle
     *
     * @param handle Account's handle
     * @throws InvalidHandleException if given handle is not unique
     * @throws IllegalHandleException if given handle is not in appropriate form
     */
    public Account(String handle) throws InvalidHandleException, IllegalHandleException {
        // checking if given handle is valid and appropriate
        if (notUniqueHandle(handle))
            throw new InvalidHandleException("Given handle already exists in the system");
        if (notAppropriateHandle(handle))
            throw new IllegalHandleException("Given handle is not appropriate");

        // assigning values
        id = generateAccountID();
        this.handle = handle;
        ALL_ACCOUNTS.add(this);

        assert (notUniqueID(id));
        assert (notUniqueHandle(this.handle));
        assert (!notAppropriateHandle(this.handle));
    }
    /**
     * Used to construct the Account object given its handle and description
     *
     * @param handle Account's handle
     * @param description Account's description
     * @throws InvalidHandleException if given handle is not unique
     * @throws IllegalHandleException if given handle is not in appropriate form
     */
    public Account(String handle, String description) throws InvalidHandleException, IllegalHandleException {
        // assigning id, handle through first constructor
        this(handle);
        // assigning values
        this.description = description;
        ALL_ACCOUNTS.set(ALL_ACCOUNTS.size() - 1, this);
    }

    /*
     * Getter method for Account's ID
     */
    public int getId() { return id; }
    /*
     * Getter method for Account's handle
     */
    public String getHandle() { return handle; }
    /*
     * Getter method for Account's description
     */
    public String getDescription() { return description; }
    /*
     * Getter method for Account's ArrayList of Posts
     */
    public ArrayList<Post> getPosts() { return posts; }
    /*
     * Getter method for ALL_ACCOUNTS (all registered Accounts)
     */
    public static ArrayList<Account> getAllAccounts() { return ALL_ACCOUNTS; }
    /*
     * Getter method for number of Account's original Posts
     */
    public int getNumOriginalPosts() {
        return this.getPosts().size() - (getNumEndorsedPosts() + getNumCommentPosts());
    }
    /*
     * Getter method for number of Account's endorsed Posts
     */
    public int getNumEndorsedPosts() { return numEndorsedPosts; }
    /*
     * Getter method for number of Account's comment Posts
     */
    public int getNumCommentPosts() { return numCommentPosts; }
    /*
     * Getter method for number of endorsements received by the Account
     */
    public int getEndorsementsReceived() {
        int numEndorsementsReceived = 0;
        for (Post post : this.getPosts()) {
            numEndorsementsReceived += post.getEndorsementsReceived();
        } return numEndorsementsReceived;
    }

    /*
     * Setter method for Account's handle
     */
    public void setHandle(String handle) { this.handle = handle; }
    /*
     * Setter method for Account's description
     */
    public void setDescription(String description) { this.description = description; }
    /*
     * Setter method for Account's ALL_ACCOUNTS
     */
    public static void setAllAccounts(ArrayList<Account> allAccounts) { ALL_ACCOUNTS = allAccounts; }
    /*
     * Used to increment number of Account's endorsed Posts
     * @param incrementBy int amount to increment by
     */
    public void incrementNumEndorsedPosts(int incrementBy) { numEndorsedPosts += incrementBy; }
    /*
     * Used to increment number of Account's comment Posts
     * @param incrementBy int amount to increment by
     */
    public void incrementNumCommentPosts(int incrementBy) { numCommentPosts += incrementBy; }
}
