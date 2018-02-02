package Suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({AccountControllerTests.class, AddressControllerTests.class,
        AdOfferControllerTests.class, AdControllerTests.class,
        BlockUserTests.class,
        BusinessControllerTests.class, CommentsControllerTests.class,
        ConversationControllerTests.class, FeedbackControllerTests.class,
        FeedControllerTests.class, FollowControllerTests.class,
        InviteControllerTests.class, MultimediaControllerTests.class,
        NotificationControllerTests.class, PlaceControllerTests.class,
        ProfileControllerTests.class, ReportControllerTests.class,
        ShareControllerTests.class, UserControllerTests.class
})
public class FullRegressionSuite {
}
