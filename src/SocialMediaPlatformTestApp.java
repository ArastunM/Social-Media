import socialmedia.*;

import java.io.IOException;
import java.util.ArrayList;


/**
 * SocialMediaPlatformTestApp is used to test the SocialMediaPlatform implemented
 * by SocialMedia.
 *
 * This is done through running functional, error, management and format tests
 */
public class SocialMediaPlatformTestApp {
	/**
	 * Used to run the implemented tests.
	 * Assertions are thrown in unexpected scenarios
	 *
	 * @param args empty string array
	 */
	public static void main(String[] args) {
		// running all of the 4 tests
		System.out.println("The system compiled and started the execution...");

		runFunctionalTesting();
		runErrorTesting();
		runManagementTesting();
		runFormatTesting();

		System.out.print("\nTesting completed");
	}

	/**
	 * Tests if individual methods of SocialMediaPlatform run as expected
	 */
	private static void runFunctionalTesting() {
		// getting ALL_ACCOUNTS from Account class
		ArrayList<Account> ALL_ACCOUNTS = Account.getAllAccounts();
		System.out.println("\nRunning the Functional Testing...");

		SocialMediaPlatform platform = new SocialMedia();

		// Testing initial values for SocialMediaPlatform
		assert (platform.getNumberOfAccounts() == 0) : "Initial SocialMediaPlatform not empty as required";
		assert (platform.getTotalOriginalPosts() == 0) : "Initial SocialMediaPlatform not empty as required";
		assert (platform.getTotalCommentPosts() == 0) : "Initial SocialMediaPlatform not empty as required";
		assert (platform.getTotalEndorsmentPosts() == 0) : "Initial SocialMediaPlatform not empty as required";
		assert (platform.getMostEndorsedPost() == -1) : "Initial getMostEndorsedPost not '-1' as required";
		assert (platform.getMostEndorsedAccount() == -1) : "Initial getMostEndorsedAccount not '-1' as required";

		int id1, id2;
		int postId1, postId2;
		int commentPostId1, commentPostId2;
		try {
			// testing creating an account
			id1 = platform.createAccount("testUser1");
			assert (platform.getNumberOfAccounts() == 1) : "number of accounts registered in the system does not match";

			// testing removing an account
			platform.removeAccount(id1);
			assert (platform.getNumberOfAccounts() == 0) : "number of accounts registered in the system does not match";

			id2 = platform.createAccount("testUser2", "i am the second registered user");
			platform.createAccount("testUser3");

			assert (platform.getNumberOfAccounts() == 2) : "number of accounts registered in the system does not match";
			assert (ALL_ACCOUNTS.get(0).getDescription().equals("i am the second registered user")) : "assigned description does not match";
			assert (ALL_ACCOUNTS.get(1).getDescription() == null) : "assigned description does not match";

			// testing changing account handle
			platform.changeAccountHandle("testUser2", "newUser2");

			assert (ALL_ACCOUNTS.get(0).getHandle().equals("newUser2")) : "assigned handle does not match";
			assert (ALL_ACCOUNTS.get(1).getHandle().equals("testUser3")) : "assigned handle does not match";

			// testing updating account description
			platform.updateAccountDescription("testUser3", "now i have a description");

			assert (ALL_ACCOUNTS.get(1).getDescription() != null) : "description not updated properly";

			// testing creating posts
			postId1 = platform.createPost("newUser2", "first post | posted by user3");
			postId2 = platform.createPost("testUser3", "second post and its message");

			assert (platform.getTotalOriginalPosts() == 2) : "number of original posts registered in the system does not match";
			assert (platform.getTotalEndorsmentPosts() == 0) : "number of endorsed posts registered in the system does not match";
			assert (platform.getTotalEndorsmentPosts() == 0) : "number of comment posts registered in the system does not match";

			// testing endorsing posts and commenting on posts
			platform.endorsePost("newUser2", postId1);
			commentPostId1 = platform.commentPost("testUser3", postId2, "my first comment");
			commentPostId2 = platform.commentPost("newUser2", commentPostId1, "my reply to your first comment");

			assert (platform.getTotalOriginalPosts() == 2) : "number of original posts registered in the system does not match";
			assert (platform.getTotalEndorsmentPosts() == 1) : "number of endorsed posts registered in the system does not match";
			assert (platform.getTotalCommentPosts() == 2) : "number of comment posts registered in the system does not match";
			assert (platform.getMostEndorsedPost() == postId1) : "most endorsed post registered in the system does not match";
			assert (platform.getMostEndorsedAccount() == id2) : "most endorsed account registered in the system does not match";

			// testing deleting posts
			platform.deletePost(postId2);

			assert (platform.getTotalOriginalPosts() == 1) : "number of original posts registered in the system does not match";
			assert (platform.getTotalCommentPosts() == 2) : "number of comment posts registered in the system does not match";
			assert (Post.findPost(commentPostId1).getMessage().equals("The original content was removed from the " +
					"system and is no longer available.")) : "comments on removed posts not handled appropriately";
			assert (Post.findPost(commentPostId2).getMessage().equals("The original content was removed from the " +
					"system and is no longer available.")) : "comments on removed posts not handled appropriately";

			// testing deleting comments
			platform.deletePost(commentPostId2);

			assert (platform.getTotalCommentPosts() == 1) : "number of comment posts registered in the system does not match";

		} catch (IllegalHandleException e) {
			assert (false) : "IllegalHandleException thrown incorrectly";
		} catch (InvalidHandleException e) {
			assert (false) : "InvalidHandleException thrown incorrectly";
		} catch (AccountIDNotRecognisedException e) {
			assert (false) : "AccountIDNotRecognizedException thrown incorrectly";
		} catch (HandleNotRecognisedException e) {
			assert (false) : "HandleNotRecognizedException thrown incorrectly";
		} catch (InvalidPostException e) {
			assert (false) : "InvalidPostException thrown incorrectly";
		} catch (NotActionablePostException e) {
			assert (false) : "NotActionablePostException thrown incorrectly";
		} catch (PostIDNotRecognisedException e) {
			assert (false) : "PostIDNotRecognizedException thrown incorrectly";
		}

		System.out.println("Functional Testing successful");
	}

	/**
	 * Tests if Exceptions are made appropriate use of upon false input
	 */
	private static void runErrorTesting() {
		System.out.println("\nRunning the Error Testing...");

		SocialMediaPlatform platform = new SocialMedia();

		// creating an account (testing with non-unique and not appropriate handles)
		try { // IllegalHandleException
			platform.createAccount("userError1");
			platform.createAccount("userError1");
			assert (false) : "IllegalHandleException was expected but not thrown";
		} catch (IllegalHandleException | InvalidHandleException ignored) {}

		try { // InvalidHandleException
			platform.createAccount("");
			assert (false) : "InvalidHandleException was expected but not thrown";
		} catch (IllegalHandleException | InvalidHandleException ignored) {}

		try { // InvalidHandleException
			platform.createAccount("thisHandleContainsMoreThan30Characters");
			assert (false) : "InvalidHandleException was expected but not thrown";
		} catch (IllegalHandleException | InvalidHandleException ignored) {}

		try { // InvalidHandleException
			platform.createAccount(" yes ");
			assert (false) : "InvalidHandleException was expected but not thrown";
		} catch (IllegalHandleException | InvalidHandleException ignored) {}

		// removing an account (testing with account handles that do not exist)
		try { // HandleNotRecognizedException
			platform.removeAccount("randomHandle");
			assert (false) : "HandleNotRecognizedException was expected but not thrown";
		} catch (HandleNotRecognisedException ignored) {}

		// updating account description (testing with account handles that do not exist)
		try { // HandleNotRecognizedException
			platform.updateAccountDescription("anotherRandomHandle", "description");
			assert (false) : "HandleNotRecognizedException was expected but not thrown";
		} catch (HandleNotRecognisedException ignored) {}

		// creating a post (testing with not appropriate post messages)
		try { // InvalidPostException
			platform.createPost("userError1", "");
			assert (false) : "InvalidPostException was expected but not thrown";
		} catch (HandleNotRecognisedException | InvalidPostException ignored) {}

		// endorsing a post (testing with post ID that do not exist and endorsing an Endorsed Post)
		try { // PostIDNotRecognizedException
			platform.endorsePost("userError1", 123);
			assert (false) : "PostIDNotRecognizedException was expected but not thrown";
		} catch (HandleNotRecognisedException | PostIDNotRecognisedException
				| NotActionablePostException ignored) {}

		try { // NotActionablePostException
			int postID = platform.createPost("user1", "post1");
			int endorsedPostID = platform.endorsePost("user1", postID);
			platform.endorsePost("userError1", endorsedPostID);
			assert (false) : "NotActionablePostException was expected but not thrown";
		} catch (HandleNotRecognisedException | InvalidPostException |
				PostIDNotRecognisedException | NotActionablePostException ignored) {}

		// commenting on a post (testing with format of the comment message)
		try { // InvalidPostException
			int postID2 = platform.createPost("userError1", "post2");
			platform.commentPost("userError1", postID2, "");
			assert (false) : "InvalidPostException was expected but not thrown";
		} catch (HandleNotRecognisedException | InvalidPostException |
				PostIDNotRecognisedException | NotActionablePostException ignored) {}

		System.out.println("Error Testing successful");
	}

	/**
	 * Tests if management-related functions in SocialMediaPlatform run as expected
	 */
	private static void runManagementTesting() {
		System.out.println("\nRunning the Management Testing...");

		SocialMediaPlatform platform = new SocialMedia();

		try {
			platform.createAccount("managementUser1");
			platform.createAccount("managementUser2");
			int id = platform.createPost("managementUser1", "first post");
			platform.commentPost("managementUser2", id, "reply to your first post");
			platform.endorsePost("managementUser2", id);

			// testing assigned values of SocialMediaPlatform
			assert (platform.getNumberOfAccounts() == 2) : "Assigned SocialMediaPlatform not as required";
			assert (platform.getTotalOriginalPosts() == 1) : "Assigned SocialMediaPlatform not as required";
			assert (platform.getTotalCommentPosts() == 1) : "Assigned SocialMediaPlatform not as required";
			assert (platform.getTotalEndorsmentPosts() == 1) : "Assigned SocialMediaPlatform not as required";

			// saving the platform and testing erasing form the platform
			platform.savePlatform("saved_platform");
			platform.erasePlatform();

			assert (platform.getNumberOfAccounts() == 0) : "Erased SocialMediaPlatform not empty as required";
			assert (platform.getTotalOriginalPosts() == 0) : "Erased SocialMediaPlatform not empty as required";
			assert (platform.getTotalCommentPosts() == 0) : "Erased SocialMediaPlatform not empty as required";
			assert (platform.getTotalEndorsmentPosts() == 0) : "Erased SocialMediaPlatform not empty as required";

			// testing loading from the saved platform
			platform.loadPlatform("saved_platform");

			assert (platform.getNumberOfAccounts() == 2) : "Reloaded SocialMediaPlatform not as required";
			assert (platform.getTotalOriginalPosts() == 1) : "Reloaded SocialMediaPlatform not as required";
			assert (platform.getTotalCommentPosts() == 1) : "Reloaded SocialMediaPlatform not as required";
			assert (platform.getTotalEndorsmentPosts() == 1) : "Reloaded SocialMediaPlatform not as required";

		} catch (IOException | InvalidPostException | NotActionablePostException |
				HandleNotRecognisedException | ClassNotFoundException | PostIDNotRecognisedException |
				IllegalHandleException | InvalidHandleException e) {
			assert (false) : e.toString() + " thrown incorrectly (managementTesting)";
		}

		System.out.println("Management Testing successful");
	}

	/**
	 * Tests if "show" functions in SocialMediaPlatform are in appropriate format
	 */
	private static void runFormatTesting() {
		System.out.println("\nRunning the Format Testing...");

		SocialMediaPlatform platform = new SocialMedia();

		int postId1, postId3;
		int commentPostId1;
		try {
			// assigning account / post values before format testing
			platform.createAccount("userFormat1");
			platform.createAccount("userFormat2", "hi, my handle is user2");
			platform.createAccount("userFormat3", "some description");

			postId1 = platform.createPost("userFormat1", "first post of user1");
			platform.createPost("userFormat1", "second post of user1");
			postId3 = platform.createPost("userFormat2", "first post of user2");

			platform.endorsePost("userFormat3", postId1);
			commentPostId1 = platform.commentPost("userFormat2", postId1, "i comment on the first post of user1");
			platform.commentPost("userFormat1", commentPostId1, "thanks for the comment, userFormat2");

			// testing showAccount method
			System.out.println("\nACCOUNT DETAILS OF 'userFormat1' (ShowAccount)");
			System.out.println(platform.showAccount("userFormat1"));

			// testing showIndividualPost method
			System.out.println("\nPOST DETAILS OF 'postId3' (ShowIndividualPost)");
			System.out.println(platform.showIndividualPost(postId3));

			// testing showPostChildrenDetails
			System.out.println("\nPOST CHILDREN DETAILS OF 'postId1 (ShowPostChildrenDetails)");
			System.out.println(platform.showPostChildrenDetails(postId1));

		} catch (IllegalHandleException e) {
			assert (false) : "IllegalHandleException thrown incorrectly (formatTesting)";
		} catch (InvalidHandleException e) {
			assert (false) : "InvalidHandleException thrown incorrectly (formatTesting)";
		} catch (HandleNotRecognisedException e) {
			assert (false) : "HandleNotRecognisedException thrown incorrectly (formatTesting)";
		} catch (InvalidPostException e) {
			assert (false) : "InvalidPostException thrown incorrectly (formatTesting)";
		} catch (NotActionablePostException e) {
			assert (false) : "NotActionablePostException thrown incorrectly (formatTesting)";
		} catch (PostIDNotRecognisedException e) {
			assert (false) : "PostIDNotRecognisedException thrown incorrectly (formatTesting)";
		}

		System.out.println("\nFormat Testing completed");
	}
}
