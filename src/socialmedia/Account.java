package socialmedia;

import socialmedia.exceptions.*;
import java.util.ArrayList;

/**
 * Used to handle Account related operations
 */
public class Account {
    // instance attributes
    // id: unique numerical identifier
    private int id;
    // handle: unique string handle (username)
    private String handle;
    // description: field for personal information
    private String description;
    // posts: list of all posts (original, comment, endorsement) made by the Account
    private ArrayList<Post> posts = new ArrayList<>();

    // static attribute
    // ALL_ACCOUNTS: used to store all registered accounts
    private static ArrayList<Account> ALL_ACCOUNTS = new ArrayList<>();


    // CREATING AN ACCOUNT
    // method: checks if the given id (numerical identifier) is unique
    private static boolean isUniqueID(int id) {
        for (Account account : ALL_ACCOUNTS) {
            if (id == account.id)
                return false;
        } return true;
    }
    // method: checks if the given handle (username) is unique
    private static boolean isUniqueHandle(String handle) {
        for (Account account : ALL_ACCOUNTS) {
            if (handle.equals(account.handle))
                return false;
        } return true;
    }
    // method: used to generate a random numerical identifier
    // TODO improve the method of generating a numerical identifier (ID)
    private static int generateAccountID() {
        // generating a 4 digit code
        int randomIdentifier = (int) (Math.random() * 9000) + 1000;
        if (!isUniqueID(randomIdentifier))
            // re-generating the ID in case it is not unique
            return generateAccountID();
        return randomIdentifier;
    }
    // method: checks if the given handle (username) is appropriate
    private static boolean isAppropriateHandle(String handle) {
        // handle cannot be empty
        boolean case1 = !handle.strip().equals("");
        // contain more than 30 characters
        boolean case2 = !(handle.length() > 30);
        // contain white space(s)
        boolean case3 = !containsWhiteSpace(handle);
        return case1 && case2 && case3;
    }
    // method: checks if a String contains white space
    private static boolean containsWhiteSpace(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (Character.isWhitespace(string.charAt(i)))
                return true;
        } return false;
    }

    // REMOVING ACCOUNTS
    // method: used to remove account with given id / handle
    public static void remove(int id) throws AccountIDNotRecognisedException {
        // finding and assigning the account to remove
        Account accountToRemove = findAccount(id);
        // TODO remove all posts related to the account
        ALL_ACCOUNTS.remove(accountToRemove);
    }
    // method: used to remove account with given handle
    public static void remove(String handle) throws HandleNotRecognisedException {
        // finding and assigning the account to remove
        Account accountToRemove = findAccount(handle);
        // TODO remove all posts related to the account
        ALL_ACCOUNTS.remove(accountToRemove);
    }
    // method: used to find the account with given id
    public static Account findAccount(int id) throws AccountIDNotRecognisedException {
        for (Account account : ALL_ACCOUNTS) {
            if (account.getId() == id)
                return account;
        } throw new AccountIDNotRecognisedException("Account with given id: " + id + " does not exist");
    }
    // method: used to find the account with given handle
    public static Account findAccount(String handle) throws HandleNotRecognisedException {
        for (Account account : ALL_ACCOUNTS) {
            if (account.getHandle().equals(handle))
                return account;
        } throw new HandleNotRecognisedException("Account with given handle: " + handle + " does not exist");
    }

    // method: String representation of the Account
    @Override
    public String toString() {
        return "[id = " + id + ", handle = " + handle
                + ", description = " + description + ", posts = " + posts + "]";
    }

    // Constructors
    public Account(String handle) throws InvalidHandleException, IllegalHandleException {
        // checking if given handle is valid and appropriate
        if (!isUniqueHandle(handle))
            throw new InvalidHandleException("Given handle already exists in the system");
        if (!isAppropriateHandle(handle))
            throw new IllegalHandleException("Given handle is not appropriate");

        // assigning values
        this.id = generateAccountID();
        this.handle = handle;
        ALL_ACCOUNTS.add(this);
    }
    public Account(String handle, String description) throws InvalidHandleException, IllegalHandleException {
        // assigning id, handle through first constructor
        this(handle);

        // checking if the description is appropriate (100 character limit)
        if (description.length() > 100)
            throw new IllegalHandleException("Given description should be up to 100 characters");

        // assigning values
        this.description = description;
        ALL_ACCOUNTS.set(ALL_ACCOUNTS.size() - 1, this);
    }

    // Getter methods
    public int getId() { return id; }
    public String getHandle() { return handle; }
    public String getDescription() { return description; }
    public ArrayList<Post> getPosts() { return posts; }
    public static ArrayList<Account> getAllAccounts() { return ALL_ACCOUNTS; }

    // Setter methods
    public void setHandle(String handle) { this.handle = handle; }
    public void setDescription(String description) { this.description = description; }

    // testing
    public static void main(String[] args) {
        try {
            // TESTING CREATING ACCOUNTS
            Account account1 = new Account("username", "my description");
            Account account2 = new Account("user2", "different description");
            Account account3 = new Account("user3", "i am a good user with a post");
            Account account4 = new Account("user4", "DESCRIPTION");

            // TESTING REMOVING ACCOUNTS
            remove(account1.id);
            remove(account2.handle);

            // TESTING CREATING POSTS
            Post post1 = new Post(account3.handle, "hello, this is my first post");
            Post post2 = new Post(account3.handle, "second post");

            // ENDORSING A POST
            EndorsedPost endorsedPost1 = new EndorsedPost(account4.handle, post2.getId());

        } catch (InvalidHandleException | IllegalHandleException | AccountIDNotRecognisedException
                | HandleNotRecognisedException | PostIDNotRecognisedException e) {
            System.out.println(e.toString());
        }
    }
}
