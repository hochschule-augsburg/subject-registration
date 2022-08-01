package de.hochschule.augsburg;

import de.hochschule.augsburg.security.UserContext;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@RecordApplicationEvents
@ActiveProfiles(profiles = {"test"})
@Import({UserContext.class})
public class BaseServiceTest {


}
